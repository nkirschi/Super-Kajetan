package model;

import util.list.List;

public class Level {
    private String backgroundFilePath;
    private List<Obstacle> obstacles;
    private List<Entity> entities;
    private double length; // Länge des Levels in px

    public Level(String backgroundFilePath, List<Obstacle> obstacles, List<Entity> entities, double length) {
        this.backgroundFilePath = backgroundFilePath;
        this.obstacles = obstacles;
        this.entities = entities;
        this.length = length;
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
        return entities.get(0); // Player ist in Entity-List immer an Index 0
    }
}
