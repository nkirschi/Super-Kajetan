package physics;

import model.*;
import util.Constants;

import static model.Behavior.ATTACK;

/**
 * Created by Max on 11.07.2017.
 */
public class AIManager {
    CollisionChecker collisionChecker;
    private int patrolCount;

    public AIManager(CollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
        patrolCount = 0;
    }

    public void handleAI(Level level, Player player) {
        for (Enemy enemy : level.getEnemies()) {
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
                    System.out.println("Ich bin AGRESSIV");
                    if (distance(player, enemy) < enemy.getAttackRange()) {
                        if (player.getX() < enemy.getX()) {
                            enemy.setViewingDirection(Direction.LEFT);
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                        }
                        attack(enemy);
                    } else {
                        if (player.getX() < enemy.getX()) {
                            enemy.setViewingDirection(Direction.LEFT);
                            moveLeft(enemy);
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                            moveRight(enemy);
                        }
                    }
                    break;
                case IDLE:
                    System.out.println("Ich warte");
                    if (distance(player, enemy) < enemy.getAttackRange()) {
                        if (player.getX() < enemy.getX()) {
                            enemy.setViewingDirection(Direction.LEFT);
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                        }
                        attack(enemy);
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
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                        }
                        attack(enemy);
                    } else {
                        switch (enemy.getViewingDirection()) {
                            case LEFT:
                                System.out.println("Ich marschiere links");
                                moveLeft(enemy);
                                break;
                            case RIGHT:
                                System.out.println("Ich marschiere rechts");
                                moveRight(enemy);
                                break;
                        }
                        patrolCount++;
                    }
                    break;
                case ELOPE:
                    System.out.println("Ich hab Angst");
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
    }

    public double distance(Entity entity1, Entity entity2) {
        return Math.sqrt((entity1.getX() - entity2.getX()) * (entity1.getX() - entity2.getX()) + (entity1.getY() - entity2.getY()) * (entity1.getY() - entity2.getY()));
    }

    public void moveLeft(Enemy enemy) {
        if (true/*links Hindernis*/) {
            //spring
            enemy.setVelocityX(-Constants.PLAYER_WALK_VELOCITY);
            collisionChecker.forEnemy(enemy);
            enemy.move();
        } else {
            //geh links
        }
    }

    public void moveRight(Enemy enemy) {
        if (true/*rechts Hindernis*/) {
            enemy.setVelocityX(Constants.PLAYER_WALK_VELOCITY);
            collisionChecker.forEnemy(enemy);
            enemy.move();
        } else {
            //geh rechts
        }
    }

    public void attack(Enemy enemy) {
        //attack
    }

    public Obstacle nearestViewblocker(Enemy enemy, Level level) {
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
