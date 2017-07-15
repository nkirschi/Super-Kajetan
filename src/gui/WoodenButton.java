package gui;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class WoodenButton extends JButton {
    WoodenButton(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(Constants.BUTTON_COLOR);
        setForeground(Constants.FOREGROUND_COLOR);
        setContentAreaFilled(false);
        setBorderPainted(true);
        addActionListener(a -> util.SoundUtil.playEffect("sounds/buttonclick.ogg", "buttonclick.ogg"));
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage img = null;
        try {
            img = util.ImageUtil.getImage("images/gui/wooden_button.png");
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
