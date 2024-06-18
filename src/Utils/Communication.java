package Utils;

import java.util.Scanner;

final public class Communication {
    /**
     * sent message to terminal on successful operation using the out stream
     * @param message
     */
    public static void writeSuccessFeedback(String message){
        if (message.length() == 0){
            System.out.println("The operation has been performed");
        }
        System.out.println("Success: "+message);
    }

    /**
     * sent message to terminal on error using the err stream
     * @param message
     */
    public static void writeErrorFeedback(String message){
        if (message.length() == 0){
            System.err.println("An error occured while performing the operation");
        }
        System.err.println("Error: "+message);
    }

    /**
     * Request input from user
     * @param scanner Instanced scanner
     * @param messagePrompt Instruction for the user
     * @return Input from user
     * @throws IllegalArgumentException
     */
    public static String requestUserAction(Scanner scanner, String messagePrompt) throws IllegalArgumentException{
        if(scanner == null){
            throw new IllegalArgumentException("Scanner has not been instanced");
        }
        if(messagePrompt == null){
            throw new IllegalArgumentException("User is missing prompt for next action");
        }
        System.out.println(messagePrompt);
		String userInput = scanner.nextLine(); 
        return userInput;
    }

    /**
     * print a message to terminal using regular out stream
     * @param message
     */
    public static void printMessage(String message){
            System.out.println(message);
    }
    /**
     * For an iterable of strings, print to terminal every string contained in the collection
     * @param iterable iterable containing String elements
     */
    public static void printStringCollection(Iterable<String> iterable){
        for (String stringItem : iterable) {
            System.out.println(" -> "+stringItem);
        }
    }
    /**
     * Print a feedback for user based on a boolean to define if must print success or error feedback
     * @param isSuccess 
     * @param messageSuccess
     * @param messageError
     */
    public static void printInstructionResult(boolean isSuccess, String messageSuccess, String messageError){
        if(!isSuccess){
            messageError = messageError == null ? "An error occurred" : messageError; 
            Utils.Communication.writeErrorFeedback(messageError);
            return;
        }
        messageSuccess = messageSuccess == null ? "The operation was successful" : messageSuccess; 
        Utils.Communication.writeSuccessFeedback(messageSuccess);
    }
}
