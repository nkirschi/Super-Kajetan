package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Coin extends Enemy {
    private final double COIN_WIDTH = 90;
    private final double COIN_HEIGHT = 90;

    public Coin(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - COIN_WIDTH / 2, y - COIN_HEIGHT, COIN_WIDTH, COIN_HEIGHT);
        viewingRange = 500;
        attackRange = 100;
        health = getMaxHealth();
        worthiness = 10;
        strength = 0;
    }

    public String getImagePath() {
        if (walking) {
            if (2 < 25) {
                return "images/enemies/enemy_skeleton_walk_1.png";
            }
            else {
                return "images/enemies/enemy_skeleton_walk_2.png";
            }
        }
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
