package gui;

/**
 * Hier wurde ein bisschen rumprobiert ^^
 */

public class Test implements Runnable {
    private static Test level;
    private boolean running;
    private boolean paused;

    private Test() {
        level = this;
    }

    public static Test getInstance() {
        if (level == null)
            level = new Test();
        return level;
    }

    public void start() {
        if (running)
            return;
        new Thread(this).start();
    }

    /**
     * Diese Methode pausiert bzw. setzt das Spiel fort!!
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * Gameloop
     */
    public void run() {
        init();
        long NS_PER_UPDATE = 20000;
        long frames = 0;
        long frameTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        long currentTime = 0;
        long elapsedTime = 0;
        long lag = 0; // Der Zeitunterschied zwischen Real Life und Spielwelt
        while (running) {

            currentTime = System.nanoTime();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            if (paused)
                continue;

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
