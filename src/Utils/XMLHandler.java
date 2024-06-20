package Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.nio.file.FileSystems;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import ToDoList.TaskList;

public class XMLHandler {
    final static String SAVE_FOLDER_NAME = "files";
    final static String SAVE_FILE_STRING = "todos.xml";
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;
    TransformerFactory transformerFactory; 
    Transformer transformer; 

    public XMLHandler(){
        try {
            this.factory = DocumentBuilderFactory.newInstance();
            this.builder = this.factory.newDocumentBuilder();
            this.document = builder.newDocument();

            this.transformerFactory = TransformerFactory.newInstance(); 
            this.transformer = transformerFactory.newTransformer(); 
            this.transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            this.transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        } catch (Exception e) {
            Communication.printErrorFeedback("Couldn't initialize xml builder : " + e.getMessage());
        }
        
    }

    /**
     * Write lists to a file in the application
     * @param lists TaskLists to save
     * @return true if successful and false otherwise
     */
    public boolean writeToDoFile(List<TaskList> lists) {
        try {
            createXmlDom(lists);
            writeFile();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Create and return the XML document containing the todo lists
     * @param lists iterable of TaskList elements to save to xml
     * @return XML Document or null if an error occured
     */
    private Document createXmlDom(List<TaskList> lists) {
        try {
            Element root = this.document.createElement("todo");
            this.document.appendChild(root);
            Element[] listsNode = this.createTodoListNodeArray(lists);
            this.appendListsToDocumentRoot(root, listsNode);
            return document;
        } catch (Exception e) {
            Communication.printErrorFeedback("Couldn't create XML DOM : " + e.getMessage());
            return null;
        }
    }

    private void appendListsToDocumentRoot(Element root, Element[] listNodes){
        for (Element listNode : listNodes) {
            root.appendChild(listNode);
        }
    }

    /**
     * creates and returns an array of xml element nodes corresponding to all existing lists
     * @param taskLists
     * @return
     */
    private Element[] createTodoListNodeArray(List<TaskList> taskLists){
        int listAmount = taskLists.size();
        Element[] listNodes = new Element[listAmount];
        for (int i = 0; i < listAmount; i++) {
            listNodes[i] = createTaskListNode(taskLists.get(i));
        }
        return listNodes;
    }

    /**
     * creates a node for a TaskList information
     * @param taskList
     * @return created list node
     */
    private Element createTaskListNode(TaskList taskList){
        Element list = this.document.createElement("list");

        Element listLabel = this.document.createElement("label");
        listLabel.appendChild(this.document.createTextNode(taskList.getLabel()));

        Element listTasks = this.createTasksNode(taskList);
        list.appendChild(listLabel);
        list.appendChild(listTasks);
        return list;
    }

    /**
     * creates the tasks node for a specific TaskList
     * @param taskList
     * @return created tasks node
     */
    private Element createTasksNode(TaskList taskList){
        Element tasks = this.document.createElement("tasks");
        for (String taskName: taskList.getTasks()) {
            Element taskNode = this.document.createElement("task");
            taskNode.appendChild(this.document.createTextNode(taskName));
            tasks.appendChild(taskNode);
        }
        return tasks;
    }

    /**
     * Writes the xml saved file for the todo lists
     * @param xmlDocument XML DOM containing the todo lists infos
     * @return true if successful, false if error occurs
     */
    private boolean writeFile(){
        try {
            DOMSource source = new DOMSource(this.document); 
            String fileFullPath = getSaveFilePath();
            StreamResult result = new StreamResult(fileFullPath); 
            this.transformer.transform(source, result); 
            System.out.println("XML file created successfully!"); 
            return true;
        } catch (Exception e) {
            Communication.printErrorFeedback("Couldn't create XML DOM : " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets current application root directory
     * @return absolute path string
     */
    private String getAppDirectory(){
        String fileDirectory = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        return fileDirectory;
    }

    /**
     * Gets the full path of the file used to save and load todos, includes the filename and extension
     * @return absolute path string with filename and file extension
     */
    private String getSaveFilePath(){
        String appDirectory = getAppDirectory();
        String[] fileLocationParameters = new String[3];
        fileLocationParameters[0] = appDirectory;
        fileLocationParameters[1] = SAVE_FOLDER_NAME;
        fileLocationParameters[2] = SAVE_FILE_STRING;
        return String.join("\\", fileLocationParameters);
    }
}
