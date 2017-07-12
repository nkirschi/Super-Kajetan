package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class WoodenButton extends JButton {
    public WoodenButton(String text) {
        super(text);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            g.drawImage(util.ImageUtil.getImage("images/woodenButton.png"), 0, 0, getWidth(), getHeight(), null);
            setOpaque(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
