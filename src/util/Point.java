package util;

/**
 * Utility-Klasse für Bildpunkte auf dem Bildschirm
 */
public class Point {
    private double x, y;

    /**
     * Default-Konstruktor mit dem Ursprungspunkt
     */
    public Point() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Parametrisierter Konstruktor mit den Koordinaten
     *
     * @param x Waagrechte Koordinate des Punktes
     * @param y Senkrechte Koordinate des Punktes
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Parametrisierter Klon-Konstruktor
     *
     * @param point Zu klonender Punkt
     */
    public Point(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * Methode zum Bewegen eines Punktes um ein Delta-x und -y
     *
     * @param x X-Koordinaten-Differenz, um die der Punkt verschoben werden soll
     * @param y Y-Koordinaten-Differenz, um die der Punkt verschoben werden soll
     */
    public void move(double x, double y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Methode zur Berechnung des Abstandes zweier Punkte
     *
     * @param point Anderer Punkt
     * @return Abstand der beiden Punkte
     */
    public double distanceTo(Point point) {
        return Math.sqrt(Math.pow(point.getX() - this.x, 2) + Math.pow(point.getY() - this.y, 2));
    }

    /**
     * Vergleichsmethode für Punkte
     *
     * @param point Vergleichspunkt
     * @return Gleich oder nicht gleich, das ist hier die Frage
     */
    public boolean equals(Point point) {
        return this.x == point.getX() && this.y == point.getY();
    }

    /**
     * Getter-Methode für die x-Koordinate
     *
     * @return X-Koordinate des Punktes
     */
    public double getX() {
        return x;
    }

    /**
     * Getter-Methode für die y-Koordinate
     *
     * @return Y-Koordinate des Punktes
     */
    public double getY() {
        return y;
    }

    /**
     * Setter-Methode für die x-Koordinate
     *
     * @param x Neue x-Koordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Setter-Methode für die y-Koordinate
     *
     * @param y Neue y-Koordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Setter-Methode für das Setzen der Position über die Koordinaten
     *
     * @param x Zu setzende x-Koordinate
     * @param y Zu setzende y-Koordinate
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Setter-Methode für das Setzen der Position des Punktes auf einen anderen Punkt
     *
     * @param position Anderer Punkt, auf dessen Position dieser hier gesetzt werden soll
     */
    public void setPosition(Point position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * Stringkonvertierungsmethode zu Testzwecken
     *
     * @return String-Repräsentation des Punktes
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + x + "," + y + ")";
    }

    public static void main(String[] args) {
        Point p = new Point(0, 0);
        Point q = new Point(3, 4);
        System.out.println(p.distanceTo(q));
        p.move(-1, 2);
        System.out.println(p);
        q.setPosition(p);
        System.out.println(p.equals(q));
    }
}
