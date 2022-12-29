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

    void deleteTask(int id);

    void deleteTasks();

    void deleteEpic(int id);

    void deleteEpics();

    void deleteSubtask(int id);

    void deleteSubtasks();

    void deleteAllTasks();

    void saveSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void addSubtaskToEpic(Subtask subtask);

    void saveTask(Task task);

    void updateTask(Task task);

    void getListAllTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

}
