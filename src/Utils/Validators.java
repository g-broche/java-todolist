package Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ToDoList.TaskList;

/**
 * Class providing static methods for the purpose of validating certain operations.
 */
public class Validators {
    /**
     * Defines allowed user command which correspond to the first word of user inputs
     */
    public enum availableActions {
        CREATE,
        ADD,
        DEL,
        DELLIST,
        SHOW,
        SHOWALL,
        SHOWLISTS,
        SWITCH,
        STOP,
        SAVE,
        LOAD,
        HELP,

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
     * Validate the inputed index to remove from the list
     * @param indexInput String corresponding to the required index value for element removal
     * @param list list from which an element removal request is ongoing
     * @return index to remove as Integer if input is not out of bounds, null otherwise
     */
    public static <E> Integer validateRemovalRequestForIndex(String indexInput, List<E> list){
        try {
            int indexToRemove = Integer.parseInt(indexInput);
            int listAmount = list.size();
            if(listAmount == 0){
                Communication.printErrorFeedback("The list is empty, nothing to remove");
                return null;
            }
            if(indexToRemove < 0){
                Communication.printErrorFeedback("The index value must be above or equal to 0");
                return null;
            }
            if(indexToRemove >= listAmount){
                Communication.printErrorFeedback("The max valid index for removal is "+(listAmount-1));
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

    
    /**
     * Validate the inputed task index to remove from the list
     * @param indexInput String corresponding to the required index value for task removal
     * @param taskList TaskList from which a task removal request is ongoing
     * @return index to remove as Integer if input is not out of bounds, null otherwise
     */
    public static Integer validateListSwitchRequest(String indexInput, List<TaskList> lists){
        try {
            int indexToRemove = Integer.parseInt(indexInput);
            int listAmount = lists.size();
            if(listAmount == 0){
                Communication.printErrorFeedback("There are no list created at the present time");
                return null;
            }
            if(indexToRemove < 0){
                Communication.printErrorFeedback("The index value must be above or equal to 0");
                return null;
            }
            if(indexToRemove >= listAmount){
                Communication.printErrorFeedback("The max valid index for removal is "+(listAmount-1));
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

    /**
     * gets array of all allowed command
     * @return array of string
     */
    static String[] getCommandList(){
        availableActions[] availableActionsValues = availableActions.values();
        int amountOfActions = availableActionsValues.length;
        String[] availableCommands = new String[amountOfActions];

        for (int i = 0; i < amountOfActions; i++) {
            availableCommands[i] = availableActionsValues[i].name().toLowerCase();
        }
        return availableCommands;
    }

    /**
     * Checks if a string contains a value
     * @param string
     * @return true if list is not null, empty or filled with space characters
     */
    public static boolean isStringNullOrEmptyOrBlank(String string){
        return string == null || string.isEmpty() || string.isBlank();
    }
}
