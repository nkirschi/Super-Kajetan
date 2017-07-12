package gui;

import util.Constants;
import util.ImageUtil;
import util.Logger;
import util.SoundUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Klasse des Hauptfensters
 */
public class MainFrame extends JFrame implements WindowListener {
    private static MainFrame instance;
    private AbstractView currentView;
    private Properties properties;

    /**
     * Privater Konstruktor, da wir nur genau ein Hauptfenster zur Laufzeit benötigen,
     * das ganz am Anfang in der statischen main()-Methode erzeugt wird
     */
    private MainFrame() {
        setTitle(Constants.GAME_TITLE + " " + Constants.GAME_VERSION);
        setSize(1024, 768);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2 - getWidth() / 2;
        int height = screenSize.height / 2 - getHeight() / 2;
        setLocation(width, height);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        try {
            setIconImage(ImageUtil.getImage("images/gui/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initProperties();

        changeTo(MainMenuView.getInstance());
        setVisible(true);
    }

    public static MainFrame getInstance() {
        if (instance == null)
            instance = new MainFrame();
        return instance;
    }

    /**
     * Die Hauptmethode der Applikation
     *
     * @param args Irrelevante Kommandozeilenparamter
     */
    public static void main(String[] args) {
        SoundUtil.init();
        SwingUtilities.invokeLater(MainFrame::getInstance);
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
        currentView.refresh();
        currentView.repaint();
    }

    public void initProperties() {
        properties = new Properties();

        try (FileReader reader = new FileReader("settings.properties")) {
            properties.load(reader);
        } catch (IOException e1) {
            try {
                Files.createFile(Paths.get("settings.properties"));
            } catch (IOException e2) {
                e1.printStackTrace();
                e2.printStackTrace();
                Logger.log(e2, Logger.ERROR);
            }
        }
    }

    /**
     * Getter-Methode für die aktuelle Ansicht
     *
     * @return Momentan aktive Ansicht
     */
    public AbstractView getCurrentView() {
        return currentView;
    }

    /**
     * Hilfsmethode für die Menü-Konstanten
     *
     * @return Dimension des Frames
     */

    public Properties getProperties() {
        return properties;
    }

    /**
     * Cleanup-Methode, die vor dem Beenden des Programms ausgeführt wird
     */
    void cleanupAndExit() {
        Logger.log("Applikation ordnungsgemäß beendet", Logger.INFO);
        Logger.close();
        SoundUtil.soundSystem.cleanup();
        System.exit(0);
    }

    /**
     * Diese Methode wird von Swing beim Versuch des Schließens ausgeführt
     *
     * @param e Das WindowEvent mit Detailinformationen (brauchen wir hier aber nicht)
     */
    @Override
    public void windowClosing(WindowEvent e) {
        cleanupAndExit();
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
