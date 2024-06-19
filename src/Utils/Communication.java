package Utils;

import java.util.Scanner;

final public class Communication {
    final static String messageRequestInput = ">>> ";
    final static String messageSuccessPrefix = "** ";
    final static String messageErrorPrefix = "!! ";
    final static String messageActionPrefix = "-> ";
    final static String messageListingPrefix = "++ ";

    /**
     * sent message to terminal on successful operation using the out stream
     * @param message
     */
    public static void printSuccessFeedback(String message){
        if (message.length() == 0){
            System.out.println(Communication.messageSuccessPrefix+"The operation has been performed");
        }
        System.out.println(Communication.messageSuccessPrefix+"Success: "+message);
    }

    /**
     * sent message to terminal on error using the err stream
     * @param message
     */
    public static void printErrorFeedback(String message){
        if (message.length() == 0){
            System.err.println(Communication.messageErrorPrefix+"An error occured while performing the operation");
        }
        System.err.println(Communication.messageErrorPrefix+"Error: "+message);
    }

    /**
     * print a message to terminal using regular out stream
     * @param message
     */
    public static void printMessage(String message){
        System.out.println(Communication.messageActionPrefix+message);
    }   

    /**
     * print a message to terminal using regular out stream requesting a user input
     * @param message
     */
    public static void printRequestForInputMessage(String message){
        System.out.println(Communication.messageRequestInput+message);
    }  
    
    /**
     * print a message to terminal using regular out stream for listing items
     * @param message
     */
    public static void printRequestListItem(String message){
        System.out.println(Communication.messageListingPrefix+message);
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
        Communication.printRequestForInputMessage(messagePrompt);
		String userInput = scanner.nextLine(); 
        return userInput;
    }

    /**
     * For an iterable of strings, print to terminal every string contained in the collection
     * @param iterable iterable containing String elements
     */
    public static void printStringCollection(Iterable<String> iterable){
        int index = 0;
        for (String stringItem : iterable) {
            Communication.printRequestListItem("["+index+"]"+stringItem);
            index++;
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
            Utils.Communication.printErrorFeedback(messageError);
            return;
        }
        messageSuccess = messageSuccess == null ? "The operation was successful" : messageSuccess; 
        Utils.Communication.printSuccessFeedback(messageSuccess);
    }

    public static void printCommandList(){
        String[] commands = Validators.getCommandList();
    }
}
