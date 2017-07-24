package logic;

import gui.KeyHandler;
import model.Cross;
import model.Enemy;
import model.Level;
import model.Player;
import util.List;
import util.SoundUtil;

public class CollisionHandler {
    private final Player player;
    private final Level level;
    private final KeyHandler keyHandler;
    private boolean strikeHeld;

    public CollisionHandler(Player player, Level level, KeyHandler keyHandler) {
        this.player = player;
        this.level = level;
        this.keyHandler = keyHandler;
    }

    public void forPlayer() {
        List<Collidable> collidables = List.concat(List.concat(level.getGrounds(), level.getObstacles()), level.getEnemies());

        Player dummy = new Player(player);
        dummy.setVelocityY(0);
        dummy.move();
        for (Collidable collidable : collidables) {
            if (dummy.collidesWith(collidable)) {
                if (collidable instanceof Cross) {
                    player.addScore(((Cross) collidable).getWorthiness());
                    ((Cross) collidable).suffer(((Cross) collidable).getMaxHealth());
                    collidables.remove(collidable);
                    break;
                }
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
                if (collidable instanceof Cross) {
                    player.addScore(((Cross) collidable).getWorthiness());
                    ((Cross) collidable).suffer(((Cross) collidable).getMaxHealth());
                    break;
                }
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

        if (!player.isExhausted()) {
            for (Enemy enemy : level.getEnemies()) {
                if (keyHandler.strike && !strikeHeld && dummy.getSword().intersects(enemy.getHitbox())) {
                    enemy.suffer(player.getStrength());
                    if (enemy.isDead())
                        player.addScore(enemy.getWorthiness());
                    else if (enemy.getBehavior().equals(Behavior.GUARD))
                        enemy.setBehavior(Behavior.ATTACK);
                }
            }

            if (keyHandler.strike) {
                if (!strikeHeld) {
                    new Thread(() -> SoundUtil.playEffect("sword_attack")).start();
                    player.addStamina(-20);
                    strikeHeld = true;
                }
            } else
                strikeHeld = false;
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
                if (!collidable.equals(player) && !flyable(level, collidable))
                    if (collidable.getHitbox().getY() - enemy.getY() + collidable.getHitbox().getHeight() < 200) {
                        enemy.setVelocityY((collidable.getHitbox().getY() - enemy.getY()) / 10);
                        enemy.setY(enemy.getY() - 50);
                    }
                    /*if(collidable.getHitbox().getY() - enemy.getY() + collidable.getHitbox().getHeight() < 200){
                        enemy.setY(enemy.getY()-50); //gettomod
                    }*/
                if (enemy.getVelocityX() > 0) {
                    enemy.setX(collidable.getHitbox().getX() - enemy.getHitbox().getWidth() / 2);
                    //enemy.setX(enemy.getX()-5);
                } else if (enemy.getVelocityX() < 0) {
                    enemy.setX(collidable.getHitbox().getX() + collidable.getHitbox().getWidth() +
                            enemy.getHitbox().getWidth() / 2);
                    //enemy.setX(enemy.getX()+5);
                }
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

    public boolean flyable(Level level, Collidable collidable){ //fixt Bug, bei dem zwei Enemys hochflogen, indem sie aneinander laufen
        for (Enemy e : level.getEnemies()) {
            if ((collidable.equals(e) && e.getVelocityY()!=0))
                return true;
        }
        return false;
    }
}
