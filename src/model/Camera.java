package model;

import java.awt.geom.Rectangle2D;

public class Camera extends Rectangle2D.Double {
    private Player player;

    public Camera(Player player, double x, double y, double width, double height) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move() {
        this.x += player.getVelocityX();
    }
}