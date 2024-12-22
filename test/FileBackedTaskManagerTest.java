import managers.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest {
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
            throw new RuntimeException(e);
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
    void shouldSaveAndLoadEmpty() {
        fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.removeAllTask();//Сохраняю пустой файл
        int test = 0;
        ArrayList<Task> tasks = fileBackedTaskManager.getListTask();
        ArrayList<Epic> epics = fileBackedTaskManager.getListEpic();
        ArrayList<Subtask> subtasks = fileBackedTaskManager.getListSubtask();
        assertEquals(tasks.size(), test, "Число задач не равно нулю");
        assertEquals(epics.size(), test, "Число эпиков не равно нулю");
        assertEquals(subtasks.size(), test, "Число подзадач не равно нулю");//Значит файл пуст
        FileBackedTaskManager fileBackedTaskManagerForLoad = FileBackedTaskManager.loadFromFile(file);
        System.out.println(fileBackedTaskManagerForLoad.toString());
        tasks = fileBackedTaskManagerForLoad.getListTask();
        epics = fileBackedTaskManagerForLoad.getListEpic();
        subtasks = fileBackedTaskManagerForLoad.getListSubtask();
        assertEquals(tasks.size(), test, "Число задач после загрузки не равно нулю");
        assertEquals(epics.size(), test, "Число эпиков после загрузки не равно нулю");
        assertEquals(subtasks.size(), test, "Число подзадач после загрузки не равно нулю");
    }
}
