package test;

import manager.Managers;
import manager.history.HistoryManager;
import manager.tasks.FileBackedTasksManager;
import manager.tasks.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest {
    private HistoryManager historyManager;
    private final File testFile = new File("resources/test_data.csv");

    @BeforeEach
    public void init() throws IOException {
        historyManager = Managers.getDefaultHistory();
        manager = new FileBackedTasksManager(testFile);
        Files.createFile(testFile.toPath());
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(testFile.toPath());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }


    @Test
    public void saveEmptyTasksList() {
        TaskManager manager = Managers.getDefault();
        new FileBackedTasksManager(testFile);

        Epic epic = new Epic("Покупки", "Еда", Instant.now(), 0);
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("Хлебобулочные", "Рогалики", Instant.now(), 0);
        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);
        Subtask subtask2 = new Subtask("Мясное", "Фарш", Instant.now(), 0);
        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);

        Epic epic2 = new Epic("Покупки", "Для дома", Instant.now(), 0);
        manager.saveEpic(epic2);
        Subtask subtask3 = new Subtask("Мясное", "Фарш", Instant.now(), 0);
        subtask3.setEpicId(epic2.getId());
        manager.saveSubtask(subtask3);

        manager.deleteAllTasks();

        assertNotNull(manager.getEpicStore(), "EpicStore не пустой");
        assertNotNull(manager.getSubtasksStore(), "SubtasksStore не пустой");
        assertTrue(manager.getTaskStore().isEmpty(), "TaskStore пустой");
    }

    @Test
    public void saveEpicWithoutSubtasks() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Планы на день:", "Выбросить мусор", Instant.now(), 0);
        manager.saveTask(task);

        Epic epic = new Epic("Покупки", "Еда", Instant.now(), 0);
        manager.saveEpic(epic);

        Epic epic2 = new Epic("Покупки", "Для дома", Instant.now(), 0);
        manager.saveEpic(epic2);

        manager.deleteAllTasks();

        assertNotNull(manager.getEpicStore(), "EpicStore не пустой");
        assertTrue(manager.getSubtasksStore().isEmpty(), "SubtasksStore пустой");
        assertNotNull(manager.getTaskStore(), "TaskStore не пустой");
    }

    @Test
    public void saveEmptyHistoryList() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Планы на день:", "Выбросить мусор", Instant.now(), 0);
        manager.saveTask(task);

        Epic epic = new Epic("Покупки", "Еда", Instant.now(), 0);
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("Хлебобулочные", "Рогалики", Instant.now(), 0);
        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);
        Subtask subtask2 = new Subtask("Мясное", "Фарш", Instant.now(), 0);
        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);

        Epic epic2 = new Epic("Покупки", "Для дома", Instant.now(), 0);
        manager.saveEpic(epic2);
        Subtask subtask3 = new Subtask("Мясное", "Фарш", Instant.now(), 0);
        subtask3.setEpicId(epic2.getId());
        manager.saveSubtask(subtask3);

        manager.deleteAllTasks();

        assertNotNull(manager.getEpicStore(), "EpicStore не пустой");
        assertNotNull(manager.getSubtasksStore(), "SubtasksStore не пустой");
        assertNotNull(manager.getTaskStore(), "TaskStore не пустой");
        assertNull(historyManager.getHistory(), "История пустая");
    }
}
