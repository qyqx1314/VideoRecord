package com.banger.zeromq.zmq.message;

public interface IBlobMessage extends IMessage {
	
	/**
	 * 得到消息Id
	 * @return
	 */
	String getMessageId();
	
	/**
	 * 设置消息Id
	 * @param messageId
	 */
	void setMessageId(String messageId);
	
	/**
	 * 文件类型
	 * @return
	 */
	String getFix();
	
	/**
	 * 文件类型
	 * @param fix
	 */
	void setFix(String fix);
	
	/** 
	 * 一批几个包
	 * @return
	 */
	int getBatch();

	/**
	 * 设置一批几个包
	 * @param batch
	 */
	void setBatch(int batch);
	
	/**
	 * 一批的最后一个包
	 * @return
	 */
	boolean isBatchEnd();
	
	/**
	 * 文件大小
	 * @return
	 */
	long getSize();
	
	/**
	 * 分包大小
	 * @return
	 */
	int getPartSize();
	
	/**
	 * 分包大小
	 * @param size
	 */
	void setPartSize(int size);
	
	/**
	 * 得到分包索引
	 * @return
	 */
	int getPartIndex();
	
	/**
	 * 设置分包索引
	 */
	void setPartIndex(int index);
	
	/**
	 * 得到分包数据
	 * @return
	 */
	byte[] getPartData();
	
	/**
	 * 设置分包数据
	 * @param data
	 */
	void setPartData(byte[] data);
	
	/**
	 * 分包数量
	 * @return
	 */
	int getPartCount();
	
	/**
	 * 是否为最后一个包
	 * @return
	 */
	boolean isLast();
	
	/**
	 * 下一个包
	 * @return
	 */
	boolean next();
	
	/**
	 * 是否有下一个包
	 * @return
	 */
	boolean hasNext();
	
}
