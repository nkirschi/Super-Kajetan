package gui;

import util.DBConnection;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;
    private JPanel highScoreList;
    private Border listCollumBorder = BorderFactory.createEmptyBorder(10,50,0,50); //bottom sollte immer 0 sein
    private Border listCellBorder = BorderFactory.createEmptyBorder(0,0,20,0); //left, right sollte immer 0 sein, wird von listCollumBorder übernommen
    private Border listCollumHeaderBorder = BorderFactory.createEmptyBorder(0,0,40,0); //top,left,right sollte immer 0 sein, sie ^

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

        update(); //Initialisiert die eigentliche Liste, damit diese auch immer aktuell ist
    }

    private JPanel initHighScoreList(){
        JPanel list = new JPanel(new FlowLayout());
        list.setBackground(GUIConstants.MENU_BACKGROUND_COLOR);



        //Einzelne Spalten
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBorder(listCollumBorder);
        namePanel.setBackground(GUIConstants.MENU_BACKGROUND_COLOR);
        list.add(namePanel);
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBackground(GUIConstants.MENU_BACKGROUND_COLOR);
        scorePanel.setBorder(listCollumBorder);
        list.add(scorePanel);
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setBackground(GUIConstants.MENU_BACKGROUND_COLOR);
        datePanel.setBorder(listCollumBorder);
        list.add(datePanel);

        //Überschriften der Spalten
        JLabel collumName = new JLabel(GUIConstants.DB_COLLUM_NAME);
        collumName.setAlignmentX(Component.CENTER_ALIGNMENT);
        collumName.setBorder(listCollumHeaderBorder);
        namePanel.add(collumName);
        JLabel collumScore = new JLabel(GUIConstants.DB_COLLUM_SCORE);
        collumScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        collumScore.setBorder(listCollumHeaderBorder);
        scorePanel.add(collumScore);
        JLabel collumDate = new JLabel(GUIConstants.DB_COLLUM_DATE);
        collumDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        collumDate.setBorder(listCollumHeaderBorder);
        datePanel.add(collumDate);

        //Füllen der Tabelle
        try {
            ResultSet highScoreSet = DBConnection.getInstance().query("SELECT * FROM " + GUIConstants.DB_TABLE + " ORDER BY " + GUIConstants.DB_COLLUM_SCORE + " DESC LIMIT 10;");
            while(highScoreSet.next()){
                JLabel nameCell = new JLabel(highScoreSet.getString(GUIConstants.DB_COLLUM_NAME));
                nameCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                nameCell.setBorder(listCellBorder);
                namePanel.add(nameCell);

                JLabel scoreCell = new JLabel(Integer.toString(highScoreSet.getInt(GUIConstants.DB_COLLUM_SCORE)));
                scoreCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                scoreCell.setBorder(listCellBorder);
                scorePanel.add(scoreCell);

                Date asd = highScoreSet.getDate(GUIConstants.DB_COLLUM_DATE);

                String dateString = "höhö"; //TODO date umschreiben

                JLabel dateCell = new JLabel(dateString);
                dateCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                dateCell.setBorder(listCellBorder);
                datePanel.add(dateCell);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            scorePanel.add(new JLabel("WARNUNG: DATENBANK KANN NICHT ERREICHT WERDEN!!! :/"));
        }

        return list;
    }

    public void update() {
        if(highScoreList != null){
            remove(highScoreList);
        }
        highScoreList = initHighScoreList();
        add(highScoreList);
        revalidate();
        repaint();
    }

    static HighscoresView getInstance() {
        if (instance == null)
            instance = new HighscoresView();
        return instance;
    }
}
