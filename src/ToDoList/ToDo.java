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
        boolean isFirstAction = true;
        do {
            String requestPrompt = isFirstAction? "Select an action to perform (type 'help' for command list)" : "Select an action to perform";
            isFirstAction = false;
            String userRequest = Utils.Communication.requestUserAction(inputScanner, requestPrompt);
            Map<String, String> parsedInput = Validators.parseInput(userRequest);
            if(parsedInput == null){
                Utils.Communication.printErrorFeedback("No command was inputed");
                continue;
            }
            if (!Validators.isActionValid(parsedInput.get("command"))){
                Utils.Communication.printErrorFeedback("There is no command called '"+userRequest+"'");
                continue;
            }
            keepAlive = performRequestedAction(parsedInput.get("command"), parsedInput.get("argument"));
        } while (keepAlive);
    }

    /**
     * Method which will execute the chain of instruction required for the user provided command
     * @param actionType Action required by a user that is allowed by the Validator class
     * @param actionArgument String representing the argument to provide to the required command if it applies
     * @return true if the program must keep running, false if program must stop after this cycle ends.
     */
    private boolean performRequestedAction(String actionType, String actionArgument){
        boolean mustKeepRunning = true;
        try {
            //use enum to tie switch cases to the enum used for validation to prevent more consistency and 
            Validators.availableActions action = Validators.availableActions.valueOf(actionType.toUpperCase());

            switch (action) {
                /* ***** NEED TO IMPLEMENT LOGIC FOR COMMAND REQUIRING ARGUMENT PARAMETER OR NOT ***** */
                //creates new TaskList with the provided label 
                case CREATE:
                    this.handleListCreation(actionArgument);
                    break;
    
                //add new task to the current active list
                case ADD:
                    this.handleTaskAdditionToList(actionArgument, this.activeList);
                    break;

                case DEL:
                    this.handleTaskRemovalFromList(actionArgument, this.activeList);
                    break;

                case SHOW:
                    this.activeList.displayTaskList();
                    break;

                case SHOWALL:
                    this.displayAllListsLabels();
                    break;
    
                case STOP:
                    mustKeepRunning = false;
                    break;
    
                case HELP:
                    Utils.Communication.printCommandList();
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            Communication.printErrorFeedback(e.getMessage());
        } catch (Exception e) {
            Communication.printErrorFeedback(e.getMessage());
        }

        return mustKeepRunning;
    }

    /**
     * Handle the process and user interaction involved in the creation of a new list
     * @param newTask user inputed name for the task to add
     * @param taskList task list to which the task must be added
     */
    private void handleListCreation(String newListName){
        boolean isRequestValid = newListName != null;
        if(!isRequestValid){
            //handle error logic here
            return;
        }
        if(this.doesTaskListExists(newListName)){
            Communication.printErrorFeedback("Action cancelled, there is already a list with the label \""+newListName+"\"");
            return;
        }
        this.activeList = new TaskList(newListName);
        this.lists.add(this.activeList);
        boolean isListCreated = this.activeList.getLabel() == newListName;
        Utils.Communication.printInstructionResult(
            isListCreated,
            "added - list: "+newListName,
            "The new list was not created for an unspecified reason"
        );
    }

    /**
     * Handle the process and user interaction involved in the addition of task to a list
     * @param newTask user inputed name for the task to add
     * @param taskList task list to which the task must be added
     */
    private void handleTaskAdditionToList(String newTask, TaskList taskList){
        boolean isRequestValid = newTask != null;
        if(!isRequestValid){
            //handle error logic here
            return;
        }
        this.activeList.addTask(newTask);
        Utils.Communication.printInstructionResult(
            this.activeList.containsTaskName(newTask),
            "Added task to list: "+this.activeList.getLabel(),
            "The task could not be added to list (list: <"+this.activeList.getLabel()+"> ; task: <"+newTask+">)"
        );
    }

    /**
     * Handle the process and user interaction involved in the removal of task from a list
     * @param inputedIndex user inputed index in string format for the task to remove
     * @param taskList task list from which the task must be removed
     */
    private void handleTaskRemovalFromList(String inputedIndex, TaskList taskList){
        Integer indexToRemove = Validators.validateTaskRemovalRequest(inputedIndex, taskList);
        boolean isRequestValid = indexToRemove != null;
        if(!isRequestValid){
            //handle error logic here
            return;
        }
        taskList.removeTask(indexToRemove);
    }

    /**
     * Display the labels of all existing TaskList
     */
    public void displayAllListsLabels(){
        if(this.lists.isEmpty()){
            Communication.printMessage("There are currently no list being managed");
            return;
        }
        Communication.printMessage("Displaying labels of all currently managed task lists:");
        Communication.printAllListlabels(this.lists);
    }

    private boolean doesTaskListExists(String label){
        for (TaskList taskList : lists) {
            if (taskList.getLabel().equals(label)){
                return true;
            }
        }
        return false;
    }
}
