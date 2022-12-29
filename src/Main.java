import manager.Managers;
import manager.history.HistoryManager;
import manager.tasks.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task("Планы на день:", "Выбросить мусор", Instant.now(), 0);
        Task task2 = new Task("Планы на неделю:", "Сдать ТЗ", Instant.now(), 0);

        Epic epic = new Epic("Покупки", "Еда", Instant.now(), 0);
        Subtask subtask = new Subtask("Хлебобулочные", "Рогалики", Instant.now(), 0);
        Subtask subtask2 = new Subtask("Мясное", "Фарш", Instant.now(), 0);
        Subtask subtask3 = new Subtask("Соусы", "Майонез", Instant.now(), 0);

        Epic epic2 = new Epic("Покупки", "Для дома", Instant.now(), 0);

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
