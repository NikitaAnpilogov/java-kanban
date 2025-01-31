import server.*;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String pathTasks = "/tasks";
    private static final String pathEpics = "/epics";
    private static final String pathSubtasks = "/subtasks";
    private static final String pathHistory = "/history";
    private static final String pathPrioritized = "/prioritized";
    public static TaskManager taskManager;
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        setUp();
    }

    public HttpTaskServer(TaskManager taskManagerNew) throws IOException {
        taskManager = taskManagerNew;
        setUp();
    }

    private void setUp() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext(pathTasks, new TasksHandler());
        httpServer.createContext(pathEpics, new EpicsHandler());
        httpServer.createContext(pathSubtasks, new SubtasksHandler());
        httpServer.createContext(pathHistory, new HistoryHandler());
        httpServer.createContext(pathPrioritized, new PrioritizedTasksHandler());
    }

    public void startServer() throws IOException {
        httpServer.start();
    }

    public void stopServer() throws IOException {
        httpServer.stop(1);
    }
}
