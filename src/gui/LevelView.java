package gui;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import model.Camera;
import model.Direction;
import model.Level;
import util.Constants;
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

    private double jumpAmount = Constants.PLAYER_JUMP_AMOUNT;

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
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
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

        final long TIME_PER_UPDATE = 1000000000 / Constants.UPDATE_CLOCK;
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

        level.getPlayer().setWalking(false);

        for (Map.Entry<Integer, Boolean> entry : keyStates.entrySet()) {
            switch (entry.getKey()) { // TODO alle Bewegungen implementieren
                case Constants.KEY_LEFT:
                    if (entry.getValue()) {
                        //if (!level.getPlayer().isJumping())
                            level.getPlayer().setWalking(true);//walking = true;
                        double moveAmount = 0;
                        if (level.getPlayer().getPosition().getX() - getWidth() / 2 > Constants.PLAYER_MOVE_AMOUNT) {
                            moveAmount = level.getPlayer().isJumping() ? 2 * Constants.PLAYER_MOVE_AMOUNT : Constants.PLAYER_MOVE_AMOUNT;
                        } else {
                            moveAmount = level.getPlayer().getPosition().getX() - getWidth() / 2;
                            level.getPlayer().setWalking(false);
                        }
                        camera.scroll(-moveAmount);
                        level.getPlayer().move(-moveAmount, 0);
                        level.getPlayer().setViewingDirection(Direction.LEFT);
                    }
                    break;
                case Constants.KEY_RIGHT:
                    if (entry.getValue()) {
                        //if (!level.getPlayer().isJumping())
                            level.getPlayer().setWalking(true);//walking = true;
                        double moveAmount = 0;
                        if (level.getPlayer().getPosition().getX() + getWidth() / 2 < level.getLength() - Constants.PLAYER_MOVE_AMOUNT) {
                            moveAmount = level.getPlayer().isJumping() ? 2 * Constants.PLAYER_MOVE_AMOUNT : Constants.PLAYER_MOVE_AMOUNT;
                        } else {
                            moveAmount = (int) level.getLength() - getWidth() / 2 - level.getPlayer().getPosition().getX();
                            level.getPlayer().setWalking(false);
                        }
                        camera.scroll(moveAmount);
                        level.getPlayer().move(moveAmount, 0);
                        level.getPlayer().setViewingDirection(Direction.RIGHT);
                    }
                    break;
                case Constants.KEY_JUMP:
                    if (entry.getValue()) {
                        level.getPlayer().setWalking(false);
                        level.getPlayer().setJumping(true);//jumping = true;
                        if (level.getPlayer().getPosition().getY() > 400 && jumpAmount > 0 && jumpAmount == Constants.PLAYER_JUMP_AMOUNT)
                            level.getPlayer().move(0, -jumpAmount);
                        else {
                            if (level.getPlayer().getPosition().getY() < Constants.GROUND_LEVEL) {
                                if (Constants.GROUND_LEVEL - level.getPlayer().getPosition().getY() >= Math.abs(jumpAmount)) {
                                    jumpAmount -= 1;
                                    level.getPlayer().move(0, -jumpAmount);
                                } else
                                    level.getPlayer().move(0, Constants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                            } else {
                                jumpAmount = Constants.PLAYER_JUMP_AMOUNT;
                                level.getPlayer().setJumping(false);//jumping = false;
                            }
                        }
                    } else {
                        if (level.getPlayer().getPosition().getY() < Constants.GROUND_LEVEL) {
                            level.getPlayer().setWalking(false);
                            if (Constants.GROUND_LEVEL - level.getPlayer().getPosition().getY() >= Math.abs(jumpAmount)) {
                                jumpAmount -= 1;
                                level.getPlayer().move(0, -jumpAmount);
                            } else
                                level.getPlayer().move(0, Constants.GROUND_LEVEL - level.getPlayer().getPosition().getY());
                        } else {
                            level.getPlayer().setJumping(false);//jumping = false;
                        }
                    }
                    break;
                case Constants.KEY_CROUCH:
                    if (entry.getValue()) {
                        level.getPlayer().setCrouching(true);
                    } else {
                        level.getPlayer().setCrouching(false);
                    }
                    break;
                case Constants.KEY_RUN:
                    if (entry.getValue() && !level.getPlayer().isJumping()) {
                        level.getPlayer().setRunning(true);
                        int signum = level.getPlayer().getViewingDirection().equals(Direction.RIGHT) ? 1 : -1;
                        camera.scroll(signum * Constants.PLAYER_MOVE_AMOUNT);
                        level.getPlayer().move(signum * Constants.PLAYER_MOVE_AMOUNT, 0);
                    } else {
                        level.getPlayer().setRunning(false);
                    }
            }

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
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2.drawRect((int) Math.round(level.getEnemies().get(0).getHitbox().getX() - camera.getX()),
                (int) Math.round(level.getEnemies().get(0).getHitbox().getY()),
                (int) Math.round(level.getEnemies().get(0).getHitbox().getWidth()),
                (int) Math.round(level.getEnemies().get(0).getHitbox().getHeight()));
        g2.setStroke(originalStroke);

        // 5. Draw Obstacles

        // 6. Draw Onscreen Info
        g2.setColor(Color.BLACK);
        g2.drawString("Sidescroller " + Constants.GAME_VERSION, 20, 20);
        String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
        g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);
        g2.drawString(level.getPlayer().toString(), getWidth() / 2 - g2.getFontMetrics().stringWidth(level.getPlayer().toString()) / 2, 20);
    }
}
