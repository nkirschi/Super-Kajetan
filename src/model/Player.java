package model;

import util.Point;

public class Player extends Entity {

    public Point position;

    public Player(Point position) {
        // Player spawnt auf Position (0,0)
        position = new Point(0, 0);
    }

    /**
     * gibt Position des Spielers aus
     */
    public Point getPosition() {
        return position;
    }

    public void move(double x, double y) {
        position.move(x, y);
    }
}
