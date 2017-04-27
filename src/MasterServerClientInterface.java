import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public interface MasterServerClientInterface extends MasterInterface {

	/**
	 * Read file from server
	 * 
	 * @param fileName
	 * @return File data
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RemoteException
	 */
	public List<ReplicaLoc> read(String fileName) throws FileNotFoundException,
			IOException, RemoteException;

	/**
	 * Start a new write transaction
	 * 
	 * @param fileName
	 * @return the required info
	 * @throws RemoteException
	 * @throws IOException
	 */
	public WriteAck write(String fileName) throws RemoteException, IOException;
	
	
	/**
	 * @param fileName
	 * @return the replica location of the primary replica of that file
	 * @throws RemoteException
	 */
	public ReplicaLoc locatePrimaryReplica(String fileName) throws RemoteException;
}
