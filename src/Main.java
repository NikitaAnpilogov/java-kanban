import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Name1", "Description1", Status.NEW);
        Task task2 = new Task("Name2", "Description2", Status.IN_PROGRESS);
        int idTask1 = taskManager.addTask(task1);
        int idTask2 = taskManager.addTask(task2);
        System.out.println(idTask1 + "  " + idTask2);
        System.out.println(task1 + "  " + task2);

        System.out.println("Check History");
        ArrayList<Task> history = taskManager.getHistory();
        for (Task t : history) {
            System.out.println(t);
        }
        System.out.println(history.size());

        Task newTask = taskManager.getTask(idTask1);

        System.out.println("Check History2");
        ArrayList<Task> history2 = taskManager.getHistory();
        for (Task t : history2) {
            System.out.println(t);
        }
        System.out.println(history2.size());

        Task newTask2 = taskManager.getTask(idTask2);
        Task newTask3 = taskManager.getTask(idTask2);
        Task newTask4 = taskManager.getTask(idTask1);


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

        Epic newEpic1 = taskManager.getEpic(idEpic1);
        Epic newEpic2 = taskManager.getEpic(idEpic2);
        Subtask newSub1 = taskManager.getSubtask(idSub1);
        Subtask newSub2 = taskManager.getSubtask(idSub3);
        Subtask newSub3 = taskManager.getSubtask(idSub3);
        Epic newEpic3 = taskManager.getEpic(idEpic1);

        System.out.println("Check History3");
        ArrayList<Task> history3 = taskManager.getHistory();
        for (Task t : history3) {
            System.out.println(t);
        }
        System.out.println(history3.size());

        Epic newEpic4 = taskManager.getEpic(idEpic2);
        System.out.println("Check History4");
        ArrayList<Task> history4 = taskManager.getHistory();
        for (Task t : history4) {
            System.out.println(t);
        }
        System.out.println(history4.size());

        System.out.println("Add" + taskManager);
        System.out.println(taskManager.getSubtasksOfEpic(idEpic1));
        newTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(newTask);
        Subtask newSubtask = taskManager.getSubtask(idSub2);
        newSubtask.setStatus(Status.NEW);
        taskManager.updateSubtask(newSubtask);
        System.out.println("Update" + taskManager);
        System.out.println("Check History5");
        ArrayList<Task> history5 = taskManager.getHistory();
        for (Task t : history5) {
            System.out.println(t);
        }
        System.out.println(history5.size());
        //taskManager.removeTask(idTask2);
        //taskManager.removeTask(idTask1);
        //taskManager.removeSubtask(idSub2);
        //taskManager.removeEpic(idEpic2);
        //taskManager.removeEpic(idEpic1);
        //taskManager.addTask(task2);
        //taskManager.getTask(idTask2);
        //taskManager.getTask(idTask1);
        //taskManager.getTask(idTask1);
        taskManager.removeAllTask();
        //taskManager.removeAllSubtask();
        taskManager.removeAllEpic();
        System.out.println("Delete" + taskManager);
        System.out.println("Check History6");
        ArrayList<Task> history6 = taskManager.getHistory();
        for (Task t : history6) {
            System.out.println(t);
        }
        System.out.println(history6.size());

        //File file = new File("c:\\Users\\nikan\\IdeaProjects\\java-kanban", "file.CSV");
        File file = new File("file.CSV");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        //fileBackedTaskManager.addTask(task1);
        //fileBackedTaskManager.addTask(task2);
        /*fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addEpic(epic2);
        subtask1 = new Subtask("NameSubtask1", "DescriptionSubtask1", Status.NEW, idEpic1);
        subtask2 = new Subtask("NameSubtask2", "DescriptionSubtask2", Status.IN_PROGRESS, idEpic1);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);
        System.out.println(fileBackedTaskManager.toString());*/
        FileBackedTaskManager fileBackedTaskManagerNew = FileBackedTaskManager.loadFromFile(file);
        System.out.println(fileBackedTaskManagerNew.toString());
    }
}
