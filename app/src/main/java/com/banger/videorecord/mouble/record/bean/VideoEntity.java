package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/21.
 */
public class VideoEntity implements Serializable {
    private String filePath;//视频文件路径
    private String fileNullName;//不加后缀的文件名
    private String startTime;//视频开始时间
    private String endTime;//视频结束时间
    private long videoSize;//视频大小
    private String fileNoneName;
    private int process;

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public String getFileNoneName() {
        return fileNoneName;
    }

    public void setFileNoneName(String fileNoneName) {
        this.fileNoneName = fileNoneName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileNullName() {
        return fileNullName;
    }

    public void setFileNullName(String fileNullName) {
        this.fileNullName = fileNullName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }
}
