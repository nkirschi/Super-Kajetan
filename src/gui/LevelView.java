package gui;

import model.Camera;
import model.Enemy;
import model.Level;
import model.Player;
import physics.AIManager;
import physics.CollisionHandler;
import physics.LawMaster;
import physics.Renderer;
import util.Constants;
import util.SoundUtil;

import javax.swing.*;
import java.awt.*;


public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Player player;
    private Camera camera; // Die aktuelle "Kamera"
    private KeyHandler keyHandler;
    private CollisionHandler collisionHandler;
    private AIManager aiManager;
    private Renderer renderer;
    private LawMaster lawMaster;
    private JPanel menuPanel;
    private JPanel deathPanel;
    private final Stroke strichel;

    private boolean running;
    private boolean paused;
    private int hz = 60, fps = 60;

    LevelView(Level level) {
        this.level = level;
        player = new Player(LobbyView.getInstance().getWidth() / 2, Constants.GROUND_LEVEL);
        camera = new Camera(player, 0, 0, getWidth(), getHeight());
        collisionHandler = new CollisionHandler(player, level);
        lawMaster = new LawMaster();
        aiManager = new AIManager(collisionHandler);
        keyHandler = new KeyHandler(player);
        renderer = new Renderer(level, camera, player, keyHandler, this);
        addKeyListener(keyHandler);
        strichel = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

        setIgnoreRepaint(true);
        setLayout(new BorderLayout());
        initPauseMenu();
        initDeathPanel();
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
                if (!keyHandler.menu) {
                    paused = false;
                    SoundUtil.soundSystem.play("background");
                }
                lastTime = System.nanoTime();
                repaint();
            }
        }
        System.out.println(this + " ist raus, Onkel Klaus!");
    }

    private void update() {
        if (keyHandler.menu) {
            paused = true;
            SoundUtil.soundSystem.pause("background");
        }

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
        collisionHandler.forPlayer();

        // Test
        aiManager.handleAI(level, player);

        // 6. Änderungen vornehmen
        player.move();
        camera.move();

        if (!hasFocus())
            keyHandler.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // 0. Reset
        g2.clearRect(0, 0, getWidth(), getHeight());

        // 1. Background
        renderer.drawBackground(g2);

        // 2. Grounds

        renderer.drawGrounds(g2);

        // 3. Player
        renderer.drawPlayer(g2);

        // 4. Enemies
        renderer.drawEnemies(g2);

        // 5. Obstacles
        renderer.drawObstacles(g2);

        // 6. Stamina Bar
        renderer.drawStaminaBar(g2);

        // 7. Debug Screen
        if (keyHandler.debug) {
            renderer.drawDebugScreen(g2);
        }

        //8. Pausen-Menü
        if (keyHandler.menu) {
            g2.setColor(new Color(0, 0, 0, 0.8f));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        /*if (!paused && keyHandler.menu) {
            paused = true;
            SoundUtil.soundSystem.pause("background");
        } else if (paused && !keyHandler.menu) {
            paused = false;
            SoundUtil.soundSystem.play("background");
        }*/
        menuPanel.setVisible(keyHandler.menu);

        //9. Game-Over-Menü
    }

    public void refresh() {
        setFocusable(true);
        requestFocusInWindow();
    }

    private void initPauseMenu() {
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

    private void initDeathPanel() {
        deathPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);

        Font buttonFont = Constants.DEFAULT_FONT.deriveFont(24f);

        WoodenButton retryButton = new WoodenButton("Nochmal Versuchen");
        retryButton.setBackground(Constants.BUTTON_COLOR);
        retryButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        retryButton.setFont(buttonFont);
        retryButton.addActionListener(a -> {

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

        deathPanel.add(retryButton, constraints);
        deathPanel.add(backButton, constraints);
        deathPanel.setOpaque(false);
        deathPanel.setVisible(false);
    }

    public boolean isPaused() {
        return paused;
    }

    public int getHz() {
        return hz;
    }

    public int getFps() {
        return fps;
    }
}