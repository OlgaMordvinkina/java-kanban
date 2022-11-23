package tasks;

public class Subtask extends Task {
    private Integer epicId;
    public Subtask(String title, String description) {
        super(title, description);
    } //подзадача

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "\nSubtask:\n" +
                "title: " + title +
                ", \ndescription: " + description +
                ", \nstatus: " + status +
                ", \nid: " + id;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}
