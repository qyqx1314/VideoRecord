package com.banger.zeromq.zmq;

import android.os.Handler;

public interface IFileUploadServer {
	
	/**
	 * 
	 * @param filename
	 * @param callback
	 */
	void uploadFileTask(String filename, Handler callback,int link,String bizID);
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	IUploadFileState getUploadFileStatus(String filename);
	
}
