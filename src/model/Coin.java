package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Coin extends Enemy {
    private final double COIN_WIDTH = 90;
    private final double COIN_HEIGHT = 90;

    public Coin(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - COIN_WIDTH / 2, y - COIN_HEIGHT, COIN_WIDTH, COIN_HEIGHT);
        viewingRange = 0;
        attackRange = 0;
        health = getMaxHealth();
        worthiness = 25;
        strength = 0;
    }

    public String getImagePath() {
        return "images/enemies/enemy_skeleton_walk_1.png";
    }

    @Override
    public int getMaxHealth() {
        return 1;
    }

    @Override
    public String toString() {
        return "Coin at " + super.toString();
    }
}
