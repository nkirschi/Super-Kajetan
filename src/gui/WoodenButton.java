package gui;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class WoodenButton extends JButton {
    public WoodenButton(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(Constants.BUTTON_COLOR);
        setContentAreaFilled(false);
        setOpaque(true);
        setBorderPainted(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage img = null;
        try {
            img = util.ImageUtil.getImage("images/gui/wooden_button.png");
            setForeground(Color.WHITE);
            setOpaque(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (getModel().isPressed()) {
            g.drawImage(img, 0, getHeight(), getWidth(), -getHeight(), null);
        } else if (getModel().isRollover()) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            BorderFactory.createLineBorder(new Color(0, 0, 0)).paintBorder(this, g, 0, 0, getWidth(), getHeight()); //TODO schönere Border
        } else {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        }
        super.paintComponent(g);
    }
}
