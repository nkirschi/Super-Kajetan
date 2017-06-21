package gui;

class HighscoreView extends AbstractView {
    private static HighscoreView instance;

    private HighscoreView(MainFrame mainFrame) {
        super(mainFrame);
    }

    public void update() {

    }

    static HighscoreView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new HighscoreView(mainFrame);
        return instance;
    }
}
