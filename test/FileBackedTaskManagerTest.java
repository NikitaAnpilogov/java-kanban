import managers.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private File file;

    @BeforeEach
    void setUp() {
        task = new Task("NameTask", "DescriptionTask", Status.IN_PROGRESS);
        epic = new Epic("NameEpic", "DescriptionEpic");
        subtask = new Subtask("NameSubtask", "DescriptinSubtask", Status.NEW, epic.getId());
        try {
            file = File.createTempFile("Test", ".CSV");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    @Test
    void shouldSaveAndLoadTasks() {
        fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubtask(subtask);
        System.out.println(fileBackedTaskManager.toString());
        FileBackedTaskManager fileBackedTaskManagerForLoad = FileBackedTaskManager.loadFromFile(file);
        System.out.println(fileBackedTaskManagerForLoad.toString());
        assertNotNull(fileBackedTaskManagerForLoad, "Файл не загрузился");
        assertEquals(fileBackedTaskManager.toString(), fileBackedTaskManagerForLoad.toString(), "Не сохранился или не загрузился файл");
    }

    @Test
    void shouldSaveEmpty() {
        fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.removeAllTask();//Сохраняю пустой файл
        int test = 0;
        List<Task> tasks = fileBackedTaskManager.getListTask();
        List<Epic> epics = fileBackedTaskManager.getListEpic();
        List<Subtask> subtasks = fileBackedTaskManager.getListSubtask();
        assertEquals(tasks.size(), test, "Число задач не равно нулю");
        assertEquals(epics.size(), test, "Число эпиков не равно нулю");
        assertEquals(subtasks.size(), test, "Число подзадач не равно нулю");//Значит файл пуст
    }

    @Test
    void shouldLoadEmpty() {
        fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.removeAllTask();//Сохраняю пустой файл
        int test = 0;
        FileBackedTaskManager fileBackedTaskManagerForLoad = FileBackedTaskManager.loadFromFile(file);
        System.out.println(fileBackedTaskManagerForLoad.toString());
        List<Task> tasks = fileBackedTaskManagerForLoad.getListTask();
        List<Epic> epics = fileBackedTaskManagerForLoad.getListEpic();
        List<Subtask> subtasks = fileBackedTaskManagerForLoad.getListSubtask();
        assertEquals(tasks.size(), test, "Число задач после загрузки не равно нулю");
        assertEquals(epics.size(), test, "Число эпиков после загрузки не равно нулю");
        assertEquals(subtasks.size(), test, "Число подзадач после загрузки не равно нулю");
    }

    @Test
    void testException() {
        Exception exception = assertThrows(ManagerSaveException.class, () -> {
            throw new ManagerSaveException("Exception");
        });
        assertEquals("Exception", exception.getMessage());
    }
}