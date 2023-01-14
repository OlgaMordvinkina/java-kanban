package manager.tasks;

import api.KVTaskClient;
import manager.Managers;
import manager.history.HistoryManager;
import utils.CSVUtil;

import java.io.IOException;

public class HTTPTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    HistoryManager historyManager = Managers.getDefaultHistory();

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        this.client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        getAllData().forEach((key, value) -> client.put(key.toString(), value));
        client.put("history", CSVUtil.historyToString(this.historyManager));
    }

    public String loadAll() {
        return client.loadAll();
    }
}
