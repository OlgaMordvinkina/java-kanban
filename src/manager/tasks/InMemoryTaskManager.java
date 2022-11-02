package manager.tasks;

import manager.Managers;
import manager.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id;

    HistoryManager historyManager = Managers.getDefaultHistory();

    protected HashMap<Integer, Task> taskStore = new HashMap<>();
    protected HashMap<Integer, Epic> epicStore = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskStore = new HashMap<>();

    Collection<Task> getTaskStore() {
        return taskStore.values();
    }

    Collection<Epic> getEpicStore() {
        return epicStore.values();
    }

    Collection<Subtask> getSubtasksStore() {
        return subtaskStore.values();
    }

    @Override
    public void saveEpic(Epic epic) {
        int currentEpicId = generateId();
        epic.setId(currentEpicId);
        epic.setStatus(TaskStatus.NEW);
        epicStore.put(currentEpicId, epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicStore.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epicStore.get(id);
        epicStore.remove(id);
        for (Subtask subtask : epic.getSubtasks()) {
            subtaskStore.remove(subtask.getId());
        }
    }

    @Override
    public void deleteTask(int id) {
        taskStore.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtaskStore.get(id);
        Epic epic = epicStore.get(subtask.getEpicId());

        if (epic != null) {
            epic.setSubtasks(removeSubtaskFromList(subtask, epic.getSubtasks()));
            epic.setStatus(updateStatus(epic.getSubtasks()));
            subtaskStore.remove(id);
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        int currentSubtaskId = generateId();
        subtask.setId(currentSubtaskId);
        subtask.setStatus(TaskStatus.NEW);
        subtaskStore.put(currentSubtaskId, subtask);
        addSubtaskToEpic(subtask);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskStore.put(subtask.getId(), subtask);
        addSubtaskToEpic(subtask);
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        Epic epic = epicStore.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            epic.setStatus(updateStatus(epic.getSubtasks()));
            epicStore.put(epic.getId(), epic);
        }
    }

    @Override
    public void saveTask(Task task) {
        int currentEpicId = generateId();
        task.setId(currentEpicId);
        task.setStatus(TaskStatus.NEW);
        taskStore.put(currentEpicId, task);
    }

    @Override
    public void updateTask(Task task) {
        taskStore.put(task.getId(), task);
    }

    @Override
    public void getListAllTasks() {
        if (!getTaskStore().isEmpty()) {
            System.out.println(getTaskStore());
        }
        if (!getEpicStore().isEmpty()) {
            System.out.println(getEpicStore());
        }
        if (!getSubtasksStore().isEmpty()) {
            System.out.println(getSubtasksStore());
        }
    }

    @Override
    public void deleteAllTasks() { //удаление всех задач (тасок, епиков, сабтасок)
        taskStore.clear();
        epicStore.clear();
        subtaskStore.clear();
    }

    @Override
    public void getTaskById(int id) {
        if (taskStore.containsKey(id)) {
            Task task = taskStore.get(id);
            recordHistory(task);
        }
    }

    @Override
    public void getEpicById(int id) {
        if (epicStore.containsKey(id)) {
            Epic epic = epicStore.get(id);
            recordHistory(epic);
        }
    }

    @Override
    public void getSubtaskById(int id) {
        if (subtaskStore.containsKey(id)) {
            Subtask subtask = subtaskStore.get(id);
            recordHistory(subtask);
        }
    }

    private void recordHistory(Task task) {
        historyManager.add(task);
    }

    private TaskStatus updateStatus(List<Subtask> subtasks) {
        List<TaskStatus> statusList = new ArrayList<>();

        for (Subtask subtask : subtasks) {
            statusList.add(subtask.getStatus());
        }

        if (containsOnlyOneStatus(TaskStatus.NEW, statusList)) {
            return TaskStatus.NEW;
        } else if (containsOnlyOneStatus(TaskStatus.DONE, statusList)) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    private List<Subtask> removeSubtaskFromList(Subtask subtask, List<Subtask> subtasks) {
        List<Subtask> result = new ArrayList<>();
        for (Subtask currentSubtask : subtasks) {
            if (!Objects.equals(currentSubtask.getId(), subtask.getId())) {
                result.add(subtask);
            }
        }
        return result;
    }

    private boolean containsOnlyOneStatus(TaskStatus status, List<TaskStatus> statusList) {
        boolean result = true;
        for (TaskStatus currentStatus : statusList) {
            if (currentStatus != status) {
                result = false;
                break;
            }
        }
        return result;
    }

    int generateId() {
        return ++id;
    }
}
