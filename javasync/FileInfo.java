package javasync;

import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author dnf
 */
public class FileInfo{
    
    final public String name;
    final public String baseFolder;
    final public FileTime lastModifiedTime;
    final public boolean deleted;
    
    
    public FileInfo(String name, String baseFolder, FileTime lastModifiedTime, boolean deleted){
        this.name = name;
        this.baseFolder = baseFolder;
        this.lastModifiedTime = lastModifiedTime;
        this.deleted = deleted;
    }

    public Path getPath(){
        return Paths.get(baseFolder+name);
    }
        
}
