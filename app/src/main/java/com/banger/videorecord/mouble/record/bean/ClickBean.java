package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/9/27.
 */
public class ClickBean implements Serializable {
    private MediaInfoBean mediaInfoBean;//数据
    private String  bizNo;

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }
    public MediaInfoBean getMediaInfoBean() {
        return mediaInfoBean;
    }

    public void setMediaInfoBean(MediaInfoBean mediaInfoBean) {
        this.mediaInfoBean = mediaInfoBean;
    }
}
