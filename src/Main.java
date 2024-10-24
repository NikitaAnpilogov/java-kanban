public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Name1", "Description1", Status.NEW);
        Task task2 = new Task("Name2", "Description2", Status.IN_PROGRESS);
        int idTask1 = taskManager.addTask(task1);
        int idTask2 = taskManager.addTask(task2);
        System.out.println(idTask1 + "  " + idTask2);
        System.out.println(task1 + "  " + task2);
        Task newTask = taskManager.getTask(idTask1);
        newTask.setStatus(Status.DONE);
        taskManager.updateTask(newTask);
        Epic epic1 = new Epic("NameEpic1", "DescriptionEpic1");
        int idEpic1 = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", Status.NEW, idEpic1);
        Subtask subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", Status.IN_PROGRESS, idEpic1);
        int idSub1 = taskManager.addSubtask(subtask1);
        int idSub2 = taskManager.addSubtask(subtask2);
        Epic epic2 = new Epic("NameEpic2", "DescriptionEpic2");
        int idEpic2 = taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("NameSubtask3", "DescriptionSubtask3", Status.DONE, idEpic2);
        int idSub3 = taskManager.addSubtask(subtask3);
        System.out.println("Add" + taskManager);
        System.out.println(taskManager.getSubtaskOfEpic(idEpic1));
        newTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(newTask);
        Subtask newSubtask = taskManager.getSubtask(idSub2);
        newSubtask.setStatus(Status.NEW);
        taskManager.updateSubtask(newSubtask);
        System.out.println("Update" + taskManager);
        taskManager.remoteTask(idTask1);
        taskManager.remoteSubtask(idSub1);
        taskManager.remoteEpic(idEpic2);
        System.out.println("Delete" + taskManager);

    }
}
