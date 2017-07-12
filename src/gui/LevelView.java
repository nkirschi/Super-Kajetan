package gui;

import model.*;
import physics.AIManager;
import physics.CollisionChecker;
import physics.LawMaster;
import util.Constants;
import util.ImageUtil;
import util.Logger;
import util.SoundUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Player player;
    private Camera camera; // Die aktuelle "Kamera"
    private KeyHandler keyHandler;
    private CollisionChecker collisionChecker;
    private AIManager aiManager;
    private LawMaster lawMaster;
    private JPanel menuPanel;

    private boolean running;
    private boolean paused;
    private int hz = 60, fps = 60;

    LevelView(Level level) {
        this.level = level;
        player = new Player(LobbyView.getInstance().getWidth() / 2, Constants.GROUND_LEVEL);
        camera = new Camera(player, 0, 0, getWidth(), getHeight());
        collisionChecker = new CollisionChecker(player, level);
        lawMaster = new LawMaster();
        aiManager = new AIManager();
        keyHandler = new KeyHandler(player);
        addKeyListener(keyHandler);

        setIgnoreRepaint(true);
        initMenu();
    }

    public void run() {
        running = true;
        SoundUtil.playRandomBackgroundMusic();

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
                repaint();
            }
        }
        System.out.println(this + " ist raus, Onkel Klaus!");
    }

    private void update() {
        // 1. Reset
        player.reset();

        // 2. Input Handling
        keyHandler.process();

        // 3. General Gravitation TODO Gravitation für jedermann
        lawMaster.applyGravitation(player);
        for (Enemy enemy : level.getEnemies())
            lawMaster.applyGravitation(enemy);

        // 4. Ausdauerverbrauch
        lawMaster.updateStamina(player, keyHandler);

        // 5. Kollision - zuerst in x- dann in y-Richtung
        collisionChecker.forPlayer();

        // Test
        aiManager.handleAI(level, player);

        // 6. Änderungen vornehmen
        player.move();
        camera.move();

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // 0. Reset
        g2.clearRect(0, 0, getWidth(), getHeight());

        // 1. Background
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

        // 2. Grounds

        for (Object object : level.getGrounds().toArray()) {
            Ground ground = (Ground) object;
            Rectangle2D.Double rectangle = new Rectangle2D.Double(ground.getHitbox().getX() - camera.getX(),
                    ground.getHitbox().getY(), ground.getHitbox().getWidth(), ground.getHitbox().getHeight());
            g2.draw(rectangle);
        }

        // 3. Player

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

        if (keyHandler.debug) {
            Stroke originalStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
            Rectangle2D playerHitbox = player.getHitbox();
            g2.drawRect((int) (playerHitbox.getX() - camera.x), (int) (playerHitbox.getY()),
                    (int) (playerHitbox.getWidth()), (int) (playerHitbox.getHeight()));
            g2.setStroke(originalStroke);
        }

        // 4. Enemies
        for (Enemy enemy : level.getEnemies()) {
            try {
                BufferedImage image;
                image = ImageUtil.getImage(enemy.getImagePath());
                int x = (int) (enemy.getX() - image.getWidth() / 2 - camera.getX());
                int y = (int) (enemy.getY() - image.getHeight());
                if (enemy.getViewingDirection().equals(Direction.RIGHT))
                    g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), this);
                else
                    g2.drawImage(image, x + image.getWidth(), y, -image.getWidth(), image.getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log(e, Logger.WARNING);
            }
        }

        // 5. Obstacles
        for (Obstacle obstacle : level.getObstacles()) {
            try {
                BufferedImage image = ImageUtil.getImage(obstacle.getImagePath());
                int x = (int) (obstacle.getX() - image.getWidth() / 2 - camera.getX());
                int y = (int) (obstacle.getY() - image.getHeight());
                g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log(e, Logger.WARNING);
            }
        }

        // 6. Stamina Bar
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

        // 7. Debug Screen
        if (keyHandler.debug) {
            g2.setColor(Color.BLACK);
            String s = Constants.GAME_TITLE + " " + Constants.GAME_VERSION;
            g2.drawString(s, getWidth() / 2 - g2.getFontMetrics().stringWidth(s) / 2, 20);

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

        //8. Pausen-Menü
        if (keyHandler.menu) {
            g2.setColor(new Color(0, 0, 0, 0.8f));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (!paused && keyHandler.menu) {
            paused = true;
            SoundUtil.soundSystem.pause("background");
        } else if (paused && !keyHandler.menu) {
            paused = false;
            SoundUtil.soundSystem.play("background");
        }
        menuPanel.setVisible(keyHandler.menu);

        //9. Game-Over-Menü
    }

    public void refresh() {
        setFocusable(true);
        requestFocusInWindow();
    }

    public void initMenu() {
        setLayout(new BorderLayout());
        menuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);

        Font buttonFont = Constants.DEFAULT_FONT.deriveFont(24f);

        WoodenButton continueButton = new WoodenButton("Fortsetzen");
        continueButton.setBackground(Constants.BUTTON_COLOR);
        continueButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        continueButton.setFont(buttonFont);
        continueButton.addActionListener(a -> {
            paused = false;
            keyHandler.menu = false;
            SoundUtil.soundSystem.play("background");
        });

        WoodenButton backButton = new WoodenButton("Zurück zur Lobby");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(buttonFont);
        backButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        backButton.addActionListener(a -> {
            paused = false;
            running = false;
            SoundUtil.soundSystem.stop("background");
            SoundUtil.soundSystem.cull("background");
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
        });

        menuPanel.add(continueButton, constraints);
        menuPanel.add(backButton, constraints);
        menuPanel.setOpaque(false);
        menuPanel.setVisible(false);

        add(menuPanel, BorderLayout.CENTER);
    }
}