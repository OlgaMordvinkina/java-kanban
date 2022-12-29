package test;

import manager.Managers;
import manager.history.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private int id;

    @BeforeEach
    public void init() {
        id = 0;
        historyManager = Managers.getDefaultHistory();
        List<Task> tasks = historyManager.getHistory();
        if(tasks != null) {
            tasks.forEach(it -> historyManager.remove(it.getId()));
        }
    }

    @Test
    public void addTaskToHistory() {
        Task task = creationTask();
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void tasksDuplicationInHistory() {
        Task task = creationTask();
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "В истории 1 запись.");

        historyManager.add(task);

        assertEquals(1, history.size(), "В истории всё ещё 1 запись.");
    }

    @Test
    public void deleteFromHistoryStart() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        Task task3 = creationTask();

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        final List<Task> history = historyManager.getHistory();

        assertEquals(List.of(task2, task3), history, "1 задача удалена из начала");
    }

    @Test
    public void deleteFromHistoryMiddle() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        Task task3 = creationTask();

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        final List<Task> history = historyManager.getHistory();

        assertEquals(List.of(task1, task3), history, "2 задача удалена из середины");
    }

    @Test
    public void deleteFromHistoryEnd() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        Task task3 = creationTask();

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        final List<Task> history = historyManager.getHistory();

        assertEquals(List.of(task1, task2), history, "3 задача удалена из конца");
    }

    @Test
    public void removeInvalidIdFromHistory() {
        Task task = creationTask();
        historyManager.add(task);

        historyManager.remove(10);

        assertEquals(List.of(task), historyManager.getHistory(), "История не изменилась");
    }

    private Task creationTask() {
        return new Task(id++, "Test title Task" + id, "test description Task" + id, TaskStatus.NEW, Instant.now(), 0);
    }
}
