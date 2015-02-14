package javasync;

import java.io.IOException;
import java.util.HashSet;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author dnf
 */
public class JavaSync {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            String settingsFile = ".folder_info";
            Paths.get(args[0]).toFile().mkdirs();
            Paths.get(args[1]).toFile().mkdirs();
            HashSet<FileInfo> folderInfo1 = Data.getFolderInfo(args[0], settingsFile);
            HashSet<FileInfo> folderInfo2 = Data.getFolderInfo(args[1], settingsFile);
            Sync.syncFolders(folderInfo1, folderInfo2, args[0], args[1]);
            Data.saveFolderInfo(args[0], settingsFile);
            Data.saveFolderInfo(args[1], settingsFile);
        } catch(IOException ioEx){
            System.out.println(ioEx);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JavaSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
