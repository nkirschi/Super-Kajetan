package gui;

import model.*;
import util.*;
import util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Key;


public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Player player;
    private Camera camera; // Die aktuelle "Kamera"
    private KeyHandler KeyHandler;
    private SoundUtil soundUtil = new SoundUtil();
    //private Timer timer;


    private JButton backButton;
    private JPanel buttonPanel;

    private boolean running;
    private boolean paused;
    private int hz = 60, fps = 60;

    LevelView(Level level) {
        this.level = level;
        player = new Player(LobbyView.getInstance().getWidth() / 2, Constants.GROUND_LEVEL);
        camera = new Camera(0, 0, getWidth(), getHeight());
        setIgnoreRepaint(true);
        setLayout(new BorderLayout());
        buttonPanel = new JPanel(new GridBagLayout());

        //Menü-Komponenten
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);

        JButton continueButton = new JButton("Weiter");
        continueButton.setBackground(Constants.BUTTON_COLOR);
        continueButton.setFont(Constants.DEFAULT_FONT);
        continueButton.addActionListener(a -> {
            paused = false;
            KeyHandler.menu = false;
        });


        backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.addActionListener(a -> {
            paused = false;
            running = false;
            //soundUtil.stop();
            SoundUtil.soundSystem.stop("background");
            SoundUtil.soundSystem.cull("background");
            //timer.cancel();
            //timer.purge();
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
        });


        buttonPanel.add(continueButton);
        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);
        buttonPanel.setVisible(false);

        add(buttonPanel, BorderLayout.CENTER);


        KeyHandler = new KeyHandler();
        addKeyListener(KeyHandler);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                paused = false;
                soundUtil.unpause();
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                paused = true;
                KeyHandler.left = false;
                KeyHandler.right = false;
                KeyHandler.run = false;
                KeyHandler.jump = false;
                KeyHandler.crouch = false;
                soundUtil.pause();
            }
        });

        Logger.log("Level initialisiert", Logger.INFO);
        /*
        SoundUtil.loop();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    update();
                    repaint();
                }
            }
        }, 0, 1000 / 60);*/
    }

    public void run() {
        running = true;
        //soundUtil.playRandom();
        SoundUtil.soundSystem.activate("background");
        SoundUtil.soundSystem.play("background");

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

            //Menu
            if (!paused && KeyHandler.menu)
                SoundUtil.soundSystem.pause("background");
            else if (paused && !KeyHandler.menu)
                SoundUtil.soundSystem.play("background");
            paused = KeyHandler.menu;
            if (KeyHandler.menu) {
                buttonPanel.setVisible(true);
            } else {
                buttonPanel.setVisible(false);
            }
        }
        System.out.println(this + " ist raus, Onkel Klaus!");
    }

    public void update() {
        // 1. Reset
        player.setVelocityX(0);
        player.setWalking(false);
        player.setRunning(false);
        player.setCrouching(false);

        // 2. Input Handling
        if (KeyHandler.left) {
            player.addVelocityX(-Constants.PLAYER_WALK_VELOCITY);
            if (!KeyHandler.right) {
                player.setViewingDirection(Direction.LEFT);
                player.setWalking(true);
            }
        }

        if (KeyHandler.right) {
            player.addVelocityX(Constants.PLAYER_WALK_VELOCITY);
            if (!KeyHandler.left) {
                player.setViewingDirection(Direction.RIGHT);
                player.setWalking(true);
            }
        }

        if (KeyHandler.run && !player.isExhausted()) {
            player.setRunning(true);
        }

        if (KeyHandler.jump && player.isOnGround() && !player.isExhausted()) {
            player.setVelocityY(-Constants.PLAYER_INITIAL_JUMP_VELOCITY);
            player.setOnGround(false);
            player.setRunning(false);
            player.setJumping(true);
        } else if (!KeyHandler.jump && !player.isOnGround()) {
            if (player.getVelocityY() < -6)
                player.setVelocityY(-6);
        }

        if (KeyHandler.crouch && !player.isExhausted()) {
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
            player.setExhausted(true);

        if (!KeyHandler.run && !KeyHandler.jump && !KeyHandler.crouch)
            player.setExhausted(false);

        util.List<Collidable> collidables = List.concat(List.concat(level.getGrounds(), level.getObstacles()), level.getEnemies());

        // 5. Kollision - zuerst in x- dann in y-Richtung
        Player dummy = new Player(player);
        dummy.setVelocityY(0);
        dummy.move();
        for (Collidable collidable : collidables) {
            if (dummy.collidesWith(collidable)) {
                if (player.getVelocityX() > 0) {
                    player.setX(collidable.getHitbox().getX() - player.getHitbox().getWidth() / 2);
                } else if (player.getVelocityX() < 0) {
                    player.setX(collidable.getHitbox().getX() + collidable.getHitbox().getWidth() +
                            player.getHitbox().getWidth() / 2);
                }
                player.setVelocityX(0);
                player.setWalking(false);
                break;
            }
        }

        dummy = new Player(player);
        dummy.setVelocityX(0);
        dummy.move();

        for (Collidable collidable : collidables) {
            if (dummy.collidesWith(collidable)) {
                if (player.getVelocityY() > 0) {
                    player.setY(collidable.getHitbox().getY());
                    player.setVelocityY(0);
                    player.setOnGround(true);
                    player.setJumping(false);
                } else if (player.getVelocityY() < 0) {
                    player.setY(collidable.getHitbox().getY() + collidable.getHitbox().getHeight() +
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
            running = false;
            JOptionPane.showMessageDialog(MainFrame.getInstance().getCurrentView(), "Game over!", "Pech", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void render() {
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = buffer.createGraphics();
        paintComponent(g2);
        getGraphics().drawImage(buffer, 0, 0, this);
        g2.dispose();
        getGraphics().dispose();
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
            int playerX = (int) (player.getX() - image.getWidth() / 2 - camera.getX());
            int playerY = (int) (player.getY() - image.getHeight());
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
        for (Enemy enemy : level.getEnemies()) {
            try {
                BufferedImage image;
                image = ImageUtil.getImage(enemy.getImagePath());
                int x = (int) Math.round(enemy.getX() - image.getWidth() / 2 - camera.getX());
                int y = (int) Math.round(enemy.getY() - image.getHeight());
                if (enemy.getViewingDirection().equals(Direction.RIGHT))
                    g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), this);
                else
                    g2.drawImage(image, x + image.getWidth(), y, -image.getWidth(), image.getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log(e, Logger.WARNING);
            }
        }

        // 5. Draw Obstacles
        for (Obstacle obstacle : level.getObstacles()) {
            try {
                BufferedImage image = ImageUtil.getImage(obstacle.getImagePath());
                int x = (int) Math.round(obstacle.getX() - image.getWidth() / 2 - camera.getX());
                int y = (int) Math.round(obstacle.getY() - image.getHeight());
                g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log(e, Logger.WARNING);
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
        if (KeyHandler.debug) {
            g2.setColor(Color.BLACK);
            g2.drawString("Sidescroller " + Constants.GAME_VERSION, getWidth() / 2 - g2.getFontMetrics().stringWidth("Sidescroller"), 20);

            String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
            g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);

            g2.drawString("P(" + player.getX() + "," + player.getY() + ")", 20, 20);
            g2.drawString("velocityX = " + player.getVelocityX(), 20, 40);
            g2.drawString("velocityY = " + player.getVelocityY(), 20, 60);
            g2.drawString("walking = " + player.isWalking(), 20, 80);
            g2.drawString("running = " + player.isRunning(), 20, 100);
            g2.drawString("jumping = " + player.isJumping(), 20, 120);
            g2.drawString("crouching = " + player.isCrouching(), 20, 140);
            g2.drawString("exhausted = " + player.isExhausted(), 20, 160);
            g2.drawString("onGround = " + player.isOnGround(), 20, 180);

        }
    }

    public void refresh() {
        setFocusable(true);
        requestFocusInWindow();
    }
}