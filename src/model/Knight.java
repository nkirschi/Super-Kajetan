package model;

import util.ImageUtil;
import util.Point;

import java.io.IOException;

public class Knight extends Enemy {
    public Knight(Point position) {
        position = new Point();
    }

    public Point getPosition() {
        return position;
    }

    public void move(double x, double y) {
        position.move(x, y);
    }

    public void loadImage() {
        try {
            ImageUtil.getImage("images");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
