package utils;

import manager.history.HistoryManager;
import manager.tasks.TypeTasks;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    public static String historyToString(HistoryManager manager) {
        List<String> idHistory = new ArrayList<>();
        for (Task history : manager.getHistory()) {
            idHistory.add(history.getId().toString());
        }
        return String.join(",", idHistory);
    }

    public static List<Integer> historyFromString(String value) {
        String[] idHistory = value.split(",");
        List<Integer> idHistoryList = new ArrayList<>();
        for (String id : idHistory) {
            idHistoryList.add(Integer.valueOf(id));
        }
        return idHistoryList;
    }

    public Task fromString(String value) {
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
}
