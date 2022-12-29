package tasks;

import java.time.Instant;

import static manager.tasks.TypeTasks.SUBTASK;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, String title, String description, TaskStatus status, Instant startTime, long duration, Integer epicId) {
        super(id, title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, TaskStatus status, Instant startTime, long duration, Integer epicId) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Instant startTime, long duration) {
        super(title, description, startTime, duration);
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", id, SUBTASK, title, status, description, startTime, duration, getEpicId());
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}
