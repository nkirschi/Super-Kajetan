package gui;

import util.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    boolean left, right, run, jump, crouch, menu, debug;

    private final int KEY_JUMP;
    private final int KEY_LEFT;
    private final int KEY_RIGHT;
    private final int KEY_CROUCH;
    private final int KEY_RUN;
    private final int KEY_MENU;
    private final int KEY_DEBUG;

    public KeyHandler() {
        super();
        if (SettingsView.getInstance().getAltControlMode()) {
            KEY_JUMP = Constants.ALT_KEY_JUMP;
            KEY_LEFT = Constants.ALT_KEY_LEFT;
            KEY_RIGHT = Constants.ALT_KEY_RIGHT;
            KEY_CROUCH = Constants.ALT_KEY_CROUCH;
            KEY_MENU = Constants.KEY_MENU;
            KEY_DEBUG = Constants.KEY_DEBUG;
            KEY_RUN = Constants.KEY_RUN;
        } else {
            KEY_JUMP = Constants.KEY_JUMP_DEFAULT;
            KEY_LEFT = Constants.KEY_LEFT_DEFAULT;
            KEY_RIGHT = Constants.KEY_RIGHT_DEFAULT;
            KEY_CROUCH = Constants.KEY_CROUCH_DEFAULT;
            KEY_MENU = Constants.KEY_MENU;
            KEY_DEBUG = Constants.KEY_DEBUG;
            KEY_RUN = Constants.KEY_RUN;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KEY_LEFT)
            left = true;
        else if (keyCode == KEY_RIGHT)
            right = true;
        else if (keyCode == KEY_RUN)
            run = true;
        else if (keyCode == KEY_JUMP)
            jump = true;
        else if (keyCode == KEY_CROUCH)
            crouch = true;
        else if (keyCode == KEY_MENU)
            menu = !menu;
        else if (keyCode == KEY_DEBUG)
            debug = !debug;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KEY_LEFT)
            left = false;
        else if (keyCode == KEY_RIGHT)
            right = false;
        else if (keyCode == KEY_RUN)
            run = false;
        else if (keyCode == KEY_JUMP)
            jump = false;
        else if (keyCode == KEY_CROUCH)
            crouch = false;
    }

    public void clear() {
        left = false;
        right = false;
        run = false;
        jump = false;
        crouch = false;
        menu = false;
        debug = false;
    }
}
