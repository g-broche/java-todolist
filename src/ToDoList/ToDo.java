package ToDoList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Utils.Communication;
import Utils.Validators;

public class ToDo {
    private List<TaskList> lists = new ArrayList<TaskList>();
    private TaskList activeList = null;

    /**
     * wrapper for the overall loop logic and dialog with the user
     * @param inputScanner instanced Scanner to read user inputs
     */
    public void run(Scanner inputScanner){
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

    /**
     * Method which will execute the chain of instruction required for the user provided command
     * @param actionType Action required by a user that is allowed by the Validator class
     * @param actionArgument String representing the argument to provide to the required command if it applies
     */
    private void performRequestedAction(String actionType, String actionArgument){
        try {
            switch (actionType.toUpperCase()) {

                /* ***** NEED TO IMPLEMENT LOGIC FOR COMMAND REQUIRING ARGUMENT PARAMETER OR NOT ***** */

                //creates new TaskList with the provided label 
                case "CREATE":
                    this.activeList = new TaskList(actionArgument);
                    Utils.Communication.writeSuccessFeedback("added - list: "+this.activeList.getLabel());
                    break;
    
                //add new task to the current active list
                case "ADD":
                    this.activeList.addTask(actionArgument);
                    Utils.Communication.printInstructionResult(
                        this.activeList.containsTask(actionArgument),
                        "Added task to list: "+this.activeList.getLabel(),
                        "The task could not be added to list (list: <"+this.activeList.getLabel()+"> ; task: <"+actionArgument+">)"
                    );
                    break;

                case "SHOW":
                    this.activeList.displayTaskList();
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
        } catch (IllegalArgumentException e) {
            Communication.writeErrorFeedback(e.getMessage());
        } catch (Exception e) {
            Communication.writeErrorFeedback(e.getMessage());
        }

    }
}
