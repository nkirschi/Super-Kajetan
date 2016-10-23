package gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kirschi
 */
public class MainWindow extends JFrame {
    /**
     * Die öffentliche statische Leere namens "main"
     * @param args Ein Feld von Fäden namens "aarrrgghs"
     */
    public static void main(String[] args) {
        new MainWindow();
    }

    public MainWindow() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d.width / 2, d.height - 400);
        setLocation(d.width / 2 - d.width / 4, d.height / 2 - ((d.height - 400) / 2));
        setVisible(true);
    }
}
