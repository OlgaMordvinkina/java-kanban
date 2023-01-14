package manager;

import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.tasks.HTTPTaskManager;
import manager.tasks.InMemoryTaskManager;
import manager.tasks.TaskManager;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefaultTask() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(String url) throws IOException, InterruptedException {
        return new HTTPTaskManager(url);
    }
}
