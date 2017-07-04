package model;

import util.Constants;
import util.Point;

import java.awt.geom.Rectangle2D;

public class Player extends Entity {
    private final double PLAYER_WIDTH = 95;
    private final double PLAYER_HEIGHT = 169;
    private int walkCount;
    private int stamina;

    public Player(Point position) {
        this.position = position;
        stamina = 1000;
        viewingDirection = Direction.RIGHT;
        hitbox = new Rectangle2D.Double(position.getX() - PLAYER_WIDTH / 2, position.getY() - PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    @Override
    public void setWalking(boolean walking) {
        if (walking)
            walkCount++;
        super.setWalking(walking);
    }

    @Override
    public void setRunning(boolean running) {
        if (running)
            walkCount++;
        super.setRunning(running);
    }

    @Override
    public void setCrouching(boolean crouching) {
        double crouchingDelta = 53;
        if (crouching && !this.crouching)
            hitbox.setRect(hitbox.x, hitbox.y + crouchingDelta, hitbox.width, hitbox.height - crouchingDelta);
        else if (!crouching && this.crouching)
            hitbox.setRect(hitbox.x, hitbox.y - crouchingDelta, hitbox.width, hitbox.height + crouchingDelta);

        super.setCrouching(crouching);
    }

    public String getImagePath() {
        if (crouching) {
            if (walkCount < 25)
                return Constants.PLAYER_CROUCH_1_IMAGE_PATH;
            else {
                if (walkCount >= 50)
                    walkCount = 0;
                return Constants.PLAYER_CROUCH_2_IMAGE_PATH;
            }
        }
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

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        if (stamina < 0)
            this.stamina = 0;
        else if (stamina > 1000)
            this.stamina = 1000;
        else
            this.stamina = stamina;
    }

    @Override
    public String toString() {
        return "Player at " + super.toString();
    }
}
