package gui;

import util.Constants;
import util.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

class MainMenuView extends AbstractView {
    private static MainMenuView instance;
    private GridBagConstraints constraints;
    private String currentName = "Ritter Arnold";

    private MainMenuView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        initButtonPanel();
        initToolPanel();
        //TODO Namenseingabefeld
    }

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        //Button-Initialisierung
        JButton lobbyButton = new JButton("SPIELEN");
        JButton settingsButton = new JButton("EINSTELLUNGEN");
        JButton highscoresButton = new JButton("HIGHSCORES");
        JButton creditsButton = new JButton("CREDITS");
        JButton exitButton = new JButton("BEENDEN");
        Logger.log("Main Menu: Buttons initialisiert", Logger.INFO);

        //Action-Listener hinzufügen
        lobbyButton.addActionListener(a -> MainFrame.getInstance().changeTo(LobbyView.getInstance()));
        settingsButton.addActionListener(a -> MainFrame.getInstance().changeTo(SettingsView.getInstance()));
        highscoresButton.addActionListener(a -> MainFrame.getInstance().changeTo(HighscoresView.getInstance()));
        creditsButton.addActionListener(a -> MainFrame.getInstance().changeTo(CreditsView.getInstance()));
        exitButton.addActionListener(a -> MainFrame.getInstance().cleanupAndExit());
        Logger.log("Main Menu: Action-Listener hinzugefügt", Logger.INFO);

        //Aussehens-Parameter setzen
        lobbyButton.setPreferredSize(Constants.defaultButtonSize);
        settingsButton.setPreferredSize(Constants.defaultButtonSize);
        highscoresButton.setPreferredSize(Constants.defaultButtonSize);
        creditsButton.setPreferredSize(Constants.defaultButtonSize);
        exitButton.setPreferredSize(Constants.defaultButtonSize);

        lobbyButton.setBackground(Constants.BUTTON_COLOR);
        settingsButton.setBackground(Constants.BUTTON_COLOR);
        highscoresButton.setBackground(Constants.BUTTON_COLOR);
        creditsButton.setBackground(Constants.BUTTON_COLOR);
        exitButton.setBackground(Constants.BUTTON_COLOR);

        Font buttonFont = Constants.DEFAULT_FONT.deriveFont(24F);
        lobbyButton.setFont(buttonFont);
        settingsButton.setFont(buttonFont);
        highscoresButton.setFont(buttonFont);
        creditsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);
        Logger.log("Main Menu: Aussehens-Parameter gesetzt", Logger.INFO);

        //Buttons hinzufügen
        buttonPanel.add(lobbyButton, constraints);
        buttonPanel.add(settingsButton, constraints);
        buttonPanel.add(highscoresButton, constraints);
        buttonPanel.add(creditsButton, constraints);
        buttonPanel.add(exitButton, constraints);
        Logger.log("Main Menu: Buttons hinzugefügt", Logger.INFO);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void initToolPanel() {
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new FlowLayout());
        toolPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        JLabel nameLabel = new JLabel("Gib deinen Namen hier ein: ");
        nameLabel.setFont(Constants.DEFAULT_FONT);
        toolPanel.add(nameLabel);

        JTextField nameTextField = new JTextField(17) {
            @Override
            public void setBorder(Border border) {
                // Nein, Böse!
            }
        };
        nameTextField.setDocument(new JTextFieldLimit(20));
        nameTextField.setText(currentName);
        nameTextField.setBackground(Constants.BUTTON_COLOR);
        nameTextField.setFont(Constants.DEFAULT_FONT);
        nameTextField.setHorizontalAlignment(JTextField.CENTER);
        toolPanel.add(nameTextField);
        nameTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                currentName = nameTextField.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                currentName = nameTextField.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                currentName = nameTextField.getText();
            }
        });

        add(toolPanel, BorderLayout.PAGE_END);
    }

    public String getCurrentName() {
        return currentName;
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

//Hilfsklasse, um die Schriftzahl des Names zu begrenzen ...
class JTextFieldLimit extends PlainDocument {
    private int limit;

    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
