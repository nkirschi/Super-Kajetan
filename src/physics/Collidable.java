package physics;

import java.awt.*;

public interface Collidable {
    default boolean collidesWith(Collidable object) {
        return getHitbox().intersects(object.getHitbox());
    }

    Rectangle getHitbox();
}
