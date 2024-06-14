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
}
