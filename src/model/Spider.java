package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Spider extends Enemy {
    private final double SPIDER_WIDTH = 128;
    private final double SPIDER_HEIGHT = 128;

    public Spider(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - SPIDER_WIDTH / 2, y - SPIDER_HEIGHT, SPIDER_WIDTH, SPIDER_HEIGHT);
        viewingRange = 100;
        attackRange = 10;
        health = getMaxHealth();
        worthiness = 5;
    }

    public String getImagePath() {
        return "images/enemies/spider.png";
    }

    @Override
    public int getMaxHealth() {
        return 200;
    }

    @Override
    public String toString() {
        return "Spider at " + super.toString();
    }
}
