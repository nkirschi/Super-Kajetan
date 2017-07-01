package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Einfach verkettete Liste unter Verwendung des Composite Design Patterns
 * Zur Benutzung siehe main()-Methode weiter unten
 */
public class List<C> implements Iterable<C> {
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

    @Override
    public Iterator<C> iterator() {
        return new Iterator<C>() {
            ListElement<C> currentElement = first;

            @Override
            public boolean hasNext() {
                return currentElement.size() > 0;
            }

            @Override
            public C next() {
                C content = currentElement.getContent();
                currentElement = currentElement.getNext();
                return content;
            }
        };
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

    public static void main(String[] args) throws Exception {
        List<Point> list = new List<>();
        list.add(new Point(0, 0));
        list.add(new Point(1, 1));
        list.add(new Point(2, 2));
        System.out.println(list.get(2));
        System.out.println(list.size());
        for (Point point : list) {
            System.out.println(point);
        }
        list.remove(new Point(1, 1));
        System.out.println(list.size());
        list.clear();
        Thread.sleep(1000);
        System.out.println(list.get(0)); // Das gibt eine saftige Ausnahme
    }
}

/**
 * Abstraktes Listenelement ("Component")
 */
abstract class ListElement<C> {
    abstract ListElement<C> add(C content);

    abstract ListElement<C> remove(C content);

    abstract C get(int i);

    abstract int size();

    abstract C getContent();

    abstract ListElement<C> getNext();

    abstract Object[] toArray(Object[] contents, int i);
}

/**
 * Konkreter Listenknoten ("Composite")
 */
class ListNode<C> extends ListElement<C> {
    private C content;
    private ListElement<C> next;

    ListNode(C content, ListElement<C> next) {
        this.content = content;
        this.next = next;
    }

    ListElement<C> add(C content) {
        next = next.add(content);
        return this;
    }

    ListElement<C> remove(C content) {
        if (this.content.equals(content))
            return next;
        next = next.remove(content);
        return this;
    }

    C get(int i) {
        if (i == 0)
            return content;
        return next.get(i - 1);
    }

    int size() {
        return next.size() + 1;
    }

    C getContent() {
        return content;
    }

    ListElement<C> getNext() {
        return next;
    }

    Object[] toArray(Object[] contents, int i) {
        contents[i] = content;
        return next.toArray(contents, i + 1);
    }
}

/**
 * Konkreter Listenabschluss ("Leaf")
 */
class ListTail<C> extends ListElement<C> {
    ListElement<C> add(C content) {
        ListNode<C> node = new ListNode<>(content, this);
        return node;
    }

    ListElement<C> remove(C content) {
        return this;
    }

    C get(int i) {
        throw new NoSuchElementException("No content at [" + i + "] in list!");
    }

    int size() {
        return 0;
    }

    C getContent() {
        return null;
    }

    ListElement<C> getNext() {
        return this;
    }

    Object[] toArray(Object[] contents, int i) {
        return contents;
    }
}