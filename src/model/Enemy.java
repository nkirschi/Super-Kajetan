package model;

public abstract class Enemy extends Entity {
    protected Behavior behavior;
    protected int walkCount;

    public Enemy(double x, double y, Behavior behavior, Direction viewingDirection) {
        this.x = x;
        this.y = y;
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
