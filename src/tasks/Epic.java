package tasks;

import java.util.ArrayList;
import java.util.List;

import static manager.tasks.TypeTasks.EPIC;

public class Epic extends Task { //эпик
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(Integer id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
    }

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    public Epic(String title, String description) {
        super(title, description);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s\n", id, EPIC, title, status, description);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtask(Subtask subtask) {
        this.subtasks.add(subtask);
    }
}
