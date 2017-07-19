package util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Constants {
    public static final String GAME_TITLE = "Super Kajetan";
    public static final String GAME_VERSION = "Alpha 1.1.2_01";

    // GUI
    public static final Color MENU_BACKGROUND_COLOR = new Color(109, 119, 131);
    public static final Color BUTTON_COLOR = new Color(197, 197, 197);
    public static final Color FOREGROUND_COLOR = new Color(225, 225, 225);
    public static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(250, 50);
    public static final Dimension DEFAULT_BUTTON_SIZE_2 = new Dimension(100, 35);
    public static final Font DEFAULT_FONT = new Font("Pixel Operator", Font.PLAIN, 17);
    public static final String MENU_BACKGROUND = "images/gui/menubackground_nobanner_sign.png";
    public static final String MENU_BACKGROUND_2 = "images/gui/menubackground_nobanner.png";

    // Datenbank
    public static final String DB_TABLE = "Highscores";
    public static final String DB_COLLUM_NAME = "Nickname";
    public static final String DB_COLLUM_SCORE = "Score";
    public static final String DB_COLLUM_DATE = "Datum";

    // Properties
    public static final String PROPERTY_CONTROL_MODE = "alt-control-mode";
    public static final String PROPERTY_MUSIC_VOLUME = "music-volume";
    public static final String PROPERTY_EFFECT_VOLUME = "effect-volume";

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
    public static final int KEY_STRIKE_DEFAULT = KeyEvent.VK_ENTER;

    public static final int ALT_KEY_LEFT = KeyEvent.VK_LEFT;
    public static final int ALT_KEY_RIGHT = KeyEvent.VK_RIGHT;
    public static final int ALT_KEY_JUMP = KeyEvent.VK_UP;
    public static final int ALT_KEY_CROUCH = KeyEvent.VK_DOWN;
    public static final int ALT_KEY_STRIKE = KeyEvent.VK_SPACE;

    //Balancing-Werte (Zur einfacheren Verwaltung hier vorzufinden)
    //Player
    public static final int PLAYER_MAX_HEALTH = 1000; //Leben
    public static final int PLAYER_STRENGTH = 100; //Angriffsstärke
    public static final double PLAYER_MAX_STAMINA = 1000; //Ausdauer

    //Knight
    public static final int KNIGHT_MAX_HEALTH = 500; //Leben
    public static final int KNIGHT_WORTHINESS = 7; //Wie viel Score man für das Töten bekommt
    public static final double KNIGHT_VIEWING_RANGE = 1000; //Die Sichtweite des Gegners in px
    public static final int KNIGHT_STRENGTH = 125; //Angriffsstärke
    public static final double KNIGHT_ATTACK_RANGE = 200; //Die Angriffs-Reichweite des Gegners in px
    public static final long KNIGHT_ATTACK_INTERVAL = (long) (1000000000 * 1.5); //Minimale Zeit zwischen Angriffen; der Teil hinter * ist in Sekunden

    //Skelett
    public static final int SKELETON_MAX_HEALTH = 200;
    public static final int SKELETON_WORTHINESS = 10;
    public static final double SKELETON_VIEWING_RANGE = 500;
    public static final int SKELETON_STRENGTH = 200;
    public static final double SKELETON_ATTACK_RANGE = 145;
    public static final long SKELETON_ATTACK_INTERVAL = (long) (1000000000 * 1.5);

    //Helper
    public static final int HELPER_MAX_HEALTH = 200;
    public static final int HELPER_WORTHINESS = -1;
    public static final double HELPER_VIEWING_RANGE = 500;
    public static final int HELPER_STRENGTH = 0;
    public static final double HELPER_ATTACK_RANGE = 0;
    public static final long HELPER_ATTACK_INTERVAL = (long) (1000000000 * 1.5);

    //Coin
    public static final int COIN_MAX_HEALTH = 1;
    public static final int COIN_WORTHINESS = 25;
    public static final double COIN_VIEWING_RANGE = 0;
    public static final int COIN_STRENGTH = 0;
    public static final double COIN_ATTACK_RANGE = 0;
    public static final long COIN_ATTACK_INTERVAL = (long) (1000000000 * 1.5);

    static {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResourceAsStream("PixelOperator.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}
