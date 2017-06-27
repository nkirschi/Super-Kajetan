package gui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;
    private String[] columnNames = {GUIConstants.DB_COLLUM_NAME, GUIConstants.DB_COLLUM_SCORE, GUIConstants.DB_COLLUM_DATE};
    private String[][] rowData = new String[10][3];

    private HighscoresView() {
        super();
        setLayout(new BorderLayout());
        setBackground(GUIConstants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(GUIConstants.MENU_BACKGROUND_COLOR);

        JButton backButton = new JButton("ZURÜCK");
        backButton.setBackground(GUIConstants.BUTTON_COLOR);
        //backButton.setPreferredSize(GUIConstants.defaultButtonSize);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        JScrollPane scrollPane = new JScrollPane(initJTable());
        add(scrollPane, BorderLayout.CENTER);

    }

    public JTable initJTable(){
        try {
            ResultSet highScoreSet = DBConnection.getInstance().query("SELECT * FROM " + GUIConstants.DB_TABLE + " ORDER BY " + GUIConstants.DB_COLLUM_SCORE + " DESC LIMIT 10;");
            for(int i = 0; highScoreSet.next(); i++){
                rowData[i][0] = highScoreSet.getString(GUIConstants.DB_COLLUM_NAME);
                rowData[i][1] = Integer.toString(highScoreSet.getInt(GUIConstants.DB_COLLUM_SCORE));
                Date date = highScoreSet.getDate(GUIConstants.DB_COLLUM_DATE);
                rowData[i][2] = Integer.toString(date.getDay()) + "." + Integer.toString(date.getMonth()) + "." + Integer.toString(date.getYear());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(rowData, columnNames);
        table.setFillsViewportHeight(true);
        table.setBackground(GUIConstants.MENU_BACKGROUND_COLOR);
        table.setShowGrid(false);
        table.setRowHeight(70);

        return table;
    }

    public void update() {
    }

    static HighscoresView getInstance() {
        if (instance == null)
            instance = new HighscoresView();
        return instance;
    }
}
