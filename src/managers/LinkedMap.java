package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class LinkedMap {
    private HashMap<Integer, Node> history = new HashMap<>();
    int lastId = -1;

    public void addHead(Task task) {
        Node node = new Node(task);
        history.put(task.getId(), node);
        lastId = task.getId();
    }

    public void linkLast(Task task) {
        if (task.getId() == lastId) {
            return;
        }
        Node node = new Node(lastId, task);
        //history.put(task.getId(), node);
        Node anotherNode = history.get(lastId);
        anotherNode.setIdNextTask(task.getId());
        lastId = task.getId();
        if (history.containsKey(task.getId())) { // Убираем повторы, если есть
            Node deletedNode = history.get(task.getId());
            if (deletedNode.getIdLastTask() == null && deletedNode.getIdNextTask() == null) {
                history.remove(task.getId());
            } else if (deletedNode.getIdLastTask() == null) { // Проверка на голову списка
                Integer next = deletedNode.getIdNextTask();
                Node nextNode = history.get(next);
                nextNode.setIdLastTask(null);
                history.remove(task.getId());
            } else if (deletedNode.getIdNextTask() == null) { // Проверка на хвост
                Integer last = deletedNode.getIdLastTask();
                Node lastNode = history.get(last);
                lastNode.setIdNextTask(null);
                history.remove(task.getId());
                lastId = lastNode.getIdTask();
            } else { // Проверка на тело списка
                Integer next = deletedNode.getIdNextTask();
                Integer last = deletedNode.getIdLastTask();
                Node nextNode = history.get(next);
                Node lastNode = history.get(last);
                nextNode.setIdLastTask(lastNode.getIdTask());
                lastNode.setIdNextTask(nextNode.getIdTask());
                history.remove(task.getId());
            }
        }
        history.put(task.getId(), node);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskHistory = new ArrayList<>();
        Integer next = lastId;
        Task task;
        if (history.size() == 0) {
            return taskHistory;
        } else {
            for (int i = 0; i < history.size(); i++) {
                Node node = history.get(next);
                task = node.getTask();
                if (node.getIdLastTask() != null) {
                    next = node.getIdLastTask();
                }
                taskHistory.add(task);
            }
        }
        return taskHistory;
    }

    public int getSize() {
        return history.size();
    }

    public void remove(int id) {
        Node deletedNode = history.get(id);
        if (deletedNode.getIdLastTask() == null && deletedNode.getIdNextTask() == null) {
            history.remove(id);
        } else if (deletedNode.getIdLastTask() == null) { // Проверка на голову списка
            Integer next = deletedNode.getIdNextTask();
            Node nextNode = history.get(next);
            nextNode.setIdLastTask(null);
            history.remove(id);
        } else if (deletedNode.getIdNextTask() == null) { // Проверка на хвост
            Integer last = deletedNode.getIdLastTask();
            Node lastNode = history.get(last);
            lastNode.setIdNextTask(null);
            history.remove(id);
            lastId = lastNode.getIdTask();
        } else { // Проверка на тело списка
            Integer next = deletedNode.getIdNextTask();
            Integer last = deletedNode.getIdLastTask();
            Node nextNode = history.get(next);
            Node lastNode = history.get(last);
            nextNode.setIdLastTask(lastNode.getIdTask());
            lastNode.setIdNextTask(nextNode.getIdTask());
            history.remove(id);
        }
    }
}
