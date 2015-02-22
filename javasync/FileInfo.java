package javasync;

import java.io.File;
import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author dnf
 */
public class FileInfo implements Serializable{
    
    final public String name;
    final public String baseFolder;
    final public long lastModifiedTime;
    final public boolean deleted;
    
    
    public FileInfo(String name, String baseFolder, FileTime lastModifiedTime, boolean deleted){
        this.name = name;
        this.baseFolder = baseFolder;
        this.lastModifiedTime = lastModifiedTime==null?0:lastModifiedTime.toMillis();
        this.deleted = deleted;
    }

    public Path getPath(){
        return Paths.get(baseFolder+name);
    }       
    
    public FileTime getLastModTime(){
        return FileTime.fromMillis(lastModifiedTime);
    }
    
    public File getFile(){
        return new File(baseFolder+name);
    }
}
