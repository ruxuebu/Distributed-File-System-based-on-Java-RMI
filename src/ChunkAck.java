import java.io.Serializable;


public class ChunkAck implements Serializable{

	
	private static final long serialVersionUID = 4267009886985001938L;
	
	private long transactionId;
	private long seqNo;

	
	public ChunkAck(long tid, long seqNo) {
		this.transactionId = tid;
		this.seqNo = seqNo;
	}
	
	public long getSeqNo(){
		return seqNo;
	}
	
	public long getTxnID() {
		return transactionId;
	}
}
