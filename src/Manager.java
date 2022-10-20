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

    void saveEpic(Epic epic) { //получение списка задач
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
    
    void deleteTasks() { //удаление всех задач
        taskStore.clear();
        epicStore.clear();
        subtaskStore.clear();
        System.out.println("Все задачи удалены.");
    }

    void getTaskById(int id) { //получение по ид
        if (taskStore.containsKey(id)) {
            taskStore.get(id);
            System.out.println("Задача добавлена.");
        } else if (epicStore.containsKey(id)) {
            epicStore.get(id);
            System.out.println("Задача добавлена.");
        } else if (subtaskStore.containsKey(id)) {
            subtaskStore.get(id);
            System.out.println("Задача добавлена.");
        } else {
            System.out.println("Задачи с таким идентификаторов не существует.");
        }
    }

    void deleteTaskById(int id) { //удалить задачу по идентификатору
        if (taskStore.containsKey(id)) {
            taskStore.remove(id);
            System.out.println("Задача удалена.");
        } else if (epicStore.containsKey(id)) {
            epicStore.remove(id);
            System.out.println("Задача удалена.");
        } else if (subtaskStore.containsKey(id)) {
            subtaskStore.remove(id);
            System.out.println("Задача удалена.");
        } else {
            System.out.println("Задачи с таким идентификаторов не существует.");
        }
    }

    void updateTask(Task task) { //обновление задачи - !!!!!!!!!
        taskStore.put(task.getId(), task);
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
