package tasks;

public class Subtask extends Task {
    private Integer epicId;
    public Subtask() {
        super();
    } //подзадача

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}
