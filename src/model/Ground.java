package model;

import util.Constants;

import java.awt.geom.Rectangle2D;

public class Ground extends Obstacle {
    private double length;
    private Rectangle2D.Double hitbox;

    public Ground(double length) {
        this.length = length;
        hitbox = new Rectangle2D.Double(0, Constants.GROUND_LEVEL, length, 0);
    }

    @Override
    public String getImagePath() {
        return "";
    }
}
