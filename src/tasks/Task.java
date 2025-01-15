package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;
    protected Type type;
    protected Duration duration; // Duration.ZERO = default
    protected LocalDateTime startTime; // LocalDateTime.MAX = default

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = this.hashCode();
        this.status = status;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, Duration duration) {
        this.name = name;
        this.description = description;
        this.id = this.hashCode();
        this.status = status;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = LocalDateTime.MAX;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.id = this.hashCode();
        this.status = status;
        this.type = Type.TASK;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.MAX;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = this.hashCode();
        this.status = Status.NEW;
        this.type = Type.TASK;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.MAX;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task newTask = (Task) obj;
        return Objects.equals(name, newTask.name) &&
                Objects.equals(description, newTask.description) &&
                Objects.equals(id, newTask.id) &&
                Objects.equals(status, newTask.status) &&
                Objects.equals(type, newTask.type) &&
                Objects.equals(duration, newTask.duration) &&
                Objects.equals(startTime, newTask.startTime);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash *= 31;
        if (description != null) {
            hash = hash + description.hashCode();
        }
        hash *= 31;
        return hash;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getEndTime() {
        if (startTime != LocalDateTime.MAX) {
            return (startTime.plus(duration));
        } else {
            return startTime;
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
