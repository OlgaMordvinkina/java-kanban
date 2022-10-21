import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private static int id;
    HashMap<Integer, Task> taskStore = new HashMap<>();
    HashMap<Integer, Epic> epicStore = new HashMap<>();
    HashMap<Integer, Subtask> subtaskStore = new HashMap<>();
    Collection<Task> getTaskStore() {
        return taskStore.values();
    }

    Collection<Epic> getEpicStore() {
        return epicStore.values();
    }

    Collection<Subtask> getSubtasksStore() {
        return subtaskStore.values();
    }

    void saveEpic(Epic epic) {
        int currentEpicId = generateId();
        epic.setId(currentEpicId);
        epic.setStatus(TaskStatus.NEW);
        epicStore.put(currentEpicId, epic);
    }

    void updateEpic(Epic epic) { //обновление epic задачи
        epicStore.put(epic.getId(), epic);
    }

    void deleteEpic(int id) {
        epicStore.remove(id);
    }

    void deleteTask(int id) {
        taskStore.remove(id);
    }

    void deleteSubtask(int id) { //удалить задачу по идентификатору
        subtaskStore.remove(id);
    }

    void saveSubtask(Subtask subtask) {
        int currentSubtaskId = generateId();
        subtask.setId(currentSubtaskId);
        subtask.setStatus(TaskStatus.NEW);
        subtaskStore.put(currentSubtaskId, subtask);
        addSubtaskToEpic(subtask);
    }

    void updateSubtask(Subtask subtask) {
        subtaskStore.put(subtask.getId(), subtask);
        addSubtaskToEpic(subtask);
    }

    void addSubtaskToEpic(Subtask subtask) {
        Epic epic = epicStore.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            epic.setStatus(updateStatus(epic.getSubtasks()));
            epicStore.put(epic.getId(), epic);
        }
    }

    void saveTask(Task task) {
        int currentEpicId = generateId();
        task.setId(currentEpicId);
        task.setStatus(TaskStatus.NEW);
        taskStore.put(currentEpicId, task);
    }

    void updateTask(Task task) { //обновление task задачи
        taskStore.put(task.getId(), task);
    }

    void getListAllTasks() {
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

    void deleteTasks() {
        taskStore.clear();
        epicStore.clear();
        subtaskStore.clear();
    }

    void getTaskById(int id) {
        if (taskStore.containsKey(id)) {
            taskStore.get(id);
        }
    }

    void getEpicById(int id) {
        if (epicStore.containsKey(id)) {
            epicStore.get(id);
        }
    }

    void getSubtask(int id) {
        if (subtaskStore.containsKey(id)) {
            subtaskStore.get(id);
        }
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
