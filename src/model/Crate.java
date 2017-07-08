package model;

import java.awt.geom.Rectangle2D;

public class Crate extends Obstacle {
    private final double CRATE_WIDTH = 100;
    private final double CRATE_HEIGHT = 100;
    protected Rectangle2D.Double hitbox;

    public Crate(double x, double y) {
        this.x = x;
        this.y = y;
        hitbox = new Rectangle2D.Double(x - CRATE_WIDTH / 2, y - CRATE_HEIGHT, CRATE_WIDTH, CRATE_HEIGHT);
    }

    @Override
    public String getImagePath() {
        return "";
    }
}
