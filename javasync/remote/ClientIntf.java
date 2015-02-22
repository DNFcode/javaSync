package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import javasync.FileInfo;
/**
 *
 * @author dnf
 */
public interface ClientIntf extends Remote{
    public void getFiles(HashSet<FileInfo> folderInfo2) throws RemoteException;
}
