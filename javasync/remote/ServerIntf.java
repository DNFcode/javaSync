package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import javasync.FileInfo;
/**
 *
 * @author dnf
 */
public interface ServerIntf extends Remote{
    public void getFiles(ClientIntf stub, HashSet<FileInfo> folderInfo2, String folderName2, String folderName1) throws RemoteException, IOException, ClassNotFoundException;
}
