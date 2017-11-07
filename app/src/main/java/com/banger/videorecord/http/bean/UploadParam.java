package com.banger.videorecord.http.bean;

/**
 * Created by zhujm on 2016/6/23.
 * 上传图片参数
 */
public class UploadParam {
    //文件名
    String name;
    //业务id
    String busNumber;

    public UploadParam(String name, String busNumber) {
        this.name = name;
        this.busNumber = busNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

}
