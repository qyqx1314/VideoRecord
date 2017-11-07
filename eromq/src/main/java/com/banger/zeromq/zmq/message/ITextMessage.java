package com.banger.zeromq.zmq.message;

public interface ITextMessage extends IMessage {
	
	/**
	 * 得到消息Id
	 * @return
	 */
	String getMessageId();
	
	/**
	 * 设置消息Id
	 */
	void setMessageId(String messageId);
	
	/**
	 * 得到消息内容
	 * @return
	 */
	String getContent();
	
	/**
	 * 设置消息内容
	 * @param text
	 */
	void setContent(String content);
	
	/**
	 * 得到字符编码
	 * @return
	 */
	MessageEncoding getEncoding();
	
	/**
	 * 设置字符编码
	 * @param encoding
	 */
	void setEncoding(MessageEncoding encoding);
}
