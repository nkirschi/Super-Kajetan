package util.list;

import java.util.NoSuchElementException;

/**
 * Konkreter Listenabschluss ("Leaf")
 */
class ListTail<C> extends ListElement<C> {
    ListElement<C> add(C content) {
        ListNode<C> node = new ListNode<>();
        node.setContent(content);
        node.setNext(this);
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

    Object[] toArray(Object[] contents, int i) {
        return contents;
    }
}
