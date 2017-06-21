package model;

import util.Point;

public class Knight extends Enemy {
    public Knight (Point position){
     position = new Point();   
    }
    
    public Point getPosition(){
        return position;   
    }
    
    public void move (doublex, double y){
     position.move(x,y);   
    }
    
    public void loadImage(){
        try {
            ImageUtil.getImage("images");
        }
            catch (IOExecption e){
             e,printStackTrace();
        }
    }
}
