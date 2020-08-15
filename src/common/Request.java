package common;

public class Request {

	private short module;
	
	private short cmd;
	
	private byte[] data;

	public short getModule() {
		return module;
	}

	public void setModule(short module) {
		this.module = module;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public int getDataLength() {
		if(data == null)
			return 0;
		return data.length;
	}

}
