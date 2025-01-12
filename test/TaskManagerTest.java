import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskManagerTest<T extends TaskManager> {
    InMemoryTaskManager taskManager;
    Task task = new Task("Name1", "Description1", Status.NEW);
    Epic epic = new Epic("Name2", "Description2");
    Subtask subtask = new Subtask("Name3", "Description3", Status.IN_PROGRESS, epic.getId());
    Task task2 = new Task("Name4", "Description4", Status.IN_PROGRESS);
    Epic epic2 = new Epic("Name5", "Description5");
    Subtask subtask2 = new Subtask("Name6", "Description6", Status.IN_PROGRESS, epic2.getId());
    Subtask subtask3 = new Subtask("Name7", "Description7", Status.NEW, epic.getId());

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void checkStatusEpicWhenAllNew() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask3);
        subtask.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask);
        Status expectedStatusEpic = Status.NEW;
        Epic testEpic = taskManager.getEpic(epic.getId());
        assertEquals(testEpic.getStatus(), expectedStatusEpic, "Статус должен быть NEW");
    }

    @Test
    void checkStatusEpicWhenAllDone() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask3);
        subtask.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask3);
        Status expectedStatusEpic = Status.DONE;
        Epic testEpic = taskManager.getEpic(epic.getId());
        assertEquals(testEpic.getStatus(), expectedStatusEpic, "Статус должен быть DONE");
    }

    @Test
    void checkStatusEpicWhenNewAndDone() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask3);
        subtask.setStatus(Status.NEW);
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        taskManager.updateSubtask(subtask3);
        Status expectedStatusEpic = Status.IN_PROGRESS;
        Epic testEpic = taskManager.getEpic(epic.getId());
        assertEquals(testEpic.getStatus(), expectedStatusEpic, "Статус должен быть IN_PROGRESS");
    }

    @Test
    void checkStatusEpicWhenAllInProgress() {
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask3);
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        Status expectedStatusEpic = Status.IN_PROGRESS;
        Epic testEpic = taskManager.getEpic(epic.getId());
        assertEquals(testEpic.getStatus(), expectedStatusEpic, "Статус должен быть IN_PROGRESS");
    }

    @Test
    void checkCrossTime() {
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 10, 0);
        task.setDuration(duration);
        task.setStartTime(time);
        time = time.plusHours(1);
        task2.setDuration(duration);
        task2.setStartTime(time);
        subtask.setDuration(duration);
        subtask.setStartTime(time);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        int expectedSizeSortedTask = 2; // subtask не попадет в этот список так как пересечение интервалов
        assertEquals(taskManager.getPrioritizedTasks().size(), expectedSizeSortedTask, "Валидация пересечения интервалов не работает");
    }
}
