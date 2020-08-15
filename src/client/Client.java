package client;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import common.RequestDecoder;
import common.RequestEncoder;
import common.ResponseDecoder;
import common.ResponseEncoder;

public class Client {

	public static void main(String[] args ) {
		ClientBootstrap bootstrap = new ClientBootstrap();
		
		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();
		
		bootstrap.setFactory(new NioClientSocketChannelFactory(boss,worker));
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new ResponseDecoder());
				pipeline.addLast("encoder", new RequestEncoder());
				pipeline.addLast("clientHandler", new ClientHandler());
				return pipeline;
			}
			
		});
		
		ChannelFuture connect = bootstrap.connect(new InetSocketAddress("127.0.0.1",8106));
//		Channel channel =(Channel) connect.getChannel();
		
	}
}
