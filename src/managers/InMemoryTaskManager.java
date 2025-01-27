package managers;

import exceptions.NotFoundException;
import exceptions.TaskCrossException;
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
        Comparator<Task> comparator = (first, second) -> first.getStartTime().compareTo(second.getStartTime());
        sortedTasks = new TreeSet<>(comparator);
    }

    @Override
    public List<Task> getListTask() {
        return tasks.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Epic> getListEpic() {
        return epics.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Subtask> getListSubtask() {
        return subtasks.values().stream()
                .collect(Collectors.toList());
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
        if (returnTask == null) {
            throw new NotFoundException("Task not found");
        } else {
            historyManager.add(returnTask);
        }
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
        if (returnEpic == null) {
            throw new NotFoundException("Epic not found");
        } else {
            historyManager.add(returnEpic);
        }
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
        if (returnSubtask == null) {
            throw new NotFoundException("Subtask not found");
        } else {
            historyManager.add(returnSubtask);
        }
        return returnSubtask;
    }

    @Override
    public Integer addTask(Task task) {
        Integer idTask = task.getId();
        tasks.put(idTask, task);
        historyManager.add(task);
        try {
            addTaskToSortedTasks(task);
        } catch (TaskCrossException e) { // Обрабатываю ошибку тут, чтобы не обрабатывать каждый addTask на ошибку
            idTask = null; // Проверяю пересечение нулем, если нуль, то задача пересекается
        }
        return idTask;
    }

    @Override
    public Integer addEpic(Epic epic) {
        int idEpic = epic.getId();
        epics.put(idEpic, epic);
        historyManager.add(epic);
        return idEpic;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        Integer idSubtask = subtask.getId();
        subtasks.put(idSubtask, subtask);
        historyManager.add(subtask);
        try {
            addTaskToSortedTasks(subtask);
        } catch (TaskCrossException e) { // Обрабатываю ошибку тут, чтобы не обрабатывать каждый addTask на ошибку
            idSubtask = null; // Проверяю пересечение нулем, если нуль, то задача пересекается
        }
        int idEpic = subtask.getIdEpic();
        checkStatusEpic(idEpic);
        //addTaskToSortedTasks(epics.get(idEpic));
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
    public boolean updateTask(Task task) {
        boolean isCross = false;
        int idTask = task.getId();
        if (tasks.containsKey(idTask)) {
            tasks.put(idTask, task);
            try {
                addTaskToSortedTasks(task);
            } catch (TaskCrossException e) { // Описал логику выше
                isCross = true;
            }
        }
        return isCross;
    }

    @Override
    public void updateEpic(Epic epic) {
        int idEpic = epic.getId();
        if (epics.containsKey(idEpic)) {
            epics.put(idEpic, epic);
        }
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean isCross = false;
        int idSubtask = subtask.getId();
        if (subtasks.containsKey(idSubtask)) {
            subtasks.put(idSubtask, subtask);
            try {
                addTaskToSortedTasks(subtask);
            } catch (TaskCrossException e) { // Описал логику выше
                isCross = true;
            }
        }
        int idEpic = subtask.getIdEpic();
        checkStatusEpic(idEpic);
        //addTaskToSortedTasks(epics.get(idEpic));
        return isCross;
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
    public List<Subtask> getSubtasksOfEpic(int id) {
        if (!epics.containsKey(id)) {
            throw new NotFoundException("Epic not found");
        }
        return subtasks.values().stream()
                .filter(subtask -> (subtask.getIdEpic() == id))
                .collect(Collectors.toList());
    }

    private void checkStatusEpic(int id) {
        Epic epic = epics.get(id);
        List<Subtask> subtaskOfEpic = getSubtasksOfEpic(id);
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void checkStartTimeAndDurationAndEndTime(Epic epic, List<Subtask> subtaskOfEpic) {
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

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
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
            if (sortedTasks.contains(task)) { // Обнаружил, что если мы изменяем задачу и обновляем ее, то она не попадает в sortedTask
                return true; // Перед стримом проверяем таску, если она уже есть в sortedTask, то сразу пропускаем ее
            }
            LocalDateTime startTime = task.getStartTime();
            LocalDateTime endTime = task.getEndTime();
            boolean isCross = sortedTasks.stream()
                    .filter(x -> (endTime.isAfter(x.getStartTime()) && endTime.isBefore(x.getEndTime())) ||
                            (startTime.isAfter(x.getStartTime()) && startTime.isBefore(x.getEndTime())) ||
                            (startTime.isAfter(x.getStartTime()) && endTime.isBefore(x.getEndTime())) ||
                            (startTime.isBefore(x.getStartTime()) && endTime.isAfter(x.getEndTime())) ||
                            (startTime.equals(x.getStartTime())) ||
                            (endTime.equals(x.getEndTime())))
                    .noneMatch(x -> true);
            if (!isCross) { // Решил выбрасывать ошибку, если задачи пересекаются, и обрабатывать ошибку выше
                throw new TaskCrossException("Task is crossing the other tasks");
            }
            return isCross;
        }
    }
}
