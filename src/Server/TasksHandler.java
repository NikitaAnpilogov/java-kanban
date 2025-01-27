package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import tasks.Task;
import java.io.*;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
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
                    Task task;
                    try {
                        task = taskManager.getTask(id);
                    } catch (NotFoundException e) {
                        sendNotFound(exchange, e.getMessage());
                        break;
                    }
                    String json = gson.toJson(task);
                    sendText(exchange, json);
                } else {
                    List<Task> listOfTasks = taskManager.getListTask();
                    String json = gson.toJson(listOfTasks);
                    sendText(exchange, json);
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String jsonSubtask = new String(inputStream.readAllBytes(), getUtf());
                Task task = gson.fromJson(jsonSubtask, Task.class);
                boolean isCross = false;
                Integer idCross = 0;
                if (isHaveId) {
                    isCross = taskManager.updateTask(task); // Если задача пересекается, то updateTask возвращает true
                } else {
                    idCross = taskManager.addTask(task); // Если задача пересекается, то addTask возвращает Integer = null
                }
                if (isCross || idCross == null) {
                    sendHasInteractions(exchange, "Task is crossing the other tasks");
                    break;
                }
                sendWithoutAnswer(exchange);
                break;
            case "DELETE":
                if (isHaveId) {
                    taskManager.removeTask(id);
                    sendText(exchange, "Task is deleted");
                } else {
                    sendNotFound(exchange, "ID is not send");
                }
                break;
            default:
                sendNotFound(exchange, "Command not found");
        }
    }
}
