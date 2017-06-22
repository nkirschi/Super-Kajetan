package gui;

import model.Level;
import util.list.List;

import javax.swing.*;

class LobbyView extends AbstractView {
    private static LobbyView instance;

    private LobbyView() {
        super();
        JButton temp = new JButton("temp");
        temp.addActionListener(a -> MainFrame.getInstance().changeTo(new LevelView(createLevel1())));
        add(temp);
    }

    public void update() {

    }

    public static LobbyView getInstance() {
        if (instance == null)
            instance = new LobbyView();
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
