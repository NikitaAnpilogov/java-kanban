package managers;

import tasks.Task;

public class Node {
    private Integer idLastTask;
    private Integer idNextTask;
    private Task task;

    public Node(int idLastTask, Task task) {
        this.idLastTask = idLastTask;
        this.task = task;
    }

    public Node(Task task, int idNextTask) {
        this.task = task;
        this.idNextTask = idNextTask;
    }

    public Node(Task task) {
        this.task = task;
    }

    public void setIdLastTask(Integer id) {
        idLastTask = id;
    }

    public void setIdNextTask(Integer id) {
        idNextTask = id;
    }

    public Integer getIdLastTask() {
        return idLastTask;
    }

    public Task getTask() {
        return task;
    }

    public Integer getIdNextTask() {
        return idNextTask;
    }

    public Integer getIdTask() {
        return task.getId();
    }
}
