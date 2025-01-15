import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Task task = new Task("Name1", "Description1", Status.NEW);
    Epic epic = new Epic("Name2", "Description2");
    Subtask subtask = new Subtask("Name3", "Description3", Status.IN_PROGRESS, epic.getId());
    Task task2 = new Task("Name4", "Description4", Status.IN_PROGRESS);
    Epic epic2 = new Epic("Name5", "Description5");
    Subtask subtask2 = new Subtask("Name6", "Description6", Status.IN_PROGRESS, epic2.getId());

    @Test
    void shouldAddAndGetTask() {
        int taskId = taskManager.addTask(task);
        Task taskTest = taskManager.getTask(taskId);
        assertEquals(taskTest, task, "addAndGetTask не работает");
    }

    @Test
    void shouldUpdateTaskAndGetListTask() {
        int taskId = taskManager.addTask(task);
        Task taskTest = taskManager.getTask(taskId);
        taskTest.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(taskTest);
        List<Task> test = taskManager.getListTask();
        assertNotNull(test, "getListTask не работает");
        assertEquals(test.size(), 1, "Задача не обновилась, а добавилась как дубль");
        Task taskTest2 = test.get(0);
        assertEquals(taskTest2.getStatus(), Status.IN_PROGRESS, "Задача не обновилась");
    }

    @Test
    void shouldRemoveTask() {
        taskManager.removeTask(task.getId());
        List<Task> taskTest = taskManager.getListTask();
        assertEquals(taskTest.size(), 0, "removeTask не работает");
    }

    @Test
    void shouldRemoveAllTask() {
        int taskId = taskManager.addTask(task);
        int taskId2 = taskManager.addTask(task2);
        taskManager.removeAllTask();
        List<Task> test = taskManager.getListTask();
        assertEquals(test.size(), 0, "removeAllTask не работает");
    }

    @Test
    void shouldAddAndGetEpic() {
        int EpicId = taskManager.addEpic(epic);
        Epic epicTest = taskManager.getEpic(EpicId);
        assertEquals(epicTest, epic, "addAndGetEpic не работает");
    }

    @Test
    void shouldUpdateEpicAndGetListEpic() {
        int epicId = taskManager.addEpic(epic);
        Epic epicTest = taskManager.getEpic(epicId);
        epicTest.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epicTest);
        List<Epic> test = taskManager.getListEpic();
        assertNotNull(test, "getListEpic не работает");
        assertEquals(test.size(), 1, "Эпик не обновился, а добавился как дубль");
        Epic epicTest2 = test.get(0);
        assertEquals(epicTest2.getStatus(), Status.IN_PROGRESS, "Эпик не обновился");
    }

    @Test
    void shouldRemoveEpic() {
        taskManager.removeAllSubtask();
        InMemoryTaskManager taskManager1 = new InMemoryTaskManager();
        int epicId = taskManager1.addEpic(epic);
        int subtaskId = taskManager1.addSubtask(subtask);
        taskManager1.removeEpic(epic.getId());
        List<Epic> epicTest = taskManager1.getListEpic();
        assertEquals(epicTest.size(), 0, "removeEpic не работает");
        List<Subtask> test = taskManager1.getListSubtask();
        assertEquals(test.size(), 0, "Не убирает подзадачи, которые связаны с эпиком");

    }

    @Test
    void shouldRemoveAllEpic() {
        InMemoryTaskManager taskManager1 = new InMemoryTaskManager();
        int epicId = taskManager1.addEpic(epic);
        int epicId2 = taskManager1.addEpic(epic2);
        taskManager1.removeAllEpic();
        List<Epic> test = taskManager1.getListEpic();
        assertEquals(test.size(), 0, "removeAllEpic не работает");
    }

    @Test
    void shouldAddAndGetSubtask() {
        int epicId = taskManager.addEpic(epic);
        int subtaskId = taskManager.addSubtask(subtask);
        Subtask subtaskTest = taskManager.getSubtask(subtaskId);
        assertEquals(subtaskTest, subtask, "addAndGetSubtask не работает");
    }

    @Test
    void shouldUpdateSubtaskAndGetListSubtask() {
        int epicId = taskManager.addEpic(epic);
        int subtaskId = taskManager.addSubtask(subtask);
        Subtask subtaskTest = taskManager.getSubtask(subtaskId);
        subtaskTest.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtaskTest);
        List<Subtask> test = taskManager.getListSubtask();
        assertNotNull(test, "getListSubtask не работает");
        assertEquals(test.size(), 1, "Подзадача не обновилась, а добавилась как дубль");
        Subtask subtaskTest2 = test.get(0);
        assertEquals(subtaskTest2.getStatus(), Status.IN_PROGRESS, "Подзадача не обновилась");
    }

    @Test
    void shouldRemoveSubtask() {
        taskManager.removeSubtask(subtask.getId());
        //Subtask subtaskTest = taskManager.getSubtask(subtask.getId());
        List<Subtask> subtaskTest = taskManager.getListSubtask();
        //assertNull(subtaskTest, "removeSubtask не работает");
        assertEquals(subtaskTest.size(), 0, "removeSubtask не работает");
    }

    @Test
    void shouldRemoveAllSubtask() {
        int subtaskId = taskManager.addTask(subtask);
        int subtaskId2 = taskManager.addTask(subtask2);
        taskManager.removeAllSubtask();
        List<Subtask> test = taskManager.getListSubtask();
        assertEquals(test.size(), 0, "removeAllSubtask не работает");
    }

    @Test
    void shouldGetSubtaskOfEpic() {
        taskManager = new InMemoryTaskManager();
        int epicId = taskManager.addEpic(epic);
        int subId = taskManager.addSubtask(subtask);
        List<Subtask> test = taskManager.getSubtasksOfEpic(epicId);
        assertEquals(test.size(), 1, "getSubtaskOfEpic не работает");
    }

    @Test
    void shouldCheckStatusEpic() {
        taskManager = new InMemoryTaskManager();
        int epicId = taskManager.addEpic(epic);
        int subId = taskManager.addSubtask(subtask);
        Subtask subtask3 = new Subtask("Name7", "Description7", Status.IN_PROGRESS, epic.getId());
        int sub3Id = taskManager.addSubtask(subtask3);
        Epic test = taskManager.getEpic(epicId);
        assertEquals(Status.IN_PROGRESS, test.getStatus(), "IN_PROGRESS должен быть");
        Subtask subTest = taskManager.getSubtask(subId);
        subTest.setStatus(Status.NEW);
        taskManager.updateSubtask(subTest);
        subTest = taskManager.getSubtask(sub3Id);
        subTest.setStatus(Status.NEW);
        taskManager.updateSubtask(subTest);
        assertEquals(Status.NEW, test.getStatus(), "NEW должен быть");
        subTest = taskManager.getSubtask(subId);
        subTest.setStatus(Status.DONE);
        taskManager.updateSubtask(subTest);
        subTest = taskManager.getSubtask(sub3Id);
        subTest.setStatus(Status.DONE);
        taskManager.updateSubtask(subTest);
        assertEquals(Status.DONE, test.getStatus(), "DONE должен быть");
    }

    @Test
    public void checkUnchangeableOfTask() {
        taskManager = new InMemoryTaskManager();
        Task expectedTask = new Task("Name1", "Description1", Status.NEW);
        int testId1 = taskManager.addTask(expectedTask);
        Task actualTask = taskManager.getTask(testId1);
        assertEquals(expectedTask, actualTask, "Добавляемая задача отличается от той, что была в итоге добавлена");
    }

    @Test
    public void checkHistoryCorrect() {
        taskManager = new InMemoryTaskManager();
        Task testTask = new Task("Name1", "Description1", Status.NEW);
        int testId1 = taskManager.addTask(testTask);
        Task expectedTask1 = taskManager.getTask(testId1);
        testTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(testTask);
        Task expectedTask2 = taskManager.getTask(testId1);
        List<Task> history = taskManager.getHistory();
        assertEquals(expectedTask2, history.get(0), "Последняя добавленная задача имеет отличающиеся параметры");
        //assertEquals(expectedTask1, history.get(0), "Первая добавленная задача имеет отличающиеся параметры");
    }

    @Test
    void shouldRemoveIdEpicInSubtask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubtask(subtask);
        inMemoryTaskManager.removeSubtask(subtask.getId());
        assertNull(subtask.getIdEpic(), "Не удаляет ID эпика");
    }

    @Test
    void checkCrossTime1() {
        taskManager = new InMemoryTaskManager();
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 10, 0);
        task.setDuration(duration);
        task.setStartTime(time);
        duration = Duration.ofMinutes(10);
        time = time.plus(duration);
        task2.setDuration(duration);
        task2.setStartTime(time);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        List sortedTasks = taskManager.getPrioritizedTasks();
        int expectedSize = 1;
        System.out.println(task);
        System.out.println(task2);
        System.out.println(taskManager);
        assertEquals(taskManager.getPrioritizedTasks().size(), expectedSize, "Задача внутри другой задачи");
        taskManager = new InMemoryTaskManager();
        task.setStartTime(time);
        task.setDuration(duration);
        time = time.minus(duration);
        duration = Duration.ofMinutes(30);
        task2.setDuration(duration);
        task2.setStartTime(time);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        System.out.println(taskManager);
        assertEquals(taskManager.getPrioritizedTasks().size(), expectedSize, "Снаружи");
    }

    @Test
    void checkCrossTime2() {
        LocalDateTime time1 = LocalDateTime.of(2000, 1, 1, 10, 0);
        Duration duration1 = Duration.ofMinutes(30);
        LocalDateTime time2 = LocalDateTime.of(2000, 1, 1, 10, 10);
        LocalDateTime time3 = LocalDateTime.of(2000, 1, 1, 9, 40);
        task.setStartTime(time1);
        task.setDuration(duration1);
        task2.setStartTime(time2);
        task2.setDuration(duration1);
        taskManager = new InMemoryTaskManager();
        taskManager.addTask(task);
        taskManager.addTask(task2);
        int expectedSize = 1;
        System.out.println(taskManager);
        assertEquals(expectedSize, taskManager.getPrioritizedTasks().size(), "После");
        task2.setStartTime(time3);
        taskManager = new InMemoryTaskManager();
        taskManager.addTask(task);
        taskManager.addTask(task2);
        System.out.println(taskManager);
        assertEquals(expectedSize, taskManager.getPrioritizedTasks().size(), "Перед");
    }

    @Test
    void checkAddDefaultTimeInSortedTasks() {
        taskManager = new InMemoryTaskManager();
        Task taskTest = new Task("name", "desc");
        taskManager.addTask(taskTest);
        int expectedSize = 0;
        assertEquals(expectedSize, taskManager.getPrioritizedTasks().size(), "пропустил default");
    }
}