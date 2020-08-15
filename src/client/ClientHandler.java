package client;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import Utils.OTP;
import Utils.PasswordManager;
import Utils.PropertiesUtils;
import Utils.RSACoder;
import common.Request;
import common.Response;
import constant.PropertiesFilePath;
import serializer.ClientKeyCheck;
import serializer.ClientOTPCheck;
import serializer.ServerKeyCheck;

public class ClientHandler extends SimpleChannelHandler {
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
				
		Response response = (Response)(e.getMessage());		
		Channel  channel = ctx.getChannel();		
		Request request;
		String serverIp = e.getChannel().getRemoteAddress().toString().
				substring(1,e.getChannel().getRemoteAddress().toString().indexOf(":"));
		
		//模块1,验证连接模块
		if(response.getModule() == 1) {
			
			int cmd = response.getCmd();
			switch (cmd) {
			//key校验
			case 1:{		
				//生成随机字符串
				String randomText = PasswordManager.getRandomString(10);
				
				request = new Request();
				ServerKeyCheck serverKeyCheck = new ServerKeyCheck();
				serverKeyCheck.readFromBytes(response.getData());
				ClientKeyCheck clientKeyCheck = new ClientKeyCheck();
				int keyNum =serverKeyCheck.getNum();
				
				//生成加密后的字符串
				String codeStr = RSACoder.encryptByPrivateKeyStr(randomText, keyNum);
				clientKeyCheck.setNum(keyNum);
				clientKeyCheck.setKey(codeStr);
				
				request.setModule((short) 1);
				request.setCmd((short) 1);
				request.setData(clientKeyCheck.getBytes());
				
				channel.write(request);
				
				//将随机串写入propert文件
				PropertiesUtils.writeProperties(serverIp, randomText, PropertiesFilePath.serversFilePath);
				break;
			}
			//OTP校验
			case 2:{
				//查询properties对应 key
				String key = PropertiesUtils.readValue(PropertiesFilePath.serversFilePath, serverIp);
				if(key == null || key.equals(""))
					break;
				
				//根据key获取当前OTP
				OTP otp = new OTP(key,(long) OTP.INIT_TIME,OTP.STEP);
				ClientOTPCheck clientOTPCheck = new ClientOTPCheck();
				clientOTPCheck.setOTPPassword(otp.getOTP());
				
				//包装request
				request = new Request();
				request.setCmd((short) 2);
				request.setModule((short) 1);
				request.setData(clientOTPCheck.getBytes());	
				
				channel.write(request);
				
				break;
			}		
			//返回码
			case 3:{
				System.out.println(response.getStateCode());
				break;
			}
			default:
				break;
			}
		}
		// TODO Auto-generated method stub
		super.messageReceived(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelClosed(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		super.exceptionCaught(ctx, e);
	}



	
}
