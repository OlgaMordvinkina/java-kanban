import manager.Manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Epic epic = new Epic();
        epic.setTitle("Покупки");
        epic.setDescription("Для дома");
        manager.saveEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setTitle("Хоз. товары");
        subtask.setDescription("Порошок");
        subtask.setEpicId(epic.getId());
        manager.saveSubtask(subtask);

        Subtask subtaskTwo = new Subtask();
        subtaskTwo.setTitle("Продукты");
        subtaskTwo.setDescription("Молоко");
        subtaskTwo.setEpicId(epic.getId());
        manager.saveSubtask(subtaskTwo);

        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtaskTwo);

        Epic epicTwo = new Epic();
        epicTwo.setTitle("Собрать вещи");
        epicTwo.setDescription("По коробкам:");
        manager.saveEpic(epicTwo);

        Subtask subtaskThree = new Subtask();
        subtaskThree.setTitle("Коробка 1:");
        subtaskThree.setDescription("Со стеклом");
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
    }
}
