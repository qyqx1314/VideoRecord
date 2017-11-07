package com.banger.zeromq.zmq.message;

public enum MessageHeadPosition {
	Message_Type(0),
	Message_Encoding(1),
	Message_Key(2),
	Message_State(7);
	
	public int value;
	
	MessageHeadPosition(int value){
		this.value = value;
	}
}
