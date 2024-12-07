package managers;

import tasks.Task;

public class Node {
    private Node nodeLast;
    private Node nodeNext;
    private Task task;

    public Node(Node idLastTask, Task task) {
        this.nodeLast = idLastTask;
        this.task = task;
    }

    public Node(Task task, Node nodeNext) {
        this.task = task;
        this.nodeNext = nodeNext;
    }

    public Node(Task task) {
        this.task = task;
    }

    public void setNodeLast(Node id) {
        nodeLast = id;
    }

    public void setNodeNext(Node id) {
        nodeNext = id;
    }

    public Node getNodeLast() {
        return nodeLast;
    }

    public Task getTask() {
        return task;
    }

    public Node getNodeNext() {
        return nodeNext;
    }

    public Integer getIdTask() {
        return task.getId();
    }
}
