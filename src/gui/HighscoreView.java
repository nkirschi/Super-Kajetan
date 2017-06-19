package gui;

class HighscoreView extends AbstractView {
    private MainFrame mainFrame;
    private static HighscoreView instance;

    private HighscoreView(MainFrame mainFrame) {
        super(mainFrame);
    }

    static HighscoreView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new HighscoreView(mainFrame);
        return instance;
    }
}
