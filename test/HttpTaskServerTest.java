import com.google.gson.*;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer;
    URI uriTask = URI.create("http://localhost:8080/tasks");
    URI uriEpic = URI.create("http://localhost:8080/epics");
    URI uriSubtask = URI.create("http://localhost:8080/subtasks");
    Gson gson = Managers.getGson();
    Task task;
    Epic epic;
    Subtask subtask;
    Task taskDefault;
    Task task2;
    Subtask subtask2;

    @BeforeEach
    void setUp() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        taskManager.removeAllSubtask();
        try {
            httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        task = new Task("Name1", "Description1", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 1, 1));
        epic = new Epic("Name1", "Description1", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 1, 1), LocalDateTime.of(2025, 1, 1, 1, 31));
        subtask = new Subtask("Name2", "Description2", Status.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 1, 1));
        taskDefault = new Task("Name3", "Description3");
        task2 = new Task("Name2", "Description2", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 1, 21));
        subtask2 = new Subtask("Name3", "Description3", Status.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 1, 21));
    }

    @AfterEach
    void end() {
        try {
            httpTaskServer.stopServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldAddTask() {
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriTask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        List<Task> listTasks = taskManager.getListTask();
        assertEquals(1, listTasks.size(), "Задача не добавилась");
    }

    @Test
    void shouldUpdateTask() {
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriTask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        task.setStatus(Status.IN_PROGRESS);
        json = gson.toJson(task);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriTask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        Task taskTest = taskManager.getTask(task.getId());
        assertEquals(Status.IN_PROGRESS, taskTest.getStatus(), "Задача не обновилась");
    }

    @Test
    void shouldGetTask() {
        taskManager.addTask(task);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        Task task1 = gson.fromJson(jsonObject, Task.class);
        assertEquals(task1, task, "Не получили задачу");
    }

    @Test
    void shouldGetTaskId() {
        taskManager.addTask(task);
        uriTask = URI.create("http://localhost:8080/tasks" + "/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task task1 = gson.fromJson(jsonObject, Task.class);
        assertEquals(task1, task, "Не получили задачу");
    }

    @Test
    void shouldDeleteTaskId() {
        taskManager.addTask(task);
        uriTask = URI.create("http://localhost:8080/tasks" + "/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        List<Task> listOfTasks = taskManager.getListTask();
        assertEquals(0, listOfTasks.size(), "Не удалил задачу");
    }

    @Test
    void shouldAddEpic() {
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        List<Epic> listEpics = taskManager.getListEpic();
        assertEquals(1, listEpics.size(), "Эпик не добавился");
    }

    @Test
    void shouldUpdateEpic() {
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        epic.setStatus(Status.IN_PROGRESS);
        json = gson.toJson(epic);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        Epic epicTest = taskManager.getEpic(epic.getId());
        assertEquals(Status.IN_PROGRESS, epicTest.getStatus(), "Эпик не обновился");
    }

    @Test
    void shouldGetEpic() {
        taskManager.addEpic(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriEpic)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        Epic epic1 = gson.fromJson(jsonObject, Epic.class);
        assertEquals(epic1, epic, "Не получили эпик");
    }

    @Test
    void shouldGetEpicId() {
        taskManager.addEpic(epic);
        uriTask = URI.create("http://localhost:8080/epics" + "/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic epic1 = gson.fromJson(jsonObject, Epic.class);
        assertEquals(epic1, epic, "Не получили эпик");
    }

    @Test
    void shouldDeleteEpicId() {
        taskManager.addEpic(epic);
        uriTask = URI.create("http://localhost:8080/epics" + "/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        List<Epic> listOfEpics = taskManager.getListEpic();
        assertEquals(0, listOfEpics.size(), "Не удалил эпик");
    }

    @Test
    void shouldAddSubtask() {
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        json = gson.toJson(subtask);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        List<Subtask> listSubtask = taskManager.getListSubtask();
        assertEquals(1, listSubtask.size(), "Подзадача не добавилась");
    }

    @Test
    void shouldUpdateSubtask() {
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        json = gson.toJson(subtask);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        subtask.setStatus(Status.IN_PROGRESS);
        json = gson.toJson(subtask);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        Subtask subtaskTest = taskManager.getSubtask(subtask.getId());
        assertEquals(Status.IN_PROGRESS, subtaskTest.getStatus(), "Подзадача не обновилась");
    }

    @Test
    void shouldGetSubtask() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriSubtask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        Subtask subtask1 = gson.fromJson(jsonObject, Subtask.class);
        assertEquals(subtask1, subtask, "Не получили подзадачу");
    }

    @Test
    void shouldGetSubtaskId() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        uriTask = URI.create("http://localhost:8080/subtasks" + "/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Subtask subtask1 = gson.fromJson(jsonObject, Subtask.class);
        assertEquals(subtask1, subtask, "Не получили подзадачу");
    }

    @Test
    void shouldDeleteSubtaskId() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        uriTask = URI.create("http://localhost:8080/subtasks" + "/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        List<Subtask> listOfSubtasks = taskManager.getListSubtask();
        assertEquals(0, listOfSubtasks.size(), "Не удалил подзадачу");
    }

    @Test
    void shouldGetSubtaskOfEpic() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        uriTask = URI.create("http://localhost:8080/epics" + "/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        Subtask subtask1 = gson.fromJson(jsonObject, Subtask.class);
        assertEquals(subtask1, subtask, "Не получили подзадачу эпика");
    }

    @Test
    void shouldGetHistory() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());
        uriTask = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject1 = jsonArray.get(0).getAsJsonObject();
        Subtask subtask1 = gson.fromJson(jsonObject1, Subtask.class);
        assertEquals(subtask1, subtask, "Не получили history");
        JsonObject jsonObject2 = jsonArray.get(1).getAsJsonObject();
        Epic epic1 = gson.fromJson(jsonObject2, Epic.class);
        assertEquals(epic1, epic, "Не получили history");
    }

    @Test
    void shouldGetPrioritized() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addTask(taskDefault);
        uriTask = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.statusCode(), "Код не 200");
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonObject jsonObject1 = jsonArray.get(0).getAsJsonObject();
        Subtask subtask1 = gson.fromJson(jsonObject1, Subtask.class);
        assertEquals(subtask1, subtask, "Не получили prioritized");
    }

    @Test
    void shouldNotFoundTaskId() {
        uriTask = URI.create("http://localhost:8080/tasks" + "/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(404, response.statusCode(), "Код не 404");
        String notFound = response.body();
        assertEquals(notFound, "Task not found", "Не получили taskNotFound");
    }

    @Test
    void shouldNotFoundEpicId() {
        uriTask = URI.create("http://localhost:8080/epics" + "/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(404, response.statusCode(), "Код не 404");
        String notFound = response.body();
        assertEquals(notFound, "Epic not found", "Не получили epicNotFound");
    }

    @Test
    void shouldNotFoundSubtaskId() {
        uriTask = URI.create("http://localhost:8080/subtasks" + "/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uriTask)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(404, response.statusCode(), "Код не 404");
        String notFound = response.body();
        assertEquals(notFound, "Subtask not found", "Не получили subtaskNotFound");
    }

    @Test
    void shouldCheckCrossTask() {
        taskManager.addTask(task2);
        String json = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriTask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(406, response.statusCode(), "Код не 406");
        List<Task> listTasks = taskManager.getListTask();
        assertEquals(2, listTasks.size());
        List<Task> sortedTask = taskManager.getPrioritizedTasks();
        assertEquals(1, sortedTask.size());
    }

    @Test
    void shouldCheckCrossSubtask() {
        String json = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.statusCode(), "Код не 201");
        taskManager.addSubtask(subtask2);
        json = gson.toJson(subtask);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uriSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(406, response.statusCode(), "Код не 406");
        List<Subtask> listSubtask = taskManager.getListSubtask();
        assertEquals(2, listSubtask.size());
        List<Task> sortedTask = taskManager.getPrioritizedTasks();
        assertEquals(1, sortedTask.size());
    }
}
