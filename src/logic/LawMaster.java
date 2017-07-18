package logic;

import gui.KeyHandler;
import model.Entity;
import model.Player;
import util.Constants;

public class LawMaster {
    public void updateStamina(Player player, KeyHandler keyHandler) {
        if (player.isWalking() && !player.isJumping() && !player.isCrouching())
            player.addStamina(-0.5);

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

        if (!keyHandler.run && !keyHandler.jump && !keyHandler.crouch)
            player.setExhausted(false);
    }

    public void applyGravitation(Entity entity) {
        entity.addVelocityY(Constants.GRAVITATIONAL_ACCELERATION);
        entity.setOnGround(false);
    }
}
