import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        String testCheck = "Task{name='Name1', description='Description1', id=459190274, status=NEW}";
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
            assertEquals(test1, test2, "Объекты Task не равны");
        }
    }
}