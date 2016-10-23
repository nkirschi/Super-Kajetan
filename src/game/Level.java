package game;

/**
 * @author Kirschi
 */

public class Level {
    private boolean running;
    private boolean paused;

    /**
     * Gameloop
     */
    public void run() {
        init();
        long NS_PER_UPDATE = 20000;
        long frames = 0;
        long frameTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        long lag = 0; // Der Zeitunterschied zwischen Real Life und Spielwelt
        while (running) {
            if (paused)
                continue;

            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            for (lag += elapsedTime; lag >= NS_PER_UPDATE; lag -= NS_PER_UPDATE)
                update();

            render();
            frames++;

            if (System.currentTimeMillis() - frameTime >= 1000) {
                System.out.println(frames + " FPS");
                frames = 0;
                frameTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Diese Methode stoppt das Spiel!!
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * Initialisierungsgedöns
     */
    private void init() {
        running = true;
    }

    /**
     * Spieleupdates
     *  - Benutzerinput verarbeiten
     *  - Veränderungen im Spiel berechnen
     *  - Postprocessing (v.a. Physik)
     */
    private void update() {
        //...
    }

    /**
     * Rendern des aktuellen Spielstatus
     *  - Nicht sichtbare Objekte erfassen
     *  - Sichtbare Objekte verarbeiten
     *  - Auf Hintergrundpuffer zeichnen
     *  - Puffer als Gesamtheit anzeigen
     */
    private void render() {
        //...
    }
}
