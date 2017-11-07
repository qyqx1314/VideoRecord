package com.banger.zeromq.zmq.message;

public enum MessageEncoding {
	UTF8(1,"utf-8"),
	UTF16(2,"utf-16"),
	GBK(3,"gbk"),
	UNCODE(4,"uncode");
	
	public int value;
	public String encoding;
	
	MessageEncoding(int value,String encoding){
		this.value = value;
		this.encoding = encoding;
	}
	
	public static MessageEncoding valueOf(int value){
		switch (value) {
	        case 1:
	            return UTF8;
	        case 2:
	            return UTF16;
	        case 3:
	        	return GBK;
	        case 4:
	        	return UNCODE;
	        default:
	            return null;
	    }
	}
}
