package gui;

import util.Constants;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class WoodenButton extends JButton {
    public WoodenButton(String text) {
        super(text);

        setHorizontalTextPosition(CENTER);
        setFocusPainted(false);
        setBackground(Constants.BUTTON_COLOR);
    }

    @Override
    public void paintComponent(Graphics g) {

        try {
            setIcon(ImageUtil.getIcon("images/gui/wooden_button.png", getWidth(), getHeight()));
            setForeground(Constants.FOREGROUND_COLOR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.paintComponent(g);
    }
}
