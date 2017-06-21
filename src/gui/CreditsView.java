package gui;

class CreditsView extends AbstractView {
    private static CreditsView instance;

    private CreditsView(MainFrame mainFrame) {
        super(mainFrame);
    }

    public void update() {

    }

    static CreditsView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new CreditsView(mainFrame);
        return instance;
    }
}
