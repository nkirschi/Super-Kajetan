import util.point

public class Player extends Entity {

    public point position;
  public Player (Point position) {
    /** Player spawnt auf Position (0,0)
    */
    position = (0,0);
  }
    /** gibt Position des Spielers aus
    */
    public Point givePosition(){
        return postition;
    }
    
    public void move() {
        
    }
}
