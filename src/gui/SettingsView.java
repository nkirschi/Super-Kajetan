package gui;

import util.Constants;
import util.Logger;
import util.SoundUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsView extends AbstractView {
    private static SettingsView instance;
    private final boolean opaque = false; //Hiermit kann man alle Panels/TextFields/... gleichzeitig opaque setzen
    private float maxVolume = 1F;
    private float minVolume = 0F;

    //Einstellungsvariablen
    private float volume = (maxVolume + minVolume) / 2;
    private boolean alt_control = false;
    private JCheckBox controllCheckBox;
    private JLabel saveLabel;

    private SettingsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);
        //setOpaque(opaque);

        //Zurück-Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        buttonPanel.setOpaque(opaque);

        WoodenButton backButton = new WoodenButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE_2);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));
        buttonPanel.add(backButton);

        //Speichern
        saveLabel = new JLabel();

        WoodenButton saveButton = new WoodenButton("Save");
        saveButton.setBackground(Constants.BUTTON_COLOR);
        saveButton.setFont(Constants.DEFAULT_FONT);
        saveButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE_2);
        saveButton.addActionListener(a -> {
            SoundUtil.soundSystem.setMasterVolume(volume);

            setAltControlMode(controllCheckBox.isSelected());
            MainFrame.getInstance().getProperties().put(Constants.PROPERTY_CONTROL_MODE, Boolean.toString(alt_control));
            MainFrame.getInstance().getProperties().put(Constants.PROPERTY_VOLUME, Float.toString(volume));
            try (FileWriter writer = new FileWriter("settings.properties")) {
                MainFrame.getInstance().getProperties().store(writer, Constants.GAME_TITLE + " " + Constants.GAME_VERSION);
                saveLabel.setText("Speichern erfolgreich!");
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log(e, Logger.ERROR);
                saveLabel.setText("Das ging in die Hose!");
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(saveLabel);

        add(buttonPanel, BorderLayout.PAGE_END);

        //Einstellungspanel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 0, 0);
        settingsPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        settingsPanel.setOpaque(opaque);

        //Header
        JLabel header = new JLabel("Einstellungen");
        header.setForeground(Constants.FOREGROUND_COLOR);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setFont(Constants.DEFAULT_FONT.deriveFont(110F));
        settingsPanel.add(header, constraints);

        Font labelFont = Constants.DEFAULT_FONT.deriveFont(Font.BOLD, 24F);

        constraints.insets = new Insets(10, 0, 0, 10);

        //Einstellungen aus Properties holen
        try {
            volume = Float.parseFloat(MainFrame.getInstance().getProperties().getProperty(Constants.PROPERTY_VOLUME));
        } catch (Exception e) {
            Logger.log(e, Logger.WARNING);
        }

        try {
            alt_control = Boolean.parseBoolean(MainFrame.getInstance().getProperties().getProperty(Constants.PROPERTY_CONTROL_MODE));
        } catch (Exception e) {
            Logger.log(e, Logger.WARNING);
        }

        //Lautstärke
        constraints.insets.set(50, 0, 0, 10);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel volumeLabel = new JLabel("Lautstärke");
        volumeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        volumeLabel.setForeground(Constants.FOREGROUND_COLOR);
        volumeLabel.setFont(labelFont);
        settingsPanel.add(volumeLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, (int) (minVolume * 100), (int) (maxVolume * 100), (int) (volume * 100)) {
            @Override
            public void setBorder(Border border) {
                //Nein
            }
        };

        volumeSlider.setOpaque(false);
        volumeSlider.addChangeListener(c -> {
            this.volume = (float) volumeSlider.getValue() / 100;
        });
        settingsPanel.add(volumeSlider, constraints);
        constraints.insets.set(10, 0, 0, 10);

        //Steuerung
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel controllLabel = new JLabel("Alternativer Steuerungsmodus");
        controllLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        controllLabel.setForeground(Constants.FOREGROUND_COLOR);
        controllLabel.setFont(labelFont);
        settingsPanel.add(controllLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        controllCheckBox = new JCheckBox();
        controllCheckBox.setOpaque(false);
        controllCheckBox.setBorderPainted(false);
        controllCheckBox.setSelected(alt_control);
        settingsPanel.add(controllCheckBox, constraints);

        //Beschreibung der Steuerung
        Color c = new Color(188, 188, 188);
        Font f = Constants.DEFAULT_FONT.deriveFont(20F);

        constraints.insets = new Insets(20, 0, 20, 0);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel b1 = new JLabel("Normale Steuerung: WASD");
        b1.setFont(f);
        b1.setForeground(c);
        settingsPanel.add(b1, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel b2 = new JLabel("Alternative Steuerung: Pfeiltasten");
        b2.setFont(f);
        b2.setForeground(c);
        settingsPanel.add(b2, constraints);

        constraints.insets = new Insets(0, 30, 0, 0);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel b11 = new JLabel("[A] Nach links");
        b11.setFont(f);
        b11.setForeground(c);
        settingsPanel.add(b11, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel b21 = new JLabel("[LEFT] Nach links");
        b21.setFont(f);
        b21.setForeground(c);
        settingsPanel.add(b21, constraints);

        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel b12 = new JLabel("[D] Nach rechts");
        b12.setFont(f);
        b12.setForeground(c);
        settingsPanel.add(b12, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel b22 = new JLabel("[RIGHT] Nach links");
        b22.setFont(f);
        b22.setForeground(c);
        settingsPanel.add(b22, constraints);

        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel b13 = new JLabel("[W] Springen");
        b13.setFont(f);
        b13.setForeground(c);
        settingsPanel.add(b13, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel b23 = new JLabel("[UP] Springen");
        b23.setFont(f);
        b23.setForeground(c);
        settingsPanel.add(b23, constraints);

        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel b14 = new JLabel("[S] Ducken");
        b14.setFont(f);
        b14.setForeground(c);
        settingsPanel.add(b14, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel b24 = new JLabel("[DOWN] Ducken");
        b24.setFont(f);
        b24.setForeground(c);
        settingsPanel.add(b24, constraints);


        add(settingsPanel, BorderLayout.CENTER);


        Logger.log("Settings geladen", Logger.INFO);
    }

    //TODO Settings speichern

    public void refresh() {
        controllCheckBox.setSelected(alt_control);
        saveLabel.setText("");
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float f) {
        volume = f;
    }

    public boolean getAltControlMode() {
        return alt_control;
    }

    public void setAltControlMode(boolean b) {
        alt_control = b;
        controllCheckBox.setSelected(b);
    }

    public static SettingsView getInstance() {
        if (instance == null)
            instance = new SettingsView();
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Praktisch der Teil der für das Hintergrundbild sorgt. Man muss natürlich auch die ganzen Panels auf nicht opaque setzen
        //-> setOpaque(false)
        try {
            g.drawImage(util.ImageUtil.getImage(Constants.MENU_BACKGROUND_2), 0, 0, getWidth(), getHeight(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.setColor(new Color(0, 0, 0, 0.7f));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
