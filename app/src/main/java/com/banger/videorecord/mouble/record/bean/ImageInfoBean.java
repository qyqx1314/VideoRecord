package com.banger.videorecord.mouble.record.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/7/6.
 */
public class ImageInfoBean extends DataSupport implements Serializable {
    private int id;
    //唯一的业务标识
    private String bizNo;
    //所属环节 1代表环节1 2代表环节2
    private int process;
    //文件路径，全路径，加后缀
    private String filePath;
    //文件名称，含后缀
    private String fileName;
    //文件名称，不含后缀
    private String fileId;
    //上传状态1 未上传 2上传中 3上传成功 4上传失败
    private int  state;
    //新增的环节id
    private String processId;
    //新增环节名称
    private String processName;


    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
