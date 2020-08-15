package common;

/**
 * 包头   | 模块号 | 命令号 | 状态码 | 长度 | 数据
 * 
 * @author 41463
 *
 */


public class Response {

	private short module;
	
	private short cmd;
	
	private int stateCode;
	
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
	
	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

}
