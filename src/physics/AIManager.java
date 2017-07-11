package physics;

import model.*;

import static model.Behavior.ATTACK;
import static model.Behavior.ELOPE;

/**
 * Created by Max on 11.07.2017.
 */
public class AIManager {
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
                    System.out.println("Ich marschiere");
                    if (distance(player, enemy) < enemy.getAttackRange()) {
                        if (player.getX() < enemy.getX()) {
                            enemy.setViewingDirection(Direction.LEFT);
                        } else {
                            enemy.setViewingDirection(Direction.RIGHT);
                        }
                        attack(enemy);
                    } else {
                        switch (enemy.getViewingDirection()) {
                            case LEFT:
                                moveLeft(enemy);
                            case RIGHT:
                                moveRight(enemy);
                        }
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
        } else {
            //geh links
        }
    }

    public void moveRight(Enemy enemy) {
        if (false/*rechts Hindernis*/) {
            //spring
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
