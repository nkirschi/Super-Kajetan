package model;

import gui.MainFrame;
import util.ImageUtil;
import util.list.List;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Level {
    private Player player;
    private List<Enemy> enemies;
    private List<Obstacle> obstacles;
    private List<Ground> grounds; // Liste von Bodenelementen
    private String backgroundFilePath;
    private double length; // Länge des Levels in px

    public Level(Player player, List<Enemy> enemies, List<Obstacle> obstacles, List<Ground> grounds,
                 String backgroundFilePath) {
        this.player = player;
        this.enemies = enemies;
        this.obstacles = obstacles;
        this.grounds = grounds;
        this.backgroundFilePath = backgroundFilePath;
        try {
            BufferedImage image = ImageUtil.getImage(backgroundFilePath);
            length = (740.0 / (double) image.getHeight()) * (double) image.getWidth();
            System.out.println(length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
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
}
