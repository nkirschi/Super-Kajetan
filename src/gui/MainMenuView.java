package gui;

import javax.swing.*;
import java.awt.*;

class MainMenuView extends AbstractView {
    private static MainMenuView instance;
    private GridBagConstraints constraints;

    private MainMenuView(MainFrame mainFrame) {
        super(mainFrame);
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);
        setBackground(GUIConstants.MENU_BACKGROUND_COLOR);
        //TODO DefaultButtonSize bestimmen (mit GUIConstants.setDefaultButtonSize())!!
        GUIConstants.setDefaultButtonSize(new Dimension(250, 50));
        initButtons();
    }

    private void initButtons() {

        //Button-Initialisierung
        JButton lobbyButton = new JButton("SPIELEN");
        JButton settingsButton = new JButton("EINSTELLUNGEN");
        JButton highscoresButton = new JButton("HIGHSCORES");
        JButton creditsButton = new JButton("CREDITS");
        JButton exitButton = new JButton("BEENDEN");

        //Action-Listener hinzufügen
        lobbyButton.addActionListener(a -> mainFrame.changeTo(LobbyView.getInstance(mainFrame)));
        settingsButton.addActionListener(a -> mainFrame.changeTo(SettingsView.getInstance(mainFrame)));
        highscoresButton.addActionListener(a -> mainFrame.changeTo(HighscoresView.getInstance(mainFrame)));
        creditsButton.addActionListener(a -> mainFrame.changeTo(CreditsView.getInstance(mainFrame)));
        exitButton.addActionListener(a -> mainFrame.cleanupAndExit());

        //Aussehens-Parameter setzen
        lobbyButton.setPreferredSize(GUIConstants.defaultButtonSize);
        settingsButton.setPreferredSize(GUIConstants.defaultButtonSize);
        highscoresButton.setPreferredSize(GUIConstants.defaultButtonSize);
        creditsButton.setPreferredSize(GUIConstants.defaultButtonSize);
        exitButton.setPreferredSize(GUIConstants.defaultButtonSize);

        lobbyButton.setBackground(GUIConstants.BUTTON_COLOR);
        settingsButton.setBackground(GUIConstants.BUTTON_COLOR);
        highscoresButton.setBackground(GUIConstants.BUTTON_COLOR);
        creditsButton.setBackground(GUIConstants.BUTTON_COLOR);
        exitButton.setBackground(GUIConstants.BUTTON_COLOR);

        //Buttons hinzufügen
        add(lobbyButton, constraints);
        add(settingsButton, constraints);
        add(highscoresButton, constraints);
        add(creditsButton, constraints);
        add(exitButton, constraints);
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
