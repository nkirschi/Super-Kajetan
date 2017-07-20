package logic;

import model.Entity;
import model.Player;
import util.Constants;

public class LawMaster {
    public void updateStamina(Player player) {
        if (player.isWalking() && !player.isJumping() && !player.isCrouching())
            player.addStamina(-1.5);

        if (player.isCrouching())
            player.addStamina(-1);
        else if (player.isRunning() || player.isJumping() && player.getVelocityY() < 0) {
            player.addStamina(-2);
        }

        if (!player.isRunning() && !player.isJumping()) {
            player.addStamina(2);
        }

        if (player.getStamina() < 10)
            player.setExhausted(true);


    }

    public void regenerate(Player player) {
        if (player.getStamina() >= 300 && !player.isWalking() && !player.isRunning() && !player.isJumping() && player.isCrouching())
            player.addHealth(1);
    }

    public void applyGravitation(Entity entity) {
        entity.addVelocityY(Constants.GRAVITATIONAL_ACCELERATION);
        entity.setOnGround(false);
    }
}
