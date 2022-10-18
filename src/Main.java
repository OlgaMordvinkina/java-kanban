import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        //Subtask subtask = new Subtask();

        String nameOut = "Переезд";
        String ditOut = "Собрать коробки";



        manager.subtaskList.put(manager.assignsId(), new Subtask(nameOut, ditOut));
        Subtask subtask = manager.subtaskList.get(manager.getId());

        System.out.println(subtask.name);
    }
}
