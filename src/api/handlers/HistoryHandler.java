package api.handlers;

import api.adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.history.HistoryManager;

import java.io.IOException;
import java.time.Instant;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HistoryHandler implements HttpHandler {
    private final HistoryManager historyManager;
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public HistoryHandler(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void handle(HttpExchange httpExchange) {
        final String query = httpExchange.getRequestURI().getQuery();
        String response;
        try {
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (query == null) {
                        response = gson.toJson(historyManager.getHistory());
                        sendText(httpExchange, response);
                        break;
                    }
                }
                default: {
                    System.out.println("Ожидаемый запрос: GET\nПолученный запрос: " + requestMethod);
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
