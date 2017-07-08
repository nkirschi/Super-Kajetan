package gui;

import model.*;
import util.Constants;
import util.ImageUtil;
import util.Logger;
import util.SoundUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.Timer;


public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Player player;
    private Camera camera; // Die aktuelle "Kamera"
    private Timer timer;

    private double verticalMoveAmount = -Constants.PLAYER_INITIAL_JUMP_VELOCITY;
    private double fallMoveAmount = 0;
    private boolean jumpingPossible = true;
    private boolean runningPossible = true;
    private boolean crouchingPossible = true;
    private boolean exhausted;

    private boolean left, right, run, jump, crouch;

    private boolean running;
    private boolean paused;
    private int hz = 60, fps = 60;

    LevelView(Level level) {
        this.level = level;
        player = new Player(LobbyView.getInstance().getWidth() / 2, Constants.GROUND_LEVEL);
        camera = new Camera(0, 0, getWidth(), getHeight());

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
            timer.cancel();
            timer.purge();
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
        });
        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);

        add(buttonPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int keyCode = keyEvent.getKeyCode();

                if (keyCode == Constants.KEY_LEFT)
                    left = true;
                else if (keyCode == Constants.KEY_RIGHT)
                    right = true;
                else if (keyCode == Constants.KEY_RUN)
                    run = true;
                else if (keyCode == Constants.KEY_JUMP)
                    jump = true;
                else if (keyCode == Constants.KEY_CROUCH)
                    crouch = true;
            }

            public void keyReleased(KeyEvent keyEvent) {
                int keyCode = keyEvent.getKeyCode();

                if (keyCode == Constants.KEY_LEFT)
                    left = false;
                else if (keyCode == Constants.KEY_RIGHT)
                    right = false;
                else if (keyCode == Constants.KEY_RUN)
                    run = false;
                else if (keyCode == Constants.KEY_JUMP)
                    jump = false;
                else if (keyCode == Constants.KEY_CROUCH)
                    crouch = false;
            }
        });

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                paused = false;
                SoundUtil.unpause();
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                paused = true;
                left = false;
                right = false;
                run = false;
                jump = false;
                crouch = false;
                SoundUtil.pause();
            }
        });

        Logger.log("Level initialisiert", Logger.INFO);
        //new Thread(this).start();

        SoundUtil.loop();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused) {
                    update();
                    repaint();
                }
            }
        }, 0, 1000 / 60);
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

                /* Theoretisch VSync - is aber bissl laggy :(
                while (System.nanoTime() - currentTime < 1000000000 / 60) {
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
        player.setVelocityX(0);
        player.setWalking(false);
        player.setRunning(false);
        player.setCrouching(false);

        // 2. Input Handling
        if (left) {
            player.addVelocityX(-Constants.PLAYER_WALK_VELOCITY);
            if (!right)
                player.setViewingDirection(Direction.LEFT);
            player.setWalking(true);
        }

        if (right) {
            player.addVelocityX(Constants.PLAYER_WALK_VELOCITY);
            if (!left)
                player.setViewingDirection(Direction.RIGHT);
            player.setWalking(true);
        }

        if (run) {
            player.setRunning(true);
        }

        if (jump && player.isOnGround()) {
            player.setVelocityY(-Constants.PLAYER_INITIAL_JUMP_VELOCITY);
            player.setOnGround(false);
            player.setRunning(false);
            player.setJumping(true);
            System.out.println(player.getVelocityX());
        } else if (!jump && !player.isOnGround()) {
            if (player.getVelocityY() < -6)
                player.setVelocityY(-6);
        }

        if (crouch) {
            player.addVelocityX(-player.getVelocityX() / 2);
            player.setCrouching(true);
        }

        if (player.isRunning() || player.isJumping())
            player.multiplyVelocityX(Constants.SPEED_FACTOR);

        // 3. General Gravitation TODO Gravitation für jedermann
        player.addVelocityY(Constants.GRAVITATIONAL_ACCELERATION);
        player.setOnGround(false);


        // 4. Kollision - vorerst nur Bodenelemente
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
                player.setWalking(false);
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
            }
        }

        // 5. Änderungen vornehmen
        player.move();
        camera.scroll(player.getVelocityX());

        if (player.getY() > 1000) {
            timer.cancel();
            timer.purge();
            MainFrame.getInstance().changeTo(LobbyView.getInstance());
            JOptionPane.showMessageDialog(MainFrame.getInstance().getCurrentView(), "Game over!", "Pech", JOptionPane.INFORMATION_MESSAGE);
        }

        // 1. Move Player + Gravitation + check Collision
        // Tastaturcheck, altobelli!

        /*
        player.setWalking(false);
        player.setRunning(false);
        player.setCrouching(false);

        double xMovement = 0;
        double yMovement = 0;


        if (!pressedKeys.contains(Constants.KEY_RUN) && !pressedKeys.contains(Constants.KEY_JUMP) && !pressedKeys.contains(Constants.KEY_CROUCH)) {
            runningPossible = true;
            jumpingPossible = true;
            crouchingPossible = true;
        }


        if (player.getStamina() < 10) {
            runningPossible = false;
            jumpingPossible = false;
            crouchingPossible = false;
        }

        for (int keyCode : pressedKeys) {
            if (keyCode == Constants.KEY_LEFT) {
                player.setWalking(true);
                xMovement -= Constants.PLAYER_WALK_VELOCITY;

            }
            if (keyCode == Constants.KEY_RIGHT) {
                player.setWalking(true);
                xMovement += Constants.PLAYER_WALK_VELOCITY;

            }
            if (keyCode == Constants.KEY_JUMP) {
                if (jumpingPossible && verticalMoveAmount < 0) {
                    fallMoveAmount = 0;
                    player.setWalking(false);
                    player.setRunning(false);
                    player.setJumping(true);
                    yMovement += verticalMoveAmount;
                }

            }
            if (keyCode == Constants.KEY_RUN) {
                if (runningPossible)
                    player.setRunning(true);

            }
            if (keyCode == Constants.KEY_CROUCH) {
                if (crouchingPossible)
                    player.setCrouching(true);

            }
        }

        if ((player.isJumping() || player.isRunning() && !player.isCrouching()))
            xMovement *= 2;

        if (player.isCrouching() && !player.isJumping())
            xMovement /= 2;

        if (xMovement < 0)
            player.setViewingDirection(Direction.LEFT);
        else if (xMovement > 0)
            player.setViewingDirection(Direction.RIGHT);
        else
            player.setRunning(false);


        camera.scroll(xMovement);
        player.move(xMovement, 0);

        Collidable collidable = null;
        for (Ground ground : level.getGrounds()) {
            if (player.collidesWith(ground)) {
                collidable = ground;
                break;
            }
        }

        if (collidable != null) {
            System.out.println(collidable);
            double d = 0;
            if (xMovement > 0) {
                d = collidable.getHitbox().getX()
                        - player.getHitbox().getX() - player.getHitbox().getWidth();
            } else if (xMovement < 0) {
                d = collidable.getHitbox().getX() + collidable.getHitbox().getWidth() -
                        player.getHitbox().getX();
            }
            player.move(d, 0);
            camera.scroll(d);
        }

        player.move(0, yMovement);


        if (player.getPosition().getX() < getWidth() / 2) {
            double d = getWidth() / 2 - player.getPosition().getX();
            player.move(d, 0);
            camera.scroll(d);
            player.setWalking(false);
            player.setRunning(false);
        } else if (player.getPosition().getX() > level.getLength() - getWidth() / 2) {
            double d = level.getLength() - getWidth() / 2 - player.getPosition().getX();
            player.move(Math.round(d), 0);
            camera.scroll(Math.round(d));
            player.setWalking(false);
            player.setRunning(false);
        }

        for (Ground ground : level.getGrounds()) {
            if (player.collidesWith(ground)) {
                collidable = ground;
                break;
            }
        }

        if (collidable != null) {

        }


        // Ausdauerverbrauch
        if (player.isWalking() && !player.isJumping() && !player.isCrouching())
            player.setStamina(player.getStamina() - 1);

        if (player.isCrouching())
            player.setStamina(player.getStamina() - 2);
        else if (player.isRunning() || player.isJumping()) {
            player.setStamina(player.getStamina() - 3);
        }

        if (player.isJumping() && yMovement >= 0)
            player.setStamina(player.getStamina() + 2);

        if (!player.isRunning() && !player.isJumping() && !player.isCrouching()) {
            player.setStamina(player.getStamina() + 2);
        }


        if (!pressedKeys.contains(Constants.KEY_JUMP) && player.isJumping())
            jumpingPossible = false;
        */
        // Gravitation
        /*if (player.getPosition().getY() < Constants.GROUND_LEVEL) {
            if (Constants.GROUND_LEVEL - player.getPosition().getY() >= verticalMoveAmount) {
                player.setWalking(false);
                player.setRunning(false);
                player.setJumping(true);
                player.move(0, verticalMoveAmount);

            } else {
                player.setJumping(false);
                player.move(0, Constants.GROUND_LEVEL - player.getPosition().getY());
                verticalMoveAmount = -Constants.PLAYER_INITIAL_JUMP_VELOCITY;
                jumpingPossible = true;
            }
        }*/

        /*
        player.move(0, fallMoveAmount);

        boolean collisionHappened = false;
        for (Ground ground : level.getGrounds()) {
            if (player.collidesWith(ground)) {
                player.move(0, ground.getHitbox().getY() - player.getPosition().getY());
                collisionHappened = true;
            }
        }

        if (collisionHappened) {
            player.setJumping(false);
            verticalMoveAmount = -Constants.PLAYER_INITIAL_JUMP_VELOCITY;
            fallMoveAmount = 0;
        } else {
            if (player.isJumping() && !pressedKeys.contains(Constants.KEY_JUMP) && verticalMoveAmount < 0) {
                player.move(0, verticalMoveAmount);
            } else if (!player.isJumping() || verticalMoveAmount >= 0)
                fallMoveAmount += Constants.GRAVITATIONAL_ACCELERATION;
            verticalMoveAmount += Constants.GRAVITATIONAL_ACCELERATION;
        }
*/
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

        // 6. Draw Onscreen Info
        g2.setColor(Color.BLACK);
        g2.drawString("Sidescroller " + Constants.GAME_VERSION, 20, 20);
        String debugInfo = hz + "\u2009Hz, " + fps + "\u2009fps";
        g2.drawString(debugInfo, getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);
        g2.drawString(player.toString(), getWidth() / 2 - g2.getFontMetrics().stringWidth(player.toString()) / 2, 20);

        Rectangle2D staminaMask = new Rectangle2D.Double(getWidth() - 220, getHeight() - 30, 200, 15);
        Rectangle2D staminaBar = new Rectangle2D.Double(getWidth() - 220, getHeight() - 30, player.getStamina() / 5, 15);
        g2.setColor(Constants.BUTTON_COLOR);
        g2.fill(staminaMask);
        g2.setColor(Constants.MENU_BACKGROUND_COLOR);
        g2.fill(staminaBar);
        g2.setColor(Color.BLACK);

        g2.drawString("jumpMoveAmount = " + verticalMoveAmount, 10, 50);
        g2.drawString("fallMoveAmount = " + fallMoveAmount, 10, 70);

        g2.setFont(Constants.DEFAULT_FONT);
        g2.drawString("Ausdauer: " + player.getStamina() / 10 + "%", getWidth() - 215, getHeight() - 17);
    }
}