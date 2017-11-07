package com.banger.zeromq.zmq.message;


public interface IMessage {
	
	/**
	 * 得到数据
	 * @return
	 */
	byte[] getData();
	
	/**
	 * 设置数据
	 */
	void setData(byte[] data);
	
	/**
	 * 得到消息类型
	 * @return
	 */
	MessageType getMessageType();
	
	/**
	 * 得到消息状态 0:正常 1:调试模式
	 * @return
	 */
	MessageState getMessageState();
	
}
