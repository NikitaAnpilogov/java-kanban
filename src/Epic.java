import java.util.ArrayList;
public class Epic extends Task {
    private ArrayList<Integer> subtasksId;

    public Epic (String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
    }
    public ArrayList<Integer> getSubtasks () {
        return subtasksId;
    }
    public void addSubtask (Subtask subtask) {
        subtasksId.add(subtask.getId());
    }
    public void remoteSubtask (int id) {
        if (!subtasksId.isEmpty()) {
            if (subtasksId.contains(id)) {
                int index = subtasksId.indexOf(id);
                subtasksId.remove(index);
            }
        }
    }

    /*public void checkStatusSubtask () {
        int stat = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.status.equals(Status.DONE)) {
                stat++;
            }
        }
        if (stat == subtasks.size()) {
            this.status = Status.DONE;
        }
    }*/
    public void remoteSubtasks () {
        if (!subtasksId.isEmpty()) {
            subtasksId.clear();
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", subtasksId=" + subtasksId +
                '}';
    }
}
