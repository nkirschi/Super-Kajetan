package util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Constants {
    public static final String GAME_TITLE = "Wir brauchen noch einen gescheiten Namen! Max' Vorschlag: \"Rains of Castamere\"";
    public static final String GAME_VERSION = "Alpha v1.1.2_01";

    // GUI
    public static final Color MENU_BACKGROUND_COLOR = new Color(109, 119, 131);
    public static final Color BUTTON_COLOR = new Color(197, 197, 197);
    public static final Color FOREGROUND_COLOR = Color.WHITE;
    public static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(250, 50);
    public static final Dimension DEFAULT_BUTTON_SIZE_2 = new Dimension(100, 35);
    public static final Font DEFAULT_FONT = new Font("Pixel Operator", Font.PLAIN, 17);
    public static final String MENU_BACKGROUND = "images/menubackground_nobanner.png";
    public static final String MENU_BACKGROUND_NOBANNER = "images/menubackground_nobanner.png";

    // Datenbank
    public static final String DB_TABLE = "Highscores";
    public static final String DB_COLLUM_NAME = "Nickname";
    public static final String DB_COLLUM_SCORE = "Score";
    public static final String DB_COLLUM_DATE = "Datum";

    // Properties
    public static final String PROPERTY_CONTROL_MODE = "alt-control-mode";
    public static final String PROPERTY_VOLUME = "volume";

    // Spielmechanik
    public static final int UPDATE_CLOCK = 60;
    public static final int GROUND_LEVEL = 720;
    public static final double SPEED_FACTOR = 2;
    public static final double PLAYER_WALK_VELOCITY = 2.5;
    public static final double PLAYER_INITIAL_JUMP_VELOCITY = 20;
    public static final double GRAVITATIONAL_ACCELERATION = 0.5;

    // Tastatur
    public static final int KEY_RUN = KeyEvent.VK_SHIFT;
    public static final int KEY_MENU = KeyEvent.VK_ESCAPE;
    public static final int KEY_DEBUG = KeyEvent.VK_F1;

    public static final int KEY_LEFT_DEFAULT = KeyEvent.VK_A;
    public static final int KEY_RIGHT_DEFAULT = KeyEvent.VK_D;
    public static final int KEY_JUMP_DEFAULT = KeyEvent.VK_W;
    public static final int KEY_CROUCH_DEFAULT = KeyEvent.VK_S;

    public static final int ALT_KEY_LEFT = KeyEvent.VK_LEFT;
    public static final int ALT_KEY_RIGHT = KeyEvent.VK_RIGHT;
    public static final int ALT_KEY_JUMP = KeyEvent.VK_UP;
    public static final int ALT_KEY_CROUCH = KeyEvent.VK_DOWN;

    //Player Bilder
    public static final String PLAYER_STAND_IMAGE_PATH = "images/char/char_stand.png";
    public static final String PLAYER_WALK_1_IMAGE_PATH = "images/char/char_walk_1.png";
    public static final String PLAYER_WALK_2_IMAGE_PATH = "images/char/char_walk_2.png";
    public static final String PLAYER_JUMP_IMAGE_PATH = "images/char/char_jump.png";
    public static final String PLAYER_CROUCH_1_IMAGE_PATH = "images/char/char_walk_crouch_1.png";
    public static final String PLAYER_CROUCH_2_IMAGE_PATH = "images/char/char_walk_crouch_2.png";

    //Knight Bilder
    public static final String KNIGHT_STAND_IMAGE_PATH = "images/char/char_stand.png";
    public static final String KNIGHT_WALK_1_IMAGE_PATH = "images/char/char_walk_1.png";
    public static final String KNIGHT_WALK_2_IMAGE_PATH = "images/char/char_walk_2.png";
    public static final String KNIGHT_JUMP_IMAGE_PATH = "images/char/char_jump.png";
    public static final String KNIGHT_CROUCH_1_IMAGE_PATH = "images/char/char_walk_crouch_1.png";
    public static final String KNIGHT_CROUCH_2_IMAGE_PATH = "images/char/char_walk_crouch_2.png";

    static {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResourceAsStream("PixelOperator.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            Logger.log(e, Logger.WARNING);
        }
    }
}
