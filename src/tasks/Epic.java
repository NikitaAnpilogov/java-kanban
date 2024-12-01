package tasks;

import java.util.ArrayList;
public class Epic extends Task { // Прошу прочесть комментарий
    private ArrayList<Integer> subtasksId; // В этом списке хранятся не сами подзадачи, а id подзадачи, которые закреплены за этим конкретным эпиком.

    // Это сделано для удобного отслеживания статуса эпика, и поиска какие подзадачи за ним закреплены
    public Epic(String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasksId;
    }

    public void addSubtask(Subtask subtask) { // Этот метод не добавляет подзадачу мапу таск менеджера, а добавляет id подзадачи в список эпика для удобной навигации
        subtasksId.add(subtask.getId());
    }

    public void removeSubtask(Integer id) { // Тоже самое только с удалением подзадачи из списка
        //if (!subtasksId.isEmpty()) {
            if (subtasksId.contains(id)) {
                //int index = subtasksId.indexOf(id);
                //subtasksId.remove(index);
                subtasksId.remove(id);
            }
        //}
    }

    public void removeSubtasks() { // Очищения списка id подзадач этого эпика
        if (!subtasksId.isEmpty()) {
            subtasksId.clear();
        }
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", subtasksId=" + subtasksId +
                '}';
    }
}
