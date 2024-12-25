package managers;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List; // "не хватает метода. который очищает все мапы" - не совсем понял какой метод требуется. Я добавил 3 метода, которые полностью удаляют таски сабтаски и эпики, соответственно они полностью очищают мапы

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
    public int addTask(Task task) {
        int answer = super.addTask(task);
        save();
        return answer;
    }

    @Override
    public int addEpic(Epic epic) {
        int answer = super.addEpic(epic);
        save();
        return answer;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int answer = super.addSubtask(subtask);
        save();
        return answer;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
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
            ArrayList<Task> tasks = getListTask();
            for (Task task : tasks) {
                String line = toStringForFile(task);
                fileWriter.write(line + "\n");
            }
            ArrayList<Epic> epics = getListEpic();
            for (Epic epic : epics) {
                String line = toStringForFile(epic);
                fileWriter.write(line + "\n");
            }
            ArrayList<Subtask> subtasks = getListSubtask();
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
                + task.getDescription();
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
        if (type.equals("TASK")) {
            task = new Task(array[2], array[4], status);
        } else if (type.equals("EPIC")) {
            task = new Epic(array[2], array[4]);
        } else {
            int id = Integer.parseInt(array[5]);
            task = new Subtask(array[2], array[4], status, id);
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
