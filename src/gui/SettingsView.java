package gui;

class SettingsView extends AbstractView {
    private static SettingsView instance;

    private SettingsView() {
        super();
    }

    public void update() {

    }

    public static SettingsView getInstance() {
        if (instance == null)
            instance = new SettingsView();
        return instance;
    }
}
