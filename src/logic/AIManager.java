package logic;

import model.*;
import util.Constants;

import static logic.Behavior.ATTACK;

/**
 * Created by Max on 11.07.2017.
 */
public class AIManager {
    CollisionHandler collisionHandler;
    private int patrolCount;

    public AIManager(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
        patrolCount = 0;
    }

    public void handleAI(Level level, Player player) {
        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isDead()) {
                level.getEnemies().remove(enemy);
                continue;
            }

            switch (enemy.getBehavior()) {
                case GUARD:
                    switch (enemy.getViewingDirection()) {
                        case LEFT:
                            if (player.getX() < enemy.getX()) {
                                if (distance(player, enemy) < enemy.getViewingRange()) {
                                    if (Math.abs(player.getY() - enemy.getY()) < (enemy.getHitbox().getHeight() / 2) + (player.getHitbox().getHeight() / 2)) { /*Betrag*/
                                        if (nearestViewblocker(enemy, level).getX() < player.getX()) {
                                            enemy.setBehavior(ATTACK);
                                        }
                                    }
                                }
                            }
                            break;
                        case RIGHT:
                            if (player.getX() > enemy.getX()) {
                                if (distance(player, enemy) < enemy.getViewingRange()) {
                                    if (Math.abs(player.getY() - enemy.getY()) < (enemy.getHitbox().getHeight() / 2) + (player.getHitbox().getHeight() / 2)) { /*Betrag*/
                                        if (nearestViewblocker(enemy, level).getX() > player.getX()) {
                                            enemy.setBehavior(ATTACK);
                                        }
                                    }
                                }
                            }
                            break;
                    }
                    if(Math.abs(enemy.getY()-player.getY()) <= enemy.getHitbox().getHeight()/2 + player.getHitbox().getHeight()/2){
                        if(Math.abs(enemy.getX()-player.getX()) <= enemy.getHitbox().getWidth()/2 + player.getHitbox().getWidth()/2){
                            enemy.setBehavior(ATTACK);
                        }
                    }
                    break;
                case ATTACK:
                    //System.out.println("Ich bin AGRESSIV");
                    if (distance(player, enemy) < enemy.getAttackRange()) {
                        if (player.getX() < enemy.getX()) {
                            enemy.setViewingDirection(Direction.LEFT);
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                        }
                        attack(enemy, player);
                    }
                    if (player.getX() < enemy.getX()) {
                        enemy.setViewingDirection(Direction.LEFT);
                        moveLeft(enemy);
                    } else {
                        enemy.setViewingDirection(Direction.RIGHT);
                        moveRight(enemy);
                    }
                    break;
                case IDLE:
                    //System.out.println("Ich warte");
                    if (distance(player, enemy) < enemy.getAttackRange()) {
                        if (player.getX() < enemy.getX()) {
                            enemy.setViewingDirection(Direction.LEFT);
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                        }
                        attack(enemy, player);
                    }
                    break;
                case PATROL:
                    if (patrolCount > 200) {
                        if (enemy.getViewingDirection().equals(Direction.LEFT))
                            enemy.setViewingDirection(Direction.RIGHT);
                        else
                            enemy.setViewingDirection(Direction.LEFT);
                        patrolCount = 0;
                    }
                    if (distance(player, enemy) < enemy.getAttackRange()) {
                        //System.out.println("Spieler in Reichweite");
                        if (player.getX() < enemy.getX()) {
                            enemy.setViewingDirection(Direction.LEFT);
                            moveLeft(enemy);
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                            moveRight(enemy);
                        }
                        patrolCount = 0;
                        attack(enemy, player);
                    }
                    switch (enemy.getViewingDirection()) {
                        case LEFT:
                            //System.out.println("Ich marschiere links");
                            moveLeft(enemy);
                            break;
                        case RIGHT:
                            //System.out.println("Ich marschiere rechts");
                            moveRight(enemy);
                            break;
                    }
                    patrolCount++;
                    collisionHandler.forEnemy(enemy);
                    enemy.move();
                    break;
                case ELOPE:
                    //System.out.println("Ich hab Angst");
                    if (player.getX() < enemy.getX()) {
                        enemy.setViewingDirection(Direction.RIGHT);
                        moveRight(enemy);
                    } else {
                        enemy.setViewingDirection(Direction.LEFT);
                        moveLeft(enemy);
                    }
                    break;
            }
            //collisionHandler.forEnemy(enemy);
            //enemy.move();
        }
    }

    private double distance(Entity entity1, Entity entity2) {
        return Math.sqrt((entity1.getX() - entity2.getX()) * (entity1.getX() - entity2.getX()) + (entity1.getY() - entity2.getY()) * (entity1.getY() - entity2.getY()));
    }

    private void moveLeft(Enemy enemy) {
        enemy.setVelocityX(-Constants.PLAYER_WALK_VELOCITY);
    }

    private void moveRight(Enemy enemy) {
        enemy.setVelocityX(Constants.PLAYER_WALK_VELOCITY);
    }

    private void attack(Enemy enemy, Player player) {
        if (System.nanoTime() - enemy.getLastAttackTime() > enemy.getMinTimeBetweenAttack()) {
            System.out.println("Hey " + System.nanoTime() / 1000000000);
            player.suffer(enemy.getStrength());
            enemy.setLastAttackTime(System.nanoTime());
        }
    }

    private Obstacle nearestViewblocker(Enemy enemy, Level level) {
        Obstacle blocker = new Crate(-99999, -99999);
        switch (enemy.getViewingDirection()) {
            case LEFT:
                blocker = new Crate(0, -99999);
                break;
            case RIGHT:
                blocker = new Crate(level.getLength(), -99999);
                break;
        }
        for (Obstacle obstacle : level.getObstacles()) {
            if (Math.abs(obstacle.getY() - enemy.getY()) < (enemy.getHitbox().getHeight() / 2) + (obstacle.getHitbox().getHeight() / 2)) { /*Betrag*/
                switch (enemy.getViewingDirection()) {
                    case LEFT:
                        if (obstacle.getX() > blocker.getX()) {
                            if (obstacle.getX() < enemy.getX()) {
                                blocker = obstacle;
                            }
                        }
                        break;
                    case RIGHT:
                        if (obstacle.getX() < blocker.getX()) {
                            if (obstacle.getX() > enemy.getX()) {
                                blocker = obstacle;
                            }
                        }
                        break;
                }
            }
        }
        return blocker;
    }
}
