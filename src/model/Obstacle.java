package model;

import java.awt.geom.Rectangle2D;

public abstract class Obstacle implements Collidable {
    protected Rectangle2D.Double hitbox;
    protected double x, y;

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

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    public abstract String getImagePath();
}
