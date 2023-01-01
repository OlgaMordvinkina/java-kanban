package manager.tasks;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import manager.Managers;
import manager.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import utils.CSVUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;


    public static void main(String[] args) {
        System.out.println("1 - записать\n2 - восстановить");
        String scanner = new Scanner(System.in).nextLine();

        switch (scanner) {
            case "2": {
                FileBackedTasksManager.loadFromFile(new File("resources/data.csv"));
                break;
            }
            case "1": {
                TaskManager manager = Managers.getDefault();
                FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("resources/data.csv"));

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
                break;
            }
        }
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");
            Map<Integer, String> allData = new HashMap<>();

            for (Integer task : taskStore.keySet()) {
                allData.put(task, taskStore.get(task).toString());
            }

            for (Integer task : epicStore.keySet()) {
                allData.put(task, epicStore.get(task).toString());
            }

            for (Integer task : subtaskStore.keySet()) {
                allData.put(task, subtaskStore.get(task).toString());
            }

            for (String value : allData.values()) {
                fileWriter.write(String.format("%s", value));
            }
            fileWriter.write("\n");
            fileWriter.write(CSVUtil.historyToString(this.historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка. Файл не записан.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        String data;
        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException exception) {
            throw new ManagerLoadException("Ошибка. Файл не прочитан.");
        }
        String[] lines = data.split("\r?\n");
        for (int i = 1; i < lines.length; i++) {
            String[] obj = lines[i].split(",");
            if (!lines[i].isBlank()) {
                int id = Integer.parseInt(obj[0]);
                TypeTasks type = TypeTasks.valueOf(obj[1]);
                String title = obj[2];
                TaskStatus status = TaskStatus.valueOf(obj[3]);
                String description = obj[4];
                Instant startTime = null;
                if (obj[5] != null && !obj[5].equals("null")) {
                    startTime = Instant.parse(obj[5]);
                }
                long duration = Long.parseLong(obj[6]);

                switch (Objects.requireNonNull(type)) {
                    case TASK: {
                        Task task = new Task(id, title, description, status, startTime, duration);
                        taskStore.put(id, task);
                        prioritizedTasks.add(task);
                        break;
                    }
                    case EPIC: {
                         Epic epic = new Epic(id, title, description, status, startTime, duration);
                        epicStore.put(id, epic);
                        prioritizedTasks.add(epic);
                        break;
                    }
                    case SUBTASK: {
                        int epicId = Integer.parseInt(obj[7]);
                        Subtask subtask = new Subtask(id, title, description, status, startTime, duration, epicId);
                        subtaskStore.put(id, subtask);
                        prioritizedTasks.add(subtask);
                        break;
                    }
                }
            } else {
                String[] history = lines[i+1].split(",");
                for (String idHistory : history) {
                    if (taskStore.containsKey(Integer.parseInt(idHistory))) {
                        fileManager.historyManager.add(taskStore.get(Integer.parseInt(idHistory)));
                    } else if (epicStore.containsKey(Integer.parseInt(idHistory))) {
                        fileManager.historyManager.add(epicStore.get(Integer.parseInt(idHistory)));
                    } else if (subtaskStore.containsKey(Integer.parseInt(idHistory))) {
                        fileManager.historyManager.add(subtaskStore.get(Integer.parseInt(idHistory)));
                    }
                }
                break;
            }
        }
        return fileManager;
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        super.addSubtaskToEpic(subtask);
        save();
    }

    @Override
    public void getListAllTasks() {
        super.getListAllTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        save();
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        save();
        return super.getSubtaskById(id);
    }
}
