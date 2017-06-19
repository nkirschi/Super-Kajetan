package gui;

class CreditView extends AbstractView {
    private MainFrame mainFrame;
    private static CreditView instance;

    private CreditView(MainFrame mainFrame) {
        super(mainFrame);
    }

    static CreditView getInstance(MainFrame mainFrame) {
        if (instance == null)
            instance = new CreditView(mainFrame);
        return instance;
    }
}
