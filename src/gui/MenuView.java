package gui;

import game.Level;

import javax.swing.*;

/**
 * @author Kirschi
 */
public class MenuView extends AbstractView {
    private JButton startButton;
    private JButton pauseButton;

    public MenuView(MainFrame mainFrame) {
        super(mainFrame);
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        startButton.addActionListener(l -> Level.getInstance().start());
        pauseButton.addActionListener(l -> Level.getInstance().pause());
        add(startButton);
        add(pauseButton);
    }

    public void update() {}
}
