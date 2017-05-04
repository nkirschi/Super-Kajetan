package gui;

import javax.swing.*;

/**
 * Abstrakte Klasse für eine abstrakte Ansicht
 */
public abstract class AbstractView extends JPanel {
    protected MainFrame mainFrame;

    /**
     * @param mainFrame Das für den weiteren Zugriff benötigte Hauptfenster
     */
    public AbstractView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    protected abstract void update();
}
