package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Klasse des Hauptfensters
 */
public class MainFrame extends JFrame implements WindowListener {

    private final int WIDTH = 1024, HEIGHT = 768;
    private AbstractView currentView;

    /**
     * Die öffentliche statische Leere namens "main"
     * @param args Ein Feld von Fäden namens "aarrrgghs"
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame();
    }

    public MainFrame() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 2 - WIDTH / 2, d.height / 2 - HEIGHT / 2);
        addWindowListener(this);
        currentView = new MenuView(this);
        add(currentView);
        setVisible(true);
    }

    public void windowOpened(WindowEvent windowEvent) {
    }

    public void windowClosing(WindowEvent e) {
        int result = JOptionPane.showConfirmDialog(this, "Wirklich beenden?", "", 0);
        if(result == 0) {
            System.exit(0);
        }

    }

    public void windowClosed(WindowEvent windowEvent) {
    }

    public void windowIconified(WindowEvent windowEvent) {
    }

    public void windowDeiconified(WindowEvent windowEvent) {
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowDeactivated(WindowEvent windowEvent) {
    }
}
