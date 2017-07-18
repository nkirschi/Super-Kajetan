package model;

import util.Constants;

import java.awt.geom.Rectangle2D;

public class Player extends Entity {
    private final double PLAYER_WIDTH = 90;
    private final double PLAYER_HEIGHT = 160;
    private final double SWORD_WIDTH = 128;
    private final double SWORD_HEIGHT = 128;

    private Rectangle2D.Double sword;
    private int walkCount;
    private double stamina;
    private boolean exhausted;

    private int score;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        velocityX = 0;
        velocityY = 0;
        health = getMaxHealth();
        strength = 100;
        onGround = true;
        stamina = 1000;
        viewingDirection = Direction.RIGHT;
        hitbox = new Rectangle2D.Double(x - PLAYER_WIDTH / 2, y - PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
        sword = new Rectangle2D.Double(x, y - hitbox.getHeight() - 10, SWORD_WIDTH, SWORD_HEIGHT);
        score = 0;
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
        sword = player.getSword();
        walking = player.isWalking();
        running = player.isRunning();
        jumping = player.isJumping();
        crouching = player.isCrouching();
    }

    @Override
    public void move() {
        super.move();
        sword.setRect(viewingDirection.equals(Direction.RIGHT) ? x - 3 : x + 3 - sword.getWidth(), y - hitbox.getHeight() - 7, sword.getWidth(), sword.getHeight());
    }

    @Override
    public int getMaxHealth() {
        return 1000;
    }

    public Rectangle2D.Double getSword() {
        if (crouching) {
            return new Rectangle2D.Double(sword.x, sword.y - 15, sword.getWidth(), sword.getHeight());
        }
        return sword;
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
        if (crouching && !this.crouching) {
            hitbox.setRect(hitbox.x, hitbox.y + crouchingDelta, hitbox.width, hitbox.height - crouchingDelta);
        } else if (!crouching && this.crouching) {
            hitbox.setRect(hitbox.x, hitbox.y - crouchingDelta, hitbox.width, hitbox.height + crouchingDelta);
        }

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

    public String getImagePathSword() {
        return Constants.KNIGHT_WEAPON_IMAGE_PATH;
    }


    public double getStamina() {
        return stamina;
    }

    public void addStamina(double stamina) {
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

    public boolean isExhausted() {
        return exhausted;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    public int getScore() {
        if(score < 0){
            return 0;
        }else {
            return score;
        }
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void reset() {
        setVelocityX(0);
        setWalking(false);
        setRunning(false);
        setCrouching(false);
    }

    @Override
    public String toString() {
        return "Player at " + super.toString();
    }
}
