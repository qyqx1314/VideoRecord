package com.banger.zeromq.zmq;

public interface IUploadSpeedLimitService {
	
	/**
	 * 重置上传速度
	 * @param speed
	 */
	public void setSpeed(int speed);
	
	/**
	 * 重新计算
	 */
	public void resetCount();
	
	/**
	 * 添加上传流量
	 * @param size
	 */
	public void addUploadSize(int size);
	
	/**
	 * 是否传的太快
	 * @return
	 */
	public boolean isTooFast();
	
}
