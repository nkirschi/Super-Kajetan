package gui;

import util.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    boolean left, right, run, jump, crouch, menu, debug;
    private int keyJump;
    private int keyLeft;
    private int keyRight;
    private int keyCrouch;

    private int keyRun;
    private int keyMenu;
    private int keyDebug;


    public KeyHandler() {
        super();
        if (SettingsView.getInstance().getAltControlMode()) {
            keyJump = Constants.ALT_KEY_JUMP;
            keyLeft = Constants.ALT_KEY_LEFT;
            keyRight = Constants.ALT_KEY_RIGHT;
            keyCrouch = Constants.ALT_KEY_CROUCH;
            keyMenu = Constants.KEY_MENU;
            keyDebug = Constants.KEY_DEBUG;
            keyRun = Constants.KEY_RUN;
        } else {
            keyJump = Constants.KEY_JUMP;
            keyLeft = Constants.KEY_LEFT;
            keyRight = Constants.KEY_RIGHT;
            keyCrouch = Constants.KEY_CROUCH;
            keyMenu = Constants.KEY_MENU;
            keyDebug = Constants.KEY_DEBUG;
            keyRun = Constants.KEY_RUN;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == keyLeft)
            left = true;
        else if (keyCode == keyRight)
            right = true;
        else if (keyCode == keyRun)
            run = true;
        else if (keyCode == keyJump)
            jump = true;
        else if (keyCode == keyCrouch)
            crouch = true;
        else if (keyCode == keyMenu)
            menu = !menu;
        else if (keyCode == keyDebug)
            debug = !debug;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == keyLeft)
            left = false;
        else if (keyCode == keyRight)
            right = false;
        else if (keyCode == keyRun)
            run = false;
        else if (keyCode == keyJump)
            jump = false;
        else if (keyCode == keyCrouch)
            crouch = false;
    }
}
