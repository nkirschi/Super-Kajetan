package gui;

import model.*;
import util.Constants;
import util.ImageUtil;
import util.Logger;
import util.SoundUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Player player;
    private Camera camera; // Die aktuelle "Kamera"
    private KeyHandler keyHandler;
    //private Timer timer;

    private boolean exhausted;

    private boolean running;
    private boolean paused;
    private int hz = 60, fps = 60;

    LevelView(Level level) {
        this.level = level;
        player = new Player(LobbyView.getInstance().getWidth() / 2, Constants.GROUND_LEVEL);
        camera = new Camera(0, 0, getWidth(), getHeight());
        setIgnoreRepaint(true);
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.setLocation(20, getHeight() - backButton.getHeight() - 20);
        backButton.addActionListener(a -> {
            SoundUtil.stop();
            running = false;
            paused = true;
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
        });
        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);

        add(buttonPanel, BorderLayout.SOUTH);

        keyHandler = new KeyHandler();
        addKeyListener(keyHandler);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                paused = false;
                SoundUtil.unpause();
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                paused = true;
                keyHandler.left = false;
                keyHandler.right = false;
                keyHandler.run = false;
                keyHandler.jump = false;
                keyHandler.crouch = false;
                SoundUtil.pause();
            }
        });

        Logger.log("Level initialisiert", Logger.INFO);

        //SoundUtil.loop();
        /*timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    refresh();
                    repaint();
                }
            }
        }, 0, 1000 / 60);*/
    }

    public void run() {
        running = true;
        SoundUtil.loop();

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

                // Theoretisch VSync - is aber bissl laggy :(
                /*while (System.nanoTime() - currentTime < 1000000000 / 60) {
                    Thread.yield();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

            } else {
                lastTime = System.nanoTime();
            }
        }
    }

    public void update() {
        // 1. Reset
        player.setWalking(false);
        player.setRunning(false);
        player.setCrouching(false);

        // 2. Input Handling
        if (keyHandler.left) {
            if (player.getVelocityX() > -Constants.PLAYER_WALK_VELOCITY)
                player.addVelocityX(-Constants.PLAYER_WALK_VELOCITY / 8);
            else
                player.setVelocityX(-Constants.PLAYER_WALK_VELOCITY);
            if (!keyHandler.right)
                player.setViewingDirection(Direction.LEFT);
            player.setWalking(true);
        } else {
            if (player.getVelocityX() < 0)
                player.addVelocityX(Constants.PLAYER_WALK_VELOCITY / 8);
            else if (!keyHandler.right)
                player.setVelocityX(0);
        }

        if (keyHandler.right) {
            if (player.getVelocityX() < Constants.PLAYER_WALK_VELOCITY)
                player.addVelocityX(Constants.PLAYER_WALK_VELOCITY / 8);
            else
                player.setVelocityX(Constants.PLAYER_WALK_VELOCITY);
            if (!keyHandler.left)
                player.setViewingDirection(Direction.RIGHT);
            player.setWalking(true);
        } else {
            if (player.getVelocityX() > 0)
                player.addVelocityX(-Constants.PLAYER_WALK_VELOCITY / 8);
            else if (!keyHandler.left)
                player.setVelocityX(0);
        }

        if (keyHandler.run && !exhausted) {
            player.setRunning(true);
        }

        if (keyHandler.jump && player.isOnGround() && !exhausted) {
            player.setVelocityY(-Constants.PLAYER_INITIAL_JUMP_VELOCITY);
            player.setOnGround(false);
            player.setRunning(false);
            player.setJumping(true);
            System.out.println(player.getVelocityX());
        } else if (!keyHandler.jump && !player.isOnGround()) {
            if (player.getVelocityY() < -6)
                player.setVelocityY(-6);
        }

        if (keyHandler.crouch && !exhausted) {
            player.multiplyVelocityX(0.5);
            player.setCrouching(true);
        }

        if (player.isRunning() || player.isJumping()) {
            player.multiplyVelocityX(Constants.SPEED_FACTOR);
        }

        // 3. General Gravitation TODO Gravitation für jedermann
        player.addVelocityY(Constants.GRAVITATIONAL_ACCELERATION);
        player.setOnGround(false);


        // 4. Ausdauerverbrauch
        if (player.isWalking() && !player.isJumping() && !player.isCrouching())
            player.addStamina(-1);

        if (player.isCrouching())
            player.addStamina(-2);
        else if (player.isRunning() || player.isJumping() && player.getVelocityY() < 0) {
            player.addStamina(-3);
        }

        if (!player.isRunning() && !player.isJumping() && !player.isCrouching()) {
            player.addStamina(2);
        }

        if (player.getStamina() < 10)
            exhausted = true;

        if (!keyHandler.run && !keyHandler.jump && !keyHandler.crouch)
            exhausted = false;

        // 5. Kollision - vorerst nur Bodenelemente
        Player dummy = new Player(player);
        dummy.setVelocityY(0);
        dummy.move();
        for (Ground ground : level.getGrounds()) {
            if (dummy.collidesWith(ground)) {
                if (player.getVelocityX() > 0) {
                    player.setVelocityX(ground.getHitbox().getX() -
                            player.getHitbox().getX() - player.getHitbox().getWidth());
                } else if (player.getVelocityX() < 0) {
                    player.setVelocityX(ground.getHitbox().getX() + ground.getHitbox().getWidth() -
                            player.getHitbox().getX());
                }
                player.setVelocityX(0);
                player.setWalking(false);
                break;
            }
        }

        dummy = new Player(player);
        dummy.setVelocityX(0);
        dummy.move();
        for (Ground ground : level.getGrounds()) {
            if (dummy.collidesWith(ground)) {
                if (player.getVelocityY() > 0) {
                    player.setY(ground.getHitbox().getY());
                    player.setVelocityY(0);
                    player.setOnGround(true);
                    player.setJumping(false);
                } else if (player.getVelocityY() < 0) {
                    player.setY(ground.getY() +
                            player.getHitbox().getHeight());
                    player.setVelocityY(0);
                }
                break;
            }
        }

        // 6. Änderungen vornehmen
        player.move();
        camera.scroll(player.getVelocityX());

        if (player.getY() > 1000) {
            //timer.cancel();
            //timer.purge();
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
            JOptionPane.showMessageDialog(MainFrame.getInstance().getCurrentView(), "Game over!", "Pech", JOptionPane.INFORMATION_MESSAGE);
        }
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
            Logger.log(e, Logger.WARNING);
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
            image = ImageUtil.getImage(player.getImagePath());
            int playerX = (int) Math.round(player.getX() - image.getWidth() / 2 - camera.getX());
            int playerY = (int) Math.round(player.getY() - image.getHeight());
            if (player.getViewingDirection().equals(Direction.RIGHT))
                g2.drawImage(image, playerX, playerY, image.getWidth(), image.getHeight(), this);
            else
                g2.drawImage(image, playerX + image.getWidth(), playerY, -image.getWidth(), image.getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(e, Logger.WARNING);
        }

        Stroke originalStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        Rectangle2D playerHitbox = player.getHitbox();
        g2.drawRect((int) Math.round(playerHitbox.getX() - camera.x), (int) Math.round(playerHitbox.getY()),
                (int) Math.round(playerHitbox.getWidth()), (int) Math.round(playerHitbox.getHeight()));
        g2.setStroke(originalStroke);


        // 4. Draw Enemies

        // 5. Draw Obstacles
        for (Obstacle obstacle : level.getObstacles()) {
            try {
                BufferedImage image = ImageUtil.getImage(obstacle.getImagePath());
                int x = (int) Math.round(obstacle.getX() - image.getWidth() / 2 - camera.getX());
                int y = (int) Math.round(obstacle.getY() - image.getHeight());
                g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 6. Draw Stamina Bar
        Rectangle2D staminaMask = new Rectangle2D.Double(getWidth() - 220, getHeight() - 30, 200, 15);
        Rectangle2D staminaBar = new Rectangle2D.Double(getWidth() - 220, getHeight() - 30, player.getStamina() / 5, 15);
        g2.setColor(Constants.BUTTON_COLOR);
        g2.fill(staminaMask);
        g2.setColor(Constants.MENU_BACKGROUND_COLOR);
        g2.fill(staminaBar);
        g2.setColor(Color.BLACK);
        Font backup = g2.getFont();
        g2.setFont(Constants.DEFAULT_FONT);
        g2.drawString("Ausdauer: " + player.getStamina() / 10 + "%", getWidth() - 215, getHeight() - 17);
        g2.setFont(backup);

        // 7. Draw Debug Screen
        if (keyHandler.debug) {
            g2.setColor(Color.BLACK);
            g2.drawString("Sidescroller " + Constants.GAME_VERSION, 20, 20);
            String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
            g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);
            g2.drawString(player.toString(), getWidth() / 2 - g2.getFontMetrics().stringWidth(player.toString()) / 2, 20);

            g2.drawString("VelocityX = " + player.getVelocityX(), 10, 50);
            g2.drawString("VelocityY = " + player.getVelocityY(), 10, 70);
        }

        // 8. Draw Menu
        if (keyHandler.menu) {
            g2.setFont(Constants.DEFAULT_FONT.deriveFont(40F));
            g2.drawString("MENU", 100, getHeight() / 2);
            g2.setFont(backup);
        }
    }

    public void refresh() {

    }
}