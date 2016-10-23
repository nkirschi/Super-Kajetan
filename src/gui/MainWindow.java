package gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kirschi
 */
public class MainWindow extends JFrame {

    private final int WIDTH = 1024, HEIGHT = 768;
    private MenuView menuView;

    /**
     * Die öffentliche statische Leere namens "main"
     * @param args Ein Feld von Fäden namens "aarrrgghs"
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new MainWindow();
    }

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 2 - WIDTH / 2, d.height / 2 - HEIGHT / 2);

        menuView = new MenuView();
        add(menuView);

        setVisible(true);
    }
}
