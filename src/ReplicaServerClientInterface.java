import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;


public interface ReplicaServerClientInterface extends ReplicaInterface {
	/**
	 * @param fileName
	 * @return File data
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RemoteException
	 */
	public FileContent read(String fileName)
			throws FileNotFoundException, RemoteException, IOException;
	
	/**
	 * 
	 * @param txnID
	 *            : the ID of the transaction to which this message relates
	 * @param msgSeqNum
	 *            : the message sequence number. Each transaction starts with
	 *            message sequence number 1.
	 * @param data
	 *            : data to write in the file
	 * @return message with required info
	 * @throws IOException
	 * @throws RemoteException
	 */
	public ChunkAck write(long txnID, long msgSeqNum, FileContent data)
			throws RemoteException, IOException;
	
	/**
	 * 
	 * @param txnID
	 *            : the ID of the transaction to which this message relates
	 * @param numOfMsgs
	 *            : Number of messages sent to the server
	 * @return true for acknowledgment
	 * @throws MessageNotFoundException
	 * @throws RemoteException
	 * @throws IOException 
	 */
	public boolean commit(long txnID, long numOfMsgs)
			throws MessageNotFoundException, RemoteException, IOException;
	
	/**
	 * * @param txnID: the ID of the transaction to which this message relates
	 * 
	 * @return true for acknowledgment
	 * @throws RemoteException
	 */
	public boolean abort(long txnID) throws RemoteException;
}
