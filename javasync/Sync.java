package javasync;

import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 *
 * @author dnf
 */
public class Sync {
    static private void syncFiles(FileInfo file, String destFolder) throws IOException{
        if(file.deleted)
            Files.delete(Paths.get(destFolder+file.name));
        else if(!file.deleted){
            Paths.get(destFolder+file.name).toFile().getParentFile().mkdirs();
            Files.copy(file.getPath(), Paths.get(destFolder+file.name), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    static public void syncFolders(HashSet<FileInfo> fldr1, HashSet<FileInfo> fldr2, String fldr1Name, String fldr2Name) throws IOException{
        for(FileInfo file1: fldr1){
            boolean newFile = true;
            for(FileInfo file2: fldr2){
                if(file1.name.equals(file2.name)){
                    if(file1.deleted && file2.deleted)
                        newFile = false;
                    else if(file1.deleted)
                        syncFiles(file1, fldr2Name);
                    else if(file2.deleted)
                        syncFiles(file2, fldr1Name);
                    else if(file1.lastModifiedTime.compareTo(file2.lastModifiedTime) > 0)
                        syncFiles(file1, fldr2Name);
                    else if(file1.lastModifiedTime.compareTo(file2.lastModifiedTime) < 0)
                        syncFiles(file2, fldr1Name);
                    fldr2.remove(file2);
                    newFile = false;
                    break;
                }
            }
            if(newFile)
                syncFiles(file1, fldr2Name);
        }
        for(FileInfo file2: fldr2)
            syncFiles(file2, fldr1Name);
    }
}
