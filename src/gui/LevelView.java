package gui;

import model.Level;
import physics.GameConstants;
import sun.applet.Main;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Rectangle2D.Double viewport; // Die aktuelle "Kamera"
    private HashMap<Character, Boolean> keyStates;

    private boolean running;
    private boolean paused;

    LevelView(Level level) {
        setLayout(new BorderLayout());
        this.level = level;
        viewport = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        keyStates = new HashMap<>();
        setDoubleBuffered(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                keyStates.put(keyEvent.getKeyChar(), true);
            }

            public void keyReleased(KeyEvent keyEvent) {
                keyStates.put(keyEvent.getKeyChar(), false);
            }
        });

        new Thread(this).start();
    }

    public void run() {
        running = true;
        int updateCount = 0;
        int frameCount = 0;
        long secondTime = 0;

        final long TIME_PER_UPDATE = 1000000000 / GameConstants.UPDATE_CLOCK;
        long lastTime = System.nanoTime();
        long lag = 0;

        while (running) {
            long currentTime = System.nanoTime();

            while (!paused) {
                currentTime = System.nanoTime();
                long elapsedTime = currentTime - lastTime;
                lastTime = currentTime;
                lag += elapsedTime;
                secondTime += elapsedTime;
                if (secondTime > 1000000000) {
                    //System.out.println(updateCount + "\u2009Hz, " + frameCount + "\u2009fps");
                    MainFrame.getInstance().setTitle("Sidescroller Alpha v1.1.2_01 [" + updateCount + "\u2009Hz, " + frameCount + "\u2009fps]");
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
            }
        }
    }

    public void update() {

        // 1. Move Player + Gravitation + check Collision
        // Tastaturcheck, altobelli!

        for (Map.Entry<Character, Boolean> entry : keyStates.entrySet()) {
            if (!entry.getValue())
                continue;
            switch (entry.getKey()) { // TODO alle Bewegungen implementieren
                case 'a':
                    viewport.setRect(viewport.x - 2.5, viewport.y, viewport.width, viewport.height);
                    level.getPlayer().move(-2.5, 0);
                    break;
                case 'd':
                    viewport.setRect(viewport.x + 2.5, viewport.y, viewport.width, viewport.height);
                    level.getPlayer().move(2.5, 0);
                    break;
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

    public void render() {
        Graphics2D g2 = (Graphics2D) getGraphics();

        if (g2 == null)
            return;

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

        // 5. Draw Enemies

        // 6. Draw Obstacles

        g2.dispose();
    }

    public static void main(String[] args) {
        new LevelView(new Level("", new util.list.List<>(), new util.list.List<>(), 0)).run();

    }
}
