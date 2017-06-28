package model;

import util.Point;

import java.awt.geom.Rectangle2D;

public abstract class Obstacle implements Collidable {
    protected Point position;
    protected Rectangle2D.Double hitbox;

    Point getPosition() {
        return position;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }
}
