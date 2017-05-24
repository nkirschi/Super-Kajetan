package util.list;

/**
 * Konkreter Listenknoten
 */
class ListNode<C> extends ListItem<C> {
    private C content;
    private ListItem<C> next;

    ListItem<C> add(C content) {
        next = next.add(content);
        return this;
    }

    ListItem<C> remove(C content) {
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

    Object[] toArray(Object[] contents, int i) {
        contents[i] = content;
        return next.toArray(contents, i + 1);
    }

    void setContent(C content) {
        this.content = content;
    }

    void setNext(ListItem<C> next) {
        this.next = next;
    }
}
