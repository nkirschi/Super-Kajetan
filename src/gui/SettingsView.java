package gui;

import util.Constants;
import util.Logger;
import util.SoundUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class SettingsView extends AbstractView {
    private static SettingsView instance;
    private float maxVolume = 1F;
    private float minVolume = 0F;

    //Einstellungsvariablen
    private float musicVolume = (maxVolume + minVolume) / 2;
    private float effectVolume = (maxVolume + minVolume) / 2;
    private boolean alt_control = false;
    private JCheckBox controllCheckBox;
    private JLabel saveLabel;
    private JSlider musicVolumeSlider;
    private JSlider effectVolumeSlider;

    private SettingsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);


        //Zurück-Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        WoodenButton backButton = new WoodenButton("Zurück");
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE_2);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));
        buttonPanel.add(backButton);

        //Speichern
        saveLabel = new JLabel();
        saveLabel.setForeground(Constants.FOREGROUND_COLOR);

        WoodenButton saveButton = new WoodenButton("Save");
        saveButton.setFont(Constants.DEFAULT_FONT);
        saveButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE_2);
        saveButton.addActionListener(a -> {
            musicVolume = (float) musicVolumeSlider.getValue() / 100;
            SoundUtil.soundSystem.setVolume(SoundUtil.MUSIC_SOURCE, musicVolume);

            effectVolume = (float) effectVolumeSlider.getValue() / 100;
            SoundUtil.soundSystem.setVolume(SoundUtil.EFFECT_SOURCE, effectVolume);

            setAltControlMode(controllCheckBox.isSelected());
            MainFrame.getInstance().getProperties().put(Constants.PROPERTY_CONTROL_MODE, Boolean.toString(alt_control));
            MainFrame.getInstance().getProperties().put(Constants.PROPERTY_MUSIC_VOLUME, Float.toString(musicVolume));
            MainFrame.getInstance().getProperties().put(Constants.PROPERTY_EFFECT_VOLUME, Float.toString(effectVolume));
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
        settingsPanel.setOpaque(false);

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
            musicVolume = Float.parseFloat(MainFrame.getInstance().getProperties().getProperty(Constants.PROPERTY_MUSIC_VOLUME));
            SoundUtil.soundSystem.setVolume(SoundUtil.MUSIC_SOURCE, musicVolume);
        } catch (Exception e) {
            Logger.log(e, Logger.WARNING);
        }

        try {
            effectVolume = Float.parseFloat(MainFrame.getInstance().getProperties().getProperty(Constants.PROPERTY_EFFECT_VOLUME));
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

        //Musik
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel musicVolumeLabel = new JLabel("Musik-Lautstärke");
        musicVolumeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        musicVolumeLabel.setForeground(Constants.FOREGROUND_COLOR);
        musicVolumeLabel.setFont(labelFont);
        settingsPanel.add(musicVolumeLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;

        musicVolumeSlider = new JSlider(JSlider.HORIZONTAL, (int) (minVolume * 100), (int) (maxVolume * 100), (int) (musicVolume * 100)) {
            @Override
            public void setBorder(Border border) {
                //Nein
            }
        };
        musicVolumeSlider.setPreferredSize(new Dimension(350, 50));


        Hashtable<Integer, JLabel> labelsm = new Hashtable<>();
        JLabel labelm1 = new JLabel("Grabesstille");
        JLabel labelm2 = new JLabel("Bergwind");
        JLabel labelm3 = new JLabel("Markt");
        JLabel labelm4 = new JLabel("Taverne");
        JLabel labelm5 = new JLabel("Schlachtfeld");
        labelm1.setForeground(Color.WHITE);
        labelm2.setForeground(Color.WHITE);
        labelm3.setForeground(Color.WHITE);
        labelm4.setForeground(Color.WHITE);
        labelm5.setForeground(Color.WHITE);
        labelsm.put(0, labelm1);
        labelsm.put(25, labelm2);
        labelsm.put(50, labelm3);
        labelsm.put(75, labelm4);
        labelsm.put(100, labelm5);
        musicVolumeSlider.setLabelTable(labelsm);
        musicVolumeSlider.setMajorTickSpacing(25);
        musicVolumeSlider.setSnapToTicks(true);
        musicVolumeSlider.setPaintLabels(true);
        musicVolumeSlider.setPaintTicks(true);


        musicVolumeSlider.setOpaque(false);
        settingsPanel.add(musicVolumeSlider, constraints);

        //Effect
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel effectVolumeLabel = new JLabel("Effekt-Lautstärke");
        effectVolumeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        effectVolumeLabel.setForeground(Constants.FOREGROUND_COLOR);
        effectVolumeLabel.setFont(labelFont);
        settingsPanel.add(effectVolumeLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;

        effectVolumeSlider = new JSlider(JSlider.HORIZONTAL, (int) (minVolume * 100), (int) (maxVolume * 100), (int) (musicVolume * 100)) {
            @Override
            public void setBorder(Border border) {
                //Nein
            }
        };
        effectVolumeSlider.setPreferredSize(new Dimension(350, 50));

        Hashtable<Integer, JLabel> labelse = new Hashtable<>();
        JLabel labele1 = new JLabel("Grabesstille");
        JLabel labele2 = new JLabel("Bergwind");
        JLabel labele3 = new JLabel("Markt");
        JLabel labele4 = new JLabel("Taverne");
        JLabel labele5 = new JLabel("Schlachtfeld");
        labele1.setForeground(Color.WHITE);
        labele2.setForeground(Color.WHITE);
        labele3.setForeground(Color.WHITE);
        labele4.setForeground(Color.WHITE);
        labele5.setForeground(Color.WHITE);
        labelse.put(0, labele1);
        labelse.put(25, labele2);
        labelse.put(50, labele3);
        labelse.put(75, labele4);
        labelse.put(100, labele5);
        effectVolumeSlider.setLabelTable(labelsm);
        effectVolumeSlider.setMajorTickSpacing(25);
        effectVolumeSlider.setSnapToTicks(true);
        effectVolumeSlider.setPaintLabels(true);
        effectVolumeSlider.setPaintTicks(true);


        effectVolumeSlider.setOpaque(false);
        settingsPanel.add(effectVolumeSlider, constraints);

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
        controllCheckBox.addActionListener(a -> SoundUtil.playEffect("buttonclick"));
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

    public void refresh() {
        controllCheckBox.setSelected(alt_control);
        musicVolumeSlider.setValue((int) (musicVolume * 100));
        effectVolumeSlider.setValue((int) (effectVolume * 100));

        saveLabel.setText("");
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float f) {
        musicVolume = f;
    }

    public float getEffectVolume() {
        return effectVolume;
    }

    public void setEffectVolume(float effectVolume) {
        this.effectVolume = effectVolume;
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
            g.setColor(new Color(0, 0, 0, 0.7f));
            g.fillRect(0, 0, getWidth(), getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
