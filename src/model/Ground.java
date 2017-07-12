package model;

import logic.Collidable;
import util.ImageUtil;
import util.Logger;

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
        if (image == null) {
            String path = "images/grounds/";
            switch (type) {
                case SOIL:
                    path += "soil.png";
                    break;
                case ROCK:
                    path += "rock.png";
                    break;
                case GRASS:
                    path += "soil.png";
                    break;
            }

            BufferedImage texture = null;
            try {
                texture = ImageUtil.getImage(path);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log("Textur konnte nicht geladen werden", Logger.ERROR);
            }
            TexturePaint paint = new TexturePaint(texture, new Rectangle(0, 0, 64, 64));

            BufferedImage result = new BufferedImage((int) hitbox.getWidth(), (int) hitbox.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D h = result.createGraphics();
            h.setPaint(paint);
            h.fillRect(0, 0, result.getWidth(), result.getHeight());

            if (type == Type.GRASS) {
                BufferedImage texture2 = null;
                try {
                    texture2 = ImageUtil.getImage("images/grounds/grass.png");
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.log("Textur konnte nicht geladen werden", Logger.ERROR);
                }
                TexturePaint paint2 = new TexturePaint(texture2, new Rectangle(0, 0, 64, 64));
                h.setPaint(paint2);
                h.fillRect(0, 0, result.getWidth(), 64);
            }
            image = result;
        }
        return image;
    }

    @Override
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }
}