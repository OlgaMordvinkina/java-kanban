package tasks;

import static manager.tasks.TypeTasks.SUBTASK;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, String title, String description, TaskStatus status, Integer epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, TaskStatus status, Integer epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description) {
        super(title, description);
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s\n", id, SUBTASK, title, status, description, getEpicId());
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}
