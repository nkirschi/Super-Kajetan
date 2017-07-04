package gui;
import util.Logger;

import util.Constants;

import javax.swing.*;
import java.awt.*;

class SettingsView extends AbstractView {
    private static SettingsView instance;

    private SettingsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        //backButton.setPreferredSize(GUIConstants.defaultButtonSize);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);
        
        Logger.log("Settings geladen", Logger.INFO);
    }

    public void update() {

    }

    public static SettingsView getInstance() {
        if (instance == null)
            instance = new SettingsView();
        return instance;
    }
}
