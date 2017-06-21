package gui;

import model.Level;
import physics.GameConstants;
import util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LevelView extends AbstractView {
    private Level level;

    private int fps = 60;
    private int frameCount = 0;
    private boolean running;
    private boolean paused;

    LevelView(Level level, MainFrame mainFrame) {
        super(mainFrame);
        this.level = level;
        //run();
    }

    public void run() {
        running = true;

        //Calculate how many ns each frame should take for our target game hertz.
        final double TIME_BETWEEN_UPDATES = 1000000000 / GameConstants.GAME_HERTZ;
        //At the very most we will update the game this many times before a new render.
        //If you're worried about visual hitches more than perfect timing, set this to 1.
        final int MAX_UPDATES_BEFORE_RENDER = 5;

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();

        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / GameConstants.TARGET_FPS;

        //Simple way of finding FPS.
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;

            if (!paused) {
                //Do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    update();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }

                render();
                lastRenderTime = now;

                //Update the frames we got.
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime) {
                    System.out.println("In second " + thisSecond + ": " + frameCount + " fps");
                    fps = frameCount;
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();

                    //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                    //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                    //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                    }

                    now = System.nanoTime();
                }
            }
        }
    }

    public void update() {
        // 1. Move Player + Gravitation + check Collision
        // Tastaturcheck, altobelli!
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
        // 1. Sidescroll

        // 2. Draw Background

        // 3. Draw Baseline

        // 4. Draw Player

        // 5. Draw Enemies

        // 6. Draw Obstacles
        frameCount++;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            BufferedImage image = ImageUtil.getImage(level.getBackgroundFilePath());

            // Verarbeitung des aktuell darzustellenden Subimages
            double rel = (double) getWidth() / (double) getHeight();
            image = image.getSubimage(100, 0, (int) Math.round(rel * image.getHeight()), image.getHeight());

            // Zeichnen des Subimages
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            double factor = getHeight() / (double) height; // Skalierungsfaktor
            g2.drawImage(image, 0, 0, (int) (width * factor), (int) (height * factor), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LevelView(new Level("", new util.list.List<>(), new util.list.List<>(), 0), null).run();

    }
}
