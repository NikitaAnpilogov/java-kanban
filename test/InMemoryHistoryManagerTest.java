import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void shouldAddAndGetHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task = new Task("Name1", "Description1", Status.NEW);
        Epic epic = new Epic("Name2", "Description2");
        Subtask subtask = new Subtask("Name3", "Description3", Status.IN_PROGRESS, epic.getId());
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        int test1 = 3;
        ArrayList<Task> test = inMemoryHistoryManager.getHistory();
        assertNotNull(test, "getHistory не работает");
        assertEquals(test1, test.size(), "Не все задачи отобразились в истории");
        Task tas = test.get(2);
        System.out.println(inMemoryHistoryManager.getHistory());
        assertEquals(tas.getName(), task.getName(), "Неправильный порядок истории в начале, не отобразилась задача");
        tas = test.get(1);
        assertEquals(tas.getName(), epic.getName(), "Неправильный порядок истории, не отобразился эпик");
        tas = test.get(0);
        assertEquals(tas.getName(), subtask.getName(), "Неправильный порядок истории в конце, не отобразилась подзадача");
        /*for (int i = 0; i < 3; i++) {
            inMemoryHistoryManager.add(task);
            inMemoryHistoryManager.add(epic);
            inMemoryHistoryManager.add(subtask);
        }
        test = inMemoryHistoryManager.getHistory();
        int test2 = 10;
        assertEquals(test2, test.size(), "Неправильный размер истории, должно быть 10");*/
        inMemoryHistoryManager.add(task);
        test = inMemoryHistoryManager.getHistory();
        tas = test.get(0);
        System.out.println(inMemoryHistoryManager.getHistory());
        assertEquals(tas.getName(), task.getName(), "Неправильный порядок истории, не удалились задачи превышающие лимит");
    }

    @Test
    void shouldCorrectOrderOfHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task = new Task("Name1", "Description1", Status.NEW);
        Epic epic = new Epic("Name2", "Description2");
        Subtask subtask = new Subtask("Name3", "Description3", Status.IN_PROGRESS, epic.getId());
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        ArrayList<Task> test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 3, "Не удаляет повторы в истории");
        assertEquals(test.getFirst(), epic, "Неправильный порядок истории в начале");
        assertEquals(test.getLast(), subtask, "Неправильный порядок истории в конце");
    }

    @Test
    void shouldRemoveTasksFromHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task = new Task("Name1", "Description1", Status.NEW);
        Epic epic = new Epic("Name2", "Description2");
        Subtask subtask = new Subtask("Name3", "Description3", Status.IN_PROGRESS, epic.getId());
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(task.getId());
        ArrayList<Task> test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 2, "Не удалил задачу из истории");
        inMemoryHistoryManager.remove(epic.getId());
        test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 1, "Не удалил епик из истории");
        inMemoryHistoryManager.remove(subtask.getId());
        test = inMemoryHistoryManager.getHistory();
        assertEquals(test.size(), 0, "Не удалил епик из истории");
    }
}