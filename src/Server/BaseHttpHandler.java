package Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.Managers;
import managers.TaskManager;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler {
    private final Charset utf = StandardCharsets.UTF_8;
    private int code;
    protected Gson gson = Managers.getGson();
    protected TaskManager taskManager = Managers.getDefault();

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        code = 200;
        sendResponse(exchange, text, code);
    }

    protected void sendWithoutAnswer(HttpExchange exchange) throws IOException {
        code = 201;
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, 0);
        String response = "";
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes(utf));
        }
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        code = 404;
        sendResponse(exchange, text, code);
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        code = 406;
        sendResponse(exchange, text, code);
    }

    private void sendResponse(HttpExchange exchange, String text, int code) throws IOException {
        byte[] response = text.getBytes(utf);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, response.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response);
        }
    }

    protected Charset getUtf() {
        return utf;
    }
}
