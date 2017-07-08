package model;

import java.awt.geom.Rectangle2D;

public abstract class Obstacle implements Collidable {
    protected Rectangle2D.Double hitbox;
    protected double x;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    protected double y;

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    public abstract String getImagePath();
}
