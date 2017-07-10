package gui;

import util.Constants;
import util.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;

public class SettingsView extends AbstractView {
    private static SettingsView instance;
    private final boolean opaque = true; //Hiermit kann man alle Panels/TextFields/... gleichzeitig opaque setzen
    private float maxVolume = 6F;
    private float minVolume = -6F;

    //Einstellungsvariablen
    private float volume = (maxVolume + minVolume) / 2;
    private boolean alt_control = false;

    private SettingsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);
        //setOpaque(opaque);

        //Zurück-Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        buttonPanel.setOpaque(opaque);

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        //Einstellungspanel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 20, 0);
        settingsPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        settingsPanel.setOpaque(opaque);

        //Header
        JLabel header = new JLabel("Einstellungen");
        header.setFont(Constants.DEFAULT_FONT.deriveFont(110F));
        settingsPanel.add(header, constraints);

        Font labelFont = Constants.DEFAULT_FONT.deriveFont(Font.BOLD, 24F);

        constraints.insets = new Insets(0, 0, 0, 20);

        //Lautstärke
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel volumeLabel = new JLabel("Lautstärke");
        volumeLabel.setFont(labelFont);
        settingsPanel.add(volumeLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, (int) minVolume, (int) maxVolume, (int) volume) {
            @Override
            public void setBorder(Border border) {
                //Nein
            }
        };
        volumeSlider.setOpaque(false);
        volumeSlider.addChangeListener(c -> {
            this.volume = (float) volumeSlider.getValue();
            //System.out.println(volumeSlider.getValue());
        });
        settingsPanel.add(volumeSlider, constraints);

        //Steuerung
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JLabel controllLabel = new JLabel("Alternativer Steuerungsmodus");
        controllLabel.setFont(labelFont);
        settingsPanel.add(controllLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JCheckBox controllCheckBox = new JCheckBox();
        controllCheckBox.setOpaque(false);
        controllCheckBox.setBorderPainted(false);
        controllCheckBox.setSelected(alt_control);
        controllCheckBox.addItemListener(e -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                alt_control = true;
            }
            else{
                alt_control = false;
            }
        });
        settingsPanel.add(controllCheckBox, constraints);

        //Beschreibung der Steuerung


        add(settingsPanel, BorderLayout.CENTER);


        Logger.log("Settings geladen", Logger.INFO);
    }

    //TODO Settings speichern

    public void refresh() {

    }

    public float getVolume() {
        return volume;
    }
    public void setVolume(float f){
        volume = f;
    }

    public boolean getAltControlMode(){
        return alt_control;
    }

    public void setAltControlMode(boolean b){
        alt_control = b;
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
            g.drawImage(util.ImageUtil.getImage(Constants.MENU_BACKGROUND_NOBANNER), 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
