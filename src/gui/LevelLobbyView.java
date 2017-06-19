package gui;

class LevelLobbyView extends AbstractView {
    private MainFrame mainFrame;
    private static LevelLobbyView instance;

    private LevelLobbyView(MainFrame mainFrame) {
        super(mainFrame);
    }

    static LevelLobbyView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new LevelLobbyView(mainFrame);
        return instance;
    }
}
