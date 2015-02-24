package javasync;

import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author dnf
 */
public class Sync implements Runnable{

    final private HashSet<FileInfo> fldr1;
    final private HashSet<FileInfo> fldr2;
    final private String fldr1Name;
    final private String fldr2Name;
    final private boolean local;
    final private MainJFrame frame;
    public HashSet<FileInfo> filesToDownload;
    public HashSet<FileInfo> filesToUpload;
            
    
    public Sync(HashSet<FileInfo> fldr1, HashSet<FileInfo> fldr2, String fldr1Name, String fldr2Name, boolean local, MainJFrame frame) {
        this.fldr1 = fldr1;
        this.fldr1Name = fldr1Name;
        this.fldr2 = fldr2;
        this.fldr2Name = fldr2Name;
        this.local = local;
        this.frame = frame;
        if(!local){
            filesToDownload = new HashSet<>();
            filesToUpload = new HashSet<>();
        }
    }
    
    private void syncFiles(FileInfo file, String destFolder) throws IOException{
        if(local){
            if(file.deleted)
                Files.delete(Paths.get(destFolder+file.name));
            else if(!file.deleted){
                Paths.get(destFolder+file.name).toFile().getParentFile().mkdirs();
                Files.copy(file.getPath(), Paths.get(destFolder+file.name), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        else if(file.deleted && destFolder == fldr1Name)
            Files.delete(Paths.get(destFolder+file.name));
        else if(destFolder == fldr1Name || file.deleted && destFolder == fldr2Name)
            filesToDownload.add(file);
        else if(destFolder == fldr2Name)
            filesToUpload.add(file);
    }
    
    public void syncFolders() throws IOException{
        int progress = 0;
        if(frame != null)
            frame.ProgressBarLabel.setText("Status: synchronization in progress...");
        for(FileInfo file1: fldr1){
            progress += 100/fldr1.size();
            if(frame != null)
                frame.ProgressBar.setValue((int) progress+1);
            boolean newFile = true;
            for(FileInfo file2: fldr2){
                if(file1.name.equals(file2.name)){
                    if(file1.deleted && file2.deleted)
                        newFile = false;
                    else if(file1.deleted)
                        syncFiles(file1, fldr2Name);
                    else if(file2.deleted)
                        syncFiles(file2, fldr1Name);
                    else if(file1.getLastModTime().compareTo(file2.getLastModTime()) > 0)
                        syncFiles(file1, fldr2Name);
                    else if(file1.getLastModTime().compareTo(file2.getLastModTime()) < 0)
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
        if(frame != null)
            frame.ProgressBarLabel.setText("Status: done!");
    }

    @Override
    public void run() {
        try {
            syncFolders();
            if(local){
                javasync.Data.saveFolderInfo(fldr1Name);
                javasync.Data.saveFolderInfo(fldr2Name);
            }
        } catch (IOException ex) {
            Logger.getLogger(Sync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
