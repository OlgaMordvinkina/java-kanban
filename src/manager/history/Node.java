package manager.history;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Node <T> {
    private T data;
    private Node<T> next;
    private Node<T> prev;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
}