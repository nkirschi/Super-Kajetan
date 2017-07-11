package model;

import java.awt.geom.Ellipse2D;

public class Spider extends Enemy {
    private double range;

    public Spider(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        viewingRange = 100;
        attackRange = 10;
    }

    public String getImagePath() {
        return "";
    }

    @Override
    public String toString() {
        return "Spider at " + super.toString();
    }
}
