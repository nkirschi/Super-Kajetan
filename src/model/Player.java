package model;

import util.ImageUtil;
import util.Point;

import java.io.IOException;

public class Player extends Entity {
    public Player(Point position) {
        // Player spawnt standardmäßig auf Position (0,0)
        position = new Point();
    }

    /**
     * Getter-Methode für die aktuelle Spielerposition
     *
     * @return Position des Spielers als Point
     */
    public Point getPosition() {
        return position;
    }

    /**
     * bewegtt den Spieler
     */
    public void move(double x, double y) {
        position.move(x, y);
    }

    /**
     * lädt Bild aus images/...
     */
    public void loadImage() {
        try {
            ImageUtil.getImage("images");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
