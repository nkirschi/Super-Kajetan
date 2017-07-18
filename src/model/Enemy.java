package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Enemy extends Entity implements Cloneable {
    protected Behavior behavior;

    protected double viewingRange;
    protected double attackRange;
    protected int worthiness;
    protected long lastAttackTime; //Zeit des letzten Angriffs
    protected long minTimeBetweenAttack = 1500000000; //Zeit in ns zwischen zwei Angriffen (1 s = 1.000.000.000 ns)

    protected final double SWORD_WIDTH = 128;
    protected final double SWORD_HEIGHT = 128;
    protected Rectangle2D.Double sword;
    protected boolean attack;

    public Enemy(double x, double y, Behavior behavior, Direction viewingDirection) {
        this.x = x;
        this.y = y;
        this.behavior = behavior;
        this.viewingDirection = viewingDirection;
        walkCount = 0;
    }

    /**
     * Copy-Konstruktor
     *
     * @param enemy Zu kopierender Gegner
     */
    public Enemy(Enemy enemy) {
        x = enemy.getX();
        y = enemy.getY();
        velocityX = enemy.getVelocityX();
        velocityY = enemy.getVelocityY();
        behavior = enemy.getBehavior();
        viewingDirection = enemy.getViewingDirection();
        attackRange = enemy.getAttackRange();
        health = enemy.getHealth();
        onGround = enemy.isOnGround();
        hitbox = new Rectangle2D.Double(enemy.getHitbox().getX(), enemy.getHitbox().getY(),
                enemy.getHitbox().getWidth(), enemy.getHitbox().getHeight());
        walking = enemy.isWalking();
        running = enemy.isRunning();
        jumping = enemy.isJumping();
        crouching = enemy.isCrouching();
        walkCount = 0;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public double getViewingRange() {
        return viewingRange;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public int getWorthiness() {
        return worthiness;
    }

    @Override
    public String getImagePath() {
        return "";
    }

    protected int walkCount;

    //Für die Regulierung der Angriffe
    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public long getMinTimeBetweenAttack() {
        return minTimeBetweenAttack;
    }

    public Rectangle2D.Double getSword() {
        return sword;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }
}
