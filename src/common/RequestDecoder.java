package common;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class RequestDecoder extends FrameDecoder{
	
	public static int BASE_LENGTH = 12;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel ch, ChannelBuffer buffer) throws Exception {
		
		if(buffer.readableBytes()>=BASE_LENGTH) {
			
			int beginReader = buffer.readerIndex(); 
			//¶ÁÈ¡°üÍ·
			while(true) {
				if(buffer.readInt()==Constant.FLAG) {
					break;
				}
			}
			
			short module = buffer.readShort();
			short cmd = buffer.readShort();
			int dataLength = buffer.readInt();
			
			if(buffer.readableBytes()<dataLength) 
			{
				buffer.readerIndex(beginReader);
				return null;
			}
			byte[] data = new byte[dataLength];
			buffer.readBytes(data);
			
			Request request = new Request();
			request.setModule(module);
			request.setCmd(cmd);
			request.setData(data);
			
			return request;
		}
		
		return null;
	}

}
