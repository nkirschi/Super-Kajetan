package gui;

import util.Constants;
import util.SoundUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        //Buttonsound
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                SoundUtil.playEffect("buttonclick");
            }
        });
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
            setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
        } else if (getModel().isRollover()) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.WHITE));
        } else {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            setBorder(BorderFactory.createEmptyBorder());
        }
        super.paintComponent(g);
    }
}
