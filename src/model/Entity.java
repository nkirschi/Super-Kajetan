package model;

import java.awt.geom.Rectangle2D;

public abstract class Entity implements Collidable {
    Rectangle2D.Double hitbox;

    int health;

    boolean walking;
    boolean running;
    boolean jumping;
    boolean crouching;

    protected double x, y;

    protected double velocityX, velocityY;

    protected Direction viewingDirection;


    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    /**
     * Ausführung der Bewegung in Abhängigkeit der Geschwindigkeitskomponenten
     */
    public void move() {
        this.x += velocityX;
        this.y += velocityY;
        hitbox.setRect(hitbox.getX() + velocityX, hitbox.getY() + velocityY, hitbox.getWidth(), hitbox.getHeight());
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        hitbox.setRect(x - hitbox.getWidth() / 2, y - hitbox.getHeight(), hitbox.getWidth(), hitbox.getHeight());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        hitbox.setRect(x - hitbox.getWidth() / 2, hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        hitbox.setRect(hitbox.getX(), y - hitbox.getHeight(), hitbox.getWidth(), hitbox.getHeight());
    }

    public int getHealth() {
        return health;
    }

    /**
     * @param damage Hinzuzufügender Schaden
     * @return Wahrheitswert, ob entity noch lebt.
     */
    public boolean suffer(int damage) {
        health -= damage;
        return health > 0;
    }

    public boolean isWalking() {
        return walking;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public Direction getViewingDirection() {
        return viewingDirection;
    }

    public void setViewingDirection(Direction viewingDirection) {
        this.viewingDirection = viewingDirection;
    }

    public abstract String getImagePath();

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") " + ": health = " + health + ", walking = " + walking +
                ", running = " + running + ", jumping = " + jumping + ", crouching = " + crouching;
    }
}
