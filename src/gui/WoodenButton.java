package gui;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class WoodenButton extends JButton {
    public WoodenButton(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(Constants.BUTTON_COLOR);
        setContentAreaFilled(false);
        setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            try {
                g.drawImage(util.ImageUtil.getImage("images/gui/wooden_button.png"), 0, getHeight(), getWidth(), -getHeight(), null);
                setForeground(Color.WHITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                g.drawImage(util.ImageUtil.getImage("images/gui/wooden_button.png"), 0, 0, getWidth(), getHeight(), null);
                setForeground(Color.WHITE);
                setOpaque(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.paintComponent(g);
    }
}
