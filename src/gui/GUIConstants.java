package gui;

import java.awt.*;

public class GUIConstants {
    public static final String GAME_VERSION = "Alpha v1.1.2_01";

    public static final Color MENU_BACKGROUND_COLOR = new Color(131, 131, 131);
    public static final Color BUTTON_COLOR = new Color(197, 197, 197);
    public static Dimension defaultButtonSize = new Dimension(250, 50);
    //TODO Default Font, Textgröße, ...

    public static final String DB_TABLE = "Highscores";
    public static final String DB_COLLUM_NAME = "Nickname";
    public static final String DB_COLLUM_SCORE = "Score";
    public static final String DB_COLLUM_DATE = "Datum";

    public static void setDefaultButtonSize(Dimension dimension) {
        defaultButtonSize = dimension;
    }
}
