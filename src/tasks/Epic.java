package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task { //эпик
    public List<Subtask> subtasks = new ArrayList<>();

    @Override
    public String toString() {
        return "Epic:\n" +
                "title: " + title +
                ", \ndescription: " + description +
                ", \nstatus: " + status +
                ", \nid: " + id;
    }
    public Epic() {
        super();
    }
}
