import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    public TaskManager () {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public ArrayList<Task> getListTask() {
        ArrayList<Task> listTask = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                listTask.add(task);
            }
        }
        return listTask;
    }
    public ArrayList<Epic> getListEpic() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                listEpic.add(epic);
            }
        }
        return listEpic;
    }
    public ArrayList<Subtask> getListSubtask() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks.values()) {
                listSubtask.add(subtask);
            }
        }
        return listSubtask;
    }
    public void removeAllTask () {
        tasks.clear();
    }
    public void removeAllEpic () {
        for (Epic epic : epics.values()) {
            ArrayList<Integer> subtasksOfEpic = epic.getSubtasks();
            for (Integer id : subtasksOfEpic) {
                removeSubtask(id);
            }
            epic.removeSubtasks();
        }
        epics.clear();
    }
    public void removeAllSubtask () {
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            checkStatusEpic(epic.getId());
        }
        subtasks.clear();
    }
    public Task getTask (int id) {
        Task returnTask = null;
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) {
                returnTask = tasks.get(id);
            }
            /*for (Integer idKey : tasks.keySet()) {
                if (idKey == id) {
                    returnTask = tasks.get(idKey);
                }
            }*/
        }
        return returnTask;
    }
    public Epic getEpic (int id) {
        Epic returnEpic = null;
        if (!epics.isEmpty()) {
            if (epics.containsKey(id)) {
                returnEpic = epics.get(id);
            }
            /*for (Integer idKey : epics.keySet()) {
                if (idKey == id) {
                    returnEpic = epics.get(idKey);
                }
            }*/
        }
        return returnEpic;
    }
    public Subtask getSubtask (int id) {
        Subtask returnSubtask = null;
        if (!subtasks.isEmpty()) {
            if (subtasks.containsKey(id)) {
                returnSubtask = subtasks.get(id);
            }
            /*for (Integer idKey : subtasks.keySet()) {
                if (idKey == id) {
                    returnSubtask = subtasks.get(idKey);
                }
            }*/
        }
        return returnSubtask;
    }
    public int addTask (Task task) {
        int idTask = task.getId();
        tasks.put(idTask, task);
        return idTask;
    }
    public int addEpic (Epic epic) {
        int idEpic = epic.getId();
        epics.put(idEpic, epic);
        return idEpic;
    }
    public int addSubtask (Subtask subtask) {
        int idSubtask = subtask.getId();
        subtasks.put(idSubtask, subtask);
        int idEpic = subtask.getIdEpic();
        Epic epic = epics.get(idEpic);
        epic.addSubtask(subtask);
        checkStatusEpic(idEpic);
        return idSubtask;
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subtasks=" + subtasks +
                '}';
    }

    public void updateTask (Task task) {
        int idTask = task.getId();
        if (tasks.containsKey(idTask)) {
            tasks.put(idTask, task);
        }
    }
    public void updateEpic (Epic epic) {
        int idEpic = epic.getId();
        if (epics.containsKey(idEpic)) {
            epics.put(idEpic, epic);
        }
    }
    public void updateSubtask (Subtask subtask) {
        int idSubtask = subtask.getId();
        if (subtasks.containsKey(idSubtask)) {
            subtasks.put(idSubtask, subtask);
        }
        int idEpic = subtask.getIdEpic();
        checkStatusEpic(idEpic);
    }
    public void removeTask (int id) {
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) {
                tasks.remove(id);
            }
        }
    }
    public void removeEpic (int id) {
        if (!epics.isEmpty()) {
            if (epics.containsKey(id)) {
                Epic epic = epics.get(id);
                ArrayList<Integer> subtask = epic.getSubtasks();
                for (int i = 0; i < subtask.size(); i++) {
                    int idSubEpic = subtask.get(i);
                    removeSubtask(idSubEpic);
                }
                epic.removeSubtasks();
                epics.remove(id);
            }
        }
    }
    public void removeSubtask (int id) {
        if (!subtasks.isEmpty()) {
            if (subtasks.containsKey(id)) {
                Subtask subtask = subtasks.get(id);
                int idEpic = subtask.getIdEpic();
                Epic epic = epics.get(idEpic);
                epic.removeSubtask(id);
                subtasks.remove(id);
                checkStatusEpic(idEpic);
            }
        }
    }
    public ArrayList<Subtask> getSubtaskOfEpic (int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();
        ArrayList<Integer> listSubtask = epic.getSubtasks();
        for (int i = 0; i < listSubtask.size(); i++) {
            Subtask subtask = subtasks.get(listSubtask.get(i));
            subtaskOfEpic.add(subtask);
        }
        return subtaskOfEpic;
    }
    private void checkStatusEpic (int id) {
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


}
