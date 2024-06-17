import java.util.Map;
import java.util.Scanner;
import Utils.Communication;
import Utils.Validators;

public class App {

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
            Map<String, String> parsedInput = Validators.parseInput(userRequest);
            if(parsedInput == null){
                Utils.Communication.writeErrorFeedback("No command was inputed");
                continue;
            }
            if (!Validators.isActionValid(parsedInput.get("command"))){
                Utils.Communication.writeErrorFeedback("There is no command called '"+userRequest+"'");
                continue;
            }
            performRequestedAction(parsedInput.get("command"), parsedInput.get("argument"));
        } while (keepAlive);
    }

    private static void performRequestedAction(String actionType, String actionArgument){
        switch (actionType.toUpperCase()) {
            case "CREATE":
                //create new list
                Utils.Communication.writeSuccessFeedback("placeholder - create list: "+actionArgument);
                break;

            case "ADD":
                //Add task to list
                Utils.Communication.writeSuccessFeedback("placeholder - add task: "+actionArgument);
                break;

            case "STOP":
                //Stop input reading
                Utils.Communication.writeSuccessFeedback("placeholder - stop scanner");
                break;

            case "HELP":
                //write list of commands in terminal
                Utils.Communication.writeSuccessFeedback("placeholder - display available commands");
                break;
        
            default:
                break;
        }
    }
}
