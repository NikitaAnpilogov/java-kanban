package managers;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history;
    final int MAX_HISTORY_LENGTH = 10;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
    @Override
    public <T extends Task> void add(T task) {
        if (history.size() == MAX_HISTORY_LENGTH) {
            history.remove(0);
        }
        Task taskHistory = (Task) task;
        history.add(taskHistory);
    }
}
