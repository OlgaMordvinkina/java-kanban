package manager.tasks;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private final File defaultFile = new File("resources/test_data.csv");


    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
        this.file = defaultFile;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");
            Map<Integer, String> allData = getAllData();

            for (String value : allData.values()) {
                fileWriter.write(String.format("%s", value));
            }
            fileWriter.write("\n");
            fileWriter.write(CSVUtil.historyToString(this.historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка. Файл не записан.");
        }
    }

    public final Map<Integer, String> getAllData() {
        Map<Integer, String> allData = new HashMap<>();

        for (Integer task : taskStore.keySet()) {
            allData.put(task, taskStore.get(task).toString());
        }

        for (Integer epic : epicStore.keySet()) {
            allData.put(epic, epicStore.get(epic).toString());
        }

        for (Integer subtask : subtaskStore.keySet()) {
            allData.put(subtask, subtaskStore.get(subtask).toString());
        }
        return allData;
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
