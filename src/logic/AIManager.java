package logic;

import model.*;
import util.Constants;
import util.List;

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
        List<Enemy> dead = new List<>();

        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isDead()) {
                dead.add(enemy);
                continue;
            }

            switch (enemy.getBehavior()) {
                case GUARD:
                    switch (enemy.getViewingDirection()) {
                        case LEFT:
                            if (player.getX() < enemy.getX()) {
                                if (distance(player, enemy) < enemy.getViewingRange()) {
                                    if (player.getY() - enemy.getY() < (enemy.getHitbox().getHeight() / 2) + (player.getHitbox().getHeight() / 2)) { /*Betrag*/
                                        if (nearestViewblocker(enemy, level).getX() < player.getX()) {
                                            enemy.setBehavior(ATTACK);
                                        }
                                    }
                                }
                            }
                        case RIGHT:
                            if (player.getX() > enemy.getX()) {
                                if (distance(player, enemy) < enemy.getViewingRange()) {
                                    if (player.getY() - enemy.getY() < (enemy.getHitbox().getHeight() / 2) + (player.getHitbox().getHeight() / 2)) { /*Betrag*/
                                        if (nearestViewblocker(enemy, level).getX() > player.getX()) {
                                            enemy.setBehavior(ATTACK);
                                        }
                                    }
                                }
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
                        System.out.println("Spieler in Reichweite");
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
        }

        for (Enemy enemy : dead) {
            level.getEnemies().remove(enemy);
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
        System.out.println("Hey");
        player.suffer(enemy.getStrength());
    }

    private Obstacle nearestViewblocker(Enemy enemy, Level level) {
        Obstacle blocker = new Crate(-999999, -999999);
        for (Obstacle obstacle : level.getObstacles()) {
            if (obstacle.getY() - enemy.getY() < (enemy.getHitbox().getHeight() / 2) + (obstacle.getHitbox().getHeight() / 2)) { /*Betrag*/
                switch (enemy.getViewingDirection()) {
                    case LEFT:
                        if (obstacle.getX() > blocker.getX()) {
                            if (obstacle.getX() < enemy.getX()) {
                                blocker = obstacle;
                            }
                        }
                    case RIGHT:
                        if (obstacle.getX() < blocker.getX()) {
                            if (obstacle.getX() > enemy.getX()) {
                                blocker = obstacle;
                            }
                        }
                }
            }
        }
        return blocker;
    }
}
