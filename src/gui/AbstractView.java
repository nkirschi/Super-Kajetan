package gui;

import javax.swing.*;

/**
 * Abstrakte Klasse für eine abstrakte Ansicht
 */
abstract class AbstractView extends JPanel {

    AbstractView() {
    }

    public abstract void update();
}
