package gui;

import model.Level;
import physics.GameConstants;
import util.ImageUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class LevelView extends AbstractView implements Runnable {
    private Level level;
    private Rectangle2D.Double viewport; // Die aktuelle "Kamera"
    private HashMap<Character, Boolean> keyStates;
    private boolean aPressed = false;

    private int updateCount = 0;
    private int frameCount = 0;
    private boolean running;
    private boolean paused;

    LevelView(Level level) {
        this.level = level;
        viewport = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        keyStates = new HashMap<>();
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

        // Zeit pro Update
        final double TIME_BETWEEN_UPDATES = 1000000000 / GameConstants.UPDATE_FREQ;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / GameConstants.TARGET_FPS;

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();

        // Performante FPS-Bestimmung
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (running) {
            double currentTime = System.nanoTime();

            if (!paused) {
                // So viele Spielupdates wie nötig, evtl. Catchup
                while (currentTime - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    update();
                    updateCount++;
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                }

                // Falls ein Update zu lange gebraucht hat, wird hier das nachfolgende übersprungen
                if (currentTime - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = currentTime - TIME_BETWEEN_UPDATES;
                }

                render();
                frameCount++;
                lastRenderTime = currentTime;

                // Framezahl-Update
                int thisSecondTime = (int) (lastUpdateTime / 1000000000);
                if (thisSecondTime > lastSecondTime) {
                    System.out.println(updateCount + "\u2009u/s, " + frameCount + "\u2009fps");
                    updateCount = 0;
                    frameCount = 0;
                    lastSecondTime = thisSecondTime;
                }


                // Dieser Abschnitt sorgt im Grunde für VSync auf 60fps
                while (currentTime - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && currentTime - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield(); // Ressourcenfreigabe für andere Prozesse
                    try {
                        Thread.sleep(1); // Zeitvertreib bis zum nächsten Gameloop-Durchlauf
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentTime = System.nanoTime();
                }
            }
        }
    }

    public void update() {

        // 1. Move Player + Gravitation + check Collision
        // Tastaturcheck, altobelli!

        for (Map.Entry<Character, Boolean> entry : keyStates.entrySet()) {
            switch (entry.getKey()) {
                case 'a':
                    if (entry.getValue())
                        viewport.setRect(viewport.x - 2.5, viewport.y, viewport.width, viewport.height);
                    break;
                case 'd':
                    if (entry.getValue())
                        viewport.setRect(viewport.x + 2.5, viewport.y, viewport.width, viewport.height);
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
