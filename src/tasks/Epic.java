package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task { //эпик
    public List<Subtask> subtasks = new ArrayList<>();

    public Epic() {
        super();
    }
}
