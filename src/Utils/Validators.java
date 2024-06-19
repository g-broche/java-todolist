package Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ToDoList.TaskList;

public class Validators {
    public enum availableActions {
        CREATE,
        ADD,
        DEL,
        SHOW,
        HELP,
        STOP
    };

    /**
     * Parse user input to separate inputed command and inputed argument (label)
     * @param userInput String inputed by the user
     * @return  null if empty input or a map of the type ["command" -> commandInputed, "argument" -> argumentInputed/null]
     */
    public static Map<String, String> parseInput(String userInput){
        userInput = userInput.trim();
        if(userInput.isEmpty() ||userInput.isBlank()){
            return null;
        }

        Map<String, String> parsedInput = new HashMap<String, String>();

        String[] inputWordArray = userInput.split(" ");
        String commandType = inputWordArray[0];
        String commandArgument = Validators.getInputedArgument(inputWordArray);

        parsedInput.put("command", commandType);
        parsedInput.put("argument", commandArgument);

        return parsedInput;
    }

    /**
     * Extract the argument part of the user input
     * @param inputedWords array corresponding the words composing the user input
     * @return String corresponding to everything except the first inputed word, or null if only one word was inputed
     */
    private static String getInputedArgument(String[] inputedWords){
        if (inputedWords.length <=1) {
            return null;
        }
        String[] argumentArray = Arrays.copyOfRange(inputedWords, 1, inputedWords.length);
        String argumentString = String.join(" ", argumentArray);
        return argumentString;
    }

    /**
     * Checks if an input correspond to an allowed action from the availableActions enum
     * @param action submitted action in the form of a string
     * @return true if action is allowed, false otherwise
     */
    public static boolean isActionValid(String action){
        try {
            availableActions.valueOf(action.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validate the inputed task index to remove from the list
     * @param indexInput String corresponding to the required index value for task removal
     * @param taskList TaskList from which a task removal request is ongoing
     * @return
     */
    public static Integer validateTaskRemovalRequest(String indexInput, TaskList taskList){
        try {
            int indexToRemove = Integer.parseInt(indexInput);
            int taskListedAmount = taskList.getTasks().size();
            if(taskListedAmount == 0){
                Communication.printErrorFeedback("The list is empty, nothing to remove");
                return null;
            }
            if(indexToRemove < 0){
                Communication.printErrorFeedback("The index value must be above or equal to 0");
                return null;
            }
            if(indexToRemove >= taskListedAmount){
                Communication.printErrorFeedback("The max valid index for removal is "+(taskListedAmount-1));
                return null;
            }
            return indexToRemove;
        } catch (NumberFormatException e) {
            Communication.printErrorFeedback("The input index must be a valid integer value");
            return null;

        } catch (Exception e) {
            Communication.printErrorFeedback("An error occurred while validating the input");
            return null;
        }
    }

    static String[] getCommandList(){
        availableActions[] availableActionsValues = availableActions.values();
        int amountOfActions = availableActionsValues.length;
        String[] availableCommands = new String[amountOfActions];

        for (int i = 0; i < amountOfActions; i++) {
            availableCommands[i] = availableActionsValues[i].name().toLowerCase();
        }
        return availableCommands;
    }
}
