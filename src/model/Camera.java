package model;

import java.awt.geom.Rectangle2D;

public class Camera extends Rectangle2D.Double {
    public Camera(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void scroll(double x) {
        this.x += x;
    }
}