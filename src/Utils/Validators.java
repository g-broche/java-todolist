package Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Validators {
    enum availableActions {
        CREATE,
        ADD,
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
}
