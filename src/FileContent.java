import java.io.Serializable;


public class FileContent implements Serializable{
	


	private static final long serialVersionUID = -6980642919938620877L;

	private String fileName;
	private byte[] data;
	
	public FileContent(String fileName, byte[] data) {
		this.fileName = fileName;
		this.data = data;
	}
	
	public FileContent(String fileName) {
		this(fileName, null);
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	
}
