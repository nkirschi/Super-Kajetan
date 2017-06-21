package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class MainMenuView extends AbstractView {
    private static MainMenuView instance;

    private MainMenuView(MainFrame mainFrame) {
        super(mainFrame);
        setLayout(new FlowLayout());
        setBackground(GUIConstants.menuBackgroundColor);
        //TODO DefaultButtonSize bestimmen (mit GUIConstants.setDefaultButtonSize())!!

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
        levelLobby.setPreferredSize(GUIConstants.defaultButtonSize);
        settings.setPreferredSize(GUIConstants.defaultButtonSize);
        highscore.setPreferredSize(GUIConstants.defaultButtonSize);
        credits.setPreferredSize(GUIConstants.defaultButtonSize);

        levelLobby.setBackground(GUIConstants.buttonColor);
        settings.setBackground(GUIConstants.buttonColor);
        highscore.setBackground(GUIConstants.buttonColor);
        credits.setBackground(GUIConstants.buttonColor);

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
