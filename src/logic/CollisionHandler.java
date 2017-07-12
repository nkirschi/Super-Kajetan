package logic;

import model.Enemy;
import model.Level;
import model.Player;
import util.List;

public class CollisionHandler {
    private Player player;
    private Level level;

    public CollisionHandler(Player player, Level level) {
        this.player = player;
        this.level = level;
    }

    public void forPlayer() {
        List<Collidable> collidables = List.concat(List.concat(level.getGrounds(), level.getObstacles()), level.getEnemies());

        Player dummy = new Player(player);
        dummy.setVelocityY(0);
        dummy.move();
        for (Collidable collidable : collidables) {
            if (dummy.collidesWith(collidable)) {
                if (player.getVelocityX() > 0) {
                    player.setX(collidable.getHitbox().getX() - player.getHitbox().getWidth() / 2);
                } else if (player.getVelocityX() < 0) {
                    player.setX(collidable.getHitbox().getX() + collidable.getHitbox().getWidth() +
                            player.getHitbox().getWidth() / 2);
                }
                player.setVelocityX(0);
                player.setWalking(false);
                break;
            }
        }

        dummy = new Player(player);
        dummy.setVelocityX(0);
        dummy.move();
        for (Collidable collidable : collidables) {
            if (dummy.collidesWith(collidable)) {
                if (player.getVelocityY() > 0) {
                    player.setY(collidable.getHitbox().getY());
                    player.setVelocityY(0);
                    player.setOnGround(true);
                    player.setJumping(false);
                } else if (player.getVelocityY() < 0) {
                    player.setY(collidable.getHitbox().getY() + collidable.getHitbox().getHeight() +
                            player.getHitbox().getHeight());
                    player.setVelocityY(0);
                }
                break;
            }
        }
    }

    public void forEnemy(Enemy enemy) {
        List<Collidable> collidables = List.concat(List.concat(level.getGrounds(), level.getObstacles()), level.getEnemies());
        collidables.add(player);

        Enemy dummy = new Enemy(enemy);
        dummy.setVelocityY(0);
        dummy.move();

        for (Collidable collidable : collidables) {
            if (collidable.equals(enemy))
                continue;
            if (dummy.collidesWith(collidable)) {
                if (enemy.getVelocityX() > 0) {
                    enemy.setX(collidable.getHitbox().getX() - enemy.getHitbox().getWidth() / 2);
                } else if (enemy.getVelocityX() < 0) {
                    enemy.setX(collidable.getHitbox().getX() + collidable.getHitbox().getWidth() +
                            enemy.getHitbox().getWidth() / 2);
                }
                if (!collidable.equals(player))
                    enemy.setVelocityY((collidable.getHitbox().getY() - enemy.getY()));
                System.out.println(collidable.getHitbox().getY() - enemy.getY());
                enemy.setVelocityX(0);
                enemy.setWalking(false);
                break;
            }
        }

        dummy = new Enemy(enemy);
        dummy.setVelocityX(0);
        dummy.move();
        for (Collidable collidable : collidables) {
            if (collidable.equals(enemy))
                continue;
            if (dummy.collidesWith(collidable)) {
                if (enemy.getVelocityY() > 0) {
                    enemy.setY(collidable.getHitbox().getY());
                    enemy.setVelocityY(0);
                    enemy.setOnGround(true);
                    enemy.setJumping(false);
                } else if (enemy.getVelocityY() < 0) {
                    enemy.setY(collidable.getHitbox().getY() + collidable.getHitbox().getHeight() +
                            enemy.getHitbox().getHeight());
                    enemy.setVelocityY(0);
                }
                break;
            }
        }
    }
}
