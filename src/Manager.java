import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.Collection;
import java.util.HashMap;
import tasks.*;

public class Manager {
    private static int id;

    HashMap<Integer, Task> taskStore = new HashMap<>();
    HashMap<Integer, Epic> epicStore = new HashMap<>();
    HashMap<Integer, Subtask> subtaskStore = new HashMap<>();


    Collection<Task> getListTasks() {
        return taskStore.values();
    }

    Collection<Epic> getListEpics() {
        return epicStore.values();
    }

    Collection<Subtask> getListSubtasks() {
        return subtaskStore.values();
    }

    void saveEpic(Epic epic) {
        int currentEpicId = assignsId();
        epic.setId(currentEpicId);
        epicStore.put(currentEpicId, epic);

        for (Subtask subtask : epic.subtasks) {
            int currentSubtaskId = assignsId();
            subtask.setId(currentSubtaskId);
            subtask.setEpicId(currentEpicId);
            subtaskStore.put(currentSubtaskId, subtask);
        }
    }
    
    void deleteTasks() {
        taskStore.clear();
        epicStore.clear();
        subtaskStore.clear();
        System.out.println("Все задачи удалены.");
    }

    void getTaskById(int id) {
        if (taskStore.containsKey(id) || epicStore.containsKey(id) || subtaskStore.containsKey(id)) {
            taskStore.get(id);
        }
    }


    void searchTask(int map) {

    }

    int assignsId() {
        return ++id;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Manager.id = id;
    }
}
