package manager.history;

import tasks.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager  implements HistoryManager {
    protected static LinkedList<Task> historyStore = new LinkedList<>();

    @Override
    public void add(Task task) {
        historyStore.add(task);
        checkListSize();
    }

    @Override
    public LinkedList<Task> getHistory() {
        return historyStore;
    }

    private void checkListSize() {
        if (historyStore.size() > 10) {
            historyStore.removeFirst();
        }
    }
}
