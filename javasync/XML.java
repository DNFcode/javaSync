package javasync;

import java.io.File; 
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author dnf
 */
public class XML {
    static public String[] getFolders(String fileName) throws IOException, SAXException, ParserConfigurationException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder(); 
        Document doc = db.parse(new File(fileName));
        NodeList folders = doc.getChildNodes().item(0).getChildNodes();
        String[] foldersList = new String[2];
        for(int i = 0, j = 0; i<folders.getLength(); i++){
            if(folders.item(i).getNodeName().equals("folder")){
                foldersList[j] = folders.item(i).getAttributes().getNamedItem("name").getNodeValue();
                j++;
            }
        }
        return foldersList;
    }
}
