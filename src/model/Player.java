package model;

import util.Constants;

import java.awt.geom.Rectangle2D;

public class Player extends Entity {
    private final double PLAYER_WIDTH = 95;
    private final double PLAYER_HEIGHT = 169;
    private int walkCount;
    private int stamina;
    private boolean exhausted;

    private boolean onGround;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        velocityX = 0;
        velocityY = 0;
        onGround = true;
        stamina = 1000;
        viewingDirection = Direction.RIGHT;
        hitbox = new Rectangle2D.Double(x - PLAYER_WIDTH / 2, y - PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    /**
     * Copy-Konstruktor für Dummy-Objekte zum Kollisionscheck
     *
     * @param player Der zu kopierende Spieler
     */
    public Player(Player player) {
        x = player.getX();
        y = player.getY();
        velocityX = player.getVelocityX();
        velocityY = player.getVelocityY();
        onGround = player.isOnGround();
        health = player.getHealth();
        stamina = player.getStamina();
        viewingDirection = player.getViewingDirection();
        hitbox = new Rectangle2D.Double(player.getHitbox().getX(), player.getHitbox().getY(),
                player.getHitbox().getWidth(), player.getHitbox().getHeight());
        walking = player.isWalking();
        running = player.isRunning();
        jumping = player.isJumping();
        crouching = player.isCrouching();
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

    public void addStamina(int stamina) {
        this.stamina += stamina;
        if (this.stamina < 0)
            this.stamina = 0;
        else if (this.stamina > 1000)
            this.stamina = 1000;
    }

    public void setStamina(int stamina) {
        if (stamina < 0)
            this.stamina = 0;
        else if (stamina > 1000)
            this.stamina = 1000;
        else
            this.stamina = stamina;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void addVelocityX(double velocityX) {
        this.velocityX += velocityX;
    }

    public void multiplyVelocityX(double factor) {
        velocityX *= factor;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void addVelocityY(double velocityY) {
        this.velocityY += velocityY;
    }

    public void mulitplyVelocityY(double factor) {
        velocityY *= factor;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isExhausted() {
        return exhausted;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    @Override
    public String toString() {
        return "Player at " + super.toString();
    }
}
