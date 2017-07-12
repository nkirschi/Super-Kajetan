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
        setBorderPainted(true);

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
            setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK));
        } else if (getModel().isRollover()) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.DARK_GRAY));
        } else {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);//TODO schönere Border
            setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.WHITE));
        }
        super.paintComponent(g);
    }
}
