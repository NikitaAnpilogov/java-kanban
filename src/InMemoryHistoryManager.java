import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
    @Override
    public <T extends Task> void add(T task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        Task taskHistory = (Task) task;
        history.add(taskHistory);
    }
}
