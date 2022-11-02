import manager.Managers;
import manager.history.HistoryManager;
import manager.tasks.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Epic epic = new Epic("Покупки", "Для дома");
        manager.saveEpic(epic);

        Subtask subtask = new Subtask("Хоз. товары", "Порошок");
        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);

        Subtask subtaskTwo = new Subtask("Продукты", "Молоко");
        subtaskTwo.setEpicId(epic.getId());
        manager.saveSubtask(subtaskTwo);

        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtaskTwo);

        Epic epicTwo = new Epic("Собрать вещи", "По коробкам:");
        manager.saveEpic(epicTwo);

        Subtask subtaskThree = new Subtask("Коробка 1:", "Со стеклом");
        subtaskThree.setEpicId(epicTwo.getId());
        manager.saveSubtask(subtaskThree);

        manager.getListAllTasks();
        System.out.println("\n");

        subtaskTwo.setTitle("Товары для ремонта");
        subtaskTwo.setDescription("Молоток");
        subtaskTwo.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtaskTwo);

        subtask.setStatus(TaskStatus.DONE);

        manager.getListAllTasks();
        System.out.println("\n");

        manager.deleteSubtask(5);

        manager.getListAllTasks();

        manager.getEpicById(1);

        manager.getEpicById(1);
        manager.getSubtaskById(2);
        manager.getSubtaskById(2);
        manager.getSubtaskById(2);
        manager.getSubtaskById(2);
        manager.getSubtaskById(2);
        manager.getSubtaskById(2);
        manager.getSubtaskById(2);
        manager.getSubtaskById(2);
        manager.getSubtaskById(4);
        manager.getSubtaskById(2);

        System.out.println(historyManager.getHistory());
    }
}
