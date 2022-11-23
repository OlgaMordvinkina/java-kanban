package manager.history;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final HistoryLinkedList<Task> historyStore = new HistoryLinkedList<>();

    @Override
    public void add(Task task) {
        historyStore.remove(historyStore, task);
        historyStore.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyStore.getTasks();
    }

    @Override
    public void remove(int id) {
        historyStore.removeById(historyStore, id);
    }
}

class HistoryLinkedList<T extends Task> {

    private final Map<Integer, Node<T>> idEndNodes = new HashMap<>();

    public Node<T> head;
    public Node<T> tail;
    private int size = 0;

    public void linkLast(T element) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            newNode.getPrev().setNext(newNode);
        }

        idEndNodes.put(element.getId(), newNode);
        size++;
    }

    public List<T> getTasks() {
        if (idEndNodes.isEmpty()) {
            return null;
        }
        List<T> allTasks = new ArrayList<>();
        if (head != null) {
            allTasks.add(head.getData());
            Node<T> next = head.getNext();
            while (next != null) {
                allTasks.add(next.getData());
                next = next.getNext();
            }
        }
        return allTasks;
    }

    public void removeNode(Node<T> node) {
        if (node != null) {
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            } else {
                head = node.getNext();
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            } else {
                tail = node.getPrev();
            }

            size--;
        }
    }

    public void remove(HistoryLinkedList<Task> historyStore, Task task) {
        Node<Task> similarNode = historyStore.getNode(task.getId());
        if (similarNode != null) {
            historyStore.removeNode(similarNode);
        }
    }

    public void removeById(HistoryLinkedList<Task> historyStore, int id) {
        Node<Task> node = historyStore.getNode(id);
        historyStore.removeNode(node);
    }

    public Node<T> getNode(int id) {
        return idEndNodes.get(id);
    }
}