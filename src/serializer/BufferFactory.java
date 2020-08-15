package serializer;



import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class BufferFactory {

	public static ChannelBuffer getBuff() {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		return buffer;
	}
}
