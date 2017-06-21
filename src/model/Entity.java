package model;

import util.Point;
import physics.Collidable;

import java.awt.*;

public abstract class Entity implements Collidable {
    protected Point position;
    protected Rectangle hitbox;
    protected int health;

    public Rectangle getHitbox() {
        return hitbox;
    }
    
    public default Point getPosition() {
        return position;
    }
    
    /**
    @param damage Hinzuzufügender Schaden
    @return Wahrheitswert, ob entity noch lebt.
    */
    public default boolean sufferDamage(int damage) {
        health -= damage;
        return health <= 0 ? false : true;
    }
    
    public default int getHealth() {
        return health;
    }
}
