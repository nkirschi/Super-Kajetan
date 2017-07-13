package logic;

import gui.KeyHandler;
import gui.LevelView;
import model.*;
import util.Constants;
import util.ImageUtil;
import util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Renderer {
    private Level level;
    private Camera camera;
    private Player player;
    private KeyHandler keyHandler;
    private LevelView view;
    private final Stroke strichel;
    private int swordState;
    private int swordAngle;

    public Renderer(Level level, Camera camera, Player player, KeyHandler keyHandler, LevelView view) {
        this.level = level;
        this.camera = camera;
        this.player = player;
        this.keyHandler = keyHandler;
        this.view = view;
        strichel = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        swordState = -1;
        swordAngle = 0;
    }

    public void drawPlayer(Graphics2D g2) {
        try {
            BufferedImage image;
            image = ImageUtil.getImage(player.getImagePath());
            int playerX = (int) (player.getX() - image.getWidth() / 2 - camera.getX());
            int playerY = (int) (player.getY() - image.getHeight());
            if (player.getViewingDirection().equals(Direction.RIGHT))
                g2.drawImage(image, playerX, playerY, image.getWidth(), image.getHeight(), null);
            else
                g2.drawImage(image, playerX + image.getWidth(), playerY, -image.getWidth(), image.getHeight(), null);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(e, Logger.WARNING);
        }

        if (keyHandler.debug) {
            Stroke originalStroke = g2.getStroke();
            g2.setStroke(strichel);
            Rectangle2D playerHitbox = player.getHitbox();
            g2.drawRect((int) (playerHitbox.getX() - camera.x), (int) (playerHitbox.getY()),
                    (int) (playerHitbox.getWidth()), (int) (playerHitbox.getHeight()));
            g2.setStroke(originalStroke);
        }
    }

    public void drawSword(Graphics2D g2, int swordAngle) {
        try {
            BufferedImage image = ImageUtil.getImage("images/sword/sword_giant.png");

            AffineTransform backup = g2.getTransform();
            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(swordAngle), player.getSword().getX() + 28, player.getSword().getY() - 28 + player.getSword().getHeight());
            g2.transform(at);

            if (player.getViewingDirection().equals(Direction.RIGHT)) {
                g2.drawImage(image, (int) (player.getSword().getX() - camera.getX()), (int) player.getSword().getY(), null);
            } else {
                g2.drawImage(image, (int) (player.getSword().getX() + player.getSword().getWidth() - camera.getX()),
                        (int) player.getSword().getY(),
                        -image.getWidth(), image.getHeight(), null);
            }
            if (keyHandler.debug) {
                Stroke originalStroke = g2.getStroke();
                g2.setStroke(strichel);
                Rectangle2D.Double rect = new Rectangle2D.Double(player.getSword().getX() - camera.getX(),
                        player.getSword().getY(), player.getSword().getWidth(), player.getSword().getHeight());
                g2.draw(rect);
                g2.setStroke(originalStroke);
            }
            g2.setTransform(backup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawBackground(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        try {
            BufferedImage image = ImageUtil.getImage(level.getBackgroundFilePath());

            // Relation von Breite zu Höhe
            double rel = (double) view.getWidth() / (double) view.getHeight();

            int width = image.getWidth(null);
            int height = image.getHeight(null);
            double factor = view.getHeight() / (double) height; // Skalierungsfaktor

            g2.drawImage(image, -(int) camera.getX(), 0, (int) (width * factor), (int) (height * factor), null);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(e, Logger.WARNING);
        }

        // Nur ein Test :D
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/backgrounds/background.gif"));
        g2.drawImage(icon.getImage(), 0, 0, view.getWidth(), view.getHeight(), null);
    }

    public void drawGrounds(Graphics2D g2) {
        for (Ground ground : level.getGrounds()) {
            Rectangle2D.Double rectangle = new Rectangle2D.Double(ground.getHitbox().getX() - camera.getX(),
                    ground.getHitbox().getY(), ground.getHitbox().getWidth(), ground.getHitbox().getHeight());
            g2.drawImage(ground.getImage(), (int) rectangle.getX(), (int) rectangle.getY(), null);
            if (keyHandler.debug) {
                Stroke originalStroke = g2.getStroke();
                g2.setStroke(strichel);
                g2.draw(rectangle);
                g2.setStroke(originalStroke);
            }
        }
    }

    public void drawEnemies(Graphics2D g2) {
        for (Enemy enemy : level.getEnemies()) {
            try {
                BufferedImage image;
                image = ImageUtil.getImage(enemy.getImagePath());
                int x = (int) (enemy.getX() - image.getWidth() / 2 - camera.getX());
                int y = (int) (enemy.getY() - image.getHeight());
                if (enemy.getViewingDirection().equals(Direction.RIGHT))
                    g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
                else
                    g2.drawImage(image, x + image.getWidth(), y, -image.getWidth(), image.getHeight(), null);
                if (keyHandler.debug) {
                    Stroke originalStroke = g2.getStroke();
                    g2.setStroke(strichel);
                    Rectangle2D.Double rect = new Rectangle2D.Double(enemy.getHitbox().getX() - camera.getX(),
                            enemy.getHitbox().getY(), enemy.getHitbox().getWidth(), enemy.getHitbox().getHeight());
                    g2.draw(rect);
                    g2.setStroke(originalStroke);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log(e, Logger.WARNING);
            }
        }
    }

    public void drawObstacles(Graphics2D g2) {
        for (Obstacle obstacle : level.getObstacles()) {
            try {
                BufferedImage image = ImageUtil.getImage(obstacle.getImagePath());
                int x = (int) (obstacle.getX() - image.getWidth() / 2 - camera.getX());
                int y = (int) (obstacle.getY() - image.getHeight());
                g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);

                if (keyHandler.debug) {
                    Stroke originalStroke = g2.getStroke();
                    g2.setStroke(strichel);
                    g2.drawRect(x, y, image.getWidth(), image.getHeight());
                    g2.setStroke(originalStroke);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log(e, Logger.WARNING);
            }
        }
    }

    public void drawStaminaBar(Graphics2D g2) {
        Rectangle2D staminaMask = new Rectangle2D.Double(view.getWidth() - 220, view.getHeight() - 30, 200, 15);
        Rectangle2D staminaBar = new Rectangle2D.Double(view.getWidth() - 220, view.getHeight() - 30, player.getStamina() / 5, 15);
        g2.setColor(Constants.BUTTON_COLOR);
        g2.fill(staminaMask);
        g2.setColor(Constants.MENU_BACKGROUND_COLOR);
        g2.fill(staminaBar);
        g2.setColor(Color.BLACK);
        Font backup = g2.getFont();
        g2.setFont(Constants.DEFAULT_FONT);
        g2.drawString("Ausdauer: " + (int) (player.getStamina() / 10) + "%", view.getWidth() - 215, view.getHeight() - 17);
        g2.setFont(backup);
    }

    public void drawDebugScreen(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        String s = Constants.GAME_TITLE + " " + Constants.GAME_VERSION;
        g2.drawString(s, view.getWidth() / 2 - g2.getFontMetrics().stringWidth(s) / 2, 20);

        String debugInfo = view.getUps() + "\u2009u/s, " + view.getFps() + "\u2009fps";
        g2.drawString(debugInfo, view.getWidth() - g2.getFontMetrics().stringWidth(debugInfo) - 20, 20);

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
