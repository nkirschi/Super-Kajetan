package model;

import physics.Collidable;
import util.ImageUtil;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Modellklasse für Bodenelemente
 * Denkbar wären z.B. Grasboden, Steinboden, Wasserboden (Tod bei Berührung)
 */
public class Ground implements Collidable {
    private double x, y;
    private Ground.Type type;
    private Image image;

    public enum Type {
        SOIL, GRASS, ROCK
    }

    private Rectangle2D.Double hitbox;

    public Ground(double x, double width, double height, Ground.Type type) {
        this.x = x;
        y = 740;
        hitbox = new Rectangle2D.Double(x - width / 2, y - height, width, height);
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImage() {
        if (image != null)
            return image;

        String path = "images/grounds/";
        switch (type) {
            case SOIL:
                path += "soil.png";
                break;
            case GRASS:
                path += "grass.png";
                break;
            case ROCK:
                path += "rock.png";
                break;
        }
        BufferedImage texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g = texture.createGraphics();
        try {
            g.drawImage(ImageUtil.getImage(path), 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TexturePaint paint = new TexturePaint(texture, new Rectangle(0, 0, 64, 64));

        BufferedImage result = new BufferedImage((int) hitbox.getWidth(), (int) hitbox.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D h = result.createGraphics();
        h.setPaint(paint);
        h.fillRect(0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }
}