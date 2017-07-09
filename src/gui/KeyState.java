package gui;

import util.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyState extends KeyAdapter {
    boolean left, right, run, jump, crouch, menu, debug;

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == Constants.KEY_LEFT)
            left = true;
        else if (keyCode == Constants.KEY_RIGHT)
            right = true;
        else if (keyCode == Constants.KEY_RUN)
            run = true;
        else if (keyCode == Constants.KEY_JUMP)
            jump = true;
        else if (keyCode == Constants.KEY_CROUCH)
            crouch = true;
        else if (keyCode == Constants.KEY_MENU)
            menu = !menu;
        else if (keyCode == Constants.KEY_DEBUG)
            debug = !debug;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == Constants.KEY_LEFT)
            left = false;
        else if (keyCode == Constants.KEY_RIGHT)
            right = false;
        else if (keyCode == Constants.KEY_RUN)
            run = false;
        else if (keyCode == Constants.KEY_JUMP)
            jump = false;
        else if (keyCode == Constants.KEY_CROUCH)
            crouch = false;
    }
}
