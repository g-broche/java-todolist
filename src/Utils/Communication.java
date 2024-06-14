package Utils;

final class Communication {
    static void writeSuccessFeedback(String message){
        if (message.length() == 0){
            System.out.println("The operation has been performed");
        }
        System.out.println("Success: "+message);
    }
    static void writeErrorFeedback(String message){
        if (message.length() == 0){
            System.out.println("An error occured while performing the operation");
        }
        System.out.println("Error: "+message);
    }
}
