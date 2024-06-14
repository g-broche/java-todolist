import java.util.Scanner;
import Utils.Communication;

public class App {
    enum availableActions {
        CREATE,
        ADD,
        HELP,
        STOP
    };
    public static void main(String[] args) throws Exception {
        Scanner inputScanner = new Scanner(System.in);
        run(inputScanner);

    }

    /**
     * wrapper for the overall loop logic and dialog with the user
     * @param inputScanner instanced Scanner to read user inputs
     */
    private static void run(Scanner inputScanner){
        boolean keepAlive = true;
        do {
            String userRequest = Utils.Communication.requestUserAction(inputScanner, "Select an action to perform");
            if (!isActionValid(userRequest)){
                Utils.Communication.writeErrorFeedback("There is no command called '"+userRequest+"'");
                continue;
            }
            performRequestedAction(userRequest.toUpperCase());
        } while (keepAlive);
    }

    private static void performRequestedAction(String action){
        switch (action) {
            case "CREATE":
                //create new list
                Utils.Communication.writeSuccessFeedback("placeholder : create list");
                break;

            case "ADD":
                //Add task to list
                Utils.Communication.writeSuccessFeedback("placeholder : add task");
                break;

            case "STOP":
                //Stop input reading
                Utils.Communication.writeSuccessFeedback("placeholder : stop scanner");
                break;

            case "HELP":
                //write list of commands in terminal
                Utils.Communication.writeSuccessFeedback("placeholder : display available commands");
                break;
        
            default:
                break;
        }
    }

    /**
     * Checks if an input correspond to an allowed action from the availableActions enum
     * @param action submitted action in the form of a string
     * @return true if action is allowed, false otherwise
     */
    private static boolean isActionValid(String action){
        try {
            availableActions.valueOf(action.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
