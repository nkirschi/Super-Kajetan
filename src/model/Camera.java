package model;

import gui.LevelView;

import java.awt.geom.Rectangle2D;

public class Camera extends Rectangle2D.Double {
    private final Player player;
    private final LevelView view;

    public Camera(Player player, LevelView view) {
        this.player = player;
        this.view = view;
        x = 0;
        y = 0;
        width = view.getWidth();
        height = view.getHeight();
    }

    public void move() {
        this.x = player.getX() - view.getWidth() / 2;
    }
}