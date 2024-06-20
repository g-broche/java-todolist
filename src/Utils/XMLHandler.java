package Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.nio.file.FileSystems;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import ToDoList.TaskList;

public class XMLHandler {
    final static String SAVE_FOLDER_NAME = "files";
    final static String SAVE_FILE_STRING = "todos.xml";
    public static boolean writeToDoFile(Iterable<TaskList> lists) {
        try {
            Document document = createXmlDom(lists);
            writeFile(document);
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
    private static Document createXmlDom(Iterable<TaskList> lists) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Create a new Document
            Document document = builder.newDocument();

            // Create root element
            Element root = document.createElement("todo");
            document.appendChild(root);
            return document;
        } catch (Exception e) {
            Communication.printErrorFeedback("Couldn't create XML DOM : " + e.getMessage());
            return null;
        }
    }

    /**
     * Writes the xml saved file for the todo lists
     * @param xmlDocument XML DOM containing the todo lists infos
     * @return true if successful, false if error occurs
     */
    private static boolean writeFile(Document xmlDocument){
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
            Transformer transformer = transformerFactory.newTransformer(); 
            DOMSource source = new DOMSource(xmlDocument); 
            String fileFullPath = getSaveFilePath();
            StreamResult result = new StreamResult(fileFullPath); 
            transformer.transform(source, result); 
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
    private static String getAppDirectory(){
        String fileDirectory = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        return fileDirectory;
    }

    /**
     * Gets the full path of the file used to save and load todos, includes the filename and extension
     * @return absolute path string with filename and file extension
     */
    private static String getSaveFilePath(){
        String appDirectory = getAppDirectory();
        String[] fileLocationParameters = new String[3];
        fileLocationParameters[0] = appDirectory;
        fileLocationParameters[1] = SAVE_FOLDER_NAME;
        fileLocationParameters[2] = SAVE_FILE_STRING;
        return String.join("\\", fileLocationParameters);
    }
}
