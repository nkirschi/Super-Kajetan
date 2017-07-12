package gui;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class WoodenButton extends JButton {
    public WoodenButton(String text) {
        super(text);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            try {
                g.drawImage(util.ImageUtil.getImage("images/gui/wooden_button.png"), 0, getHeight(), getWidth(), -getHeight(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                g.drawImage(util.ImageUtil.getImage("images/gui/wooden_button.png"), 0, 0, getWidth(), getHeight(), null);
                setOpaque(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.paintComponent(g);
    }
}
