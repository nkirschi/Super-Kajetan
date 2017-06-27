package gui;

import model.Level;
import physics.GameConstants;
import util.AudioPlayer;
import util.ImageUtil;

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

    private boolean looksLeft;
    private boolean jumping;
    private double jumpAmount = 16;
    private int countToNextJump = 0;

    private boolean running;
    private boolean paused;
    private int hz, fps;

    LevelView(Level level) {
        this.level = level;
        viewport = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        keyStates = new HashMap<>();
        setDoubleBuffered(true);

        audioPlayer = new AudioPlayer();

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
        new Thread(() -> audioPlayer.loop("sounds/gottmituns.ogg")).start();
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
                        viewport.setRect(viewport.x - 2.5, viewport.y, viewport.width, viewport.height);
                        level.getPlayer().move(-2.5, 0);
                        looksLeft = true;
                    }
                    break;
                case 'd':
                    if (entry.getValue()) {
                        viewport.setRect(viewport.x + 2.5, viewport.y, viewport.width, viewport.height);
                        level.getPlayer().move(2.5, 0);
                        looksLeft = false;
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
                                    jumpAmount -= 1;
                                    level.getPlayer().move(0, -jumpAmount);
                                } else
                                    level.getPlayer().move(0, GameConstants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                            } else {
                                jumpAmount = 16;
                                jumping = false;
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
                            jumpAmount = 16;
                            jumping = false;
                        }
                    }
                    break;
                case 's':
                    //ducken
                    break;
            }

        }

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
                image = ImageUtil.getImage("images/char/char_jump.png");
            else
                image = ImageUtil.getImage("images/char/char_walk_or_stand.png");
            int playerX = (int) Math.round(level.getPlayer().getPosition().getX() - image.getWidth() / 2 - viewport.getX());
            int playerY = (int) Math.round(level.getPlayer().getPosition().getY() - image.getHeight());
            if (!looksLeft)
                g2.drawImage(image, playerX, playerY, image.getWidth(), image.getHeight(), this);
            else
                g2.drawImage(image, playerX + image.getWidth(), playerY, -image.getWidth(), image.getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AffineTransform at = g2.getTransform();

        g2.setColor(Color.BLACK);
        g2.drawString("Sidescroller " + GUIConstants.GAME_VERSION, 20, 20);
        String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
        g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);

        String playerPosition = "Player position: " + level.getPlayer().getPosition();
        g2.drawString(playerPosition, getWidth() / 2 - g2.getFontMetrics().stringWidth(playerPosition) / 2, 20);
    }

    public static void main(String[] args) {
        new LevelView(new Level("", new util.list.List<>(), new util.list.List<>(), 0)).run();

    }
}
