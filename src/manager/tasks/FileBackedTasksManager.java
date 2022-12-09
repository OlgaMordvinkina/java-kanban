package manager.tasks;

import exceptions.ManagerSaveException;
import manager.Managers;
import manager.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public static void main(String[] args) {
        System.out.println("1 - восстановить\n2 - записать");
        String scanner = new Scanner(System.in).nextLine();

        switch (scanner) {
            case "1": {
                FileBackedTasksManager.loadFromFile(new File("resources/data.csv"));
                break;
            }
            case "2": {
                TaskManager manager = Managers.getDefault();
                FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("resources/data.csv"));

                Task task = new Task("Планы на день:", "Выбросить мусор");
                manager.saveTask(task);

                Task task2 = new Task("Планы на неделю:", "Сдать ТЗ");
                manager.saveTask(task2);

                Epic epic = new Epic("Покупки", "Еда");
                manager.saveEpic(epic);
                Subtask subtask = new Subtask("Хлебобулочные", "Рогалики");
                subtask.setEpicId(epic.getId());
                manager.saveSubtask(subtask);
                Subtask subtask2 = new Subtask("Мясное", "Фарш");
                subtask2.setEpicId(epic.getId());
                manager.saveSubtask(subtask2);

                Epic epic2 = new Epic("Покупки", "Для дома");
                manager.saveEpic(epic2);
                Subtask subtask3 = new Subtask("Мясное", "Фарш");
                subtask3.setEpicId(epic2.getId());
                manager.saveSubtask(subtask3);

                manager.getTaskById(1);
                manager.getSubtaskById(4);
                manager.getEpicById(3);
                manager.getEpicById(6);
                manager.getTaskById(2);
                manager.getSubtaskById(5);

                fileManager.save();
                System.out.println(FileBackedTasksManager.historyFromString("5,4,1,0,1"));
                break;
            }
        }
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic\n");
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
            fileWriter.write(historyToString(this.historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка. Файл не записан.");
        }
    }

    private static String historyToString(HistoryManager manager) {
        List<String> idHistory = new ArrayList<>();
        for (Task history : manager.getHistory()) {
            idHistory.add(history.getId().toString());
        }
        return String.join(",", idHistory);
    }

    private static List<Integer> historyFromString(String value) {
        String[] idHistory = value.split(",");
        List<Integer> idHistoryList = new ArrayList<>();
        for (String id : idHistory) {
            idHistoryList.add(Integer.valueOf(id));
        }
        return idHistoryList;
    }

    private Task fromString(String value) {
        String[] allTasks = value.split(",");
        int id = Integer.parseInt(allTasks[0]);
        TypeTasks typeTask = TypeTasks.valueOf(allTasks[1]);
        String title = allTasks[2];
        TaskStatus status = TaskStatus.valueOf(allTasks[3]);
        String description = allTasks[4];

        switch (typeTask) {
            case TASK:
                return new Task(id, title, description, status);
            case EPIC:
                return new Epic(id, title, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(allTasks[5]);
                return new Subtask(id, title, description, status, epicId);
            default:
                return null;
        }
    }

    private static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        String data;
        boolean arm = false;
        boolean fields = true;
        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка. Файл не прочитан.");
        }
        String[] lines = data.split("\r?\n");
        for (String line : lines) {
            if (fields) {
                fields = false;
                continue;
            }
            if (line.isBlank()) {
                arm = true;
                continue;
            }
            String[] obj = line.split(",");
            if (arm) {
                for (String idHistory : obj) {

                    fileManager.getTaskById(Integer.parseInt(idHistory));
                    fileManager.getEpicById(Integer.parseInt(idHistory));
                    fileManager.getSubtaskById(Integer.parseInt(idHistory));
                }
                break;
            }

            int id = Integer.parseInt(obj[0]);
            TypeTasks type = TypeTasks.valueOf(obj[1]);
            String title = obj[2];
            TaskStatus status = TaskStatus.valueOf(obj[3]);
            String description = obj[4];

            switch (Objects.requireNonNull(type)) {
                case TASK: {
                    Task task = new Task(id, title, description, status);
                    taskStore.put(id, task);
                    break;
                }
                case EPIC: {
                    Epic epic = new Epic(id, title, description, status);
                    epicStore.put(id, epic);
                    break;
                }
                case SUBTASK: {
                    int epicId = Integer.parseInt(obj[5]);
                    Subtask subtask = new Subtask(id, title, description, status, epicId);
                    subtaskStore.put(id, subtask);
                    break;
                }
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
    public void getTaskById(int id) {
        super.getTaskById(id);
        save();
    }

    @Override
    public void getEpicById(int id) {
        super.getEpicById(id);
        save();
    }

    @Override
    public void getSubtaskById(int id) {
        super.getSubtaskById(id);
        save();
    }
}
