import org.junit.jupiter.api.Test;

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
        Task tas = test.get(0);
        assertEquals(tas.getName(), task.getName(), "Неправильный порядок истории в начале, не отобразилась задача");
        tas = test.get(1);
        assertEquals(tas.getName(), epic.getName(), "Неправильный порядок истории, не отобразился эпик");
        tas = test.get(2);
        assertEquals(tas.getName(), subtask.getName(), "Неправильный порядок истории в конце, не отобразилась подзадача");
        for (int i = 0; i < 3; i++) {
            inMemoryHistoryManager.add(task);
            inMemoryHistoryManager.add(epic);
            inMemoryHistoryManager.add(subtask);
        }
        test = inMemoryHistoryManager.getHistory();
        int test2 = 10;
        assertEquals(test2, test.size(), "Неправильный размер истории, должно быть 10");
        tas = test.get(0);
        assertEquals(tas.getName(), subtask.getName(), "Неправильный порядок истории, не удалились задачи превышающие лимит");
    }

}