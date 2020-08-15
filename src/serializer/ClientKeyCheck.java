package serializer;

public class ClientKeyCheck extends Serializer {

	private int num;
	
	private String key;
	
	@Override
	public void read() {
		this.num = readInt();
		this.key = readString();
		
	}

	@Override
	public void write() {
		writeInt(this.num);
		writeString(this.key);
		
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
