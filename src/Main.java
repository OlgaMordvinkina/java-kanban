import tasks.*;
import java.util.Collection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        //tasks.Subtask subtask = new tasks.Subtask();

        String nameOut = "Переезд";
        String ditOut = "Собрать коробки";


        Epic epic = new Epic();
        epic.setTitle("title1");
        epic.setDescription("test_epic");

        Subtask subtask = new Subtask();
        subtask.setTitle(nameOut);
        subtask.setDescription(ditOut);

        epic.subtasks.add(subtask);

        manager.saveEpic(epic);

        Collection<Epic> epics = manager.getListEpics();

        System.out.println(manager.subtaskStore);
    }
}
