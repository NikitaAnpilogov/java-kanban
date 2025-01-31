package managers;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File fileForSave;

    public FileBackedTaskManager(String dir, String name) {
        fileForSave = new File(dir, name);
    }

    public FileBackedTaskManager(File file) {
        fileForSave = file;
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    @Override
    public Integer addTask(Task task) {
        Integer answer = super.addTask(task);
        save();
        return answer;
    }

    @Override
    public Integer addEpic(Epic epic) {
        Integer answer = super.addEpic(epic);
        save();
        return answer;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        Integer answer = super.addSubtask(subtask);
        save();
        return answer;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean isCross = super.updateTask(task);
        save();
        return isCross;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean isCross = super.updateSubtask(subtask);
        save();
        return isCross;
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            for (String line : lines) {
                Task task = fileBackedTaskManager.fromString(line);
                switch (task.getType()) {
                    case Type.TASK:
                        fileBackedTaskManager.loadTask(task);
                        break;
                    case Type.EPIC:
                        Epic epic = (Epic) task;
                        fileBackedTaskManager.loadEpic(epic);
                        break;
                    default:
                        Subtask subtask = (Subtask) task;
                        fileBackedTaskManager.loadSubtask(subtask);
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(fileForSave.toString(), StandardCharsets.UTF_8)) {
            List<Task> tasks = getListTask();
            for (Task task : tasks) {
                String line = toStringForFile(task);
                fileWriter.write(line + "\n");
            }
            List<Epic> epics = getListEpic();
            for (Epic epic : epics) {
                String line = toStringForFile(epic);
                fileWriter.write(line + "\n");
            }
            List<Subtask> subtasks = getListSubtask();
            for (Subtask subtask : subtasks) {
                String line = toStringForFile(subtask);
                fileWriter.write(line + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    private String toStringForFile(Task task) {
        String line = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getDuration().toSeconds() + "," + task.getStartTime().toString();
        if (task.getType() == Type.SUBTASK) {
            Subtask subtask = (Subtask) task;
            line = line + "," + subtask.getIdEpic();
        }
        return line;
    }

    private Task fromString(String value) {
        String[] array = value.split(",");
        Status status;
        String statusOfTask = array[3];
        if (statusOfTask.equals("NEW")) {
            status = Status.NEW;
        } else if (statusOfTask.equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else {
            status = Status.DONE;
        }
        Task task;
        String type = array[1];
        long second = Long.parseLong(array[5]);
        Duration duration = Duration.ofSeconds(second);
        LocalDateTime time = LocalDateTime.parse(array[6]);
        if (type.equals("TASK")) {
            task = new Task(array[2], array[4], status, duration, time);
        } else if (type.equals("EPIC")) {
            LocalDateTime endTime = time.plus(duration);
            task = new Epic(array[2], array[4], status, duration, time, endTime);
        } else {
            int id = Integer.parseInt(array[7]);
            task = new Subtask(array[2], array[4], status, id, duration, time);
        }
        return task;
    }

    private void loadTask(Task task) {
        if (task != null) {
            super.addTask(task);
        }
    }

    private void loadEpic(Epic epic) {
        if (epic != null) {
            super.addEpic(epic);
        }
    }

    private void loadSubtask(Subtask subtask) {
        if (subtask != null) {
            super.addSubtask(subtask);
        }
    }
}
