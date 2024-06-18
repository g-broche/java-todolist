package ToDoList;

import java.util.ArrayList;
import java.util.List;

import Utils.Communication;

/**
 * TaskList
 * Class containing logic to instantiate a task list related to a topic and manage
 * the associated list of tasks
 */
public class TaskList {
    private String label;
    private List<String> tasks = new ArrayList<String>();

    /**
     * Constructs a new TaskList object
     * @param topicName name for the global topic the list of tasks are associated with
     * @throws IllegalArgumentException
     */
    public TaskList(String Listlabel) throws IllegalArgumentException{
        if (Listlabel.length() == 0){
            throw new IllegalArgumentException("The name of a list must not be null"); 
        }
        this.label = Listlabel;
    }

    /**
     * Gets label of this instance of TaskList
     * @return TaskList label
     */
    public String getLabel(){
        return this.label;
    }

    /**
     * Gets list of task featured in this TaskList
     * @return List of the string representing each task
     */
    public List<String> getTasks(){
        return this.tasks;
    }

    /**
     * Checks if a specific task is present inside this list's tasks
     * @param taskName Name of task to look for
     * @return True if task is present, false otherwise
     */
    public boolean containsTaskName(String taskName){
        return this.tasks.contains(taskName);
    }

    /**
     * Adds a new task to the list after performing input checks
     * @param newTaskName name of task to add
     * @return true if the collection changed implying the task was added
     * @throws IllegalArgumentException
     */
    public boolean addTask(String newTaskName) throws IllegalArgumentException{
        if (newTaskName.length() == 0){
            throw new IllegalArgumentException("The name of a task must not be null"); 
        }
        if (this.doesTaskExistsInList(newTaskName)){
            throw new IllegalArgumentException("A task with this name has already been listed"); 
        }
        return this.tasks.add(newTaskName);   
    }

    // implement modifyTask

    // implement removeTask
    public boolean removeTask(int indexToRemove){
        try {
            String taskToRemoved = this.tasks.remove(indexToRemove);
            boolean isSuccess = !taskToRemoved.isEmpty();
            Communication.printInstructionResult(
                isSuccess,
                "Task <"+taskToRemoved+"> has been removed",
                "Failed to remove task at index "+indexToRemove
                );
            return isSuccess;
        } catch (Exception e) {
            Communication.writeErrorFeedback(e.getMessage());
            return false;
        }
        
        
    }

    // implement displayTaskList
    public void displayTaskList(){
        if(this.tasks.isEmpty()){
            Communication.printMessage("The list "+this.label+" is currently empty!");
            return;
        }
        Communication.printMessage("Displaying content of list "+this.label+":");
        Communication.printStringCollection(tasks);
    }

    /**
     * Checks if a tasks is present in the instance tasks attribute
     * @param taskName
     * @return true if task is present and false otherwise
     */
    private boolean doesTaskExistsInList(String taskName){
        return this.tasks.contains(taskName);
    }
}