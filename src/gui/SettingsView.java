package gui;

import util.GameConstants;

import javax.swing.*;
import java.awt.*;

class SettingsView extends AbstractView {
    private static SettingsView instance;

    private SettingsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(GameConstants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(GameConstants.MENU_BACKGROUND_COLOR);

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(GameConstants.BUTTON_COLOR);
        //backButton.setPreferredSize(GUIConstants.defaultButtonSize);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    public void update() {

    }

    public static SettingsView getInstance() {
        if (instance == null)
            instance = new SettingsView();
        return instance;
    }
}
