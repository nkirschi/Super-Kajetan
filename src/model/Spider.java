package model;

public class Spider extends Enemy {
    public Spider(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);

    }

    public String getImagePath() {
        return "";
    }
}
