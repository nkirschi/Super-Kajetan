package util.list;

import util.Point;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Einfach verkettete Liste
 */
public class List<C> {
    private ListItem<C> first;

    public List() {
        first = new ListTail<>();
    }

    public void add(C content) {
        first = first.add(content);
    }

    public void remove(C content) {
        first = first.remove(content);
    }

    public void clear() {
        first = new ListTail<>();
    }

    public int size() {
        return first.size();
    }

    public C get(int i) {
        return first.get(i);
    }

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
        System.out.println(list.get(0));
    }
}
