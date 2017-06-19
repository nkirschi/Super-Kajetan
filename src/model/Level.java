package model;

import util.list.List;

public class Level {
    private String backgroundFilePath;
    private List<Obstacle> obstacles;
    private List<Entity> entities;

    public Level(String backgroundFilePath, List<Obstacle> obstacles, List<Entity> entities) {
        this.backgroundFilePath = backgroundFilePath;
        this.obstacles = obstacles;
        this.entities = entities;
    }
}
