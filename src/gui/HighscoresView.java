package gui;

import util.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

class HighscoresView extends AbstractView {
    private static HighscoresView instance;
    private ResultSet highScoreSet;

    private HighscoresView() {
        super();
        update();
    }

    public void update() {
        try {
            highScoreSet = DBConnection.getInstance().query("SELECT * FROM "+GUIConstants.DB_TABLE+" ORDER BY "+GUIConstants.DB_COLLUM_SCORE+" DESC LIMIT 10;");
            while(highScoreSet.next()){
                System.out.println(highScoreSet.getString(GUIConstants.DB_COLLUM_NAME)+"   "+highScoreSet.getInt(GUIConstants.DB_COLLUM_SCORE));
            }
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
