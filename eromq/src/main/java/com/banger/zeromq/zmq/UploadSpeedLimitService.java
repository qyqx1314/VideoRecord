package com.banger.zeromq.zmq;

import java.util.Date;


public class UploadSpeedLimitService implements IUploadSpeedLimitService {
	private int currentSpeed = -1;				//当前速度
	private long uploadSize = 0;				//以上上传流量
	private long currentTime = new Date().getTime();
	
	/**
	 * 重置上传速度
	 * @param speed
	 */
	public void setSpeed(int speed){
		if(speed!=currentSpeed){
			currentSpeed = speed;
			resetCount();
		}
	}
	
	/**
	 * 重新计算
	 */
	public void resetCount(){
		uploadSize = 0;
		currentTime = new Date().getTime();
	}
	
	/**
	 * 添加上传流量
	 * @param size
	 */
	public void addUploadSize(int size){
		uploadSize+=size;
	}
	
	/**
	 * 是否传的太快
	 * @return
	 */
	public boolean isTooFast(){
		if(currentSpeed>0){
			long ts = new Date().getTime()-currentTime;
			if(ts>0){
				float t = ts/1000;
				long s = uploadSize/1024;
				float speed = s/t;
				return speed>currentSpeed;
			}
		}
		return false;
	}

}
