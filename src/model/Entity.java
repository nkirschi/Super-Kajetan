package model;

import physics.Collidable;
import util.Point;

import java.awt.geom.Rectangle2D;

public abstract class Entity implements Collidable {
    protected Point position; // Der Punkt in der Mitte unten am Entity
    protected Rectangle2D.Double hitbox;
    protected int health;

    Point getPosition() {
        return position;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    /**
     * Bewegung der Entität um Koordinatendifferenzen
     *
     * @param x Delta-x, um das verschoben wird
     * @param y Delta-y, um das verschoben wird
     */
    void move(double x, double y) {
        position.move(x, y);
        hitbox.setRect(hitbox.getX() + x, hitbox.getY() + y, hitbox.getWidth(), hitbox.getHeight());
    }

    /**
     * @param damage Hinzuzufügender Schaden
     * @return Wahrheitswert, ob entity noch lebt.
     */
    boolean sufferDamage(int damage) {
        health -= damage;
        return health > 0;
    }

    int getHealth() {
        return health;
    }
}
