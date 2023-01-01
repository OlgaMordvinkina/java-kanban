package manager.tasks;

import exceptions.ManagerTaskIntersection;
import manager.Managers;
import manager.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id;

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected static final HashMap<Integer, Task> taskStore = new HashMap<>();
    protected static final HashMap<Integer, Epic> epicStore = new HashMap<>();
    protected static final HashMap<Integer, Subtask> subtaskStore = new HashMap<>();

    protected static final Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    });

    @Override
    public Collection<Task> getTaskStore() {
        return taskStore.values();
    }

    @Override
    public Collection<Epic> getEpicStore() {
        return epicStore.values();
    }

    @Override
    public Collection<Subtask> getSubtasksStore() {
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
    public void saveSubtask(Subtask subtask) {
        int currentSubtaskId = generateId();
        subtask.setId(currentSubtaskId);
        subtask.setStatus(TaskStatus.NEW);
        subtaskStore.put(currentSubtaskId, subtask);
        addNewPrioritizedTask(subtask);
        addSubtaskToEpic(subtask);
    }

    @Override
    public void saveTask(Task task) {
        int currentEpicId = generateId();
        task.setId(currentEpicId);
        task.setStatus(TaskStatus.NEW);
        addNewPrioritizedTask(task);
        taskStore.put(currentEpicId, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicStore.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        addNewPrioritizedTask(subtask);
        subtaskStore.put(subtask.getId(), subtask);
        addSubtaskToEpic(subtask);
    }

    @Override
    public void updateTask(Task task) {
        addNewPrioritizedTask(task);
        taskStore.put(task.getId(), task);
    }

    @Override
    public void deleteTask(int id) {
        taskStore.remove(id);
        historyManager.remove(id);
        prioritizedTasks.removeIf(task -> task.getId() == id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epicStore.get(id);
        epicStore.remove(id);
        for (Subtask subtask : epic.getSubtasks()) {
            deleteSubtask(subtask.getId());
        }
        historyManager.remove(id);
        prioritizedTasks.removeIf(task -> task.getId() == id);
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
        historyManager.remove(id);
        prioritizedTasks.removeIf(task -> task.getId() == id);
    }

    @Override
    public void deleteTasks() {
        for (Task task : taskStore.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.removeIf(tasks -> tasks.getId().equals(task.getId()));
        }
        taskStore.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epicStore.values()) {
            historyManager.remove(epic.getId());
            prioritizedTasks.removeIf(task -> task.getId().equals(epic.getId()));
        }
        epicStore.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtaskStore.values()) {
            historyManager.remove(subtask.getId());
            prioritizedTasks.removeIf(task -> task.getId().equals(subtask.getId()));
        }
        subtaskStore.clear();
    }

    @Override
    public void deleteAllTasks() {
        deleteTasks();
        deleteEpics();
        deleteSubtasks();
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        Epic epic = epicStore.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);

            epic.setDuration(epic.getSubtasks().stream()
                    .map(Subtask::getDuration)
                    .reduce(0L, Long::sum)
            );

            epic.setStartTime(epic.getSubtasks().stream()
                    .map(Task::getStartTime)
                    .filter(Objects::nonNull)
                    .min(Instant::compareTo)
                    .orElse(null)
            );

            epic.setStatus(updateStatus(epic.getSubtasks()));
            epicStore.put(epic.getId(), epic);
        }
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
    public Task getTaskById(int id) {
        Task task = null;
        if (taskStore.containsKey(id)) {
            task = taskStore.get(id);
            recordHistory(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = null;
        if (epicStore.containsKey(id)) {
            epic = epicStore.get(id);
            recordHistory(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = null;
        if (subtaskStore.containsKey(id)) {
            subtask = subtaskStore.get(id);
            recordHistory(subtask);
        }
        return subtask;
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

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        checkTaskIntersection();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks); //отсортирован при инициализации
    }

    private void checkTaskIntersection() {
        getPrioritizedTasks().stream().reduce((a, b) -> {
            if (b.getStartTime() != null && a.getStartTime() != null && b.getStartTime().isBefore(a.getEndTime())) {
                throw new ManagerTaskIntersection("Задачи пересекаются");
            }
            return b;
        });
    }

    int generateId() {
        return ++id;
    }
}
