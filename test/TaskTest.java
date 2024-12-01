import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private InMemoryTaskManager inMemoryTaskManager;
    private Task test1;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager();
        test1 = new Task("Name1", "Description1", Status.NEW);
    }

    @Test
    void shouldGetName() {
        String testCheck = "Name1";
        assertEquals(testCheck, test1.getName(), "Не работает getName");
    }

    @Test
    void shouldSetName() {
        String testCheck = "Name2";
        test1.setName("Name2");
        assertEquals(testCheck, test1.getName(), "Не работает setName");
    }

    @Test
    void shouldGetDescription() {
        String testCheck = "Description1";
        assertEquals(testCheck, test1.getDescription(), "Не работает getDescription");
    }

    @Test
    void shouldSetDescription() {
        String testCheck = "Description2";
        test1.setDescription("Description2");
        assertEquals(testCheck, test1.getDescription(), "Не работает setDescription");
    }

    @Test
    void shouldGetStatus() {
        Status testCheck = Status.NEW;
        assertEquals(testCheck, test1.getStatus(), "Не работает getStatus");
    }

    @Test
    void shouldSetStatus() {
        Status testCheck = Status.IN_PROGRESS;
        test1.setStatus(Status.IN_PROGRESS);
        assertEquals(testCheck, test1.getStatus(), "Не работает setStatus");
    }

    @Test
    void shouldTestToString() {
        String testCheck = "tasks.Task{name='Name1', description='Description1', id=459190274, status=NEW}";
        assertEquals(testCheck, test1.toString(), "Не работает toString");
    }

    @Test
    void shouldGetId() {
        int testCheck = 459190274;
        assertEquals(testCheck, test1.getId(), "Не работает getId");
    }

    @Test
    void shouldTaskEqualTaskIfIdEquals() {
        inMemoryTaskManager = new InMemoryTaskManager();
        test1 = new Task("Name1", "Description1", Status.NEW);
        Task test2 = new Task("Name1", "Description1", Status.NEW);
        int testId1 = inMemoryTaskManager.addTask(test1);
        int testId2 = inMemoryTaskManager.addTask(test2);
        assertEquals(testId1, testId2, "ID не равны");
        if (testId1 == testId2) {
            assertEquals(test1, test2, "Объекты tasks.Task не равны");
        }
    }

    @Test
    public void checkUnchangeableOfTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task expectedTask = new Task("Name1", "Description1", Status.NEW);
        int testId1 = taskManager.addTask(expectedTask);
        Task actualTask = taskManager.getTask(testId1);
        assertEquals(expectedTask, actualTask, "Добавляемая задача отличается от той, что была в итоге добавлена");
    }

    @Test
    public void checkHistoryCorrect() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task testTask = new Task("Name1", "Description1", Status.NEW);
        int testId1 = taskManager.addTask(testTask);
        Task expectedTask1 = taskManager.getTask(testId1);
        testTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(testTask);
        Task expectedTask2 = taskManager.getTask(testId1);
        ArrayList<Task> history = taskManager.getHistory();
        assertEquals(expectedTask2, history.get(0), "Последняя добавленная задача имеет отличающиеся параметры");
        //assertEquals(expectedTask1, history.get(0), "Первая добавленная задача имеет отличающиеся параметры");
    }

    @Test
    void conflictId() { // "Нет тестов на конфликты между заданным и сгенерированным id" в этой программе нет возможности
    } // задавать ID вручную, они генерируются и задаются автоматически при создании объекта в зависимости от хеша, поэтому
} // сделать тест на конфликт заданного и сгенерированного ID нельзя. Если я чего-то не понимаю, прошу объясните подробнее