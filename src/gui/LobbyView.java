package gui;

import model.Level;
import util.list.List;

class LobbyView extends AbstractView {
    private MainFrame mainFrame;
    private static LobbyView instance;

    private LobbyView(MainFrame mainFrame) {
        super(mainFrame);
    }

    static LobbyView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new LobbyView(mainFrame);
        return instance;
    }

    /**
     * Hässliche Hardcoded-Level-Methode
     */
    private void createLevel1() {
        Level level1 = new Level("images/background1.png", new List<>(), new List<>(), 1000);
    }
}
