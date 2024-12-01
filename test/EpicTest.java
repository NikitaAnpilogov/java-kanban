import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private InMemoryTaskManager inMemoryTaskManager;
    private Epic test1;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager();
        test1 = new Epic("Name1", "Description1");
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
        String testCheck = "tasks.Epic{name='Name1', status=NEW, id=459190274, description='Description1', subtasksId=[]}";
        assertEquals(testCheck, test1.toString(), "Не работает toString");
    }

    @Test
    void shouldAddSubtaskAndGetSubtask() { // По отдельности проверить  не получится
        Subtask subtask = new Subtask("Name2", "Description2", Status.IN_PROGRESS, test1.getId());
        test1.addSubtask(subtask);
        ArrayList<Integer> subtasks = test1.getSubtasks();
        ArrayList<Integer> testCheck = new ArrayList<>();
        testCheck.add(459191266);
        assertEquals(testCheck, subtasks, "Не работают addSubtaskAndGetSubtask");
    }

    @Test
    void shouldRemoveSubtask() {
        Subtask subtask = new Subtask("Name2", "Description2", Status.IN_PROGRESS, test1.getId());
        test1.addSubtask(subtask);
        test1.removeSubtask(subtask.getId());
        ArrayList<Integer> subtasks = test1.getSubtasks();
        ArrayList<Integer> testCheck = new ArrayList<>();
        assertEquals(testCheck, subtasks, "Не работает removeSubtask");
    }

    @Test
    void shouldRemoveSubtasks() {
        Subtask subtask = new Subtask("Name2", "Description2", Status.IN_PROGRESS, test1.getId());
        test1.addSubtask(subtask);
        test1.removeSubtasks();
        ArrayList<Integer> subtasks = test1.getSubtasks();
        ArrayList<Integer> testCheck = new ArrayList<>();
        assertEquals(testCheck, subtasks, "Не работает removeSubtasks");
    }

    @Test
    void shouldEpicEqualEpicIfIdEquals() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic test1 = new Epic("Name1", "Description1");
        Epic test2 = new Epic("Name1", "Description1");
        int testId1 = inMemoryTaskManager.addEpic(test1);
        int testId2 = inMemoryTaskManager.addEpic(test2);
        assertEquals(testId1, testId2, "ID не равны");
        if (testId1 == testId2) {
            assertEquals(test1, test2, "Объекты tasks.Epic не равны");
        }
    }

    @Test
    void shouldEpicCantAddInThisEpicLikeSubtask() { // Добавить Эпик в этот же эпик в виде подзадачи
    } // В этой реализации программы нельзя, так как мы присвиваем эпику подзадачу во время
} // Создания подзадачи, и для ее создания нам нужен ID созданного эпика