package gui;

class HighScoreView extends AbstractView {
    private MainFrame mainFrame;
    private static HighScoreView instance;

    private HighScoreView(MainFrame mainFrame) {
        super(mainFrame);
    }

    static HighScoreView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new HighScoreView(mainFrame);
        return instance;
    }
}
