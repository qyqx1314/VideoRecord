package com.banger.videorecord.bean.param;

/**
 * 上传设备信息参数
 * Created by zhujm on 2016/6/14.
 */
public class DeviceParams extends BaseParams {
    //机型
    String model;
    //系统版本
    String osVersion;
    //品牌
    String brand;
    //sdk版本
    String sdk;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }
}
