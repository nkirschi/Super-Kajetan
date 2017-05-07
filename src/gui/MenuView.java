package gui;

import game.Test;

import javax.swing.*;

/**
 * @author Kirschi
 */
public class MenuView extends AbstractView {

    public MenuView(MainFrame mainFrame) {
        super(mainFrame);
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause/Weiter");
        JLabel runningLabel = new JLabel("Status: Läuft");
        startButton.addActionListener(l -> Test.getInstance().start());
        pauseButton.addActionListener(l -> Test.getInstance().pause());
        add(startButton);
        add(pauseButton);
    }

    public void update() {}
}
