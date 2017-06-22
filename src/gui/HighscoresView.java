package gui;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;

    private HighscoresView() {
        super();
    }

    public void update() {

    }

    static HighscoresView getInstance() {
        if (instance == null)
            instance = new HighscoresView();
        return instance;
    }
}
