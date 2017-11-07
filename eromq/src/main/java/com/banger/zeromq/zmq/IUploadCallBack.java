package com.banger.zeromq.zmq;

public interface IUploadCallBack {

	/**
	 * 
	 * @param state
	 * @param message
	 */
	void callback(IUploadFileState state);
	
}
