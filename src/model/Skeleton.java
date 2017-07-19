package model;

import logic.Behavior;
import util.Constants;

import java.awt.geom.Rectangle2D;

public class Skeleton extends Enemy {
    private final double SKELETON_WIDTH = 90;
    private final double SKELETON_HEIGHT = 148;

    private final double SPEAR_WIDTH = 169;
    private final double SPEAR_HEIGHT = 169;

    public Skeleton(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - SKELETON_WIDTH / 2, y - SKELETON_HEIGHT, SKELETON_WIDTH, SKELETON_HEIGHT);
        viewingRange = Constants.SKELETON_VIEWING_RANGE;
        attackRange = Constants.SKELETON_ATTACK_RANGE;
        minTimeBetweenAttack = Constants.SKELETON_ATTACK_INTERVAL;
        health = getMaxHealth();
        worthiness = Constants.SKELETON_WORTHINESS;
        strength = Constants.SKELETON_STRENGTH;
        weapon = new Rectangle2D.Double(x - 40, y - hitbox.getHeight() + 17, SPEAR_WIDTH, SPEAR_HEIGHT);
    }

    @Override
    public void move() {
        super.move();
        weapon.setRect(viewingDirection.equals(Direction.RIGHT) ? x - 40 : x + 40 - weapon.getWidth(), y - hitbox.getHeight() + 17, weapon.getWidth(), weapon.getHeight());
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
    public String getWeaponImagePath(boolean attacking) {
        return attacking ? "images/spear/spear_strike.png" : "images/spear/spear.png";
    }

    @Override
    public int getMaxHealth() {
        return Constants.SKELETON_MAX_HEALTH;
    }

    @Override
    public String toString() {
        return "Skeleton at " + super.toString();
    }

    @Override
    public Rectangle2D.Double getWeapon() {
        if (crouching) {
            return new Rectangle2D.Double(weapon.x, weapon.y - 15, weapon.getWidth(), weapon.getHeight());
        }
        return weapon;
    }
}
