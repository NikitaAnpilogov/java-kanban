package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> history;
    private Node first;
    private Node last;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public <T extends Task> void add(T task) {
        Task historyTask = (Task) task;
        linkLast(historyTask);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    private void linkLast(Task task) {
        if (last != null) {
            if (task.equals(last.getTask())) {
                return;
            }
        }
        final Node node = new Node(last, task);
        if (first == null) {
            first = node;
        } else {
            last.setNodeNext(node);
        }
        last = node;
        if (history.containsKey(task.getId())) {
            removeNode(task.getId());
        }
        history.put(task.getId(), node);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> taskHistory = new ArrayList<>();
        Node node = last;
        while (node != null) {
            taskHistory.add(node.getTask());
            node = node.getNodeLast();
        }
        return taskHistory;
    }

    private void removeNode(int id) {
        if (history.containsKey(id)) {
            Node deletedNode = history.get(id);
            if (deletedNode.getNodeLast() == null && deletedNode.getNodeNext() == null) {
                history.remove(id);
            } else if (deletedNode.getNodeLast() == null) { // Проверка на голову списка
                Node nextNode = deletedNode.getNodeNext();
                nextNode.setNodeLast(null);
                history.remove(id);
            } else if (deletedNode.getNodeNext() == null) { // Проверка на хвост
                Node lastNode = deletedNode.getNodeLast();
                lastNode.setNodeNext(null);
                history.remove(id);
                last = lastNode;
            } else { // Проверка на тело списка
                Node nextNode = deletedNode.getNodeNext();
                Node lastNode = deletedNode.getNodeLast();
                nextNode.setNodeLast(lastNode);
                lastNode.setNodeNext(nextNode);
                history.remove(id);
            }
        }
        if (history.isEmpty()) {
            last = null;
            first = null;
        }
    }
}