package physics;

import java.awt.geom.Rectangle2D;

public interface Collidable {
    default boolean collidesWith(Collidable object) {
        return getHitbox().intersects(object.getHitbox());
    }

    Rectangle2D.Double getHitbox();
}