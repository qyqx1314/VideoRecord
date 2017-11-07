package com.banger.zeromq.zmq.message;

public interface IMessageParser {
	
	IMessage parser(byte[] data);
	
}
