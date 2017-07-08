package model;

import java.awt.geom.Rectangle2D;

/**
 * Modellklasse für Bodenelemente
 * Denkbar wären z.B. Grasboden, Steinboden, Wasserboden (Tod bei Berührung)
 */
public class Ground implements Collidable {
    private double x, y;

    private Rectangle2D.Double hitbox;
    public Ground(double x, double width, double height) {
        this.x = x;
        y = 740;
        hitbox = new Rectangle2D.Double(x - width / 2, y - height, width, height);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getImagePath() {
        return "";
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }
}