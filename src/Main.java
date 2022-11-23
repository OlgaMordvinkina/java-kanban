import manager.Managers;
import manager.history.HistoryManager;
import manager.tasks.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task("Планы на день:", "Выбросить мусор");
        Task task2 = new Task("Планы на неделю:", "Сдать ТЗ");

        Epic epic = new Epic("Покупки", "Еда");
        Subtask subtask = new Subtask("Хлебобулочные", "Рогалики");
        Subtask subtask2 = new Subtask("Мясное", "Фарш");
        Subtask subtask3 = new Subtask("Соусы", "Майонез");

        Epic epic2 = new Epic("Покупки", "Для дома");

        manager.saveTask(task);
        manager.saveTask(task2);

        manager.saveEpic(epic);

        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);

        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);

        subtask3.setEpicId(epic.getId());
        manager.saveSubtask(subtask3);

        manager.saveEpic(epic2);

        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask2);
        epic.getSubtasks().add(subtask3);
        System.out.println("\n");
        manager.getListAllTasks();

        manager.getEpicById(7);
        manager.getSubtaskById(4);
        manager.getEpicById(7);
        manager.getSubtaskById(6);
        manager.getTaskById(2);
        manager.getSubtaskById(5);

        System.out.println("\n");
        System.out.println(historyManager.getHistory());

        manager.getSubtaskById(4);
        manager.getSubtaskById(6);
        manager.getEpicById(7);
        manager.getSubtaskById(5);
        manager.getTaskById(2);

        System.out.println("\n");
        System.out.println(historyManager.getHistory());

        manager.deleteTask(2);

        System.out.println("\n");
        System.out.println(historyManager.getHistory());

        manager.deleteEpic(3);


        System.out.println("\n");
        System.out.println(historyManager.getHistory());
    }
}
