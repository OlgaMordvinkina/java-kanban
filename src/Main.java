import api.servers.HttpTaskServer;
import api.servers.KVServer;
import manager.Managers;
import manager.tasks.FileBackedTasksManager;
import manager.tasks.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.CSVUtil;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        TaskManager manager = Managers.getDefault("http://localhost:" + KVServer.PORT + "/");
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();


        /*FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("resources/data.csv"));

        Task task = new Task("Планы на день:", "Выбросить мусор", null, 0);
        manager.saveTask(task);

        Task task2 = new Task("Планы на неделю:", "Сдать ТЗ", Instant.parse("2023-01-05T11:58:56.704873Z"), 0);
        manager.saveTask(task2);

        Epic epic = new Epic("Покупки", "Еда", Instant.parse("2023-01-02T11:58:56.704873Z"), 0);
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("Хлебобулочные", "Рогалики", Instant.parse("2023-01-01T11:58:56.704873Z"), 0);
        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);
        Subtask subtask2 = new Subtask("Мясное", "Фарш", Instant.parse("2023-01-04T11:58:56.704873Z"), 0);
        subtask2.setEpicId(epic.getId());
        manager.saveSubtask(subtask2);

        Epic epic2 = new Epic("Покупки", "Для дома", Instant.parse("2023-01-06T11:58:56.704873Z"), 0);
        manager.saveEpic(epic2);
        Subtask subtask3 = new Subtask("Мясное", "Фарш", null, 0);
        subtask3.setEpicId(epic2.getId());
        manager.saveSubtask(subtask3);

        manager.getTaskById(1);
        manager.getSubtaskById(4);
        manager.getEpicById(3);
        manager.getEpicById(6);
        manager.getTaskById(2);
        manager.getSubtaskById(5);

        fileManager.save();
        System.out.println(CSVUtil.historyFromString("5,4,1,0,1"));

        httpTaskServer.stop();
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();*/

    }
}
