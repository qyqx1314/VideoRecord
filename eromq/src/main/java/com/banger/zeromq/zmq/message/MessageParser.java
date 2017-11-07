package com.banger.zeromq.zmq.message;

public class MessageParser implements IMessageParser {

	public IMessage parser(byte[] data) {
		MessageHead.checkDataHead(data);
		MessageType type = MessageHead.getMessageType(data);
		switch(type){
			case Text_Message:
				return new TextMessage(data);
			case File_Message:
				return new FileMessage(data);
		}
		return null;
	}
	
}
