package model;

import util.Point;

import java.awt.geom.Rectangle2D;

/**
 * Modellklasse für Bodenelemente
 * Denkbar wären z.B. Grasboden, Steinboden, Wasserboden (Tod bei Berührung)
 */
public class Ground implements Collidable {
    private Point position;
    private Rectangle2D.Double hitbox;

    public Ground(Point position, double width, double height) {
        hitbox = new Rectangle2D.Double(position.getX() - width / 2, position.getY() - height, width, height);
    }

    public String getImagePath() {
        return "";
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }
}