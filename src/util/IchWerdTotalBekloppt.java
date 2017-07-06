package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class IchWerdTotalBekloppt extends JPanel {
    private static final int MOVE_VELOCITY = 3;
    private static final int INITIAL_JUMP_VELOCITY = -16;
    private static final int JUMP_TERMINATION_VELOCITY = -6;
    private boolean left, right, jump, onGround = true;
    private double posX = 400, posY = 480;
    private double velX = 0, velY = 0;
    private final double GRAVITY = 0.5;
    private Rectangle2D.Double player;
    private ArrayList<Rectangle2D.Double> gameObjects;

    public IchWerdTotalBekloppt() {
        player = new Rectangle2D.Double(posX, posY, 10, 20);
        gameObjects = new ArrayList<>();
        gameObjects.add(new Rectangle2D.Double(40, 150, 20, 20));
        gameObjects.add(new Rectangle2D.Double(700, 300, 10, 20));
        gameObjects.add(new Rectangle2D.Double(350, 250, 20, 10));
        gameObjects.add(new Rectangle2D.Double(550, 400, 30, 10));
        gameObjects.add(new Rectangle2D.Double(200, 480, 10, 20));
        gameObjects.add(new Rectangle2D.Double(0, 500, 800, 10));
        gameObjects.add(new Rectangle2D.Double(0, 0, 10, 510));
        gameObjects.add(new Rectangle2D.Double(774, 0, 10, 510));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_A:
                        left = true;
                        break;
                    case KeyEvent.VK_D:
                        right = true;
                        break;
                    case KeyEvent.VK_W:
                        jump = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_A:
                        left = false;
                        break;
                    case KeyEvent.VK_D:
                        right = false;
                        break;
                    case KeyEvent.VK_W:
                        jump = false;
                        break;
                }
            }
        });

        setBackground(new Color(0, 2, 47));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
                update();
            }
        }, 0, 1000 / 60);
    }

    public void update() {
        velX = 0;

        if (left && posX > 2) {
            velX -= MOVE_VELOCITY;
        }
        if (right && posX < 780)
            velX += MOVE_VELOCITY;

        if (jump && onGround) {
            velY = INITIAL_JUMP_VELOCITY;
            onGround = false;
        } else if (!jump && !onGround) {
            if (velY < JUMP_TERMINATION_VELOCITY)
                velY = -6;
        }

        velY += GRAVITY;
        onGround = false;

        Rectangle2D.Double dummy = new Rectangle2D.Double(posX + velX, player.getY(), player.getWidth(), player.getHeight());
        for (Rectangle2D.Double gameObject : gameObjects) {
            if (dummy.intersects(gameObject)) {
                if (velX > 0) {
                    velX = gameObject.getX() - player.getX() - player.getWidth();
                } else if (velX < 0) {
                    velX = gameObject.getX() + gameObject.getWidth() - player.getX();
                }
            }
        }

        dummy.setRect(player.getX(), posY + velY, player.getWidth(), player.getHeight());
        for (Rectangle2D.Double gameObject : gameObjects) {
            if (dummy.intersects(gameObject)) {
                if (velY > 0) {
                    posY = gameObject.getY() - player.getHeight();
                    onGround = true;
                    velY = 0;
                } else if (velY < 0) {
                    posY = gameObject.getY() + gameObject.getHeight();
                    velY = 0;
                }
            }
        }

        posX += velX;
        posY += velY;
        player.setRect(posX, posY, 10, 20);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(99, 202, 255));
        g2.fill(player);
        g2.drawString("x = " + posX + "; y = " + posY, 25, 20);
        g2.setColor(Color.LIGHT_GRAY);
        for (Rectangle2D gameObject : gameObjects)
            g2.fill(gameObject);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        IchWerdTotalBekloppt ichWerdTotalBekloppt = new IchWerdTotalBekloppt();
        frame.add(ichWerdTotalBekloppt);
        ichWerdTotalBekloppt.setFocusable(true);
        ichWerdTotalBekloppt.requestFocusInWindow();
        frame.setVisible(true);
    }
}
