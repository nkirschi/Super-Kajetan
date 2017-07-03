package model;

import util.Point;

import java.awt.geom.Rectangle2D;

public class Knight extends Enemy {
    private final double KNIGHT_WIDTH = 100;
    private final double KNIGHT_HEIGHT = 200;

    public Knight(Point position) {
        this.position = position;
        hitbox = new Rectangle2D.Double(position.getX() - KNIGHT_WIDTH / 2, position.getY() - KNIGHT_HEIGHT, KNIGHT_WIDTH, KNIGHT_HEIGHT);
    }

    public String getImagePath() {
        return "";
    }
}
