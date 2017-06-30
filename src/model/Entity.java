package model;

import util.Point;

import java.awt.geom.Rectangle2D;

public abstract class Entity implements Collidable {
    Point position; // Der Punkt in der Mitte unten am Entity
    Rectangle2D.Double hitbox;

    int health;

    boolean walking;
    boolean running;
    boolean jumping;
    boolean crouching;


    protected Direction viewingDirection;

    public Point getPosition() {
        return position;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    /**
     * Bewegung der Entität um Koordinatendifferenzen
     *
     * @param x Delta-x, um das verschoben wird
     * @param y Delta-y, um das verschoben wird
     */
    public void move(double x, double y) {
        position.move(x, y);
        hitbox.setRect(hitbox.getX() + x, hitbox.getY() + y, hitbox.getWidth(), hitbox.getHeight());
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
        return position + ": health = " + health + ", walking = " + walking + ", running = " + running +
                ", jumping = " + jumping + ", crouching = " + crouching;
    }
}
