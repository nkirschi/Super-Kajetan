package gui;

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
}
