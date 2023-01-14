package test;

import api.servers.HttpTaskServer;
import api.servers.KVServer;
import com.google.gson.Gson;
import manager.Managers;
import manager.tasks.FileBackedTasksManager;
import manager.tasks.TaskManager;
import manager.tasks.TypeTasks;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest extends FileBackedTasksManager {
    private KVServer server;
    private TaskManager manager;
    private HttpTaskServer httpTaskServer;
    private Gson gson = new Gson();
    private String apiToken;

    @BeforeEach
    public void create() {
        try {
            server = new KVServer();
            server.start();
            manager = Managers.getDefault("http://localhost:" + KVServer.PORT + "/");
            httpTaskServer = new HttpTaskServer(manager);
            httpTaskServer.start();

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:" + KVServer.PORT + "/register"))
                    .header("Content-Type", "application/json")
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void stop() {
        server.stop();
        httpTaskServer.stop();
        manager.deleteAllTasks();
    }

    @Test
    public void taskLoadFromServer() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        Task task = new Task("titleTestTask1", "descriptionTestTask1", Instant.now(), 0);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        assertEquals(response.statusCode(), 200);
        assertEquals(response.body(), "Task was created");

        task = manager.getPrioritizedTasks().get(0);

        URI uri1 = URI.create("http://localhost:8078/load/" + task.getId() + "?API_TOKEN=" + apiToken);
        HttpRequest request1 = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response1 = client.send(request1,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );
        assertEquals(response1.statusCode(), 200);
        assertEquals(response1.body(),
                String.format("%s,%s,%s,%s,%s,%s,%s\n",
                        task.getId(), TypeTasks.TASK, task.getTitle(), TaskStatus.NEW,
                        task.getDescription(), task.getStartTime().toString(), task.getDuration()
                )
        );
    }

    @Test
    public void loadAllFromServer() {
        Task task = new Task("titleTestTask1", "descriptionTestTask1", Instant.now(), 0);
        manager.saveTask(task);
        Epic epic = new Epic("titleTestEpic1", "descriptionTestEpic1", Instant.now(), 0);
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("titleTestSubtask1", "descriptionTestSubtask1",
                TaskStatus.NEW, Instant.now(), 0, epic.getId());
        manager.saveSubtask(subtask);
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());
        manager.save();
        List<Task> history = Managers.getDefaultHistory().getHistory();

        String allDataFromKVServer = manager.loadAll();
        List<String> arraysData = gson.fromJson(allDataFromKVServer, List.class);
        String task1 = arraysData.get(0);
        String epic1 = arraysData.get(1);
        String subtask1 = arraysData.get(2);
        String history1 = arraysData.get(3);

        assertEquals(task.toString(), task1);
        assertEquals(subtask.toString(), subtask1);
        assertEquals(epic.toString(), epic1);
        assertEquals(history1, history.stream()
                .map(it -> it.getId().toString())
                .collect(Collectors.joining(","))
        );
    }
}