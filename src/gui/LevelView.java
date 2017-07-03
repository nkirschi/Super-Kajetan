package gui;

import model.*;
import util.AudioPlayer;
import util.Constants;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;

public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Camera camera; // Die aktuelle "Kamera"
    private HashSet<Integer> pressedKeys;

    private AudioPlayer audioPlayer;
    private JProgressBar staminaBar;

    private double verticalMoveAmount = -Constants.PLAYER_VERTICAL_MOVE_AMOUNT;
    private boolean jumpingPossible = true;

    private boolean running;
    private boolean paused;
    private int hz, fps;

    LevelView(Level level) {
        this.level = level;
        camera = new Camera(0, 0, getWidth(), getHeight());
        //keyStates = new HashMap<>();
        pressedKeys = new HashSet<>();

        audioPlayer = new AudioPlayer();

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.setLocation(20, getHeight() - backButton.getHeight() - 20);
        backButton.addActionListener(a -> {
            audioPlayer.stop();
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
        });
        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);

        JPanel staminaPanel = new JPanel();
        staminaPanel.setLayout(new BoxLayout(staminaPanel, BoxLayout.Y_AXIS));
        staminaPanel.add(new JLabel("Ausdauer: "));
        staminaBar = new JProgressBar(0, 1000);
        staminaBar.setValue(staminaBar.getMaximum());
        staminaBar.setForeground(Constants.MENU_BACKGROUND_COLOR);
        staminaBar.setBackground(Constants.BUTTON_COLOR);
        staminaPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        staminaPanel.add(staminaBar);
        staminaPanel.setOpaque(false);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(staminaPanel, BorderLayout.EAST);
        bottomPanel.setOpaque(false);

        add(bottomPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //keyStates.put(keyEvent.getKeyCode(), true);
                pressedKeys.add(keyEvent.getKeyCode());
            }

            public void keyReleased(KeyEvent keyEvent) {
                //keyStates.put(keyEvent.getKeyCode(), false);
                pressedKeys.remove(keyEvent.getKeyCode());
            }
        });

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                paused = false;
                audioPlayer.unpause();
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                paused = true;
                audioPlayer.pause();
            }
        });

        new Thread(this).start();
    }

    public void run() {
        running = true;
        audioPlayer.playLoop();

        int updateCount = 0;
        int frameCount = 0;

        final double TIME_PER_UPDATE = 1000000000 / Constants.UPDATE_CLOCK;
        double lastTime = System.nanoTime();
        double secondTime = 0;
        double lag = 0;

        while (running) {
            if (!paused) {
                double currentTime = System.nanoTime();
                double elapsedTime = currentTime - lastTime;
                lastTime = currentTime;
                lag += elapsedTime;
                secondTime += elapsedTime;

                if (secondTime > 1000000000) {
                    hz = updateCount;
                    fps = frameCount;
                    secondTime = 0;
                    updateCount = 0;
                    frameCount = 0;
                }

                while (lag >= TIME_PER_UPDATE) {
                    update();
                    updateCount++;
                    lag -= TIME_PER_UPDATE;
                }

                repaint();
                frameCount++;

                /* Theoretisch VSync - is aber bissl laggy :(
                while (System.nanoTime() - currentTime < 1000000000 / 60) {
                    Thread.yield();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                */
            } else {
                lastTime = System.nanoTime();
                pressedKeys.clear();
            }
        }
    }

    public void update() {

        // 1. Move Player + Gravitation + check Collision
        // Tastaturcheck, altobelli!
        level.getPlayer().setWalking(false);
        level.getPlayer().setRunning(false);
        level.getPlayer().setCrouching(false);

        double xMovement = 0;
        double yMovement = 0;

        for (int keyCode : pressedKeys) {
            switch (keyCode) {
                case Constants.KEY_LEFT:
                    level.getPlayer().setWalking(true);
                    xMovement -= Constants.PLAYER_HORIZONTAL_MOVE_AMOUNT;
                    break;
                case Constants.KEY_RIGHT:
                    level.getPlayer().setWalking(true);
                    xMovement += Constants.PLAYER_HORIZONTAL_MOVE_AMOUNT;
                    break;
                case Constants.KEY_JUMP:
                    if (!jumpingPossible || verticalMoveAmount >= 0)
                        break;
                    level.getPlayer().setWalking(false);
                    level.getPlayer().setRunning(false);
                    level.getPlayer().setJumping(true);
                    yMovement += verticalMoveAmount;
                    break;
                case Constants.KEY_RUN:
                    level.getPlayer().setRunning(true);
                    break;
                case Constants.KEY_CROUCH:
                    level.getPlayer().setCrouching(true);
                    break;
            }
        }

        if (level.getPlayer().getPosition().getX() < getWidth() / 2) {
            double d = getWidth() / 2 - level.getPlayer().getPosition().getX();
            level.getPlayer().move(d, 0);
            camera.scroll(d);
            level.getPlayer().setWalking(false);
            level.getPlayer().setRunning(false);
        } else if (level.getPlayer().getPosition().getX() > level.getLength() - getWidth() / 2) {
            double d = level.getLength() - getWidth() / 2 - level.getPlayer().getPosition().getX();
            level.getPlayer().move(d, 0);
            camera.scroll(Math.floor(d));
            level.getPlayer().setWalking(false);
            level.getPlayer().setRunning(false);
        }

        if (xMovement < 0)
            level.getPlayer().setViewingDirection(Direction.LEFT);
        else if (xMovement > 0)
            level.getPlayer().setViewingDirection(Direction.RIGHT);
        else
            level.getPlayer().setRunning(false);

        if (level.getPlayer().isRunning() || level.getPlayer().isJumping()) {
            staminaBar.setValue(staminaBar.getValue() - 5);
            xMovement *= 2;
        }

        if (level.getPlayer().isCrouching())
            staminaBar.setValue(staminaBar.getValue() - 3);

        if (!level.getPlayer().isRunning() && !level.getPlayer().isJumping() && !level.getPlayer().isCrouching())
            staminaBar.setValue(staminaBar.getValue() + 3);

        camera.scroll(xMovement);
        level.getPlayer().move(xMovement, yMovement);

        Collidable collidable = null;
        for (Ground ground : level.getGrounds()) {
            if (level.getPlayer().collidesWith(ground)) {
                collidable = ground;
                break;
            }
        }

        if (collidable != null) {
            System.out.println("Collision");
        }

        // Gravitation
        if (!pressedKeys.contains(Constants.KEY_JUMP) && level.getPlayer().isJumping())
            jumpingPossible = false;

        if (level.getPlayer().getPosition().getY() < Constants.GROUND_LEVEL) {

            collidable = null;
            for (Ground ground : level.getGrounds()) {
                if (level.getPlayer().collidesWith(ground)) {
                    collidable = ground;
                    break;
                }
            }

            if (collidable != null) {
                System.out.println("Collison");
            }

            if (Constants.GROUND_LEVEL - level.getPlayer().getPosition().getY() >= verticalMoveAmount) {
                level.getPlayer().setWalking(false);
                level.getPlayer().setRunning(false);
                level.getPlayer().setJumping(true);
                level.getPlayer().move(0, verticalMoveAmount);
                verticalMoveAmount += Constants.GRAVITATIONAL_ACCELERATION;
            } else {
                level.getPlayer().setJumping(false);
                level.getPlayer().move(0, Constants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                verticalMoveAmount = -Constants.PLAYER_VERTICAL_MOVE_AMOUNT;
                jumpingPossible = true;
            }
        }

        // 2. Move Enemies + Gravitation + check Collision
        // Gravitationschecks
        // Kollisionschecks

        // 3. Move Arrows + Gravitation + check Collision
        // Später, mein Sohn!

        // 4. Damage & Kill
        // Health-Updates
        // Aufräumen
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // 1. Draw Background
        g2.setColor(Color.WHITE);
        try {
            BufferedImage image = ImageUtil.getImage(level.getBackgroundFilePath());
            // Verarbeitung des aktuell darzustellenden Subimages
            double rel = (double) getWidth() / (double) getHeight();
            //image = image.getSubimage(0, 0, (int) Math.round(rel * image.getHeight()), image.getHeight());

            int width = image.getWidth(null);
            int height = image.getHeight(null);
            double factor = getHeight() / (double) height; // Skalierungsfaktor

            g2.drawImage(image, -(int) camera.getX(), 0, (int) (width * factor), (int) (height * factor), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Draw Grounds

        for (Object object : level.getGrounds().toArray()) {
            Ground ground = (Ground) object;
            Rectangle2D.Double rectangle = new Rectangle2D.Double(ground.getHitbox().getX() - camera.getX(),
                    ground.getHitbox().getY(), ground.getHitbox().getWidth(), ground.getHitbox().getHeight());
            g2.draw(rectangle);
        }

        // 3. Draw Player

        try {
            BufferedImage image;
            image = ImageUtil.getImage(level.getPlayer().getImagePath());
            int playerX = (int) Math.round(level.getPlayer().getPosition().getX() - image.getWidth() / 2 - camera.getX());
            int playerY = (int) Math.round(level.getPlayer().getPosition().getY() - image.getHeight());
            if (level.getPlayer().getViewingDirection().equals(Direction.RIGHT))
                g2.drawImage(image, playerX, playerY, image.getWidth(), image.getHeight(), this);
            else
                g2.drawImage(image, playerX + image.getWidth(), playerY, -image.getWidth(), image.getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stroke originalStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        Rectangle2D playerHitbox = level.getPlayer().getHitbox();
        g2.drawRect((int) Math.round(playerHitbox.getX() - camera.x), (int) Math.round(playerHitbox.getY()),
                (int) Math.round(playerHitbox.getWidth()), (int) Math.round(playerHitbox.getHeight()));
        g2.setStroke(originalStroke);


        // 4. Draw Enemies

        // 5. Draw Obstacles

        // 6. Draw Onscreen Info
        g2.setColor(Color.BLACK);
        g2.drawString("Sidescroller " + Constants.GAME_VERSION, 20, 20);
        String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
        g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);
        g2.drawString(level.getPlayer().toString(), getWidth() / 2 - g2.getFontMetrics().stringWidth(level.getPlayer().toString()) / 2, 20);
    }
}
