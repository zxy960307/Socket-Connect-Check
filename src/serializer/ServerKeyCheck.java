package serializer;

public class ServerKeyCheck extends Serializer {

	private int num;
	
	@Override
	public void read() {
		this.num = readInt();	
	}

	@Override
	public void write() {
		writeInt(this.num);		
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	

	
}
