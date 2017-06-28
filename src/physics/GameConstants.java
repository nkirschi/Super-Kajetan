package physics;

import java.awt.event.KeyEvent;

public class GameConstants {
    public static final int UPDATE_CLOCK = 50;
    public static final int GROUND_LEVEL = 720;
    public static final double CROUCHING_DELTA = 60;
    public static final double PLAYER_MOVE_AMOUNT = 2.5;
    public static final double PLAYER_JUMP_AMOUNT = 16;

    public static final int KEY_JUMP = KeyEvent.VK_W;
    public static final int KEY_LEFT = KeyEvent.VK_A;
    public static final int KEY_RIGHT = KeyEvent.VK_D;
    public static final int KEY_CROUCH = KeyEvent.VK_S;

    public static final String PLAYER_STAND_IMAGE_PATH = "images/char/char_stand_0.66.png";
    public static final String PLAYER_WALK_1_IMAGE_PATH = "images/char/char_walk_1_0.66.png";
    public static final String PLAYER_WALK_2_IMAGE_PATH = "images/char/char_walk_2_0.66.png";
    public static final String PLAYER_JUMP_IMAGE_PATH = "images/char/char_jump_0.66.png";

}
