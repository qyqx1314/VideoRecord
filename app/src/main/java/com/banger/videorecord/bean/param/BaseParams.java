package com.banger.videorecord.bean.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhujm on 2016/6/21.
 */
public class BaseParams {
    //将java对象的属性转换成指定的json名字
    //设备ID
    @SerializedName("deviceId")
    private String imei;
    //设备IP
    @SerializedName("localIp")
    private String ip;
    //版本
    @SerializedName("version")
    private String currentVersionName;
    
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCurrentVersionName() {
        return currentVersionName;
    }

    public void setCurrentVersionName(String currentVersionName) {
        this.currentVersionName = currentVersionName;
    }
}
