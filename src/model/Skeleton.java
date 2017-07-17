package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Skeleton extends Enemy {
    private final double SKELETON_WIDTH = 90;
    private final double SKELETON_HEIGHT = 160;

    public Skeleton(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - SKELETON_WIDTH / 2, y - SKELETON_HEIGHT, SKELETON_WIDTH, SKELETON_HEIGHT);
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