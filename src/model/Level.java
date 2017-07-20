package model;

import util.ImageUtil;
import util.List;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level {
    private final List<Enemy> enemies;
    private final List<Obstacle> obstacles;
    private final List<Ground> grounds; // Liste von Bodenelementen
    private final String backgroundFilePath;
    private final int basescore;
    private double length; // Länge des Levels in px

    public Level(List<Enemy> enemies, List<Obstacle> obstacles, List<Ground> grounds,
                 String backgroundFilePath, int basescore) {
        this.enemies = enemies;
        this.obstacles = obstacles;
        this.grounds = grounds;
        //Damit niemand flüchten kann!
        this.grounds.add(new Ground(-300, 600, 788, Ground.Type.SOIL));

        this.backgroundFilePath = backgroundFilePath;
        this.basescore = basescore;

        try {
            BufferedImage image = ImageUtil.getImage(backgroundFilePath);
            length = (740.0 / (double) image.getHeight()) * (double) image.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Ground> getGrounds() {
        return grounds;
    }

    public double getLength() {
        return length;
    }

    public String getBackgroundFilePath() {
        return backgroundFilePath;
    }

    public int getBasescore() {
        return basescore;
    }
}
