package api.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.tasks.TaskManager;
import tasks.Epic;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange httpExchange) {
        String response;
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        response = gson.toJson(taskManager.getEpicStore());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic/?\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getEpicById(id));
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
                    Epic epic;
                    String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8);

                    JsonElement jsonElement = JsonParser.parseString(bodyRequest);
                    JsonObject epicObject = jsonElement.getAsJsonObject();

                    String title = epicObject.get("title").getAsString();
                    String description = epicObject.get("description").getAsString();
                    Instant startTime = Instant.ofEpochSecond(
                            epicObject.get("startTime").getAsJsonObject().get("seconds").getAsLong(),
                            epicObject.get("startTime").getAsJsonObject().get("nanos").getAsLong()
                    );
                    long duration = Long.parseLong(epicObject.get("duration").getAsString());

                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        if (!epicObject.keySet().contains("id")) {
                            epic = new Epic(title, description, startTime, duration);
                            taskManager.saveEpic(epic);
                            System.out.println("Epic создан.");
                            sendText(httpExchange, "Epic was created");
                            break;
                        }
                    }

                    if (Pattern.matches("^/tasks/epic/?\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        TaskStatus status = TaskStatus.valueOf(epicObject.get("status").getAsString());
                        if (id != -1) {
                            epic = new Epic(id, title, description, status, startTime, duration);
                            taskManager.updateEpic(epic);
                            System.out.println("Epic с id = " + id + " обновлён.");
                            break;
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        taskManager.deleteEpics();
                        System.out.println("Все эпики удалены.");
                        break;
                    }

                    if (Pattern.matches("^/tasks/epic/?\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpic(id);
                            System.out.println("Эпик с id = " + id + " удалён.");
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
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
