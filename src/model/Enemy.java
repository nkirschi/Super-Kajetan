package model;

import util.Point;

import java.awt.geom.Rectangle2D;

public abstract class Enemy extends Entity {
    protected Behavior behavior;
    protected int walkCount;

    public Enemy(Point position, Behavior behavior, Direction viewingDirection) {
        this.position = position;
        this.behavior = behavior;
        this.viewingDirection = viewingDirection;
        walkCount = 0;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }
}
