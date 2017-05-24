package gui;

import util.ImageUtil;
import util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * Klasse des Hauptfensters
 */
public class MainFrame extends JFrame implements WindowListener {
    private AbstractView currentView;

    public static Insets BUTTON_INSETS = new Insets(2, 6, 2, 6);
    public static int BUTTON_ICON_TEXT_GAP = 5;

    private MainFrame() {
        setTitle("Sidescroller 0.0.1");
        setSize(800, 600);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        try {
            setIconImage(ImageUtil.getInternalImage("images/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(Logger.WARNING, e);
        }
        setVisible(true);
    }

    /**
     * Die Hauptmethode der Applikation
     *
     * @param args Irrelevante Kommandozeilenparamter
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame();
    }

    /**
     * Eine wichtige Methode, um die aktuelle Ansicht zu wechseln
     *
     * @param view Ansicht, zu der gewechselt werden soll
     */
    public void changeTo(AbstractView view) {
        if (currentView != null) {
            remove(currentView);
        }
        currentView = view;
        add(currentView);
        revalidate();
        repaint();
        currentView.revalidate();
        currentView.update();
        currentView.repaint();
    }

    /**
     * Getter-Methode für die aktuelle Ansicht
     *
     * @return Momentan aktive Ansicht
     */
    public AbstractView getCurrentView() {
        return currentView;
    }

    private void cleanUpBeforeClose() { //TODO alle Sachen, die beim entfernen aufgeführt werden sollen hinzufügen
        Logger.log(Logger.INFO, "Applikation ordnungsgemäß beendet");
        Logger.close();
        System.exit(0);
    }

    /**
     * Diese Methode wird von Swing beim Versuch des Schließens ausgeführt
     *
     * @param e Das WindowEvent mit Detailinformationen (brauchen wir hier aber nicht)
     */
    @Override
    public void windowClosing(WindowEvent e) {
        cleanUpBeforeClose();
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}