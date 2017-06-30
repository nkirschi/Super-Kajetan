package model;

import util.ImageUtil;
import util.Point;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class Crate extends Obstacle {
    private final double CRATE_WIDTH = 100;
    private final double CRATE_HEIGHT = 100;
    protected Rectangle2D.Double hitbox;

    public Crate(Point position) {
        this.position = position;
        hitbox = new Rectangle2D.Double(position.getX() - CRATE_WIDTH / 2, position.getY() - CRATE_HEIGHT, CRATE_WIDTH, CRATE_HEIGHT);
    }

    @Override
    public String getImagePath() {
        return "";
    }
}
