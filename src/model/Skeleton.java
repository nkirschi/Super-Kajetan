package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Skeleton extends Enemy {
    private final double SKELETON_WIDTH = 90;
    private final double SKELETON_HEIGHT = 160;

    private final double SWORD_WIDTH = 169;
    private final double SWORD_HEIGHT = 169;

    public Skeleton(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - SKELETON_WIDTH / 2, y - SKELETON_HEIGHT, SKELETON_WIDTH, SKELETON_HEIGHT);
        viewingRange = 500;
        attackRange = 170;
        health = getMaxHealth();
        worthiness = 10;
        strength = 200;
        sword = new Rectangle2D.Double(x, y - hitbox.getHeight() + 0, SWORD_WIDTH, SWORD_HEIGHT);
    }

    @Override
    public void move() {
        super.move();
        sword.setRect(viewingDirection.equals(Direction.RIGHT) ? x - 3 : x + 3 - sword.getWidth(), y - hitbox.getHeight() + 0, sword.getWidth(), sword.getHeight());
    }

    public String getImagePath() {
        if (walking) {
            if (walkCount < 25) {
                return "images/enemies/enemy_skeleton_walk_1.png";
            } else {
                return "images/enemies/enemy_skeleton_walk_2.png";
            }
        }
        return "images/enemies/enemy_skeleton_walk_1.png";
    }

    @Override
    public String getSwordImagePath(boolean strike) {
        return strike ? "images/spear/spear_strike.png" : "images/spear/spear.png";
    }

    @Override
    public int getMaxHealth() {
        return 200;
    }

    @Override
    public String toString() {
        return "Skeleton at " + super.toString();
    }

    @Override
    public Rectangle2D.Double getSword() {
        if (crouching) {
            return new Rectangle2D.Double(sword.x, sword.y - 15, sword.getWidth(), sword.getHeight());
        }
        return sword;
    }
}
