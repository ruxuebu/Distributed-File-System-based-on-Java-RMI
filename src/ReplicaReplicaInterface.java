import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface ReplicaReplicaInterface extends ReplicaInterface {
	
	public boolean reflectUpdate(long txnID, String fileName, ArrayList<byte[]> data) throws RemoteException, IOException;
	
	public void releaseLock(String fileName)throws RemoteException;
}
