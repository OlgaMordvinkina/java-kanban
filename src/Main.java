import api.servers.HttpTaskServer;
import api.servers.KVServer;
import manager.Managers;
import manager.tasks.TaskManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        TaskManager manager = Managers.getDefault("http://localhost:" + KVServer.PORT + "/");
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }
}
