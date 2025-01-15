package managers;

import tasks.Task;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(nodeLast, node.nodeLast) && Objects.equals(nodeNext, node.nodeNext) && Objects.equals(task, node.task);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (task != null) {
            hash = hash + task.hashCode();
        }
        hash *= 31;
        return hash;
    }
}
