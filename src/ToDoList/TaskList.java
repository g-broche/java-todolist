package ToDoList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskList
 * Class containing logic to instantiate a task list related to a topic and manage
 * the associated list of tasks
 */
public class TaskList {
    private String topic;
    private List<String> tasks = new ArrayList<String>();

    /**
     * Constructs a new TaskList object
     * @param topicName name for the global topic the list of tasks are associated with
     * @throws IOException
     */
    public TaskList(String topicName) throws IOException{
        if (topicName.length() == 0){
            throw new IOException("The name of a topic must not be null"); 
        }
        this.topic = topicName;
    }

    /**
     * Adds a new task to the list after performing input checks
     * @param newTaskName
     * @throws IOException
     */
    public void addTask(String newTaskName) throws IOException{
        if (newTaskName.length() == 0){
            throw new IOException("The name of a task must not be null"); 
        }
        if (this.doesTaskExistsInList(newTaskName)){
            throw new IOException("A task with this name has already been listed"); 
        }
        this.tasks.add(newTaskName);   
    }

    // implement modifyTask

    // implement removeTask

    // implement displayTaskList

    /**
     * Checks if a tasks is present in the instance tasks attribute
     * @param taskName
     * @return true if task is present and false otherwise
     */
    private boolean doesTaskExistsInList(String taskName){
        return this.tasks.contains(taskName);
    }
}