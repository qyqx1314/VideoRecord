package com.banger.zeromq.zmq.message;

import com.banger.zeromq.util.StringUtil;

public abstract class AbstractMessage implements IMessage {
	protected byte[] data;
	
	public AbstractMessage(){
		this.data = new byte[8];
    	data[MessageHeadPosition.Message_Encoding.value] = Byte.valueOf(String.valueOf(MessageEncoding.UTF8.value));
	}
	
	public AbstractMessage(byte[] data){
		this.data = data;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
	public void setData(byte[] data){
		this.data = data;
	}
	
	public MessageType getMessageType() {
		return MessageHead.getMessageType(this.data);
	}
	
	public void setMessageType(MessageType type){
		MessageHead.setMessageType(this.data, type);
	}
	
	public MessageState getMessageState() {
		return MessageHead.getMessageState(this.data);
	}
	
	public void setMessageState(MessageState state){
		MessageHead.setMessageState(this.data, state);
	}
	
	protected byte[] getData(int pos,int len){
		if(this.data.length<pos+len){
			 throw new MessageException(StringUtil.format("取数据超出数组范围 size:{0} pos:{1} len:{2}",this.data.length,pos,len));
		}
		byte[] newData = new byte[len];
		System.arraycopy(data, pos, newData, 0, len);
		return newData;
	}
	
	protected void setData(byte[] data,int pos){
		if(data!=null){
			int len = pos + data.length;
			if(this.data.length<len){
				byte[] newData = new byte[len];
				System.arraycopy(this.data, 0, newData, 0, this.data.length);
				System.arraycopy(data, 0, newData, pos, data.length);
				this.data = newData;
			}else{
				System.arraycopy(data, 0, this.data, pos, data.length);
			}
		}
	}
	
	protected void resizeData(int size){
		if(this.data!=null){
			byte[] newData = new byte[size];
			System.arraycopy(this.data, 0, newData, 0, (this.data.length<size)?this.data.length:size);
			this.data = newData;
		}
	}
}
