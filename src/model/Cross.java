package model;

import logic.Behavior;
import util.Constants;
import util.SoundUtil;

import java.awt.geom.Rectangle2D;

public class Cross extends Enemy {
    private final double CROSS_WIDTH = 70;
    private final double CROSS_HEIGHT = 90;

    public Cross(double x, double y) {
        super(x, y, Behavior.CROSS, Direction.RIGHT);
        hitbox = new Rectangle2D.Double(x - CROSS_WIDTH / 2, y - CROSS_HEIGHT, CROSS_WIDTH, CROSS_HEIGHT);
        viewingRange = Constants.CROSS_VIEWING_RANGE;
        attackRange = Constants.CROSS_ATTACK_RANGE;
        minTimeBetweenAttack = Constants.CROSS_ATTACK_INTERVAL;
        health = getMaxHealth();
        worthiness = Constants.CROSS_WORTHINESS;
        strength = Constants.CROSS_STRENGTH;
        paintWeapon = false;
        paintHealth = false;
    }

    public String getImagePath() {
        return "images/enemies/cross.png";
    }

    @Override
    public int getMaxHealth() {
        return Constants.CROSS_MAX_HEALTH;
    }

    @Override
    public String toString() {
        return "Cross at " + super.toString();
    }

    @Override
    public void suffer(int damage) {
        health -= damage;
        new Thread(() -> SoundUtil.playEffect("cross")).start();
    }

    @Override
    public void move() {
    }

    @Override
    public void addVelocityX(double velocityX) {
    }

    @Override
    public void addVelocityY(double velocityY) {
    }
}
