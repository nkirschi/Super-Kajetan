/**
 * @author Kirschi
 */

public class Game {
    private boolean running;

    /**
     * Die öffentliche statische Leere namens "main"
     * @param args Kommondozeilenargumente
     */
    public static void main(String[] args) {
        new Game().start();
    }

    /**
     * Diese Methode startet das Spiel :O
     */
    private void start() {
        init();
        while (running) {
            update();
            render();
        }
    }

    /**
     * Diese Methode stoppt das Spiel!!
     */
    public void stop() {
        running = false;
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
