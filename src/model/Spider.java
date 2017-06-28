package model;

import util.ImageUtil;
import util.Point;

import java.io.IOException;

public class Spider extends Enemy {
    public Spider(Point position) {
        position = new Point();
    }

    public Point getPosition() {
        return position;
    }

    public void move(double x, double y) {
        position.move(x, y);
    }
}
