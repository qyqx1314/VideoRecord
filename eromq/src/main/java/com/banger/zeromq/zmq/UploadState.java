package com.banger.zeromq.zmq;

public enum UploadState {
	waiting,			//排队等待上传
	uploading,			//上传中
	failed,				//上传失败
	error,
	success;			//上传成功
}