package gui;

import util.Constants;
import util.DBConnection;
import util.ImageUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;
    private JPanel highScoreList;
    private Border listCollumBorder = BorderFactory.createEmptyBorder(20,50,0,50); //bottom sollte immer 0 sein
    private Border listCellBorder = BorderFactory.createEmptyBorder(20,0,20,0); //left, right sollte immer 0 sein, wird von listCollumBorder übernommen
    private Border listCollumHeaderBorder = BorderFactory.createEmptyBorder(35,0,35,0); //top,left,right sollte immer 0 sein, sie ^

    private HighscoresView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        //backButton.setPreferredSize(GUIConstants.defaultButtonSize);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        update(); //Initialisiert die eigentliche Liste, damit diese auch immer aktuell ist
    }

    private JPanel initHighScoreList(){
        JPanel list = new JPanel(new FlowLayout());
        list.setBackground(Constants.MENU_BACKGROUND_COLOR);

        //Tolle Spalte links mit Erster, Zweiter, ...
        JPanel fancyCollumPanel =  new JPanel();
        fancyCollumPanel.setLayout(new BoxLayout(fancyCollumPanel,BoxLayout.Y_AXIS));
        fancyCollumPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        fancyCollumPanel.setBorder(listCollumBorder);
        list.add(fancyCollumPanel);

        try {
            ImageIcon trophyImage = ImageUtil.getIcon("images/trophy.png");
            System.out.println(trophyImage.getIconHeight()+"   "+trophyImage.getIconWidth());
            JLabel trophyLabel = new JLabel(trophyImage);
            trophyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            fancyCollumPanel.add(trophyLabel);

            //Setzt jetzt die Border der Spalten-Überschriften entsprechend, um für Höhe des Bildes zu kompensieren. (15/2 kompensiert für texthöhe. unschön, wissen wir ....)
            //TODO textgröße irgendwie anders bestimmen!!!!!
            listCollumHeaderBorder = BorderFactory.createEmptyBorder(trophyImage.getIconHeight()/2-15/2,0,trophyImage.getIconHeight()/2-15/2,0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 1;i<11;i++ ){
            JLabel label = new JLabel("Platz "+i);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setBorder(listCellBorder);
            fancyCollumPanel.add(label);
        }

        //Einzelne Spalten
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBorder(listCollumBorder);
        namePanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        list.add(namePanel);
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        scorePanel.setBorder(listCollumBorder);
        list.add(scorePanel);
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        datePanel.setBorder(listCollumBorder);
        list.add(datePanel);

        //Überschriften der Spalten
        JLabel collumName = new JLabel(Constants.DB_COLLUM_NAME);
        collumName.setAlignmentX(Component.CENTER_ALIGNMENT);
        collumName.setBorder(listCollumHeaderBorder);
        namePanel.add(collumName);
        JLabel collumScore = new JLabel(Constants.DB_COLLUM_SCORE);
        collumScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        collumScore.setBorder(listCollumHeaderBorder);
        scorePanel.add(collumScore);
        JLabel collumDate = new JLabel(Constants.DB_COLLUM_DATE);
        collumDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        collumDate.setBorder(listCollumHeaderBorder);
        datePanel.add(collumDate);

        //Füllen der Tabelle
        try {
            ResultSet highScoreSet = DBConnection.getInstance().query("SELECT * FROM " + Constants.DB_TABLE + " ORDER BY " + Constants.DB_COLLUM_SCORE + " DESC LIMIT 10;");
            while(highScoreSet.next()){
                JLabel nameCell = new JLabel(highScoreSet.getString(Constants.DB_COLLUM_NAME));
                nameCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                nameCell.setBorder(listCellBorder);
                namePanel.add(nameCell);

                JLabel scoreCell = new JLabel(Integer.toString(highScoreSet.getInt(Constants.DB_COLLUM_SCORE)));
                scoreCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                scoreCell.setBorder(listCellBorder);
                scorePanel.add(scoreCell);

                Date date = highScoreSet.getDate(Constants.DB_COLLUM_DATE);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
                String dateString = dateFormat.format(date);

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
