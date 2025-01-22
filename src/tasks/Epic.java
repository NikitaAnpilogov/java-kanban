package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;
    private LocalDateTime endTime; // LocalDateTime.MIN = default

    public Epic(String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
        this.type = Type.EPIC;
        endTime = LocalDateTime.MIN;
    }

    public Epic(String name, String desc, Status status, Duration duration, LocalDateTime start, LocalDateTime end) {
        super(name, desc, status, duration, start); // for load
        subtasksId = new ArrayList<>();
        this.type = Type.EPIC;
        endTime = end;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasksId;
    }

    public void addSubtask(Subtask subtask) { // Этот метод не добавляет подзадачу мапу таск менеджера, а добавляет id подзадачи в список эпика для удобной навигации
        subtasksId.add(subtask.getId());
    }

    public void removeSubtask(Integer id) { // Тоже самое только с удалением подзадачи из списка
        //if (!subtasksId.isEmpty()) {
        if (subtasksId.contains(id)) {
            //int index = subtasksId.indexOf(id);
            //subtasksId.remove(index);
            subtasksId.remove(id);
        }
        //}
    }

    public void removeSubtasks() { // Очищения списка id подзадач этого эпика
        if (!subtasksId.isEmpty()) {
            subtasksId.clear();
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId +
                ", endTime=" + endTime +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
