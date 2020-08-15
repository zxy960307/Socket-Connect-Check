package common;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class RequestEncoder extends OneToOneEncoder{

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel ch, Object object) throws Exception {
		
		Request request = (Request)(object);
		
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(Constant.FLAG);
		buffer.writeShort(request.getModule());
		buffer.writeShort(request.getCmd());
		buffer.writeInt(request.getDataLength());
		if(request.getData() != null ) {
			buffer.writeBytes(request.getData());
		}
				
		return buffer;
	}

}
