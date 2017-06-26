package gui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;
    private ResultSet highScoreSet;

    private HighscoresView() {
        super();
        setLayout(new BorderLayout());
        setBackground(GUIConstants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(GUIConstants.MENU_BACKGROUND_COLOR);

        JButton backButton = new JButton("ZURÜCK");
        backButton.setBackground(GUIConstants.BUTTON_COLOR);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        update();
    }

    public void update() {
        try {
            highScoreSet = DBConnection.getInstance().query("SELECT * FROM "+GUIConstants.DB_TABLE+" ORDER BY "+GUIConstants.DB_COLLUM_SCORE+" DESC LIMIT 10;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static HighscoresView getInstance() {
        if (instance == null)
            instance = new HighscoresView();
        return instance;
    }
}
