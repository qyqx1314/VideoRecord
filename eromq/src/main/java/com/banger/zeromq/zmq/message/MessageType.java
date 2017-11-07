package com.banger.zeromq.zmq.message;

public enum MessageType {
	Text_Message(1),				//文本消息
	File_Message(2);				//文件消息
	
	public int value;
	
	MessageType(int value){
		this.value = value;
	}
	
	public static MessageType valueOf(int value){
		switch (value) {
	        case 1:
	            return Text_Message;
	        case 2:
	            return File_Message;
	        default:
	            return null;
        }
	}
}
