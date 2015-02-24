package remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import javasync.Data;
import javasync.FileInfo;
import javasync.MainJFrame;
/**
 *
 * @author dnf
 */
public class HostSocket implements Runnable{
    
    private final String hostIP;
    private final int port;
    private final HashSet<FileInfo> files;
    private final String folder;
    private final boolean isHost;
    private final MainJFrame frame;

    public HostSocket(String hostIP, int port, HashSet<FileInfo> files, String folder, boolean isHost, MainJFrame frame){
        this.port = port;
        this.files = files;
        this.folder = folder;
        this.isHost = isHost;
        this.hostIP = hostIP;
        this.frame = frame;
    }
    
    public void sendFiles(Socket socket) throws IOException{
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[8192];
        dos.writeLong(files.size());
        float progress = frame.ProgressBar.getValue();
        frame.ProgressBarLabel.setText("Status: sending files...");
        for (FileInfo fileInfo: files) {
            progress += 50/files.size() +0.5;
            frame.ProgressBar.setValue((int) progress+1);
            File file = fileInfo.getFile();
            FileInputStream fis = new FileInputStream(file);
            dos.writeUTF(fileInfo.name);
            dos.writeLong(file.length());
            int read = 0;
            while ((read = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, read);
            }
        }
        frame.ProgressBarLabel.setText("Status: done!");
    }
    
    public void getFiles(Socket socket) throws IOException{
        byte[] buffer = new byte[8192];
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        float progress = frame.ProgressBar.getValue();
        long amount = dis.readLong();
        frame.ProgressBarLabel.setText("Status: getting files...");
        for(long i = 0; i<amount; i++) {
            progress += 50/amount +0.5;
            frame.ProgressBar.setValue((int) progress);
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();
            (new File(folder+fileName)).getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(folder+fileName, false)) {
                long rounds = fileSize/buffer.length;
                long tail = fileSize%buffer.length;
                for (int j = 0; j < rounds; j++) {
                    dis.readFully(buffer);
                    fos.write(buffer);
                }
                dis.readFully(buffer,0, (int) tail);
                fos.write(buffer,0, (int) tail);
            }
        }
        frame.ProgressBarLabel.setText("Status: done!");
        Data.saveFolderInfo(folder);
    }
    
    @Override
    public void run() {
        if(isHost){
            try(ServerSocket ss = new ServerSocket(port)){
                
                Socket socket = ss.accept();
                getFiles(socket);
                sendFiles(socket);
            } catch (Exception ex) {
                Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*try(ServerSocket ss = new ServerSocket(2525)){
                Socket socket = ss.accept();
                
            } catch (IOException ex) {
                Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
        else{
            try(Socket socket = new Socket()){
                socket.connect(new InetSocketAddress(hostIP, port), 10000);
                sendFiles(socket);
                getFiles(socket);
                
            } catch (Exception ex) {
                Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*try(Socket socket = new Socket()){
                socket.connect(new InetSocketAddress(hostIP, 2525), 10000);
                
            } catch (IOException ex) {
                Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
        
        
    }
    
}
