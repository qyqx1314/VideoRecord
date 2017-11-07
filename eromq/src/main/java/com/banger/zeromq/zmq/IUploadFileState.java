package com.banger.zeromq.zmq;
/**
 * 上传文件名
 * @author zhusw
 *
 */
public interface IUploadFileState {

	/**
	 * 视频文件名
	 * @return
	 */
	String getFilename();

	/**
	 * 得到上传状态
	 * @return
	 */
	UploadState getState();

	/**
	 * 得到上传信息
	 * @return
	 */
	String getMessage();

	/**
	 * 当上传失败时，可获取日志
	 * @return
	 */
	String getLog();

	/**
	 *
	 * @return
	 */
	Integer getProgress();

	int getLink();
	String getBizId();
}
