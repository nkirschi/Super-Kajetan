package gui;

class MainMenuView extends AbstractView {
    private MainFrame mainFrame;
    private static MainMenuView instance;

    private MainMenuView(MainFrame mainFrame) {
        super(mainFrame);
    }

    public static MainMenuView getInstance(MainFrame mainframe) {
        if (instance == null) {
            instance = new MainMenuView(mainframe);
        }
        return instance;
    }

    public void update() {
    }
}
