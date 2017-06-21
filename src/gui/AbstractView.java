package gui;

import javax.swing.*;

/**
 * Abstrakte Klasse für eine abstrakte Ansicht
 */
abstract class AbstractView extends JPanel {
    protected MainFrame mainFrame;

    /**
     * @param mainframe Das für den weiteren Zugriff benötigte Hauptfenster
     */
    AbstractView(MainFrame mainframe) {
        this.mainFrame = mainframe;
    }

    public abstract void update();
}
