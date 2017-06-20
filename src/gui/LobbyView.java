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
    
    private void createLevel1() {
        Level level1 = new Level("images/background1.png", new List<Entity>, new List<Obstacle>, 1000);
    }
}
