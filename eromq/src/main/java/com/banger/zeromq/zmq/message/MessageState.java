package com.banger.zeromq.zmq.message;

public enum MessageState {
	Normal(0),
	Debug(1);
	
	public int value;
	
	MessageState(int value){
		this.value = value;
	}
	
	public static MessageState valueOf(int value){
		switch (value) {
	        case 0:
	            return Normal;
	        case 1:
	            return Debug;
	        default:
	            return null;
        }
	}

}
