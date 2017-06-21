package util.list;

import util.Point;

/**
 * Einfach verkettete Liste unter Verwendung des Composite Design Patterns
 * Zur Benutzung siehe main()-Methode weiter unten
 */
public class List<C> {
    private ListElement<C> first;

    /**
     * Konstruktion einer Liste mit einem Abschluss als erstem Element
     */
    public List() {
        first = new ListTail<>();
    }

    /**
     * Rekursives Hinzufügen eines Objektes in die Liste
     *
     * @param content
     */
    public void add(C content) {
        first = first.add(content);
    }

    /**
     * Rekursives Entfernen eines Objektes aus der Liste
     *
     * @param content
     */
    public void remove(C content) {
        first = first.remove(content);
    }

    /**
     * Komplettes Leeren der Liste
     */
    public void clear() {
        first = new ListTail<>();
    }

    /**
     * Ermitteln der Größe der Liste
     *
     * @return Anzahl an gespeicherten Objekten
     */
    public int size() {
        return first.size();
    }

    /**
     * Erfragen eines Objektes aus der Liste mit einem Index
     *
     * @param i Position in der Liste, beginnend mit 0
     * @return Objekt an der Stelle i
     */
    public C get(int i) {
        if (i < 0)
            throw new IllegalArgumentException("Index less than zero!");
        return first.get(i);
    }

    /**
     * Überführen der Liste in ein Feld von Objekten
     *
     * @return Array aller Listenobjekte in unveränderter Reihenfolge
     */
    public Object[] toArray() {
        Object[] contents = new Object[size()];
        return first.toArray(contents, 0);
    }

    public static void main(String[] args) {
        List<Point> list = new List<>();
        list.add(new Point(0, 0));
        list.add(new Point(1, 1));
        list.add(new Point(2, 2));
        System.out.println(list.size());
        System.out.println(list.get(2));
        list.remove(new Point(1, 1));
        System.out.println(list.size());
        list.clear();
        System.out.println(list.get(0)); // Das gibt eine saftige Ausnahme
    }
}
