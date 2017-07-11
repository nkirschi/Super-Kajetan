package model;

/**
 * Created by Max on 11.07.2017.
 */
public class Logic {
    public void handleAI(Level level, Player player) {
        for (Enemy enemy : level.getEnemies()) {
            switch (enemy.getBehavior()) {
                case IDLE:
                    System.out.println("Ich warte");
                    if(distance(player, enemy)<enemy.getAttackRange()){
                        //enemy.attack();
                    }
                    break;
                case PATROL:
                    System.out.println("Ich marschiere");
                    switch (enemy.getViewingDirection()){
                        case LEFT:

                        case RIGHT:
                    }
                    break;
                case ELOPE:
                    System.out.println("Ich hab Angst");
                    if(player.getX()<enemy.getX()){
                        moveRight(enemy);
                    }else{
                        moveLeft(enemy);
                    }
                    break;
            }
        }
    }

    public double distance(Entity entity1, Entity entity2){
        return Math.sqrt((entity1.getX() - entity2.getX())*(entity1.getX() - entity2.getX()) + (entity1.getY() - entity2.getY())*(entity1.getY() - entity2.getY()));
    }
    public void moveLeft(Enemy enemy){
        if(/*links Hindernis*/){
            //enemy.jump()
        }else{
            //geh links
        }
    }

    public void moveRight(Enemy enemy){
        if(/*rechts Hindernis*/){
            //enemy.jump()
        }else{
            //geh rechts
        }
    }
}
