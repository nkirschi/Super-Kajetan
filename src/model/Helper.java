package model;

import logic.Behavior;
import util.Constants;

import java.awt.geom.Rectangle2D;


public class Helper extends Enemy {
    private final double HELPER_WIDTH = 90;
    private final double HELPER_HEIGHT = 148;

    public Helper(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - HELPER_WIDTH / 2, y - HELPER_HEIGHT, HELPER_WIDTH, HELPER_HEIGHT);
        viewingRange = Constants.HELPER_VIEWING_RANGE;
        attackRange = Constants.HELPER_ATTACK_RANGE;
        minTimeBetweenAttack = Constants.HELPER_ATTACK_INTERVAL;
        health = getMaxHealth();
        worthiness = Constants.HELPER_WORTHINESS;
        strength = Constants.HELPER_STRENGTH;
        weapon = new Rectangle2D.Double(x, y - hitbox.getHeight() + 5, 0, 0);
    }

    @Override
    public void move() {
        super.move();
        weapon.setRect(viewingDirection.equals(Direction.RIGHT) ? x - 3 : x + 3 - weapon.getWidth(), y - hitbox.getHeight() + 5, weapon.getWidth(), weapon.getHeight());
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
        return "images/none.png";
    }

    @Override
    public int getMaxHealth() {
        return Constants.HELPER_MAX_HEALTH;
    }

    @Override
    public String toString() {
        return "Helper at " + super.toString();
    }

    @Override
    public Rectangle2D.Double getWeapon() {
        if (crouching) {
            return new Rectangle2D.Double(weapon.x, weapon.y - 15, weapon.getWidth(), weapon.getHeight());
        }
        return weapon;
    }
}