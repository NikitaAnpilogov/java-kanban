package Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;
import java.io.IOException;
import java.util.List;

public class PrioritizedTasksHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
            String json = gson.toJson(prioritizedTasks);
            sendText(exchange, json);
        }
    }
}
