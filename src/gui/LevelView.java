package gui;

import model.Camera;
import model.Level;
import physics.GameConstants;
import util.AudioPlayer;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Camera camera; // Die aktuelle "Kamera"
    private HashMap<Integer, Boolean> keyStates;

    private AudioPlayer audioPlayer;
    private JButton backButton;

    private boolean looksLeft;
    private double jumpAmount = GameConstants.PLAYER_JUMP_AMOUNT;
    private int walkCount = 0;
    private boolean walkFlag;

    private boolean running;
    private boolean paused;
    private int hz, fps;

    LevelView(Level level) {
        this.level = level;
        camera = new Camera(0, 0, getWidth(), getHeight());
        keyStates = new HashMap<>();

        audioPlayer = new AudioPlayer();

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        backButton = new JButton("Zurück");
        backButton.setBackground(GUIConstants.BUTTON_COLOR);
        backButton.setLocation(20, getHeight() - backButton.getHeight() - 20);
        backButton.addActionListener(a -> {
            audioPlayer.stop();
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
        });

        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                keyStates.put(keyEvent.getKeyCode(), true);
            }

            public void keyReleased(KeyEvent keyEvent) {
                keyStates.put(keyEvent.getKeyCode(), false);
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
        audioPlayer.playLoop();
        running = true;
        int updateCount = 0;
        int frameCount = 0;

        final long TIME_PER_UPDATE = 1000000000 / GameConstants.UPDATE_CLOCK;
        long lastTime = System.nanoTime();
        long secondTime = 0;
        long lag = 0;

        while (running) {
            while (!paused) {
                long currentTime = System.nanoTime();
                long elapsedTime = currentTime - lastTime;
                lastTime = currentTime;
                lag += elapsedTime;
                secondTime += elapsedTime;
                if (secondTime > 1000000000) {
                    //System.out.println(updateCount + "\u2009Hz, " + frameCount + "\u2009fps");
                    //MainFrame.getInstance().setTitle("Sidescroller Alpha v1.1.2_01 [" + updateCount + "\u2009Hz, " + frameCount + "\u2009fps]");
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

                /* Theoretisch VSync
                while (System.nanoTime() - currentTime < 16016667) {
                    Thread.yield();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                */
            }

            lastTime = System.nanoTime();
        }
    }

    public void update() {

        // 1. Move Player + Gravitation + check Collision
        // Tastaturcheck, altobelli!
        //walking = false;
        level.getPlayer().setWalking(false);

        for (Map.Entry<Integer, Boolean> entry : keyStates.entrySet()) {
            switch (entry.getKey()) { // TODO alle Bewegungen implementieren
                case KeyEvent.VK_A:
                    if (entry.getValue()) {
                        level.getPlayer().setWalking(true);//walking = true;
                        double moveAmount = 0;
                        if (level.getPlayer().getPosition().getX() - getWidth() / 2 > GameConstants.PLAYER_MOVE_AMOUNT) {
                            moveAmount = level.getPlayer().isJumping() ? 2 * GameConstants.PLAYER_MOVE_AMOUNT : GameConstants.PLAYER_MOVE_AMOUNT;
                            walkCount++;
                        } else {
                            moveAmount = level.getPlayer().getPosition().getX() - getWidth() / 2;
                        }
                        camera.scroll(-moveAmount);
                        level.getPlayer().move(-moveAmount, 0);
                        looksLeft = true;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (entry.getValue()) {
                        level.getPlayer().setWalking(true);//walking = true;
                        double moveAmount = 0;
                        if (level.getPlayer().getPosition().getX() + getWidth() / 2 < level.getLength() - GameConstants.PLAYER_MOVE_AMOUNT) {
                            moveAmount = level.getPlayer().isJumping() ? 2 * GameConstants.PLAYER_MOVE_AMOUNT : GameConstants.PLAYER_MOVE_AMOUNT;
                            walkCount++;
                        } else {
                            moveAmount = (int) level.getLength() - getWidth() / 2 - level.getPlayer().getPosition().getX();
                        }
                        camera.scroll(moveAmount);
                        level.getPlayer().move(moveAmount, 0);
                        looksLeft = false;
                    }
                    break;
                case KeyEvent.VK_W:
                    if (entry.getValue()) {
                        level.getPlayer().setJumping(true);//jumping = true;
                        if (level.getPlayer().getPosition().getY() > 400 && jumpAmount > 0)
                            level.getPlayer().move(0, -jumpAmount);
                        else {
                            if (level.getPlayer().getPosition().getY() < GameConstants.GROUND_LEVEL) {
                                if (GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY() >= Math.abs(jumpAmount)) {
                                    jumpAmount -= 1;
                                    level.getPlayer().move(0, -jumpAmount);
                                } else
                                    level.getPlayer().move(0, GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                            } else {
                                jumpAmount = GameConstants.PLAYER_JUMP_AMOUNT;
                                level.getPlayer().setJumping(false);//jumping = false;
                            }
                        }
                    } else {
                        if (level.getPlayer().getPosition().getY() < GameConstants.GROUND_LEVEL) {
                            if (GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY() >= Math.abs(jumpAmount)) {
                                jumpAmount -= 1;
                                level.getPlayer().move(0, -jumpAmount);
                            } else
                                level.getPlayer().move(0, GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                        } else {
                            level.getPlayer().setJumping(false);//jumping = false;
                        }
                    }
                    break;
                case KeyEvent.VK_S:
                    //ducken
                    break;
                case KeyEvent.VK_SHIFT:
                    if (entry.getValue() && !level.getPlayer().isJumping()) {
                        int signum = looksLeft ? -1 : 1;
                        camera.scroll(signum * GameConstants.PLAYER_MOVE_AMOUNT);
                        level.getPlayer().move(signum * GameConstants.PLAYER_MOVE_AMOUNT, 0);
                    }
            }

        }

        if (walkCount >= 15) {
            walkFlag = !walkFlag;
            walkCount = 0;
        }

        // Gravitationschecks
        // Kollisionschecks

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

        // 2. Draw Baseline

        // 3. Draw Player

        try {
            BufferedImage image;
            if (level.getPlayer().isJumping())
                image = ImageUtil.getImage("images/char/char_jump_0.66.png");
            else if (level.getPlayer().isWalking()) {
                if (walkFlag)
                    image = ImageUtil.getImage("images/char/char_walk_1_0.66.png");
                else
                    image = ImageUtil.getImage("images/char/char_walk_2_0.66.png");
            } else {
                image = ImageUtil.getImage("images/char/char_stand_0.66.png");
            }
            int playerX = (int) Math.round(level.getPlayer().getPosition().getX() - image.getWidth() / 2 - camera.getX());
            int playerY = (int) Math.round(level.getPlayer().getPosition().getY() - image.getHeight());
            if (!looksLeft)
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
        //g2.setFont(new Font("Consolas", Font.PLAIN, 14));
        g2.setColor(Color.BLACK);
        g2.drawString("Sidescroller " + GUIConstants.GAME_VERSION, 20, 20);
        String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
        g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);

        String playerPosition = "Player position: " + level.getPlayer().getPosition();
        g2.drawString(playerPosition, getWidth() / 2 - g2.getFontMetrics().stringWidth(playerPosition) / 2, 20);
    }
}
