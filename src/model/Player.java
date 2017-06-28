package model;

import util.Constants;
import util.Point;

import java.awt.geom.Rectangle2D;

public class Player extends Entity {
    private final double PLAYER_WIDTH = 95;
    private final double PLAYER_HEIGHT = 169;
    private int walkCount;

    public Player(Point position) {
        this.position = position;
        walkCount = 0;
        viewingDirection = Direction.RIGHT;
        hitbox = new Rectangle2D.Double(position.getX() - PLAYER_WIDTH / 2, position.getY() - PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void setCrouching(boolean crouching) {
        double crouchingDelta = 50;
        if (crouching && !this.crouching)
            hitbox.setRect(hitbox.x, hitbox.y + crouchingDelta, hitbox.width, hitbox.height - crouchingDelta);
        else if (!crouching && this.crouching)
            hitbox.setRect(hitbox.x, hitbox.y - crouchingDelta, hitbox.width, hitbox.height + crouchingDelta);

        super.setCrouching(crouching);
    }

    @Override
    public void setWalking(boolean walking) {
        walkCount++;
        super.setWalking(walking);
    }

    public String getImagePath() {
        if (jumping)
            return Constants.PLAYER_JUMP_IMAGE_PATH;
        if (walking) {
            if (walkCount < 25)
                return Constants.PLAYER_WALK_1_IMAGE_PATH;
            else {
                if (walkCount >= 50)
                    walkCount = 0;
                return Constants.PLAYER_WALK_2_IMAGE_PATH;
            }
        }
        return Constants.PLAYER_STAND_IMAGE_PATH;
    }

    @Override
    public String toString() {
        return "Player at " + super.toString();
    }
}
