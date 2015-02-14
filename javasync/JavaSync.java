package javasync;

import java.io.IOException;
import java.util.HashSet;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
/**
 *
 * @author dnf
 */
public class JavaSync {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String settingsFile = ".folder_info";
        String XMLsettingsFile = "settings.xml";
        try{
            if(args.length == 1)
                args = XML.getFolders(args[0]);
            else if(args.length == 0)
                args = XML.getFolders(XMLsettingsFile);
            Paths.get(args[0]).toFile().mkdirs();
            Paths.get(args[1]).toFile().mkdirs();
            HashSet<FileInfo> folderInfo1 = Data.getFolderInfo(args[0], settingsFile);
            HashSet<FileInfo> folderInfo2 = Data.getFolderInfo(args[1], settingsFile);
            Sync.syncFolders(folderInfo1, folderInfo2, args[0], args[1]);
            Data.saveFolderInfo(args[0], settingsFile);
            Data.saveFolderInfo(args[1], settingsFile);
        } catch(IOException ioEx){
            System.out.println(ioEx);
        } catch (ClassNotFoundException | SAXException | ParserConfigurationException ex) {
            Logger.getLogger(JavaSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
