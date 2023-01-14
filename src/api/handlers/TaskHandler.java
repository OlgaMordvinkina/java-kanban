package api.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.tasks.TaskManager;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange httpExchange) {
        String response;
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        response = gson.toJson(taskManager.getTaskStore());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/?\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Неккоректный id = " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    break;
                }
                case "POST": {
                    Task task;
                    String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8);

                    JsonElement jsonElement = JsonParser.parseString(bodyRequest);
                    JsonObject taskObject = jsonElement.getAsJsonObject();

                    String title = taskObject.get("title").getAsString();
                    String description = taskObject.get("description").getAsString();
                    Instant startTime = Instant.ofEpochSecond(
                            taskObject.get("startTime").getAsJsonObject().get("seconds").getAsLong(),
                            taskObject.get("startTime").getAsJsonObject().get("nanos").getAsLong()
                    );
                    long duration = Long.parseLong(taskObject.get("duration").getAsString());

                    if (Pattern.matches("^/tasks/task/$", path)) {
                        if (!taskObject.keySet().contains("id")) {
                            task = new Task(title, description, startTime, duration);
                            taskManager.saveTask(task);
                            System.out.println("Таск создан.");
                            sendText(httpExchange, "Task was created");
                            break;
                        }
                    }

                    if (Pattern.matches("^/tasks/task/?\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        TaskStatus status = TaskStatus.valueOf(taskObject.get("status").getAsString());
                        if (id != -1) {
                            task = new Task(id, title, description, status, startTime, duration);
                            taskManager.updateTask(task);
                            System.out.println("Таск с id = " + id + " обновлён.");
                            break;
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        taskManager.deleteTasks();
                        System.out.println("Все эпики удалены.");
                        break;
                    }

                    if (Pattern.matches("^/tasks/task/?\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteTask(id);
                            System.out.println("Таск с id = " + id + " удалён.");
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Неккоректный id = " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
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

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (Exception e) {
            return -1;
        }
    }

    //    NumberFormatException | NullPointerException |  taskManager.getTaskById(id) != null
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
