package manager.tasks;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.Collection;

public interface TaskManager {

    Collection<Task> getTaskStore();

    Collection<Epic> getEpicStore();

    Collection<Subtask> getSubtasksStore();

    void saveEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    void deleteTask(int id);

    void deleteSubtask(int id);

    void saveSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void addSubtaskToEpic(Subtask subtask);

    void saveTask(Task task);

    void updateTask(Task task);

    void getListAllTasks();

    void deleteAllTasks();

    void getTaskById(int id);

    void getEpicById(int id);

    void getSubtaskById(int id);

}