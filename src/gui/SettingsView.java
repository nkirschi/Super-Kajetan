package gui;
import util.Logger;

import util.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SettingsView extends AbstractView {
    private static SettingsView instance;
    private float maxVolume = 100F;
    private float minVolume = 0F;
    private float volume = maxVolume;

    private SettingsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        //Zurück-Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

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

        //Header
        JLabel header = new JLabel("Einstellungen");
        header.setFont(Constants.DEFAULT_FONT.deriveFont(110F));
        settingsPanel.add(header, constraints);

        Font labelFont = Constants.DEFAULT_FONT.deriveFont(Font.BOLD, 24F);

        constraints.insets = new Insets(0,0,0,20);
        constraints.gridwidth = GridBagConstraints.RELATIVE;

        //Lautstärke
        JLabel volumeLabel = new JLabel("Lautstärke");
        volumeLabel.setFont(labelFont);
        settingsPanel.add(volumeLabel, constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, (int)minVolume, (int)maxVolume, (int)volume){
            @Override public void setBorder(Border border){
                //Nein
            }
        };
        volumeSlider.setOpaque(false);
        volumeSlider.addChangeListener(c -> {
            this.volume = (float)volumeSlider.getValue();
            //System.out.println(volumeSlider.getValue());
        });
        settingsPanel.add(volumeSlider, constraints);


        add(settingsPanel, BorderLayout.CENTER);


        Logger.log("Settings geladen", Logger.INFO);
    }

    public void update() {

    }

    public float getVolume(){
        return volume;
    }

    public static SettingsView getInstance() {
        if (instance == null)
            instance = new SettingsView();
        return instance;
    }
}
