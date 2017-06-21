package gui;

import javax.swing.*;

class MainMenuView extends AbstractView {
    private MainFrame mainFrame;
    private static MainMenuView instance;

    private MainMenuView(MainFrame mainFrame) {
        super(mainFrame);
        setBackground(GUIConstants.menuBackground);

        initButtons();
    }

    private void initButtons() {
        //Button-Initialisierung
        JButton levelLobby = new JButton("PLAY");
        JButton settings = new JButton("SETTINGS");
        JButton highscore = new JButton("HIGHSCORES");
        JButton credits = new JButton("CREDITS");

        //Action-Listener hinzufügen
        levelLobby.addActionListener(a -> mainFrame.changeTo(LobbyView.getInstance(mainFrame)));
        settings.addActionListener(a -> mainFrame.changeTo(SettingsView.getInstance(mainFrame)));
        highscore.addActionListener(a -> mainFrame.changeTo(HighscoreView.getInstance(mainFrame)));
        credits.addActionListener(a -> mainFrame.changeTo(CreditsView.getInstance(mainFrame)));

        //Aussehens-Parameter setzen

        //Buttons hinzufügen
        add(levelLobby);
        add(settings);
        add(highscore);
        add(credits);
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
