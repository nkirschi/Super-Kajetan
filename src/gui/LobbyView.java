package gui;

import model.Level;
import util.list.List;

class LobbyView extends AbstractView {
    private static LobbyView instance;

    private LobbyView(MainFrame mainFrame) {
        super(mainFrame);
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
    private void createLevel1() {
        Level level1 = new Level("images/background1.png", new List<>(), new List<>(), 1000);
    }
}
