package gui;

import util.GameConstants;

import javax.swing.*;
import java.awt.*;

class MainMenuView extends AbstractView {
    private static MainMenuView instance;
    private GridBagConstraints constraints;
    private String currentName;

    private MainMenuView() {
        super();
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);
        setBackground(GameConstants.MENU_BACKGROUND_COLOR);
        GameConstants.setDefaultButtonSize(new Dimension(250, 50));
        initButtons();
        //TODO Namenseingabefeld
    }

    private void initButtons() {

        //Button-Initialisierung
        JButton lobbyButton = new JButton("SPIELEN");
        JButton settingsButton = new JButton("EINSTELLUNGEN");
        JButton highscoresButton = new JButton("HIGHSCORES");
        JButton creditsButton = new JButton("CREDITS");
        JButton exitButton = new JButton("BEENDEN");

        //Action-Listener hinzufügen
        lobbyButton.addActionListener(a -> MainFrame.getInstance().changeTo(LobbyView.getInstance()));
        settingsButton.addActionListener(a -> MainFrame.getInstance().changeTo(SettingsView.getInstance()));
        highscoresButton.addActionListener(a -> MainFrame.getInstance().changeTo(HighscoresView.getInstance()));
        creditsButton.addActionListener(a -> MainFrame.getInstance().changeTo(CreditsView.getInstance()));
        exitButton.addActionListener(a -> MainFrame.getInstance().cleanupAndExit());

        //Aussehens-Parameter setzen
        lobbyButton.setPreferredSize(GameConstants.defaultButtonSize);
        settingsButton.setPreferredSize(GameConstants.defaultButtonSize);
        highscoresButton.setPreferredSize(GameConstants.defaultButtonSize);
        creditsButton.setPreferredSize(GameConstants.defaultButtonSize);
        exitButton.setPreferredSize(GameConstants.defaultButtonSize);

        lobbyButton.setBackground(GameConstants.BUTTON_COLOR);
        settingsButton.setBackground(GameConstants.BUTTON_COLOR);
        highscoresButton.setBackground(GameConstants.BUTTON_COLOR);
        creditsButton.setBackground(GameConstants.BUTTON_COLOR);
        exitButton.setBackground(GameConstants.BUTTON_COLOR);

        //Buttons hinzufügen
        add(lobbyButton, constraints);
        add(settingsButton, constraints);
        add(highscoresButton, constraints);
        add(creditsButton, constraints);
        add(exitButton, constraints);
    }

    public static MainMenuView getInstance() {
        if (instance == null) {
            instance = new MainMenuView();
        }
        return instance;
    }

    public void update() {
    }
}
