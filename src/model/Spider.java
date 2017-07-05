package model;

import util.Point;

import java.awt.geom.Rectangle2D;

public class Spider extends Enemy {
    public Spider(Point position, Behavior behavior, Direction viewingDirection) {
        super(position, behavior, viewingDirection);

    }

    public String getImagePath() {
        return "";
    }
}
