package model;

public abstract class Enemy extends Entity {
    protected Behavior behavior;

    protected double height;
    protected double width;

    protected double viewingRange;
    protected double attackRange;

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

    public double getViewingRange() {
        return viewingRange;
    }

    public double getAttackRange() {
        return attackRange;
    }

    protected int walkCount;

    public double getHeight() { return height; }

    public double getWidth() { return width; }
}
