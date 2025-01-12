package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private HistoryManager historyManager;
    private TreeSet<Task> sortedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        Comparator<Task> comparator = new Comparator<>() {
            @Override
            public int compare(Task firstTask, Task secondTask) {
                if (firstTask.getStartTime().isBefore(secondTask.getStartTime())) {
                    return (-1);
                } else {
                    return 1;
                }
            }
        };
        sortedTasks = new TreeSet<>(comparator);
    }

    @Override
    public ArrayList<Task> getListTask() {
        /*ArrayList<Task> listTask = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                listTask.add(task);
            }
        }
        return listTask;*/
        List<Task> listTask = tasks.values().stream()
                .collect(Collectors.toList());
        return (ArrayList<Task>) listTask;
    }

    @Override
    public ArrayList<Epic> getListEpic() {
        /*ArrayList<Epic> listEpic = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                listEpic.add(epic);
            }
        }
        return listEpic;*/
        List<Epic> listEpic = epics.values().stream()
                .collect(Collectors.toList());
        return (ArrayList<Epic>) listEpic;
    }

    @Override
    public ArrayList<Subtask> getListSubtask() {
        /*ArrayList<Subtask> listSubtask = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks.values()) {
                listSubtask.add(subtask);
            }
        }
        return listSubtask;*/
        List<Subtask> listSubtask = subtasks.values().stream()
                .collect(Collectors.toList());
        return (ArrayList<Subtask>) listSubtask;
    }

    @Override
    public void removeAllTask() {
        for (Integer task : tasks.keySet()) {
            historyManager.remove(task);
            Task newTask = tasks.get(task);
            removeTaskFromSortedTasks(newTask);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpic() {
        removeAllSubtask();
        for (Integer ep : epics.keySet()) {
            historyManager.remove(ep);
            Epic epic = epics.get(ep);
            //removeTaskFromSortedTasks(epic);
        }
        epics.clear();
    }

    @Override
    public void removeAllSubtask() {
        Subtask subtask;
        for (Integer sub : subtasks.keySet()) {
            subtask = subtasks.get(sub);
            subtask.removeIdEpic();
            historyManager.remove(sub);
            removeTaskFromSortedTasks(subtask);
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
        addTaskToSortedTasks(task);
        return idTask;
    }

    @Override
    public int addEpic(Epic epic) {
        int idEpic = epic.getId();
        epics.put(idEpic, epic);
        historyManager.add(epic);
        //addTaskToSortedTasks(epic);
        return idEpic;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int idSubtask = subtask.getId();
        subtasks.put(idSubtask, subtask);
        historyManager.add(subtask);
        addTaskToSortedTasks(subtask);
        int idEpic = subtask.getIdEpic();
        //Epic epic = epics.get(idEpic);
        //epic.addSubtask(subtask);
        checkStatusEpic(idEpic);
        addTaskToSortedTasks(epics.get(idEpic));
        return idSubtask;
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "tasks=" + tasks +
                ", epics=" + epics +
                ", subtasks=" + subtasks +
                ", historyManager=" + historyManager +
                ", sortedTasks=" + sortedTasks +
                '}';
    }

    @Override
    public void updateTask(Task task) {
        int idTask = task.getId();
        if (tasks.containsKey(idTask)) {
            tasks.put(idTask, task);
            addTaskToSortedTasks(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int idEpic = epic.getId();
        if (epics.containsKey(idEpic)) {
            epics.put(idEpic, epic);
            //addTaskToSortedTasks(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int idSubtask = subtask.getId();
        if (subtasks.containsKey(idSubtask)) {
            subtasks.put(idSubtask, subtask);
            addTaskToSortedTasks(subtask);
        }
        int idEpic = subtask.getIdEpic();
        checkStatusEpic(idEpic);
        addTaskToSortedTasks(epics.get(idEpic));
    }

    @Override
    public void removeTask(int id) {
        if (!tasks.isEmpty()) {
            if (tasks.containsKey(id)) {
                historyManager.remove(id);
                removeTaskFromSortedTasks(tasks.get(id));
                tasks.remove(id);
            }
        }
    }

    @Override
    public void removeEpic(int id) {
        if (!epics.isEmpty()) {
            if (epics.containsKey(id)) {
                for (Subtask subtask : subtasks.values()) {
                    if (subtask.getIdEpic() == id) {
                        removeSubtask(subtask.getId());
                    }
                }
                historyManager.remove(id);
                //removeTaskFromSortedTasks(epics.get(id));
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
                historyManager.remove(id);
                subtask.removeIdEpic();
                removeTaskFromSortedTasks(subtasks.get(id));
                subtasks.remove(id);
                checkStatusEpic(idEpic);
                removeTaskFromSortedTasks(epics.get(id));
            }
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(int id) {
        /*Epic epic = epics.get(id);
        ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getIdEpic() == id) {
                subtaskOfEpic.add(subtask);
            }
        }
        return subtaskOfEpic;*/
        List<Subtask> subtaskOfEpic = subtasks.values().stream()
                .filter(subtask -> (subtask.getIdEpic() == id))
                .collect(Collectors.toList());
        return (ArrayList<Subtask>) subtaskOfEpic;
    }

    private void checkStatusEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtaskOfEpic = getSubtasksOfEpic(id);
        int numNewStatus = 0;
        int numInProgressStatus = 0;
        int numDoneStatus = 0;
        for (int i = 0; i < subtaskOfEpic.size(); i++) {
            Subtask subtask = subtaskOfEpic.get(i);
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
        checkStartTimeAndDurationAndEndTime(epic, subtaskOfEpic);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void checkStartTimeAndDurationAndEndTime(Epic epic, ArrayList<Subtask> subtaskOfEpic) {
        LocalDateTime startTimeEpic = LocalDateTime.MAX;
        Duration generalDuration = Duration.ZERO;
        LocalDateTime startTimeSub;
        Duration durationSub;
        for (int i = 0; i < subtaskOfEpic.size(); i++) {
            Subtask subtask = subtaskOfEpic.get(i);
            startTimeSub = subtask.getStartTime();
            durationSub = subtask.getDuration();
            if (startTimeSub.isBefore(startTimeEpic)) {
                startTimeEpic = startTimeSub;
            }
            generalDuration = generalDuration.plus(durationSub);
        }
        epic.setStartTime(startTimeEpic);
        epic.setDuration(generalDuration);
        LocalDateTime endTime = startTimeEpic.plus(generalDuration);
        epic.setEndTime(endTime);
    }

    public ArrayList<Task> getPrioritizedTasks() {
        ArrayList<Task> prioritizedTasks = new ArrayList<>(sortedTasks);
        return prioritizedTasks;
    }

    private void addTaskToSortedTasks(Task task) {
        LocalDateTime startTime = task.getStartTime();
        if (!startTime.equals(LocalDateTime.MAX)) { // LocalDateTime.MAX = default
            if (checkCrossTime(task)) {
                sortedTasks.add(task);
            }
        }
    }

    private void removeTaskFromSortedTasks(Task task) {
        if (sortedTasks.contains(task)) {
            sortedTasks.remove(task);
        }
    }

    private boolean checkCrossTime(Task task) {
        if (task.getStartTime().equals(LocalDateTime.MAX) || task.getDuration().equals(Duration.ZERO)) {
            return false;
        } else {
            LocalDateTime startTime = task.getStartTime();
            LocalDateTime endTime = task.getEndTime();
            long crossTasks = sortedTasks.stream()
                    .filter(sortedTask -> !(sortedTask.getStartTime().isAfter(startTime) && endTime.isBefore(sortedTask.getEndTime())))
                    .filter(sortedTask -> !(sortedTask.getEndTime().isBefore(endTime) && startTime.isAfter(sortedTask.getStartTime())))
                    .filter(sortedTask -> !(startTime.isAfter(sortedTask.getStartTime()) && endTime.isBefore(sortedTask.getEndTime())))
                    .filter(sortedTask -> !(sortedTask.getStartTime().isAfter(startTime) && sortedTask.getEndTime().isBefore(endTime)))
                    .count();
            if (crossTasks > 0) {
                return false;
            } else {
                return true;
            }
        }
    }
}
