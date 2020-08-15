package serializer;



import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

public abstract class Serializer {

	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	protected ChannelBuffer writeBuffer;
	
	protected ChannelBuffer readBuffer;
	
	public abstract void read();
	
	public abstract void write();
	
	public Serializer writeInt(Integer value) {
		writeBuffer.writeInt(value);
		return this;
	}
	
	public Serializer writeShort(Short value) {
		writeBuffer.writeShort(value);
		return this;
	}
	
	public Serializer writeString(String value) {
		
		if(value == null || value.equals(""))
		{
			writeBuffer.writeShort(0);
			return this;
		}
		
		byte[] data = value.getBytes(CHARSET);
		short len = (short) data.length; 
		writeBuffer.writeShort(len);
		writeBuffer.writeBytes(data);
		return this;
	}
	
	public String readString() {
		int size = readBuffer.readShort();
		if(size == 0) 
			return "";
		
		byte[] bytes = new byte[size];
		readBuffer.readBytes(bytes);
		
		return new String(bytes,CHARSET);
	}
	
	public int readInt() {
		return readBuffer.readInt();
	}
	
	public short readShort() {
		return readBuffer.readShort();
	}
	
	
	public void readFromBytes(byte[] bytes) {
		readBuffer = BufferFactory.getBuff();
		readBuffer.writeBytes(bytes);
		read();
		readBuffer.clear();
	}
	
	public byte[] getBytes() {
		writeToLocalBuff();
		byte[] bytes = null;
		if(writeBuffer.writerIndex() == 0) {
			bytes = new byte[0];
		}
		else {
			bytes = new byte[writeBuffer.writerIndex()];
			writeBuffer.readBytes(bytes);
		}
		
		writeBuffer.clear();
		return bytes;
	}
	
	public ChannelBuffer writeToLocalBuff() {
		writeBuffer = BufferFactory.getBuff();
		write();
		return writeBuffer;
	}
}
