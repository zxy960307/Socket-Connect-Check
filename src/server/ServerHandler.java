package server;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import Utils.OTP;
import Utils.PropertiesUtils;
import Utils.RSACoder;
import client.Client;
import common.Request;
import common.Response;
import common.StateCode;
import constant.PropertiesFilePath;
import serializer.ClientKeyCheck;
import serializer.ClientOTPCheck;
import serializer.ServerKeyCheck;

public class ServerHandler extends SimpleChannelHandler {

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		
		String clientIp = e.getChannel().getRemoteAddress().toString().
				substring(1,e.getChannel().getRemoteAddress().toString().indexOf(":"));
		System.out.println("���µĿͻ�������,ip��ַΪ"+clientIp);
		Channel channel = ctx.getChannel();
		short module = (short)1;
		short cmd;
		Response response;
		//����Ƿ����״�����
		String key = PropertiesUtils.readValue(PropertiesFilePath.clientsFilePath, clientIp);
		//�״����� cmdΪ1
		if(key == null || key.equals("")) {
			cmd = (short)1;
			int randomNum = RSACoder.getPublicKeyRandomSize();
			response = new Response();
			ServerKeyCheck serverKeyCheck = new ServerKeyCheck();
			serverKeyCheck.setNum(randomNum);
			response.setModule(module);
			response.setCmd(cmd);
			response.setStateCode(StateCode.PROCESSING);
			response.setData(serverKeyCheck.getBytes());
			
			channel.write(response);
			
		}
		//��n������,otp��֤,cmdΪ2
		else  {
			cmd = (short)2;
			response = new Response();
			response.setModule(module);
			response.setCmd(cmd);
			response.setStateCode(StateCode.PROCESSING);
			channel.write(response);
		}
		
		super.channelConnected(ctx, e);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		String clientIp = e.getChannel().getRemoteAddress().toString().
				substring(1,e.getChannel().getRemoteAddress().toString().indexOf(":"));
		Channel channel = ctx.getChannel();
		Request request = (Request)e.getMessage();
		int module = request.getModule();
		int cmd = request.getCmd();
		Response response;
		boolean checkResult = false;
		if(module == 1) {
			//keyУ��
			if(cmd == 1) {			
				ClientKeyCheck clientKeyCheck = new ClientKeyCheck();
				clientKeyCheck.readFromBytes(request.getData());
				int keyNum = clientKeyCheck.getNum();
				String clientKeyStr = clientKeyCheck.getKey();
				//�����ַ���
				String clientKey =  RSACoder.decipheringPrivateStr(clientKeyStr, keyNum);
				//�״���֤ͨ��
				if(!clientKey.equals("")) {
					//������֤�ɹ���Ϣ
					response = new Response();
					response.setModule((short) 1);
					response.setCmd((short) 3);
					response.setStateCode(StateCode.SUCCESS);
					
					channel.write(response);
					
					System.out.println("�ͻ����״�У��ɹ�,ip��ַΪ"+clientIp);
					
					//��keyд���ļ�
					PropertiesUtils.writeProperties(clientIp, clientKey, PropertiesFilePath.clientsFilePath);
				}else {
					
					System.out.println("�ͻ����״�У��ʧ��,ip��ַΪ"+clientIp);
					
					response = new Response();
					response.setModule((short) 1);
					response.setCmd((short) 3);
					response.setStateCode(StateCode.FAIL);
					
					channel.write(response);
					
					channel.close();
					
				}
				
			}
			//OTPУ��
			else if(cmd == 2) {
				
				String key = PropertiesUtils.readValue(PropertiesFilePath.clientsFilePath, clientIp);//��ȡkey
				ClientOTPCheck clientOTPCheck = new ClientOTPCheck();
				clientOTPCheck.readFromBytes(request.getData());
				String clientOtpPassword = clientOTPCheck.getOTPPassword();
				OTP serverOTP = new OTP(key,(long) OTP.INIT_TIME,OTP.STEP);
				if(serverOTP.getOTP().equals(clientOtpPassword))
					checkResult = true;
				response = new Response();
				response.setModule((short)module);
				response.setCmd((short) 3);
				if(checkResult == true)
				{
					response.setStateCode(StateCode.SUCCESS);
					System.out.println("�ͻ���У��ɹ�,ip��ַΪ"+clientIp);
					channel.write(response);
				}else {
					response.setStateCode(StateCode.FAIL);
					System.out.println("�ͻ���У��ʧ��,ip��ַΪ"+clientIp);
					channel.write(response);
					channel.close();
				}
				
			}
		}	
		super.messageReceived(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.channelClosed(ctx, e);
	}



	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, e);
	}
	

}
