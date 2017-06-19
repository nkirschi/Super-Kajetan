package gui;

class CreditsView extends AbstractView {
    private MainFrame mainFrame;
    private static CreditsView instance;

    private CreditsView(MainFrame mainFrame) {
        super(mainFrame);
    }

    static CreditsView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new CreditsView(mainFrame);
        return instance;
    }
}
