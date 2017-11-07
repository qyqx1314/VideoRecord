package com.banger.zeromq.zmq.message;

import com.banger.zeromq.util.ByteUtil;

import java.io.UnsupportedEncodingException;

public class TextMessage extends AbstractMessage implements ITextMessage {
	private final int msgIdStart = 8;
	private final int msgIdLen = 64;
    private final int dataStart = 72;   //数据存放的起始位
    
    public TextMessage(String messageId) {
    	this.setEncoding(MessageEncoding.UTF8);
    	this.setMessageType(MessageType.Text_Message);
    	this.setMessageId(messageId);
    }
    
    public TextMessage(byte[] data){
    	super(data);
    }
    
    public void clear(){
    	
    }
    
    public String getMessageId(){
    	byte[] data = this.getData(msgIdStart,msgIdLen);
		try {
			return new String(ByteUtil.removeZeroBytes(data),this.getEncoding().encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    public void setMessageId(String messageId){
    	try {
			byte[] data = messageId.getBytes(this.getEncoding().encoding);
			this.setData(data, msgIdStart);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public String getContent() {
		int len = this.data.length-dataStart;
		if(len>0){
			byte[] data = this.getData(dataStart,len);
			try {
				return new String(data,this.getEncoding().encoding);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public void setContent(String content) {
		try {
			byte[] data = content.getBytes(this.getEncoding().encoding);
			this.setData(data, dataStart);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MessageEncoding getEncoding() {
		return MessageHead.getEncoding(this.data);
	}

	public void setEncoding(MessageEncoding encoding) {
		MessageHead.setEncoding(this.data, encoding);
	}

}
