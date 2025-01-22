import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task = new Task("Name1", "Description1", Status.NEW);
        epic = new Epic("Name2", "Description2");
        subtask = new Subtask("Name3", "Description3", Status.IN_PROGRESS, epic.getId());
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
    }

    @AfterEach
    void afterEach() {
        inMemoryHistoryManager.remove(task.getId());
        inMemoryHistoryManager.remove(epic.getId());
        inMemoryHistoryManager.remove(subtask.getId());
    }

    @Test
    void shouldAddAndGetHistory() {
        int test1 = 3;
        List<Task> test = inMemoryHistoryManager.getHistory();
        assertNotNull(test, "getHistory не работает");
        assertEquals(test1, test.size(), "Не все задачи отобразились в истории");
        Task tas = test.get(2);
        System.out.println(inMemoryHistoryManager.getHistory());
        assertEquals(tas.getName(), task.getName(), "Неправильный порядок истории в начале, не отобразилась задача");
        tas = test.get(1);
        assertEquals(tas.getName(), epic.getName(), "Неправильный порядок истории, не отобразился эпик");
        tas = test.get(0);
        assertEquals(tas.getName(), subtask.getName(), "Неправильный порядок истории в конце, не отобразилась подзадача");
        inMemoryHistoryManager.add(task);
        test = inMemoryHistoryManager.getHistory();
        tas = test.get(0);
        System.out.println(inMemoryHistoryManager.getHistory());
        assertEquals(tas.getName(), task.getName(), "Неправильный порядок истории, не удалились задачи превышающие лимит");
    }

    @Test
    void shouldCorrectOrderOfHistory() {
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        List<Task> test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 3, "Не удаляет повторы в истории");
        assertEquals(test.getFirst(), epic, "Неправильный порядок истории в начале");
        assertEquals(test.getLast(), subtask, "Неправильный порядок истории в конце");
    }

    @Test
    void shouldRemoveTasksFromHistory() {
        inMemoryHistoryManager.remove(task.getId());
        List<Task> test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 2, "Не удалил задачу из истории");
        inMemoryHistoryManager.remove(epic.getId());
        test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 1, "Не удалил епик из истории");
        inMemoryHistoryManager.remove(subtask.getId());
        test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 0, "Не удалил епик из истории");
    }

    @Test
    void checkEmptyHistory() {
        InMemoryHistoryManager inMemoryHistoryManagerTest = new InMemoryHistoryManager();
        int expectedSizeHistory = 0;
        assertEquals(inMemoryHistoryManagerTest.getHistory().size(), expectedSizeHistory, "История должна быть пуста");
    }

    @Test
    void checkDoubleTaskInHistory() {
        inMemoryHistoryManager.add(task);
        int expectedSizeHistoryWithDouble = 3;
        assertEquals(inMemoryHistoryManager.getHistory().size(), expectedSizeHistoryWithDouble, "Пропустил дубль");
    }

    @Test
    void checkRemoveFirst() {
        inMemoryHistoryManager.remove(subtask.getId());
        String expectedTask = "Name2";
        ArrayList<Task> tasks = new ArrayList<>(inMemoryHistoryManager.getHistory());
        assertEquals(tasks.getFirst().getName(), expectedTask, "Удалил не первый элемент");
    }

    @Test
    void checkRemoveLast() {
        inMemoryHistoryManager.remove(task.getId());
        String expectedTask = "Name2";
        ArrayList<Task> tasks = new ArrayList<>(inMemoryHistoryManager.getHistory());
        assertEquals(tasks.getLast().getName(), expectedTask, "Удалил не последний элемент");
    }

    @Test
    void checkRemoveIntermediate() {
        inMemoryHistoryManager.remove(epic.getId());
        String expectedTask = "Name1";
        ArrayList<Task> tasks = new ArrayList<>(inMemoryHistoryManager.getHistory());
        assertEquals(tasks.get(1).getName(), expectedTask, "Удалил не средний элемент");
    }
}