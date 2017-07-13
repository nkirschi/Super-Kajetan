package model;

import java.awt.geom.Rectangle2D;

public class Barrel extends Obstacle {
    private final double BARREL_WIDTH = 98;
    private final double BARREL_HEIGHT = 128;

    public Barrel(double x, double y) {
        this.x = x;
        this.y = y;
        hitbox = new Rectangle2D.Double(x - BARREL_WIDTH / 2, y - BARREL_HEIGHT, BARREL_WIDTH, BARREL_HEIGHT);
    }

    @Override
    public String getImagePath() {
        return "images/obstacles/barrel.png";
    }
}
