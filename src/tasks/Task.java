package tasks;

public class Task { //задача
    private Integer id;
    private String title;
    private String description;
    private String status;

    public Task(Integer id, String title, String description, String status) {
        this.title = title;
        this.description = description;
    }

    public Task() {
    }

    @Override
    public String toString() {
        return "tasks.Task:\n" +
                "title: " + title +
                ", \ndescription: " + description +
                ", \nstatus: " + status +
                ", \nid: " + id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
