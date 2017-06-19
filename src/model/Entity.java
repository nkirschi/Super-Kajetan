package model;

import util.Point;
import physics.Collidable;

import java.awt.*;

public abstract class Entity implements Collidable {
    protected Point position;
    protected Rectangle hitbox;

    public Rectangle getHitbox() {
        return hitbox;
    }
}
