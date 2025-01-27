package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.List;

public interface TaskManager {
    List<Task> getListTask();

    List<Epic> getListEpic();

    List<Subtask> getListSubtask();

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubtask();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    Integer addTask(Task task);

    Integer addEpic(Epic epic);

    Integer addSubtask(Subtask subtask);

    boolean updateTask(Task task);

    void updateEpic(Epic epic);

    boolean updateSubtask(Subtask subtask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    List<Subtask> getSubtasksOfEpic(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
