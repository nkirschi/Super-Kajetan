package gui;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;

    private HighscoresView(MainFrame mainFrame) {
        super(mainFrame);
    }

    public void update() {

    }

    static HighscoresView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new HighscoresView(mainFrame);
        return instance;
    }
}
