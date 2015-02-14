package javasync;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashSet;
      
/**
 *
 * @author dnf
 */
public class Data {
    static public HashSet<FileInfo> getFolderInfo(String folderPath, String infoFileName) throws IOException, ClassNotFoundException{
        HashSet<FileInfo> infoSet = getFolderInfo(Paths.get(folderPath), Paths.get(folderPath), new HashSet<>(), infoFileName);
        HashSet<String> filesNames;
        try{
            filesNames = loadFolderInfo(folderPath, infoFileName);
        } catch(IOException ex){
            return infoSet;
        }
        for(String name: filesNames){
            boolean deleted = true;
            for(FileInfo file: infoSet)
                if(file.name.equals(name)){
                    deleted = false;
                    break;
                }
            if(deleted)
                infoSet.add(new FileInfo(name, folderPath, null, deleted));
        }
        return infoSet;
    }
    
    static private HashSet<FileInfo> getFolderInfo(Path folderPath, Path currentPath, HashSet<FileInfo> set, String infoFileName) throws IOException{
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    getFolderInfo(folderPath, entry, set, infoFileName);
                } else {
                    String name = folderPath.relativize(entry).toString();
                    if(!name.equals(infoFileName)){
                        FileTime modifyTime = Files.getLastModifiedTime(entry);
                        set.add(new FileInfo(name, folderPath.toString()+"/", modifyTime, false));
                    }
                }
            }
            return set;
        }
    }
    
    static public void saveFolderInfo(String folderPath, String fileName) throws IOException{
        HashSet<FileInfo> infoSet = getFolderInfo(Paths.get(folderPath), Paths.get(folderPath), new HashSet<>(), fileName);
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(folderPath+fileName))){
            HashSet<String> filesNames = new HashSet<>();
            for(FileInfo file: infoSet)
                filesNames.add(file.name);
            out.writeObject(filesNames);
        }
    }
    
    static public HashSet<String> loadFolderInfo(String folderPath, String fileName) throws IOException, ClassNotFoundException{
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(folderPath+fileName))){
            return (HashSet<String>) in.readObject();
        }
    }
}
