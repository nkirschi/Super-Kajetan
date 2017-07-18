package model;

import logic.Behavior;
import util.SoundUtil;

import java.awt.geom.Rectangle2D;

public class Coin extends Enemy {
    private final double COIN_WIDTH = 78;
    private final double COIN_HEIGHT = 90;

    public Coin(double x, double y) {
        super(x, y, Behavior.IDLE, Direction.RIGHT);
        hitbox = new Rectangle2D.Double(x - COIN_WIDTH / 2, y - COIN_HEIGHT, COIN_WIDTH, COIN_HEIGHT);
        viewingRange = 0;
        attackRange = 0;
        health = getMaxHealth();
        worthiness = 25;
        strength = 0;
    }

    public String getImagePath() {
        return "images/enemies/coin.png";
    }

    @Override
    public int getMaxHealth() {
        return 1;
    }

    @Override
    public String toString() {
        return "Coin at " + super.toString();
    }

    @Override
    public boolean suffer(int damage) {
        health -= damage;
        new Thread(() -> SoundUtil.playEffect("coin")).start();

        return health > 0;
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
