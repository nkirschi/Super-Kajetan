package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Hilfsklasse für die einfachere Handhabung von Bildern ;)
 */
public class ImageUtil {
    // Der Bilder-Cache; Wenn man hin- und hernavigiert, soll doch nicht jedes mal dasselbe Bild neu geladen werden ;)
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();

    /**
     * Der Konstruktor ist hier privat, da von dieser Klasse nie ein Objekt existieren soll.
     * Somit sind auch sämtliche Methoden hier drin statisch!
     */
    private ImageUtil() {
    }

    /**
     * Methode für das Laden eines Bildes
     *
     * @param path Der Pfad der Bilddatei, ausgehend vom src root folder
     * @return Das Bild als Objekt der Klasse BufferedImage
     * @throws IOException falls es Probleme mit dem angegebenen Pfad gibt
     */
    public static BufferedImage getInternalImage(String path) throws IOException {
        if (!imageCache.containsKey(path)) {
            InputStream stream = ClassLoader.getSystemResourceAsStream(path);
            if (stream == null)
                throw new IOException("Datei nicht gefunden"); // wichtig für die Nachvollziehbarkeit von Fehlern
            BufferedImage image = ImageIO.read(stream);
            imageCache.put(path, image);
        }
        return imageCache.get(path);
    }

    /**
     * Abstrahierte Methode für die Rückgabe eines Bildes als ImageIcon
     *
     * @param path Der Pfad der Bilddatei, ausgehend vom src root folder
     * @return Das Ergebnis von getInternalImage als ImageIcon
     * @throws IOException von getInternalImage durchgeschoben
     */
    public static ImageIcon getInternalIcon(String path) throws IOException {
        return new ImageIcon(getInternalImage(path));
    }

    /**
     * Erweiterung von getInternalIcon mit parametisierter Bildgröße
     *
     * @param path   Der Pfad der Bilddatei, ausgehend vom src root folder
     * @param width  Die neue Breite des Icons
     * @param height Die neue Höhe des Icons
     * @return Das Ergebnis von getInternalIcon mit den neuen Maßen
     * @throws IOException von getInternalIcon durchgeschoben
     */
    public static ImageIcon getInternalIcon(String path, int width, int height) throws IOException {
        return new ImageIcon(getInternalIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * Methode für das Laden eines externen Bildes
     *
     * @param path Der Pfad der Bilddatei im allgemeinen Dateisystem
     * @return Das Bild als Objekt der Klasse BufferedImage
     * @throws IOException falls es Probleme mit dem angegebenen Pfad gibt
     */
    public static BufferedImage getExternalImage(String path) throws IOException {
        if (!imageCache.containsKey(path)) {
            InputStream stream = new FileInputStream(path);
            BufferedImage image = ImageIO.read(stream);
            imageCache.put(path, image);
        }
        return imageCache.get(path);
    }

    /**
     * Abstrahierte Methode für die Rückgabe eines externen Bildes als ImageIcon
     *
     * @param path Der Pfad der Bilddatei im allgemeinen Dateisystem
     * @return Das Ergebnis von getExternalImage als ImageIcon
     * @throws IOException von getExternalImage durchgeschoben
     */
    public static ImageIcon getExternalIcon(String path) throws IOException {
        return new ImageIcon(getExternalImage(path));
    }

    /**
     * Erweiterung von getExternalIcon mit parametisierter Bildgröße
     *
     * @param path   Der Pfad der Bilddatei im allgemeinen Dateisystem
     * @param width  Die neue Breite des Icons
     * @param height Die neue Höhe des Icons
     * @return Das Ergebnis von getExternalIcon mit den neuen Maßen
     * @throws IOException von getExternalIcon durchgeschoben
     */
    public static ImageIcon getExternalIcon(String path, int width, int height) throws IOException {
        return new ImageIcon(getExternalIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
