package tasks;

public class Subtask extends Task {
    private int idEpic;

    public Subtask (String name, String description, Status status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }
    public void setIdEpic (int id) {
        idEpic = id;
    }
    public int getIdEpic () {
        return idEpic;
    }
}
