package util;

import sun.awt.AWTAccessor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Constants {
    public static final String GAME_VERSION = "Alpha v1.1.2_01";

    // GUI
    public static final Color MENU_BACKGROUND_COLOR = new Color(131, 131, 131);
    public static final Color BUTTON_COLOR = new Color(197, 197, 197);
    public static final Dimension defaultButtonSize = new Dimension(250, 50);
    public static final Font DEFAULT_FONT = new Font("Pixel Operator", Font.PLAIN, 17);

    // Datenbank
    public static final String DB_TABLE = "Highscores";
    public static final String DB_COLLUM_NAME = "Nickname";
    public static final String DB_COLLUM_SCORE = "Score";
    public static final String DB_COLLUM_DATE = "Datum";

    // Spielmechanik
    public static final int UPDATE_CLOCK = 50;
    public static final int GROUND_LEVEL = 720;
    public static final double PLAYER_HORIZONTAL_MOVE_AMOUNT = 2.5;
    public static final double PLAYER_VERTICAL_MOVE_AMOUNT = 16;
    public static final double GRAVITATIONAL_ACCELERATION = 0.75;

    // Tastatur
    public static int KEY_JUMP = KeyEvent.VK_W;
    public static int KEY_LEFT = KeyEvent.VK_A;
    public static int KEY_RIGHT = KeyEvent.VK_D;
    public static int KEY_CROUCH = KeyEvent.VK_S;
    public static int KEY_RUN = KeyEvent.VK_SHIFT;

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
