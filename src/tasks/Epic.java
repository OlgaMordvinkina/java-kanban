package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task { //эпик
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic() {
        super();
    }

    @Override
    public String toString() {
        return "Epic:\n" +
                "title: " + title +
                ", \ndescription: " + description +
                ", \nstatus: " + status +
                ", \nid: " + id;
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
