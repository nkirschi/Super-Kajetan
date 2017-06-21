package model;

import util.Point;

public class Spider extends Enemy {
    public Spider(Point position) {
        position = new Point();
    }

    public Point getPosition() {
        return position;
    }

    public void move(double x, double y) {
        position.move(x, y);
    }
    
    public void loadImage() {
        try {
            ImageUtil.getImage("images");   
        }   catch (IOExecption e) {
            e.ptrintStackTrace();   
        }
    }
}
