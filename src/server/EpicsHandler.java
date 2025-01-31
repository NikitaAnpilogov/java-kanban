package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import tasks.Epic;
import tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] arrayPath = path.split("/");
        boolean isHaveId = false;
        boolean isHaveSubtask = false;
        int id = 0;
        if (arrayPath.length > 2) {
            isHaveId = true;
            id = Integer.parseInt(arrayPath[2]);
            if (arrayPath.length > 3) {
                isHaveSubtask = true;
            }
        }
        switch (method) {
            case "GET":
                if (isHaveId) {
                    Epic epic;
                    try {
                        epic = taskManager.getEpic(id);
                    } catch (NotFoundException e) {
                        sendNotFound(exchange, e.getMessage());
                        break;
                    }
                    if (isHaveSubtask) {
                        List<Subtask> subtasksOfEpic;
                        try {
                            subtasksOfEpic = taskManager.getSubtasksOfEpic(id);
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, e.getMessage());
                            break;
                        }
                        String json = gson.toJson(subtasksOfEpic);
                        sendText(exchange, json);
                        break;
                    }
                    String json = gson.toJson(epic);
                    sendText(exchange, json);
                } else {
                    List<Epic> listOfEpics = taskManager.getListEpic();
                    String json = gson.toJson(listOfEpics);
                    sendText(exchange, json);
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String jsonEpic = new String(inputStream.readAllBytes(), getUtf());
                Epic epic = gson.fromJson(jsonEpic, Epic.class);
                if (isHaveId) {
                    taskManager.updateEpic(epic);
                } else {
                    taskManager.addEpic(epic);
                }
                sendWithoutAnswer(exchange);
                break;
            case "DELETE":
                if (isHaveId) {
                    taskManager.removeEpic(id);
                    sendText(exchange, "Epic is deleted");
                } else {
                    sendNotFound(exchange, "ID is not send");
                }
                break;
            default:
                sendNotFound(exchange, "Command not found");
        }
    }
}
