package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Knight extends Enemy {
    private final double SPIDER_WIDTH = 90;
    private final double SPIDER_HEIGHT = 160;

    public Knight(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - SPIDER_WIDTH / 2, y - SPIDER_HEIGHT, SPIDER_WIDTH, SPIDER_HEIGHT);
        viewingRange = 500;
        attackRange = 100;
        health = getMaxHealth();
        worthiness = 5;
    }

    public String getImagePath() {
        return "images/enemies/skeleton_1.png";
    }

    @Override
    public int getMaxHealth() {
        return 200;
    }

    @Override
    public String toString() {
        return "Skeleton at " + super.toString();
    }
}
