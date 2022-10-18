import java.util.HashMap;

public class Manager {
    private static int id;

    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Subtask> epicList = new HashMap<>();
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    HashMap<Integer, Task> taskSheet = new HashMap<>();
    HashMap<String, Subtask> sheet = new HashMap<>();


    void gettingListTasks() {
    }

    void deleteTasks() {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();
        System.out.println("Все задачи удалены.");
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
