package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] arrayPath = path.split("/");
        boolean isHaveId = false;
        int id = 0;
        if (arrayPath.length > 2) {
            isHaveId = true;
            id = Integer.parseInt(arrayPath[2]);
        }
        switch (method) {
            case "GET":
                if (isHaveId) {
                    Subtask subtask;
                    try {
                        subtask = taskManager.getSubtask(id);
                    } catch (NotFoundException e) {
                        sendNotFound(exchange, e.getMessage());
                        break;
                    }
                    String json = gson.toJson(subtask);
                    sendText(exchange, json);
                } else {
                    List<Subtask> listOfSubtasks = taskManager.getListSubtask();
                    String json = gson.toJson(listOfSubtasks);
                    sendText(exchange, json);
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String jsonSubtask = new String(inputStream.readAllBytes(), getUtf());
                Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
                boolean isCross = false;
                Integer idCross = 0;
                if (isHaveId) {
                    isCross = taskManager.updateSubtask(subtask); // Если задача пересекается, то updateTask возвращает true
                } else {
                    idCross = taskManager.addSubtask(subtask); // Если задача пересекается, то addTask возвращает Integer = null
                }
                if (isCross || idCross == null) {
                    sendHasInteractions(exchange, "Subtask is crossing the other tasks");
                }
                sendWithoutAnswer(exchange);
                break;
            case "DELETE":
                if (isHaveId) {
                    taskManager.removeSubtask(id);
                    sendText(exchange, "Subtask is deleted");
                } else {
                    sendNotFound(exchange, "ID is not send");
                }
                break;
            default:
                sendNotFound(exchange, "Command not found");
        }
    }
}
