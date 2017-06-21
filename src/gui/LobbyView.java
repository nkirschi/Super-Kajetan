package gui;

import model.Level;
import util.list.List;

import javax.swing.*;

class LobbyView extends AbstractView {
    private static LobbyView instance;

    private LobbyView(MainFrame mainFrame) {
        super(mainFrame);
        JButton temp = new JButton("temp");
        temp.addActionListener(a -> mainFrame.changeTo(new LevelView(createLevel1(), mainFrame)));
        add(temp);
    }

    public void update() {

    }

    public static LobbyView getInstance(MainFrame mainframe) {
        if (instance == null)
            instance = new LobbyView(mainframe);
        return instance;
    }

    /**
     * Hässliche Hardcoded-Level-Methode
     */
    private Level createLevel1() {
        Level level1 = new Level("images/background1.jpg", new List<>(), new List<>(), 1000);
        return level1;
    }
}
