import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private InMemoryTaskManager inMemoryTaskManager;
    private Subtask test1;
    private Epic test2;
    @BeforeEach
    void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager();
        test2 = new Epic("Name2", "Description2");
        test1 = new Subtask("Name1", "Description1", Status.NEW, test2.getId());
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
    void shouldGetIdEpic() {
        int testCheck = 459191266;
        assertEquals(testCheck, test1.getIdEpic(), "Не работает getIdEpic");
    }
    @Test
    void shouldSetIdEpic() {
        int testCheck = 123456789;
        test1.setIdEpic(123456789);
        assertEquals(testCheck, test1.getIdEpic(), "Не работает setIdEpic");
    }
    @Test
    void shouldSubtaskEqualSubtaskIfIdEquals() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic testEpic = new Epic("Name1", "Description1");
        int testIdEpic = inMemoryTaskManager.addEpic(testEpic);

        Subtask test1 = new Subtask("Name1", "Description1", Status.NEW, testIdEpic);
        Subtask test2 = new Subtask("Name1", "Description1", Status.NEW, testIdEpic);
        int testId1 = inMemoryTaskManager.addSubtask(test1);
        int testId2 = inMemoryTaskManager.addSubtask(test2);
        assertEquals(testId1, testId2, "ID не равны");
        if (testId1 == testId2) {
            assertEquals(test1, test2, "Объекты Subtask не равны");
        }
    }
    @Test
    void shouldSubtaskCantAddInThisSubtaskLikeEpic() { // Сделать подзадачу свим же эиком нельзя
    } // Так как при создании подзадачи требуется ID эпика, ID подзадачи туда поставить нельзя
} // Так как ID подзадачи можно получить только после объявления