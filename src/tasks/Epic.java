package tasks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static manager.tasks.TypeTasks.EPIC;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();
    private Instant endTime = Instant.ofEpochMilli(0);

    public Epic(Integer id, String title, String description, TaskStatus status, Instant startTime, long duration) {
        super(id, title, description, status, startTime, duration);
        this.endTime = this.getEndTime();
    }

    public Epic(String title, String description, TaskStatus status, Instant startTime, long duration) {
        super(title, description, status, startTime, duration);
    }

    public Epic(String title, String description, Instant startTime, long duration) {
        super(title, description, startTime, duration);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s\n", id, EPIC, title, status, description, startTime, duration);
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
