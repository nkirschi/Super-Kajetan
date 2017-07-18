package model;

import logic.Behavior;

import java.awt.geom.Rectangle2D;

public class Knight extends Enemy {
    private final double KNIGHT_WIDTH = 90;
    private final double KNIGHT_HEIGHT = 160;

    public Knight(double x, double y, Behavior behavior, Direction viewingDirection) {
        super(x, y, behavior, viewingDirection);
        hitbox = new Rectangle2D.Double(x - KNIGHT_WIDTH / 2, y - KNIGHT_HEIGHT, KNIGHT_WIDTH, KNIGHT_HEIGHT);
        viewingRange = 1000;
        attackRange = 200;
        health = 500;
        strength = 200;
        worthiness = 10;
        sword = new Rectangle2D.Double(x, y - hitbox.getHeight() - 10, SWORD_WIDTH, SWORD_HEIGHT);
    }

    @Override
    public void move() {
        super.move();
        sword.setRect(viewingDirection.equals(Direction.RIGHT) ? x : x - sword.getWidth(), y - hitbox.getHeight() - 10, sword.getWidth(), sword.getHeight());
    }

    @Override
    public int getMaxHealth() {
        return 500;
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
                return "images/enemies/enemy_knight_walk_crouch_1.png";
            else {
                if (walkCount >= 50)
                    walkCount = 0;
                return "images/enemies/enemy_knight_walk_crouch_2.png";
            }
        }
        if (jumping)
            return "images/enemies/enemy_knight_jump.png";
        if (walking) {
            if (walkCount < 25)
                return "images/enemies/enemy_knight_walk_1.png";
            else {
                if (walkCount >= 50)
                    walkCount = 0;
                return "images/enemies/enemy_knight_walk_2.png";
            }
        }
        return "images/enemies/enemy_knight_stand.png";
    }

    @Override
    public Rectangle2D.Double getSword() {
        if (crouching) {
            return new Rectangle2D.Double(sword.x, sword.y - 15, sword.getWidth(), sword.getHeight());
        }
        return sword;
    }

    @Override
    public String toString() {
        return "Kight at " + super.toString();
    }
}
