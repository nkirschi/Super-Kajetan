package model;

import gui.MainFrame;
import util.ImageUtil;
import util.list.List;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level {
    private String backgroundFilePath;
    private List<Obstacle> obstacles;
    private List<Entity> entities;
    private double length; // Länge des Levels in px

    public Level(String backgroundFilePath, List<Obstacle> obstacles, List<Entity> entities) {
        this.backgroundFilePath = backgroundFilePath;
        this.obstacles = obstacles;
        this.entities = entities;
        this.length = length;
        try {
            BufferedImage image = ImageUtil.getImage(backgroundFilePath);
            length = (740.0 / (double) image.getHeight()) * (double) image.getWidth();
            System.out.println(length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBackgroundFilePath() {
        return backgroundFilePath;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public double getLength() {
        return length;
    }

    public Player getPlayer() {
        return (Player) entities.get(0); // Player ist in Entity-List immer an Index 0
    }
}
