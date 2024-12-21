package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getListTask() {
        ArrayList<Task> listTask = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                listTask.add(task);
            }
        }
        return listTask;
    }

    @Override
    public ArrayList<Epic> getListEpic() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                listEpic.add(epic);
            }
        }
        return listEpic;
    }

    @Override
    public ArrayList<Subtask> getListSubtask() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks.values()) {
                listSubtask.add(subtask);
            }
        }
        return listSubtask;
    }

    @Override
    public void removeAllTask() {
        for (Integer task : tasks.keySet()) {
            historyManager.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpic() {
        for (Epic epic : epics.values()) {
            ArrayList<Integer> subtasksOfEpic = epic.getSubtasks();
            ArrayList<Integer> subtasksOfEpic2 = new ArrayList<>(List.copyOf(subtasksOfEpic));
            for (Integer id : subtasksOfEpic2) {
                removeSubtask(id);
            }
            epic.removeSubtasks();
        }
        for (Integer ep : epics.keySet()) {
            historyManager.remove(ep);
        }
        epics.clear();
    }

    @Override
    public void removeAllSubtask() {
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            checkStatusEpic(epic.getId());
        }
        Subtask subtask;
        for (Integer sub : subtasks.keySet()) {
            subtask = subtasks.get(sub);
            subtask.removeIdEpic();
            historyManager.remove(sub);
        }
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task returnTask = null;
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) {
                returnTask = tasks.get(id);
            }
        }
        historyManager.add(returnTask);
        return returnTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic returnEpic = null;
        if (!epics.isEmpty()) {
            if (epics.containsKey(id)) {
                returnEpic = epics.get(id);
            }
        }
        historyManager.add(returnEpic);
        return returnEpic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask returnSubtask = null;
        if (!subtasks.isEmpty()) {
            if (subtasks.containsKey(id)) {
                returnSubtask = subtasks.get(id);
            }
        }
        historyManager.add(returnSubtask);
        return returnSubtask;
    }

    @Override
    public int addTask(Task task) {
        int idTask = task.getId();
        tasks.put(idTask, task);
        historyManager.add(task);
        return idTask;
    }

    @Override
    public int addEpic(Epic epic) {
        int idEpic = epic.getId();
        epics.put(idEpic, epic);
        historyManager.add(epic);
        return idEpic;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int idSubtask = subtask.getId();
        subtasks.put(idSubtask, subtask);
        historyManager.add(subtask);
        int idEpic = subtask.getIdEpic();
        Epic epic = epics.get(idEpic);
        epic.addSubtask(subtask);
        checkStatusEpic(idEpic);
        return idSubtask;
    }

    @Override
    public String toString() {
        return "managers.TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subtasks=" + subtasks +
                '}';
    }

    @Override
    public void updateTask(Task task) {
        int idTask = task.getId();
        if (tasks.containsKey(idTask)) {
            tasks.put(idTask, task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int idEpic = epic.getId();
        if (epics.containsKey(idEpic)) {
            epics.put(idEpic, epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int idSubtask = subtask.getId();
        if (subtasks.containsKey(idSubtask)) {
            subtasks.put(idSubtask, subtask);
        }
        int idEpic = subtask.getIdEpic();
        checkStatusEpic(idEpic);
    }

    @Override
    public void removeTask(int id) {
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) {
                historyManager.remove(id);
                tasks.remove(id);
            }
        }
    }

    @Override
    public void removeEpic(int id) {
        if (!epics.isEmpty()) {
            if (epics.containsKey(id)) {
                Epic epic = epics.get(id);
                boolean flag = true;
                ArrayList<Integer> subtask = epic.getSubtasks();
                int numOfSub = subtask.size();
                for (int i = 0; i < numOfSub; i++) {
                    //int idSubEpic = subtask.get(i);
                    //removeSubtask(idSubEpic);
                    removeSubtask(subtask.get(0));
                }
                //epic.removeSubtasks();
                historyManager.remove(id);
                epics.remove(id);
            }
        }
    }

    @Override
    public void removeSubtask(int id) {
        if (!subtasks.isEmpty()) {
            if (subtasks.containsKey(id)) {
                Subtask subtask = subtasks.get(id);
                int idEpic = subtask.getIdEpic();
                Epic epic = epics.get(idEpic);
                epic.removeSubtask(id);
                historyManager.remove(id);
                subtask.removeIdEpic();
                subtasks.remove(id);
                checkStatusEpic(idEpic);
            }
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();
        ArrayList<Integer> listSubtask = epic.getSubtasks();
        for (int i = 0; i < listSubtask.size(); i++) {
            Subtask subtask = subtasks.get(listSubtask.get(i));
            subtaskOfEpic.add(subtask);
        }
        return subtaskOfEpic;
    }

    private void checkStatusEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskOfEpic = epic.getSubtasks();
        int numNewStatus = 0;
        int numInProgressStatus = 0;
        int numDoneStatus = 0;
        for (int i = 0; i < subtaskOfEpic.size(); i++) {
            Subtask subtask = subtasks.get(subtaskOfEpic.get(i));
            if (subtask.getStatus() == Status.NEW) {
                numNewStatus++;
            } else if (subtask.getStatus() == Status.DONE) {
                numDoneStatus++;
            } else {
                numInProgressStatus++;
            }
        }
        if (numDoneStatus == subtaskOfEpic.size()) {
            epic.setStatus(Status.DONE);
        } else if (numNewStatus == subtaskOfEpic.size() || subtaskOfEpic.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}
