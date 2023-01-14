package api.servers;

import api.handlers.*;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.history.HistoryManager;
import manager.tasks.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    TaskManager taskManager;
    HistoryManager historyManager = Managers.getDefaultHistory();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.server = HttpServer.create();
        this.taskManager = taskManager;

        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task/", new TaskHandler(taskManager));
        server.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        server.createContext("/tasks/epic/", new EpicHandler(taskManager));
        server.createContext("/tasks/", new TasksHandler(taskManager));
        server.createContext("/tasks/history/", new HistoryHandler(historyManager));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }
}
