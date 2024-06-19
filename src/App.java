import java.util.Scanner;
import ToDoList.ToDo;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner inputScanner = new Scanner(System.in);
        ToDo toDo = new ToDo();
        toDo.run(inputScanner);
        inputScanner.close();
    }
}
