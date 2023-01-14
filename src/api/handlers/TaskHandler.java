package api.handlers;

import api.adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.tasks.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange httpExchange) {
        final String query = httpExchange.getRequestURI().getQuery();
        String response;
        try {
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (query == null) {
                        response = gson.toJson(taskManager.getTaskStore());
                        sendText(httpExchange, response);
                        break;
                    } else {
                        final int id = Integer.parseInt(query.substring(3));
                        response = gson.toJson(taskManager.getTaskById(id));
                        sendText(httpExchange, response);
                    }
                    break;
                }
                case "POST": {
                    String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8);
                    String json = bodyRequest.replaceAll("\n|\\s", "");
                    final Task task = gson.fromJson(json, Task.class);

                    if (query == null) {
                        if (!json.contains("id")) {
                            taskManager.saveTask(task);
                            System.out.println("Task создан.");
                            sendText(httpExchange, "Task was created");
                        } else {
                            taskManager.updateTask(task);
                            System.out.println("Task с id = " + task.getId() + " обновлён.");
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (query == null) {
                        taskManager.deleteTasks();
                        System.out.println("Все эпики удалены.");
                        break;
                    } else {
                        final int id = Integer.parseInt(query.substring(3));
                        taskManager.deleteTask(id);
                            System.out.println("Таск с id = " + id + " удалён.");
                            httpExchange.sendResponseHeaders(200, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Ожидаемый запрос: GET, POST, DELETE.\nПолученный запрос: " + requestMethod);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
