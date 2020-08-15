package common;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class ResponseDecoder extends FrameDecoder{
	
	public static int BASE_LENGTH = 12;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel ch, ChannelBuffer buffer) throws Exception {
		
		if(buffer.readableBytes()>=12) {
			
			int beginReader = buffer.readerIndex(); 
			//¶ÁÈ¡°üÍ·
			while(true) {
				if(buffer.readInt()==Constant.FLAG) {
					break;
				}
			}
			
			short module = buffer.readShort();
			short cmd = buffer.readShort();
			int stateCode = buffer.readInt();
			int dataLength = buffer.readInt();
			
			if(buffer.readableBytes()<dataLength) 
			{
				buffer.readerIndex(beginReader);
				return null;
			}
			byte[] data = new byte[dataLength];
			buffer.readBytes(data);
			
			Response response = new Response();
			response.setModule(module);
			response.setCmd(cmd);
			response.setStateCode(stateCode);
			response.setData(data);
			
			return response;
		}
		
		return null;
	}

}
