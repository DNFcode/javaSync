package remote;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasync.Data;
import javasync.FileInfo;
import javasync.Sync;

/**
 *
 * @author dnf
 */
public class SyncServer implements ServerIntf{
    
    private int TCPport;

    @Override
    public void getFiles(ClientIntf stub, HashSet<FileInfo> folderInfo2, String folderName2, String folderName1) throws RemoteException, IOException, ClassNotFoundException{
        System.out.println("CLIENT CONNECTED");
        HashSet<FileInfo> folderInfo1 = Data.getFolderInfo(folderName1);
        Sync sync = new Sync(folderInfo1, folderInfo2, folderName1, folderName2, false);
        sync.run();
        Thread host = new Thread(new HostSocket(null, TCPport, sync.filesToUpload, folderName1, true));
        host.start();
        System.out.println("ASK FOR FILES");
        stub.getFiles(sync.filesToDownload);
    }
    
    public void start(int port, int TCPport){
        this.TCPport = TCPport;
        
        try {
            ServerIntf stub = (ServerIntf)UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("javaSync", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            Logger.getLogger(SyncServer.class.getName()).log(Level.SEVERE, null, e);
            System.exit (1);
        }
    }
}
