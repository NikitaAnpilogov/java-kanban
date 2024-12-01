package tasks;

import java.util.Objects;
public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.id = this.hashCode();
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = this.hashCode();
        this.status = Status.NEW;
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
                Objects.equals(status, newTask.status);
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
        /*if (id != null) {
            hash = hash + id.hashCode();
        }
        hash *= 31;
        if (status != null) {
            hash = hash + status.hashCode();
        }*/
        return hash;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
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
}
