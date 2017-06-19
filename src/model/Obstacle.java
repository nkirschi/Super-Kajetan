package model;

import physics.Collidable;
import util.Point;

import java.awt.*;

public abstract class Obstacle implements Collidable {
    protected Point position;
    protected Rectangle hitbox;

    public Rectangle getHitbox() {
        return hitbox;
    }
}
