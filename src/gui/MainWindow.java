package gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kirschi
 */
public class MainWindow extends JFrame {
    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d.width / 2, d.height - 400);
        setLocation(d.width / 2 - d.width / 4, d.height / 2 - ((d.height - 400) / 2));
    }
}
