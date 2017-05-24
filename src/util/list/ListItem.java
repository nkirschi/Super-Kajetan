package util.list;

/**
 * Abstraktes Listenelement
 */
abstract class ListItem<C> {
    abstract ListItem<C> add(C content);

    abstract ListItem<C> remove(C content);

    abstract C get(int i);

    abstract int size();

    abstract Object[] toArray(Object[] contents, int i);
}
