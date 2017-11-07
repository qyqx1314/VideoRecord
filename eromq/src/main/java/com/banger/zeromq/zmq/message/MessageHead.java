package com.banger.zeromq.zmq.message;

import com.banger.zeromq.util.StringUtil;

public class MessageHead {
	private static final int HeadMaxSize = 8;
	private static final int KeySize = 4;
	
    public static void checkDataHead(byte[] data){
        if (data == null || data.length == 0)
            throw new MessageException("解析的消息为空,无效消息");
        if (data.length < HeadMaxSize)
            throw new MessageException(StringUtil.format("消息 大小 {0} 小于{1}位,消息描述信息失消", data.length, HeadMaxSize));
	}
    
    public static MessageType getMessageType(byte[] data){
    	byte b = data[MessageHeadPosition.Message_Type.value];
    	return MessageType.valueOf(Integer.parseInt(Byte.toString(b)));
    }
    
    public static MessageEncoding getEncoding(byte[] data) {
    	byte b = data[MessageHeadPosition.Message_Encoding.value];
    	return MessageEncoding.valueOf(Integer.parseInt(Byte.toString(b)));
	}
    
    public static MessageState getMessageState(byte[] data){
    	byte b = data[MessageHeadPosition.Message_State.value];
    	return MessageState.valueOf(Integer.parseInt(Byte.toString(b)));
    }
    
    public static void setEncoding(byte[] data,MessageEncoding encoding){
    	byte b = Byte.valueOf(String.valueOf(encoding.value));
    	data[MessageHeadPosition.Message_Encoding.value] = b;
    }
    
    public static void setMessageType(byte[] data,MessageType type){
    	byte b = Byte.valueOf(String.valueOf(type.value));
    	data[MessageHeadPosition.Message_Type.value] = b;
    }
    
    public static void setMessageState(byte[] data,MessageState state){
    	byte b = Byte.valueOf(String.valueOf(state.value));
    	data[MessageHeadPosition.Message_State.value] = b;
    }
    
    public static byte[] getKey(byte[] data){
    	byte[] bytes = new byte[KeySize];
    	for(int i=0;i<KeySize;i++){
    		bytes[i] = data[MessageHeadPosition.Message_Key.value+i];
    	}
    	return bytes;
    }
    
    public static void setKey(byte[] data,byte[] key){
    	if(key.length==KeySize){
	    	for(int i=0;i<KeySize;i++){
	    		data[i] = key[i];
	    	}
    	}
    }
}
