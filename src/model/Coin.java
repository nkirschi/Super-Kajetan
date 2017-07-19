package model;

import logic.Behavior;
import util.Constants;
import util.SoundUtil;

import java.awt.geom.Rectangle2D;

public class Coin extends Enemy {
    private final double COIN_WIDTH = 78;
    private final double COIN_HEIGHT = 90;

    public Coin(double x, double y) {
        super(x, y, Behavior.COIN, Direction.RIGHT);
        hitbox = new Rectangle2D.Double(x - COIN_WIDTH / 2, y - COIN_HEIGHT, COIN_WIDTH, COIN_HEIGHT);
        viewingRange = Constants.COIN_VIEWING_RANGE;
        attackRange = Constants.COIN_ATTACK_RANGE;
        minTimeBetweenAttack = Constants.COIN_ATTACK_INTERVAL;
        health = getMaxHealth();
        worthiness = Constants.COIN_WORTHINESS;
        strength = Constants.COIN_STRENGTH;
        paintWeapon = false;
        paintHealth = false;
    }

    public String getImagePath() {
        return "images/enemies/coin.png";
    }

    @Override
    public int getMaxHealth() {
        return Constants.COIN_MAX_HEALTH;
    }

    @Override
    public String toString() {
        return "Coin at " + super.toString();
    }

    @Override
    public void suffer(int damage) {
        health -= damage;
        new Thread(() -> SoundUtil.playEffect("coin")).start();
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
