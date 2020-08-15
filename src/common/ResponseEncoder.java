package common;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class ResponseEncoder extends OneToOneEncoder{

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel ch, Object object) throws Exception {
		
		Response response = (Response)(object);
		
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(Constant.FLAG);
		buffer.writeShort(response.getModule());
		buffer.writeShort(response.getCmd());
		buffer.writeInt(response.getStateCode());
		buffer.writeInt(response.getDataLength());
		if(response.getData() != null ) {
			buffer.writeBytes(response.getData());
		}
				
		return buffer;
	}

}
