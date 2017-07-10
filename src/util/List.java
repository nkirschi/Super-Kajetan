package util;

import java.awt.*;
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
     * @param content Hinzuzufügendes Inhaltsobjekt
     */
    public void add(C content) {
        first = first.add(content);
    }

    /**
     * Anhängen einer anderen Liste am Ende
     *
     * @param list Anzuhängende Liste
     */
    public void append(List<C> list) {
        first = first.append(list.first);
    }

    /**
     * Rekursives Entfernen eines Objektes aus der Liste
     *
     * @param content Zu entfernendes Inhaltsobjekt
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
     * Abprüfen der Existenz eines bestimmten Objekts in der Liste
     *
     * @param content Auf Enthalten zu prüfendes Inhzaltsobjekt
     * @return Ergebnis der Überprüfung als Wahrheitswert
     */
    public boolean contains(C content) {
        for (C c : this) {
            if (c.equals(content))
                return true;
        }
        return false;
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
     * Implementierte Methode der Iterable-Schnittstelle
     *
     * @return Iterator-Objekt für die erweiterte for-Schleife
     */
    @Override
    public Iterator<C> iterator() {
        return new Iterator<C>() {
            ListElement<C> current = null;

            @Override
            public boolean hasNext() {
                if (current == null)
                    return first instanceof ListNode;
                return current.getNext() instanceof ListNode;
            }

            @Override
            public C next() {
                if (current == null) {
                    current = first;
                    return current.getContent();
                }
                current = current.getNext();
                return current.getContent();
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
        first.toArray(contents, 0);
        return contents;
    }
}

/**
 * Abstraktes Listenelement ("Component")
 */
abstract class ListElement<C> {
    abstract ListElement<C> add(C content);

    abstract ListElement<C> append(ListElement<C> element);

    abstract ListElement<C> remove(C content);

    abstract C get(int i);

    abstract int size();

    abstract C getContent();

    abstract ListElement<C> getNext();

    abstract void toArray(Object[] contents, int i);
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

    @Override
    ListElement<C> add(C content) {
        next = next.add(content);
        return this;
    }

    @Override
    ListElement<C> append(ListElement<C> element) {
        next = next.append(element);
        return this;
    }

    @Override
    ListElement<C> remove(C content) {
        if (this.content.equals(content))
            return next;
        next = next.remove(content);
        return this;
    }

    @Override
    C get(int i) {
        if (i == 0)
            return content;
        return next.get(i - 1);
    }

    @Override
    int size() {
        return next.size() + 1;
    }

    @Override
    C getContent() {
        return content;
    }

    @Override
    ListElement<C> getNext() {
        return next;
    }

    @Override
    void toArray(Object[] contents, int i) {
        contents[i] = content;
        next.toArray(contents, i + 1);
    }
}

/**
 * Konkreter Listenabschluss ("Leaf")
 */
class ListTail<C> extends ListElement<C> {
    @Override
    ListElement<C> add(C content) {
        return new ListNode<>(content, this);
    }

    @Override
    ListElement<C> append(ListElement<C> element) {
        return element;
    }

    @Override
    ListElement<C> remove(C content) {
        return this;
    }

    @Override
    C get(int i) {
        throw new NoSuchElementException("No content at [" + i + "] in list!");
    }

    @Override
    int size() {
        return 0;
    }

    @Override
    C getContent() {
        return null;
    }

    @Override
    ListElement<C> getNext() {
        return this;
    }

    @Override
    void toArray(Object[] contents, int i) {

    }
}
