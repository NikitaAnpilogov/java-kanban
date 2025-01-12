package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getListTask();

    ArrayList<Epic> getListEpic();

    ArrayList<Subtask> getListSubtask();

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubtask();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    ArrayList<Subtask> getSubtasksOfEpic(int id);

    ArrayList<Task> getHistory();

    ArrayList<Task> getPrioritizedTasks();
}
