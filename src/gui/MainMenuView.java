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
import java.io.IOException;

class MainMenuView extends AbstractView {
    private static MainMenuView instance;
    private final boolean opaque = false; //Hiermit kann man alle Panels/TextFields/... gleichzeitig opaque setzen
    private GridBagConstraints constraints;
    private String currentName = "Ritter Arnold";

    private MainMenuView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);
        //setOpaque(opaque);
        initButtonPanel();
        initToolPanel();
        //TODO Namenseingabefeld
    }

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(opaque);
        buttonPanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        //Button-Initialisierung
        WoodenButton lobbyButton = new WoodenButton("SPIELEN");
        WoodenButton highscoresButton = new WoodenButton("HIGHSCORES");
        WoodenButton settingsButton = new WoodenButton("EINSTELLUNGEN");
        WoodenButton creditsButton = new WoodenButton("CREDITS");
        WoodenButton exitButton = new WoodenButton("BEENDEN");
        Logger.log("Main Menu: Buttons initialisiert", Logger.INFO);

        //Action-Listener hinzufügen
        lobbyButton.addActionListener(a -> MainFrame.getInstance().changeTo(LobbyView.getInstance()));
        highscoresButton.addActionListener(a -> MainFrame.getInstance().changeTo(HighscoresView.getInstance()));
        settingsButton.addActionListener(a -> MainFrame.getInstance().changeTo(SettingsView.getInstance()));
        creditsButton.addActionListener(a -> MainFrame.getInstance().changeTo(CreditsView.getInstance()));
        exitButton.addActionListener(a -> MainFrame.getInstance().cleanupAndExit());
        Logger.log("Main Menu: Action-Listener hinzugefügt", Logger.INFO);

        //Aussehens-Parameter setzen
        lobbyButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        highscoresButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        settingsButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        creditsButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        exitButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);

        lobbyButton.setBackground(Constants.BUTTON_COLOR);
        highscoresButton.setBackground(Constants.BUTTON_COLOR);
        settingsButton.setBackground(Constants.BUTTON_COLOR);
        creditsButton.setBackground(Constants.BUTTON_COLOR);
        exitButton.setBackground(Constants.BUTTON_COLOR);

        Font buttonFont = Constants.DEFAULT_FONT.deriveFont(24F);
        lobbyButton.setFont(buttonFont);
        highscoresButton.setFont(buttonFont);
        settingsButton.setFont(buttonFont);
        creditsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);
        Logger.log("Main Menu: Aussehens-Parameter gesetzt", Logger.INFO);

        //Buttons hinzufügen
        buttonPanel.add(lobbyButton, constraints);
        buttonPanel.add(highscoresButton, constraints);
        buttonPanel.add(settingsButton, constraints);
        buttonPanel.add(creditsButton, constraints);
        buttonPanel.add(exitButton, constraints);
        Logger.log("Main Menu: Buttons hinzugefügt", Logger.INFO);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void initToolPanel() {
        JPanel toolPanel = new JPanel();
        toolPanel.setOpaque(opaque);
        toolPanel.setLayout(new FlowLayout());
        toolPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        JLabel nameLabel = new JLabel("Gib deinen Namen hier ein: ");
        nameLabel.setFont(Constants.DEFAULT_FONT);
        nameLabel.setForeground(Color.WHITE);
        toolPanel.add(nameLabel);

        JTextField nameTextField = new JTextField(17) {
            @Override
            public void setBorder(Border border) {
                if (!opaque) {
                    super.setBorder(border);
                }
            }
        };
        nameTextField.setOpaque(true);
        nameTextField.setDocument(new JTextFieldLimit(20));
        nameTextField.setText(currentName);
        nameTextField.setBackground(new Color(0, 0, 0, 0.5f));
        nameTextField.setForeground(Color.WHITE);
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
        Logger.log("Tool Panel initialisiert", Logger.INFO);
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

    public void refresh() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Praktisch der Teil der für das Hintergrundbild sorgt. Man muss natürlich auch die ganzen Panels auf nicht opaque setzen
        //-> setOpaque(false)
        try {
            g.drawImage(util.ImageUtil.getImage(Constants.MENU_BACKGROUND), 0, 0, getWidth(), getHeight(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }


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
