package model;

import util.list.List;

public class Level {
    private String backgroundFilePath;
    private List<Obstacle> obstacles;
    private List<Entity> entities;
    private double length; // Länge des Levels in px

    public Level(String backgroundFilePath, List<Obstacle> obstacles, List<Entity> entities) {
        this.backgroundFilePath = backgroundFilePath;
        this.obstacles = obstacles;
        this.entities = entities;
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
}
