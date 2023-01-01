package test;

import manager.tasks.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {
    protected TaskManager manager;

    @Test
    public void addNewTask() {
        Task task = creationTask();
        manager.saveTask(task);

        final Task savedTask = manager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = new ArrayList<>(manager.getTaskStore());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void epicStatusIfEmptyListOfSubtasks() {
        Epic epic = creationEpic();
        manager.saveEpic(epic);

        final Epic savedEpic = manager.getEpicById(epic.getId());

        assertTrue(savedEpic.getSubtasks().isEmpty());
        assertEquals(savedEpic.getStatus(), TaskStatus.NEW);
    }

    @Test
    public void epicStatusIfSubtasksStatusIsNEW() {
        Epic epic = creationEpic();
        manager.saveEpic(epic);

        final Epic savedEpic = manager.getEpicById(epic.getId());

        Subtask subtask = creationSubtask();
        Subtask subtask2 = creationSubtask();

        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);
        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);

        assertEquals(savedEpic.getStatus(), TaskStatus.NEW);
    }

    @Test
    public void epicStatusIfSubtasksStatusIsDONE() {
        Epic epic = creationEpic();
        manager.saveEpic(epic);

        final Epic savedEpic = manager.getEpicById(epic.getId());

        Subtask subtask = creationSubtask();
        Subtask subtask2 = creationSubtask();

        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);
        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);

        subtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);
        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);

        assertEquals(savedEpic.getStatus(), TaskStatus.DONE);
    }

    @Test
    public void epicStatusIfSubtasksStatusIsNEWAndDONE() {
        Epic epic = creationEpic();
        manager.saveEpic(epic);

        final Epic savedEpic = manager.getEpicById(epic.getId());

        assertTrue(savedEpic.getSubtasks().isEmpty());

        Subtask subtask = creationSubtask();
        Subtask subtask2 = creationSubtask();

        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);
        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);

        assertEquals(savedEpic.getStatus(), TaskStatus.NEW);

        subtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask);
        assertEquals(savedEpic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void epicStatusIfSubtasksStatusIsIN_PROGRESS() {
        Epic epic = creationEpic();
        manager.saveEpic(epic);

        final Epic savedEpic = manager.getEpicById(epic.getId());

        Subtask subtask = creationSubtask();
        Subtask subtask2 = creationSubtask();

        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);
        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);


        subtask.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask);
        manager.updateSubtask(subtask2);
        assertEquals(savedEpic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void checkingEpicForSubtask() {
        Epic epic = creationEpic();
        manager.saveEpic(epic);

        Subtask subtask = creationSubtask();
        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);

        assertEquals(
                manager.getSubtaskById(subtask.getId()).getEpicId(),
                epic.getId(),
                "У subtask есть epic"
        );
    }

    @Test
    public void getTaskStoreTest() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        manager.saveTask(task1);
        manager.saveTask(task2);

        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);

        Subtask subtask1 = creationSubtask();
        Subtask subtask2 = creationSubtask();
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);

        Collection<Task> tasks = List.of(task1, task2);
        Collection<Task> taskStore = manager.getTaskStore();
        assertArrayEquals(tasks.toArray(), taskStore.toArray(), "Возвращается коллекция Тасок");
    }

    @Test
    public void getEpicStoreTest() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        manager.saveTask(task1);
        manager.saveTask(task2);

        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);

        Subtask subtask1 = creationSubtask();
        Subtask subtask2 = creationSubtask();
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);

        Collection<Epic> epics = List.of(epic1, epic2);
        Collection<Epic> epicStore = manager.getEpicStore();
        assertArrayEquals(epics.toArray(), epicStore.toArray(), "Возвращается коллекция Эпиков");
    }

    @Test
    public void getSubtasksStoreTest() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        manager.saveTask(task1);
        manager.saveTask(task2);

        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);

        Subtask subtask1 = creationSubtask();
        Subtask subtask2 = creationSubtask();
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);

        Collection<Subtask> subtasks = List.of(subtask1, subtask2);
        Collection<Subtask> subtaskStore = manager.getSubtasksStore();
        assertArrayEquals(subtasks.toArray(), subtaskStore.toArray(), "Возвращается коллекция Сабтасок");
    }

    @Test
    public void saveEpicTest() {
        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();

        assertEquals(0, manager.getEpicStore().size(), "Эпики не сохранёны");
        
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);

        assertArrayEquals(List.of(epic1, epic2).toArray(),
                manager.getEpicStore().toArray(),
                "Эпики сохранёны");
    }

    @Test
    public void saveSubtaskTest() {
        Subtask subtask1 = creationSubtask();
        Subtask subtask2 = creationSubtask();

        assertEquals(0, manager.getSubtasksStore().size(), "Сабтаски не сохранёны");

        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);

        assertArrayEquals(List.of(subtask1, subtask2).toArray(),
                manager.getSubtasksStore().toArray(),
                "Сабтаски сохранёны");
    }

    @Test
    public void saveTaskTest() {
        Task task1 = creationTask();
        Task task2 = creationTask();

        assertEquals(0, manager.getTaskStore().size(), "Эпики не сохранёны");

        manager.saveTask(task1);
        manager.saveTask(task2);

        assertArrayEquals(List.of(task1, task2).toArray(),
                manager.getTaskStore().toArray(),
                "Эпики сохранёны");
    }

    @Test
    public void updateEpicTest() {
        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);

        Object[] expected1 = List.of(epic1, epic2).toArray();
        Object[] actual = manager.getEpicStore().toArray();
        assertArrayEquals(expected1,
                actual,
                "Список эпиков");

        epic1 = new Epic(epic1.getId(), "Изменённый title", "Изменённый description", TaskStatus.IN_PROGRESS, Instant.now(), 0);
        manager.updateEpic(epic1);

        Object[] expected = List.of(epic1, epic2).toArray();
        Object[] actual1 = manager.getEpicStore().toArray();
        assertArrayEquals(expected,
                actual1,
                "Список эпиков обновлён.");
    }

    @Test
    public void deleteTaskTest() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        manager.saveTask(task1);
        manager.saveTask(task2);

        assertArrayEquals(List.of(task1, task2).toArray(),
                manager.getTaskStore().toArray(),
                "TaskStore c 2 элементами");

        manager.deleteTask(task2.getId());

        assertArrayEquals(List.of(task1).toArray(),
                manager.getTaskStore().toArray(),
                "TaskStore c 1 элементом. Второй элемент удалён.");
    }

    @Test
    public void deleteEpicTest() {
        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);

        assertArrayEquals(List.of(epic1, epic2).toArray(),
                manager.getEpicStore().toArray(),
                "EpicStore c 2 элементами");

        manager.deleteEpic(epic2.getId());

        assertArrayEquals(List.of(epic1).toArray(),
                manager.getEpicStore().toArray(),
                "EpicStore c 1 элементом. Второй элемент удалён.");
    }

    @Test
    public void deleteSubtaskTest() {
        Epic epic1 = creationEpic();
        manager.saveEpic(epic1);

        Subtask subtask1 = creationSubtask();
        Subtask subtask2 = creationSubtask();
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);

        subtask2.setEpicId(epic1.getId());

        assertArrayEquals(List.of(subtask1, subtask2).toArray(),
                manager.getSubtasksStore().toArray(),
                "SubtaskStore c 2 элементами");

        manager.deleteSubtask(subtask2.getId());

        Collection<Subtask> subtasksStore = manager.getSubtasksStore();
        assertArrayEquals(List.of(subtask1).toArray(),
                subtasksStore.toArray(),
                "SubtaskStore c 1 элементом. Второй элемент удалён.");
    }

    @Test
    public void deleteTasksTest() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        Task task3 = creationTask();
        manager.saveTask(task1);
        manager.saveTask(task2);
        manager.saveTask(task3);

        assertEquals(3, manager.getTaskStore().size(), "TaskStore не пустой.");

        manager.deleteTasks();

        assertEquals(0, manager.getTaskStore().size(), "TaskStore пустой.");
    }

    @Test
    public void deleteEpicsTest() {
        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();
        Epic epic3 = creationEpic();
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);
        manager.saveEpic(epic3);

        assertEquals(3, manager.getEpicStore().size(), "EpicStore не пустой.");

        manager.deleteEpics();

        assertEquals(0, manager.getEpicStore().size(), "EpicStore пустой.");
    }

    @Test
    public void deleteSubtasksTest() {
        Subtask subtask1 = creationSubtask();
        Subtask subtask2 = creationSubtask();
        Subtask subtask3 = creationSubtask();
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);
        manager.saveSubtask(subtask3);

        assertEquals(3, manager.getSubtasksStore().size(), "SubtaskStore не пустой.");

        manager.deleteSubtasks();

        assertEquals(0, manager.getSubtasksStore().size(), "SubtaskStore пустой.");
    }

    @Test
    public void deleteAllTasksTest() {
        Task task = creationTask();
        Epic epic = creationEpic();
        Subtask subtask = creationSubtask();
        manager.saveTask(task);
        manager.saveTask(epic);
        manager.saveSubtask(subtask);
        subtask.setEpicId(epic.getId());

        assertEquals(2, manager.getTaskStore().size(), "TaskStore не пустой.");
        assertEquals(1, manager.getEpicStore().size(), "EpicStore не пустой.");
        assertEquals(1, manager.getSubtasksStore().size(), "SubtasksStore не пустой.");

        manager.deleteAllTasks();

        assertEquals(0, manager.getTaskStore().size(), "TaskStore пустой.");
        assertEquals(0, manager.getEpicStore().size(), "EpicStore пустой.");
        assertEquals(0, manager.getSubtasksStore().size(), "SubtasksStore пустой.");
    }

    @Test
    public void getListAllTasksTest() {}

    @Test
    public void getTaskById() {
        Task task1 = creationTask();
        Task task2 = creationTask();
        manager.saveTask(task1);
        manager.saveTask(task2);

        assertNull(manager.getTaskById(10), "Не существует");

        assertEquals(task1, manager.getTaskById(task1.getId()), "Возвращает верную задачу");
    }

    @Test
    public void getEpicById() {
        Epic epic1 = creationEpic();
        Epic epic2 = creationEpic();
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);

        assertNull(manager.getEpicById(10), "Не существует");

        assertEquals(epic1, manager.getEpicById(epic1.getId()), "Возвращает верную задачу");
    }

    @Test
    public void getSubtaskById() {
        Subtask subtask1 = creationSubtask();
        Subtask subtask2 = creationSubtask();
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);

        assertNull(manager.getSubtaskById(10), "Не существует");

        assertEquals(subtask1, manager.getSubtaskById(subtask1.getId()), "Возвращает верную задачу");
    }

    private Task creationTask() {
        return new Task("Test title Task", "test description Task", Instant.now(), 0);
    }

    private Epic creationEpic() {
        return new Epic("Test title Epic", "test description Epic", Instant.now(), 0);
    }

    private Subtask creationSubtask() {
        return new Subtask("Test title Subtask", "test description Subtask", Instant.now(), 0);
    }
}