package com.banger.zeromq.zmq;

import android.util.Log;

import com.banger.zeromq.util.FileUtil;

import java.io.File;
import java.util.Date;
public class UploadFileState implements IUploadFileState {
	private String fileId;				//文件Id
	private String filename;			//文件名
	private String fileExt;				//文件后缀
	private String name;				//文件短名称
	private String path;				//文件路径
	private UploadState state;			//传输状态
	private Long filesize;				//文件大小
	private Long uploadFilesize;		//上传文件大小
	private Long serverFilesize;		//服务临时文件大小
	private Date updateTime;			//修改时间
	private Date beginTime;				//开始时间
	private Date endTime;				//完成上传时间
	private String log;					//日志文件
	private String sendBeginXml;		//文件开始信息
	private String sendEndXml;			//文件结束信息
	private String message;
	private String bizId;
	private int link;

	public String getFileId(){
		return fileId;
	}
	public String getFileExt() {
		return fileExt;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public UploadState getState() {
		return state;
	}
	public void setState(UploadState state) {
		this.state = state;
	}
	public Long getFilesize() {
		return filesize;
	}
	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}
	public Long getUploadFilesize() {
		return uploadFilesize;
	}
	public void setUploadFilesize(Long uploadFilesize) {
		this.uploadFilesize = uploadFilesize;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getServerFilesize() {
		return serverFilesize;
	}
	public void setServerFilesize(Long serverFilesize) {
		this.serverFilesize = serverFilesize;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSendBeginXml() {
		return sendBeginXml;
	}
	public void setSendBeginXml(String sendBeginXml) {
		this.sendBeginXml = sendBeginXml;
	}
	public String getSendEndXml() {
		return sendEndXml;
	}
	public void setSendEndXml(String sendEndXml) {
		this.sendEndXml = sendEndXml;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public int getLink() {
		return link;
	}

	public void setLink(int link) {
		this.link = link;
	}

	public Integer getProgress() {
		if(this.filesize>0){
			int fs = (int) (this.filesize / 1000);
			int us = (int) (this.getUploadFilesize() / 1000);
			if(us>fs){
				return 100;
			}
			return us * 100 / fs;
		}
		return -1;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void clear(){
		this.state = UploadState.waiting;
		this.uploadFilesize = 0L;
		this.serverFilesize = 0L;
		this.message = "";
	}


	public UploadFileState(String filename){
		this.fileId = filename.substring(filename.lastIndexOf('/')+1,filename.indexOf("."));
		this.filename = filename;
		this.name = FileUtil.getFileName(filename);
		this.fileExt = FileUtil.getFileFix(filename);
		this.state = UploadState.waiting;
		this.uploadFilesize = 0L;
		this.serverFilesize = 0L;
		this.log = "";
		this.message = "";
		File file = new File(filename);
		if(file.exists()){
			this.filesize = file.length();
			this.updateTime = new Date(file.lastModified());
		}
	}
}
