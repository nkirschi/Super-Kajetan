package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

/**
 * Created by Max on 18.07.2017.
 */

public class Helper extends Enemy {
    private final double HELPER_WIDTH = 90;
    private final double HELPER_HEIGHT = 160;

    private final double SWORD_WIDTH = 128;
    private final double SWORD_HEIGHT = 128;

    public Helper(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - HELPER_WIDTH / 2, y - HELPER_HEIGHT, HELPER_WIDTH, HELPER_HEIGHT);
        viewingRange = 500;
        attackRange = 0;
        health = getMaxHealth();
        worthiness = -1;
        strength = 0;
        sword = new Rectangle2D.Double(x, y - hitbox.getHeight() + 5, SWORD_WIDTH, SWORD_HEIGHT);
    }

    @Override
    public void move() {
        super.move();
        sword.setRect(viewingDirection.equals(Direction.RIGHT) ? x - 3 : x + 3 - sword.getWidth(), y - hitbox.getHeight() + 5, sword.getWidth(), sword.getHeight());
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
        return strike ? "images/sword/sword_giant_strike.png" : "images/sword/sword_giant.png";
    }

    @Override
    public int getMaxHealth() {
        return 200;
    }

    @Override
    public String toString() {
        return "Helper at " + super.toString();
    }

    @Override
    public Rectangle2D.Double getSword() {
        if (crouching) {
            return new Rectangle2D.Double(sword.x, sword.y - 15, sword.getWidth(), sword.getHeight());
        }
        return sword;
    }
}