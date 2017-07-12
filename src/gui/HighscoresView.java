package gui;

import util.Constants;
import util.DBConnection;
import util.ImageUtil;
import util.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;
    private JPanel highScoreList;
    private Border listCollumBorder = BorderFactory.createEmptyBorder(10, 50, 0, 50); //bottom sollte immer 0 sein
    private Border listCellBorder = BorderFactory.createEmptyBorder(20, 0, 20, 0); //left, right sollte immer 0 sein, wird von listCollumBorder übernommen
    private Border listCollumHeaderBorder = BorderFactory.createEmptyBorder(35, 0, 35, 0); //top,left,right sollte immer 0 sein, sie ^

    private HighscoresView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        WoodenButton backButton = new WoodenButton("Zurück");
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE_2);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private JPanel initHighScoreList() {
        //Damit alles schön mittig ist (auf der Y-Achse)
        JPanel listTopLevelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);
        listTopLevelPanel.setOpaque(false);

        JPanel list = new JPanel(new FlowLayout());
        list.setOpaque(false);

        //Tolle Spalte links mit Erster, Zweiter, ...
        JPanel fancyCollumPanel = new JPanel();
        fancyCollumPanel.setLayout(new BoxLayout(fancyCollumPanel, BoxLayout.Y_AXIS));
        fancyCollumPanel.setBorder(listCollumBorder);
        fancyCollumPanel.setOpaque(false);
        list.add(fancyCollumPanel);

        try {
            ImageIcon trophyImage = ImageUtil.getIcon("images/gui/trophy.png");
            JLabel trophyLabel = new JLabel(trophyImage);
            trophyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            fancyCollumPanel.add(trophyLabel);

            //Setzt jetzt die Border der Spalten-Überschriften entsprechend, um für Höhe des Bildes zu kompensieren. (15/2 kompensiert für texthöhe. unschön, wissen wir ....)
            listCollumHeaderBorder = BorderFactory.createEmptyBorder(trophyImage.getIconHeight() / 2 - Constants.DEFAULT_FONT.getSize() / 2, 0, trophyImage.getIconHeight() / 2 - Constants.DEFAULT_FONT.getSize() / 2, 0);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(e, Logger.WARNING);
        }


        //Einzelne Spalten
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBorder(listCollumBorder);
        namePanel.setOpaque(false);
        list.add(namePanel);
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBorder(listCollumBorder);
        scorePanel.setOpaque(false);
        list.add(scorePanel);
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setBorder(listCollumBorder);
        datePanel.setOpaque(false);
        list.add(datePanel);

        //Überschriften der Spalten
        JLabel nameColumn = new JLabel(Constants.DB_COLLUM_NAME);
        nameColumn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameColumn.setBorder(listCollumHeaderBorder);
        nameColumn.setForeground(Constants.FOREGROUND_COLOR);
        nameColumn.setFont(Constants.DEFAULT_FONT.deriveFont(Font.BOLD));
        namePanel.add(nameColumn);
        JLabel scoreColumn = new JLabel(Constants.DB_COLLUM_SCORE);
        scoreColumn.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreColumn.setBorder(listCollumHeaderBorder);
        scoreColumn.setForeground(Constants.FOREGROUND_COLOR);
        scoreColumn.setFont(Constants.DEFAULT_FONT.deriveFont(Font.BOLD));
        scorePanel.add(scoreColumn);
        JLabel dateColumn = new JLabel(Constants.DB_COLLUM_DATE);
        dateColumn.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateColumn.setBorder(listCollumHeaderBorder);
        dateColumn.setForeground(Constants.FOREGROUND_COLOR);
        dateColumn.setFont(Constants.DEFAULT_FONT.deriveFont(Font.BOLD));
        datePanel.add(dateColumn);

        //Füllen der Tabelle
        try {
            ResultSet highScoreSet = DBConnection.getInstance().query("SELECT * FROM " + Constants.DB_TABLE + " ORDER BY " + Constants.DB_COLLUM_SCORE + " DESC LIMIT 10;");
            for (int i = 1; highScoreSet.next(); i++) { //i wird für die Platznummer gebraucht ....
                JLabel nameCell = new JLabel(highScoreSet.getString(Constants.DB_COLLUM_NAME));
                nameCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                nameCell.setBorder(listCellBorder);
                nameCell.setForeground(Constants.FOREGROUND_COLOR);
                nameCell.setFont(Constants.DEFAULT_FONT);
                namePanel.add(nameCell);

                JLabel scoreCell = new JLabel(Integer.toString(highScoreSet.getInt(Constants.DB_COLLUM_SCORE)));
                scoreCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                scoreCell.setBorder(listCellBorder);
                scoreCell.setForeground(Constants.FOREGROUND_COLOR);
                scoreCell.setFont(Constants.DEFAULT_FONT);
                scorePanel.add(scoreCell);

                Date date = highScoreSet.getDate(Constants.DB_COLLUM_DATE);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String dateString = dateFormat.format(date);

                JLabel dateCell = new JLabel(dateString);
                dateCell.setAlignmentX(Component.CENTER_ALIGNMENT);
                dateCell.setBorder(listCellBorder);
                dateCell.setForeground(Constants.FOREGROUND_COLOR);
                dateCell.setFont(Constants.DEFAULT_FONT);
                datePanel.add(dateCell);

                //Für jeden Platz das Fancy "Platz .." Schild in dem Fancy Seiten-Panel; wird hier erzeugt, damit es keine leeren Plätze gibt ( falls weniger als 10 Highscores existieren )
                JLabel label = new JLabel(i + ". Platz");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                label.setBorder(listCellBorder);
                label.setForeground(Constants.FOREGROUND_COLOR);
                label.setFont(Constants.DEFAULT_FONT.deriveFont(Font.BOLD));
                fancyCollumPanel.add(label);

                listTopLevelPanel.add(list, constraints);
                Logger.log("Highscores initialisiert", Logger.INFO);

                if (highScoreSet.getString(Constants.DB_COLLUM_NAME).compareTo(MainMenuView.getInstance().getCurrentName()) == 0) {
                    Color currentPlayerScores = new Color(177, 108, 0);
                    nameCell.setForeground(currentPlayerScores);
                    scoreCell.setForeground(currentPlayerScores);
                    dateCell.setForeground(currentPlayerScores);
                    label.setForeground(currentPlayerScores);
                }

            }
            highScoreSet.close(); // gaaanz wichtig!!
        } catch (Exception e) {
            e.printStackTrace();
            listTopLevelPanel.add(new JLabel("HOPPLA! Da ist wohl was schief gegangen :/"), constraints);
            Logger.log("Fehler beim initialisieren der Highscores", Logger.WARNING);
            Logger.log(e, Logger.WARNING);
        }

        Logger.log("Highscores initialisiert", Logger.INFO);
        return listTopLevelPanel;
    }

    public void refresh() {
        if (highScoreList != null) {
            remove(highScoreList);
        }
        highScoreList = initHighScoreList();
        add(highScoreList, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    static HighscoresView getInstance() {
        if (instance == null)
            instance = new HighscoresView();
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
