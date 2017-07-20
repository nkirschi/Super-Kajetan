package gui;

import util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

class MainMenuView extends AbstractView {
    private static MainMenuView instance;
    private GridBagConstraints constraints;
    private String currentName = "Ritter Kajetan";

    private MainMenuView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        initButtonPanel();
        initToolPanel();
    }

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);

        //Button-Initialisierung
        WoodenButton lobbyButton = new WoodenButton("SPIELEN");
        WoodenButton highscoresButton = new WoodenButton("HIGHSCORES");
        WoodenButton settingsButton = new WoodenButton("EINSTELLUNGEN");
        WoodenButton creditsButton = new WoodenButton("CREDITS");
        WoodenButton exitButton = new WoodenButton("BEENDEN");

        //Action-Listener hinzufügen
        lobbyButton.addActionListener(a -> MainFrame.getInstance().changeTo(LobbyView.getInstance()));
        highscoresButton.addActionListener(a -> MainFrame.getInstance().changeTo(HighscoresView.getInstance()));
        settingsButton.addActionListener(a -> MainFrame.getInstance().changeTo(SettingsView.getInstance()));
        creditsButton.addActionListener(a -> MainFrame.getInstance().changeTo(CreditsView.getInstance()));
        exitButton.addActionListener(a -> MainFrame.getInstance().cleanupAndExit());

        //Aussehens-Parameter setzen
        lobbyButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        highscoresButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        settingsButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        creditsButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        exitButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);

        Font buttonFont = Constants.DEFAULT_FONT.deriveFont(24F);
        lobbyButton.setFont(buttonFont);
        highscoresButton.setFont(buttonFont);
        settingsButton.setFont(buttonFont);
        creditsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        //Buttons hinzufügen
        buttonPanel.add(lobbyButton, constraints);
        buttonPanel.add(highscoresButton, constraints);
        buttonPanel.add(settingsButton, constraints);
        buttonPanel.add(creditsButton, constraints);
        buttonPanel.add(exitButton, constraints);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void initToolPanel() {
        JPanel toolPanel = new JPanel();
        toolPanel.setOpaque(false);
        toolPanel.setLayout(new FlowLayout());
        toolPanel.setBorder(new EmptyBorder(0, 0, 27, 0));

        JLabel nameLabel = new JLabel("Gib deinen Namen hier ein: ");
        nameLabel.setFont(Constants.DEFAULT_FONT);
        nameLabel.setForeground(Color.BLACK);
        toolPanel.add(nameLabel);

        JTextField nameTextField = new JTextField(15);
        nameTextField.setOpaque(false);

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

        nameTextField.setDocument(new JTextFieldLimit(18));
        nameTextField.setText(currentName);
        //nameTextField.setBackground(new Color(0, 0, 0, 0.5f));
        nameTextField.setForeground(Color.BLACK);
        nameTextField.setFont(Constants.DEFAULT_FONT);
        nameTextField.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.WHITE));
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

        //Titel
        g.setFont(Constants.DEFAULT_FONT.deriveFont(110F));
        g.setColor(Constants.FOREGROUND_COLOR);
        int x = (getWidth() - (int) g.getFontMetrics().getStringBounds(Constants.GAME_TITLE, g).getWidth()) / 2;
        int y = (int) g.getFontMetrics().getStringBounds(Constants.GAME_TITLE, g).getHeight();
        g.drawString(Constants.GAME_TITLE, x, y);

        // Trololol
        double x0 = 160, y0 = 540;
        double x1 = 160, y1 = 440;
        double x2 = 80, y2 = 540;
        double x3 = 240, y3 = 540;

        for (int i = 0; i <= 7331; i++) {
            g.drawLine((int) x0, (int) y0, (int) x0, (int) y0);

            switch (ThreadLocalRandom.current().nextInt(3)) {
                case 0:
                    x0 += (x1 - x0) / 2;
                    y0 += (y1 - y0) / 2;
                    break;
                case 1:
                    x0 += (x2 - x0) / 2;
                    y0 += (y2 - y0) / 2;
                    break;
                case 2:
                    x0 += (x3 - x0) / 2;
                    y0 += (y3 - y0) / 2;
                    break;
            }
        }
    }
}
