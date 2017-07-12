package model;

public class Spider extends Enemy {
    private final double SPIDER_WIDTH = 95;
    private final double SPIDER_HEIGHT = 50;

    public Spider(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        viewingRange = 100;
        attackRange = 10;
    }

    public String getImagePath() {
        return "";
    }

    @Override
    public String toString() {
        return "Spider at " + super.toString();
    }
}
