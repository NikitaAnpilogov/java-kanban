package tasks;

public class Subtask extends Task {
    private Integer idEpic;

    public Subtask(String name, String description, Status status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
        this.type = Type.SUBTASK;
    }

    public void setIdEpic(Integer id) {
        idEpic = id;
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void removeIdEpic() {
        idEpic = null;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
