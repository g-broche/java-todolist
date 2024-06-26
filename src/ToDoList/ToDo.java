package ToDoList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import Utils.Communication;
import Utils.Validators;
import Utils.XMLHandler;

/**
 * Class containing the run method with the application whole logic and bringing all the necessary class and methods together.
 */
public class ToDo {
    private List<TaskList> lists = new ArrayList<TaskList>();
    private TaskList activeList = null;
    private Scanner inputScanner = null;

    /**
     * Construct ToDo class which handles the main logic of the application
     * @param inputScanner Scanner instance required to read user inputs
     */
    public ToDo(Scanner inputScanner){
        this.inputScanner = inputScanner;
    }

    /**
     * wrapper for the overall loop logic and dialog with the user
     */
    public void run(){
        boolean keepAlive = true;
        boolean isFirstAction = true;
        do {
            String requestPrompt = this.createUserPromptForRunCycle(isFirstAction);
            isFirstAction = false;
            String userInput = Utils.Communication.requestUserAction(this.inputScanner, requestPrompt);
            Map<String, String> parsedInput = Validators.parseInput(userInput);
            if(parsedInput == null){
                Utils.Communication.printErrorFeedback("No command was inputed");
                continue;
            }
            if (!Validators.isActionValid(parsedInput.get("command"))){
                Utils.Communication.printErrorFeedback("There is no command called '"+userInput+"'");
                continue;
            }
            keepAlive = performRequestedAction(parsedInput.get("command"), parsedInput.get("argument"));
        } while (keepAlive);
    }

    /**
     * Create prompt message show to user for each run cycle
     * @param isFirstCycle
     * @return String for the prompt to use depending on current state
     */
    private String createUserPromptForRunCycle(boolean isFirstCycle){
        if(isFirstCycle){
            return "Select an action to perform (type 'help' for command list)";
        }
        if(this.activeList != null && !Validators.isStringNullOrEmptyOrBlank(this.activeList.getLabel())){
            return "Select an action to perform (current list: \""+this.activeList.getLabel()+"\")";
        }
        return "Select an action to perform";
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
            //use enum to tie switch cases to the enum used for validation to add more consistency
            Validators.availableActions action = Validators.availableActions.valueOf(actionType.toUpperCase());
            switch (action) {
                //creates new TaskList with the provided label 
                case CREATE:
                    this.handleListCreation(actionArgument);
                    break;
    
                //add new task to the current active list
                case ADD:
                    if(this.isActiveListValid()){
                        this.handleTaskAdditionToList(actionArgument, this.activeList);
                    }
                    break;

                case DEL:
                    if(this.isActiveListValid()){
                        this.handleTaskRemovalFromList(actionArgument, this.activeList);
                    }
                    break;
                case DELLIST:
                    this.handleListDeletion(actionArgument);
                    break;

                case SHOW:
                    if(this.isActiveListValid()){
                        this.activeList.displayTaskList();
                    }
                    break;

                case SHOWALL:
                    this.displayAllListsContent();
                    break;

                case SHOWLISTS:
                    this.displayAllListsLabels();
                    break;

                case SWITCH:
                    this.handleActiveListSwitch(actionArgument);
                    break;

                case STOP:
                    mustKeepRunning = false;
                    break;

                case SAVE:
                    this.handleSavingListsToXml();
                    break;

                case LOAD:
                    this.handleLoadingListsFromXml();
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
     * @param initialLabelInput user inputed name for the task to add
     */
    private void handleListCreation(String labelInput){
        if(Validators.isStringNullOrEmptyOrBlank(labelInput)){
            labelInput = this.requestForUserInputedArgument("Input the name of the list to create");
        }
        boolean isRequestedInputStillNull = Validators.isStringNullOrEmptyOrBlank(labelInput);
        if(isRequestedInputStillNull){
            Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
            return;
        }
        String newListName = labelInput;
        if(this.doesTaskListExists(newListName)){
            Communication.printErrorFeedback("Action cancelled, there is already a list with the label \""+newListName+"\"");
            return;
        }
        this.activeList = new TaskList(newListName);
        this.lists.add(this.activeList);
        boolean isListCreated = this.activeList.getLabel() == newListName;
        Utils.Communication.printInstructionResult(
            isListCreated,
            "added and switched to list: \""+newListName+"\"",
            "The new list was not created for an unspecified reason"
        );
    }

    /**
     * Handle the process and user interaction involved in the addition of task to a list
     * @param newTask user inputed name for the task to add
     * @param taskList task list to which the task must be added
     */
    private void handleTaskAdditionToList(String taskNameInput, TaskList taskList){
        if(Validators.isStringNullOrEmptyOrBlank(taskNameInput)){
            taskNameInput = this.requestForUserInputedArgument("Input the task to add to list \""+this.activeList.getLabel()+"\"");
        }
        boolean isRequestedInputStillNull = Validators.isStringNullOrEmptyOrBlank(taskNameInput);
        if(isRequestedInputStillNull){
            Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
            return;
        }
        String newTask = taskNameInput;
        if(taskList.doesTaskExistsInList(newTask)){
            Communication.printErrorFeedback("Action cancelled, there is already a task named \""+newTask+"\" in list \""+taskList.getLabel()+"\"");
            return;
        }
        this.activeList.addTask(newTask);
        Utils.Communication.printInstructionResult(
            this.activeList.containsTaskName(newTask),
            "Added \""+newTask+"\" to list: "+this.activeList.getLabel(),
            "The task could not be added to list (list: <"+this.activeList.getLabel()+"> ; task: <"+newTask+">)"
        );
    }

    /**
     * Handle the process and user interaction involved in the removal of task from a list
     * @param inputedIndex user inputed index in string format for the task to remove
     * @param taskList task list from which the task must be removed
     */
    private void handleTaskRemovalFromList(String inputedIndex, TaskList taskList){
        if(Validators.isStringNullOrEmptyOrBlank(inputedIndex)){
            taskList.displayTaskList();
            inputedIndex = this.requestForUserInputedArgument("Input the number index of the task to remove");
        }
        boolean isRequestedInputStillNull = Validators.isStringNullOrEmptyOrBlank(inputedIndex);
        if (isRequestedInputStillNull) {
            Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
            return;
        }
        Integer indexToRemove = Validators.validateRemovalRequestForIndex(inputedIndex, taskList.getTasks());
        boolean isRequestValid = indexToRemove != null;
        if(!isRequestValid){
            Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
            return;
        }
        taskList.removeTask(indexToRemove);
    }

    /**
     * Handles the deletion of a task list
     * @param inputedIndex provided index in initial user input at command call, if none a request will be made
     */
    private void handleListDeletion(String inputedIndex){
        if(this.lists.isEmpty()){
            Communication.printErrorFeedback("There are no initialized list of tasks currently, action cancelled");
            return;
        }
        if(Validators.isStringNullOrEmptyOrBlank(inputedIndex)){
            displayAllListsLabels();
            inputedIndex = this.requestForUserInputedArgument("Input the number index of the list to delete");
        }
        boolean isRequestedInputStillNull = Validators.isStringNullOrEmptyOrBlank(inputedIndex);
        if (isRequestedInputStillNull) {
            Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
            return;
        }
        try {
            Integer indexToRemove = Validators.validateRemovalRequestForIndex(inputedIndex, this.lists);
            String labelListToRemove = this.lists.get(indexToRemove).getLabel();
            boolean isRequestValid = indexToRemove != null;
            if(!isRequestValid){
                Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
                return;
            }
            boolean isSuccess = this.deleteList(indexToRemove);
            Communication.printInstructionResult(
                isSuccess,
                "List <"+labelListToRemove+"> has been removed",
                "Failed to remove list at index "+indexToRemove
                );
        } catch (Exception e) {
            Communication.printErrorFeedback(e.getMessage());
        }
    }

    /**
     * Delete a TaskList from ToDo.lists based on index requested. Will switch active TaskList to last of list if it was removed in the process
     * or to null if lists is empty
     * @param index index of list to delete
     */
    private boolean deleteList(int index){
        try {
            boolean isCurrentListForDeletion = this.lists.get(index).equals(this.activeList);
            TaskList removedElement = this.lists.remove(index);
            boolean isRemoved = removedElement != null ? true : false;
            if(!isCurrentListForDeletion){
                return isRemoved;
            }
            if(this.lists.isEmpty()){
                this.activeList = null;
                return isRemoved;
            }
            this.activeList = this.lists.getLast();
            return isRemoved;
        } catch (Exception e) {
            return false;
        }
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

    /**
     * Checks if the given label corresponds to an existing TaskList instance label.
     * @param label label to look for in all managed lists
     * @return true if a list as the given label, false otherwise
     */
    private boolean doesTaskListExists(String label){
        for (TaskList taskList : lists) {
            if (taskList.getLabel().equals(label)){
                return true;
            }
        }
        return false;
    }

    /**
     * Request input from user
     * @param requestPrompt message to display to the user for the request
     * @return input sent by the user
     */
    private String requestForUserInputedArgument(String requestPrompt){
        try {
            String userInput = Utils.Communication.requestUserAction(this.inputScanner, requestPrompt);
            return userInput;
        } catch (Exception e) {
            Communication.printErrorFeedback(e.getMessage());
            return null;
        }
    }

    /**
     * Handle switching to a different list
     * @param inputedIndex inputed index of the list to switch to
     */
    private void handleActiveListSwitch(String inputedIndex){
        if(Validators.isStringNullOrEmptyOrBlank(inputedIndex)){
            this.displayAllListsLabels();
            String prompt = "Input the number index of the list to switch to (current active list is ["+this.getListIndex(this.activeList)+"])";
            inputedIndex = this.requestForUserInputedArgument(prompt);
        }
        boolean isRequestedInputStillNull = Validators.isStringNullOrEmptyOrBlank(inputedIndex);
        if (isRequestedInputStillNull) {
            Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
            return;
        }
        Integer indexToRemove = Validators.validateListSwitchRequest(inputedIndex, this.lists);
        boolean isRequestValid = indexToRemove != null;
        if(!isRequestValid){
            Communication.printErrorFeedback("Invalid argument given, returning to main command flow");
            return;
        }
        this.switchListTo(indexToRemove);
    }

    /**
     * Switch the active list to a different list
     * @param indexToSwitchTo index of the list to switch to inside of ToDo.lists
     */
    private void switchListTo(int indexToSwitchTo){
        try {
            int currentActiveListIndex = this.getListIndex(this.activeList);
            if (currentActiveListIndex == indexToSwitchTo){
                Communication.printErrorFeedback("No change occured as this is already the active list");
                return;
            }
            String previousListLabel = this.activeList.getLabel();
            this.activeList = this.lists.get(indexToSwitchTo);
            Communication.printSuccessFeedback("Switched from \""+previousListLabel+"\" to \""+this.activeList.getLabel()+"\"");
        } catch (Exception e) {
            Communication.printErrorFeedback(e.getMessage());
        }
        
    }

    /**
     * Gets index of a TaskList instance inside of ToDo.lists
     * @param list TaskList instance
     * @return index of list in ToDo.lists
     */
    private Integer getListIndex(TaskList list){
        return this.lists.indexOf(list);
    }

    /**
     * Display the content of all managed TaskList
     */
    private void displayAllListsContent(){
        if (this.lists.isEmpty()) {
            Communication.printMessage("There are no initialized lists");
        }
        Communication.printMessage("Listing content of all lists");
        for (TaskList taskList : lists) {
            taskList.displayTaskList();
        }
    }

    /**
     * Check if ToDo.activeList is a valid instance of TaskList, will print error message if not
     * @return true if activeList is not null and instance of TaskList
     */
    private boolean isActiveListValid(){
        boolean isValid = this.activeList != null && this.activeList instanceof TaskList;
        if(!isValid){
            Communication.printErrorFeedback("A list must be active for this command to work");
        }
        return isValid;
    }

    /**
     * Saves all existing lists to an XML file
     */
    private void handleSavingListsToXml(){
        XMLHandler xmlHandler = new XMLHandler();
        xmlHandler.writeToDoFile(this.lists);
    }

    /**
     * Load previously saved lists into the current ToDo.lists and loads the last one as active list
     */
    private void handleLoadingListsFromXml(){
        XMLHandler xmlHandler = new XMLHandler();
        List<TaskList> retrievedLists = xmlHandler.loadToDoFromFile();
        if (retrievedLists == null || retrievedLists.isEmpty()) {
            return;
        }
        for (TaskList taskList : retrievedLists) {
            this.lists.add(taskList);
        }
        this.activeList = this.lists.getLast();
        Communication.printSuccessFeedback("Loaded list from saved file");
        this.displayAllListsContent();
    }
}
