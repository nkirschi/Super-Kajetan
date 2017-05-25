package util.list;

/**
 * Abstraktes Listenelement ("Component")
 */
abstract class ListElement<C> {
    abstract ListElement<C> add(C content);

    abstract ListElement<C> remove(C content);

    abstract C get(int i);

    abstract int size();

    abstract Object[] toArray(Object[] contents, int i);
}
