package util.list;

/**
 * Konkreter Listenabschluss
 */
class ListTail<C> extends ListItem<C> {
    ListItem<C> add(C content) {
        ListNode<C> node = new ListNode<>();
        node.setContent(content);
        node.setNext(this);
        return node;
    }

    ListItem<C> remove(C content) {
        return this;
    }

    C get(int i) {
        return null;
    }

    int size() {
        return 0;
    }

    Object[] toArray(Object[] contents, int i) {
        return contents;
    }
}
