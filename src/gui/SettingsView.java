package gui;

class SettingsView extends AbstractView {
    private MainFrame mainframe;
    private static SettingsView instance;

    private SettingsView(MainFrame mainframe) {
        super(mainframe);
    }

    public static SettingsView getInstance(MainFrame mainframe) {
        if (instance == null)
            instance = new SettingsView(mainframe);
        return instance;
    }
}
