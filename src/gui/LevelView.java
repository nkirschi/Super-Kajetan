package gui;

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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Rectangle2D.Double viewport; // Die aktuelle "Kamera"
    private HashMap<Character, Boolean> keyStates;

    private AudioPlayer audioPlayer;
    private Thread audioThread;
    private JButton backButton;

    private boolean looksLeft;
    private boolean jumping;
    private boolean walking;
    private double jumpAmount = 8;
    private int countToNextJump = 0;
    private int walkCount = 0;
    private boolean walkFlag;

    private boolean running;
    private boolean paused;
    private int hz, fps;

    LevelView(Level level) {
        this.level = level;
        viewport = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        keyStates = new HashMap<>();

        audioPlayer = new AudioPlayer();
        audioThread = new Thread(() -> audioPlayer.randomLoop());

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        backButton = new JButton("Zurück");
        backButton.setBackground(GUIConstants.BUTTON_COLOR);
        backButton.setLocation(20, getHeight() - backButton.getHeight() - 20);
        backButton.addActionListener(a -> {
            audioThread.stop();
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
        });

        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                keyStates.put(keyEvent.getKeyChar(), true);
            }

            public void keyReleased(KeyEvent keyEvent) {
                keyStates.put(keyEvent.getKeyChar(), false);
            }
        });

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                paused = false;
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                paused = true;
            }
        });

        new Thread(this).start();
    }

    public void run() {
        audioThread.start();
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

                render();
                frameCount++;

                /*
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

        for (Map.Entry<Character, Boolean> entry : keyStates.entrySet()) {
            switch (entry.getKey()) { // TODO alle Bewegungen implementieren
                case 'a':
                    if (entry.getValue()) {
                        walking = true;
                        if (level.getPlayer().getPosition().getX() - getWidth() / 2 > 0) {
                            viewport.setRect(viewport.x - 2.5, viewport.y, viewport.width, viewport.height);
                            level.getPlayer().move(-2.5, 0);
                            walkCount++;
                        }
                        looksLeft = true;

                    } else {
                        walking = false;
                    }
                    break;
                case 'd':
                    if (entry.getValue()) {
                        walking = true;
                        if (level.getPlayer().getPosition().getX() + getWidth() / 2 < level.getLength()) {
                            viewport.setRect(viewport.x + 2.5, viewport.y, viewport.width, viewport.height);
                            level.getPlayer().move(2.5, 0);
                            walkCount++;
                        }
                        looksLeft = false;
                    } else {
                        walking = false;
                    }
                    break;
                case 'w':
                    jumping = true;
                    if (entry.getValue() && countToNextJump == 0) {
                        if (level.getPlayer().getPosition().getY() > 400 && jumpAmount > 0)
                            level.getPlayer().move(0, -jumpAmount);
                        else {
                            if (level.getPlayer().getPosition().getY() < GameConstants.GROUND_LEVEL) {
                                if (GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY() >= Math.abs(jumpAmount)) {
                                    jumpAmount -= 0.5;
                                    level.getPlayer().move(0, -jumpAmount);
                                } else
                                    level.getPlayer().move(0, GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                            } else {
                                jumpAmount = 8;
                                jumping = false;
                            }
                        }
                    } else {
                        if (level.getPlayer().getPosition().getY() < GameConstants.GROUND_LEVEL) {
                            if (GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY() >= Math.abs(jumpAmount)) {
                                jumpAmount -= 0.5;
                                level.getPlayer().move(0, -jumpAmount);
                            } else
                                level.getPlayer().move(0, GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                        } else {
                            jumpAmount = 8;
                            jumping = false;
                        }
                    }
                    break;
                case 's':
                    //ducken
                    break;
            }

        }

        if (walkCount >= 15) {
            walkFlag = !walkFlag;
            walkCount = 0;
        }
        System.out.println(walking);

        /*
        if (level.getPlayer().getPosition().getY() < GameConstants.GROUND_LEVEL) {
            if (GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY() < 5)
                level.getPlayer().move(0, GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
            else
                level.getPlayer().move(0, 5);
        } else {
            jumpAmount = 16;
        }*/

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

    public void render() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        try {
            BufferedImage image = ImageUtil.getImage(level.getBackgroundFilePath());
            // Verarbeitung des aktuell darzustellenden Subimages
            double rel = (double) getWidth() / (double) getHeight();
            //image = image.getSubimage(0, 0, (int) Math.round(rel * image.getHeight()), image.getHeight());

            // Zeichnen des Subimages
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            double factor = getHeight() / (double) height; // Skalierungsfaktor
            g2.drawImage(image, -(int) viewport.getX(), 0, (int) (width * factor), (int) (height * factor), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 1. Sidescroll

        // 2. Draw Background

        // 3. Draw Baseline

        // 4. Draw Player

        try {
            BufferedImage image;
            if (jumping)
                image = ImageUtil.getImage("images/char/char_jump_0.66.png");
            else if (walking) {
                if (walkFlag)
                    image = ImageUtil.getImage("images/char/char_walk_1_0.66.png");
                else
                    image = ImageUtil.getImage("images/char/char_walk_2_0.66.png");
            } else {
                image = ImageUtil.getImage("images/char/char_stand_0.66.png");
            }
            int playerX = (int) Math.round(level.getPlayer().getPosition().getX() - image.getWidth() / 2 - viewport.getX());
            int playerY = (int) Math.round(level.getPlayer().getPosition().getY() - image.getHeight());
            if (!looksLeft)
                g2.drawImage(image, playerX, playerY, image.getWidth(), image.getHeight(), this);
            else
                g2.drawImage(image, playerX + image.getWidth(), playerY, -image.getWidth(), image.getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stroke backup = g2.getStroke();
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        Rectangle2D playerHitbox = level.getPlayer().getHitbox();
        g2.drawRect((int) Math.round(playerHitbox.getX() - viewport.x), (int) Math.round(playerHitbox.getY()),
                (int) Math.round(playerHitbox.getWidth()), (int) Math.round(playerHitbox.getHeight()));
        g2.setStroke(backup);

        g2.setColor(Color.BLACK);
        g2.drawString("Sidescroller " + GUIConstants.GAME_VERSION, 20, 20);
        String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
        g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);

        String playerPosition = "Player position: " + level.getPlayer().getPosition();
        g2.drawString(playerPosition, getWidth() / 2 - g2.getFontMetrics().stringWidth(playerPosition) / 2, 20);
    }
}
