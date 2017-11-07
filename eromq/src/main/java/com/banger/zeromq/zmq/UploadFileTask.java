package com.banger.zeromq.zmq;

import android.os.Handler;
import android.os.Message;

import com.banger.zeromq.util.HttpUtil;
import com.banger.zeromq.util.StringUtil;
import com.banger.zeromq.zmq.message.FileMessage;
import com.banger.zeromq.zmq.message.IBlobMessage;
import com.banger.zeromq.zmq.message.IMessage;
import com.banger.zeromq.zmq.message.IMessageParser;
import com.banger.zeromq.zmq.message.ITextMessage;
import com.banger.zeromq.zmq.message.MessageParser;
import com.banger.zeromq.zmq.message.TextMessage;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;


public class UploadFileTask implements Runnable {
	private String actionAddr = "cluster/getClusterConfig.html";
	private IUploadConfig config;
	private UploadFileState state;
	private IUploadSpeedLimitService speed;
	private Handler handler;
	private Socket fSocket;
	private Socket mSocket;
	private ZContext context;
	private  int link;
	private  String bizId;

	public UploadFileTask(ZContext context,IUploadConfig config,UploadFileState state,IUploadSpeedLimitService speed,Handler handler,int link,String bizID){
		this.context = context;
		this.config = config;
		this.state = state;
		this.speed = speed;
		this.handler = handler;
		this.link = link;
		this.bizId = bizID;
	}

	@Override
	public void run() {
		this.state.setState(UploadState.uploading);
		state.setLink(link);
		state.setBizId(bizId);
		try {
			//Date t1 = new Date();
			Map<String, String> map = this.getClusterConfig();

			String fileUploadAddr = map.get("zmqFileUrl");
			String xmlUploadAddr = map.get("zmqMessageUrl");

			fSocket = this.context.createSocket(ZMQ.REQ);
			mSocket = this.context.createSocket(ZMQ.REQ);

			fSocket.connect(fileUploadAddr);
			mSocket.connect(xmlUploadAddr);

			int timeout = 300*1000;

			fSocket.setSendTimeOut(timeout);
			fSocket.setReceiveTimeOut(timeout);
			mSocket.setSendTimeOut(timeout);
			mSocket.setReceiveTimeOut(timeout);

			state.setBeginTime(new Date());
			String filename = state.getFilename();

			//发送开始消息
			String text = this.sendFileBegin(mSocket);

			if(StringUtil.isNotEmpty(text)){
				map = resolveXml(text);

				int partSize = 102400;
				long filesize = (map.containsKey("fileSize"))?Long.parseLong(map.get("fileSize")):0;
				int partIndex = new Long(filesize/partSize).intValue();

				if(filesize>0){
					partIndex = partIndex-1;
					state.setServerFilesize(filesize);
					state.setUploadFilesize(filesize);
				}
				IBlobMessage msg = new FileMessage(filename);
				if(partIndex>0)msg.setPartIndex(partIndex);
				if(partSize!=1024)msg.setPartSize(partSize);
				msg.setMessageId(filename.substring(filename.lastIndexOf('/') + 1, filename.indexOf(".")));
				int uploadSpeed = (map.containsKey("speed"))?Integer.parseInt(map.get("speed")):100;
				speed.setSpeed(uploadSpeed);

				int callSize = 5;

				while(msg.hasNext()){
					if(speed.isTooFast()){
						Thread.sleep(500);
						continue;
					}
					msg.next();
					byte[] part = msg.getData();
					fSocket.send(part,ZMQ.NOBLOCK);
					Thread.sleep(100);
					fSocket.recv(0);
					speed.addUploadSize(part.length); 	//默认每次发送100KB （包大小）
					long uploadSize = state.getUploadFilesize()+msg.getPartData().length;
					state.setUploadFilesize(uploadSize);

					if(state.getProgress()%5==0){
						Message message = Message.obtain();
						message.arg1 = link;
						message.obj = state;
						handler.sendMessage(message);
					}
				}

				Thread.sleep(1000);

				//发送完毕消息
				text = sendFileEnd(mSocket);

				if(StringUtil.isNotEmpty(text)){
					map.clear();
					map = resolveXml(text);
					String result = map.containsKey("result")?map.get("result"):"";

					if("success".equals(result)){
						//File ff = new File(filename);
						Thread.sleep(200);
						//ff.delete();
						state.setState(UploadState.success);
						state.setMessage("文件上传成功");
					}else{
						System.out.println("文件发送失败："+ map.get("error") +"["+state.getFilename()+"]");
						state.setState(UploadState.failed);
						state.setMessage("文件发送失败:"+map.get("error"));
					}
				}else{
					state.setState(UploadState.error);
					state.setMessage("连接超时,上传失败!");
				}
			}else{
				state.setState(UploadState.error);
				state.setMessage("连接超时,上传失败!");
			}


		}catch(ZMQException e){
			System.out.println("传输文件出错 文件["+state.getFilename()+"] 地址["+config.getLocalIp()+"] "+e.getMessage());
			state.setState(UploadState.error);
			state.setMessage("连接超时,上传失败!");
		}catch(Exception e){
			System.out.println("传输文件出错 文件["+state.getFilename()+"] 地址["+config.getLocalIp()+"] "+e.getMessage());
			//e.printStackTrace();
			state.setState(UploadState.error);
			state.setMessage(e.getMessage());
		}finally{
			try{
				this.context.destroySocket(fSocket);
				this.context.destroySocket(mSocket);
			}catch(Exception e){
				System.out.println("关闭连接出错："+e.getMessage());
				state.setMessage("关闭上传连接导常 error:"+e.getMessage());
			}
			Message message = Message.obtain();
			message.obj = state;
			message.arg1 = link;
			handler.sendMessage(message);
		}
	}

	private Map<String, String> resolveXml(String message) throws SAXException, IOException, ParserConfigurationException {
		Map<String, String> resultMap = new HashMap<String, String>();
		if(!StringUtil.isNullOrEmpty(message)){
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader read = new StringReader("<?xml version=\"1.0\" encoding=\"utf-8\" ?>"+message.trim());
			Document document = builder.parse(new InputSource(read));
			Node root = document.getFirstChild();
			for(int i=0;i<root.getChildNodes().getLength();i++){
				Node node = root.getChildNodes().item(i);
				resultMap.put(node.getNodeName(),node.getTextContent());
			}
		}
		return resultMap;
	}

	private Map<String,String> getClusterConfig() throws SAXException, IOException, ParserConfigurationException {
		String url = this.config.getServerAddress()+this.actionAddr;
		int timeout = 10*1000;    //10秒连接超时
		String result = HttpUtil.sendGet(url, "ip="+this.config.getLocalIp(),timeout);
		if(StringUtil.isNotEmpty(result)){
			if(result.charAt(0)=='<'){
				return this.resolveXml(result);
			}else{
				throw new RuntimeException("请求服务地址: "+url+" 返回错误,请检查服务地址是否正确！");
			}
		}else{
			throw new RuntimeException("请求服务地址: "+url+" 返回地址为空 ,请检查服务地址是否正确！");
		}
	}

	private String sendFileBegin(Socket socket){
		byte[] replay = new byte[0];
		ITextMessage replayMsg = new TextMessage("sendVideoFileBegin");
		StringBuilder sb = new StringBuilder();
		sb.append("<message>");
		sb.append("<fileId>"+state.getFileId()+"</fileId>");
		sb.append("<fileName>"+state.getName()+"</fileName>");
		sb.append("<ip>"+config.getLocalIp()+"</ip>");
		sb.append("<recordNo>"+state.getFileId()+"</recordNo>");
		sb.append("<transit>2</transit>");			//0 托盘  1 中转站  2 android
		sb.append("</message>");
		replayMsg.setContent(sb.toString());
		replay = replayMsg.getData();
		socket.send(replay,ZMQ.NOBLOCK);

		IMessageParser parser = new MessageParser();
		byte[] data= socket.recv(0);
		if(data!=null && data.length>0){
			IMessage message = parser.parser(data);
			if(message instanceof ITextMessage){
				ITextMessage msg = (ITextMessage)message;
				String text = msg.getContent();
				System.out.println(text);
				return text;
			}
		}
		return null;
	}

	private String sendFileEnd(Socket socket){
		byte[] replay = new byte[0];
		ITextMessage replayMsg = new TextMessage("sendVideoFileEnd");
		StringBuilder sb = new StringBuilder();
		sb.append("<message>");
		sb.append("<fileId>"+state.getFileId()+"</fileId>");
		sb.append("<fileExt>"+state.getFileExt()+"</fileExt>");
		sb.append("<fileSize>"+state.getFilesize()+"</fileSize>");
		sb.append("<fileName>"+state.getName()+"</fileName>");
		sb.append("<recordNo>"+state.getFileId()+"</recordNo>");
		sb.append("</message>");
		replayMsg.setContent(sb.toString());
		replay = replayMsg.getData();
		socket.send(replay,ZMQ.NOBLOCK);

		IMessageParser parser = new MessageParser();
		byte[] data= socket.recv(0);
		if(data!=null && data.length>0){
			IMessage message = parser.parser(data);
			if(message instanceof ITextMessage){
				ITextMessage msg = (ITextMessage)message;
				String text = msg.getContent();
				System.out.println(text);
				return text;
			}
		}
		return null;
	}
}
