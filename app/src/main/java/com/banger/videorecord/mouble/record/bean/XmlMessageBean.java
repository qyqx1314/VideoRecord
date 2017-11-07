package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhusiliang on 16/7/11.
 */
public class XmlMessageBean implements Serializable {
    //业务信息
    private BusinessInfoBean businessInfoBean;
    //上传的录像集合
    private List<VideoInfoBean> videoList;
    //上传的图片集合
    private List<ImageInfoBean> imageList;
    //是否是重新上传
    private boolean isReloadImage;
    private boolean isReloadVideo;
    //视频1的上传进度
    private int videoProgress1 = -1;
    //视频2的上传进度
    private int videoProgress2 = -1;
    //判断是重新上传哪笔记录
    private int reloadState=-1;
    private boolean isImageDone = false;

//    //自定义用户信息
//    private CustomerInfoBean data;
//    //自定义环节信息
//    private List<ProcessInfo> processInfoList;


    public int getReloadState() {
        return reloadState;
    }

    public void setReloadState(int reloadState) {
        this.reloadState = reloadState;
    }

    public boolean isImageDone() {
        return isImageDone;
    }

    public void setIsImageDone(boolean isImageDone) {
        this.isImageDone = isImageDone;
    }

    public int getVideoProgress1() {
        return videoProgress1;
    }

    public void setVideoProgress1(int videoProgress1) {
        this.videoProgress1 = videoProgress1;
    }

    public int getVideoProgress2() {
        return videoProgress2;
    }

    public void setVideoProgress2(int videoProgress2) {
        this.videoProgress2 = videoProgress2;
    }

    public boolean isReloadImage() {
        return isReloadImage;
    }

    public void setIsReloadImage(boolean isReloadImage) {
        this.isReloadImage = isReloadImage;
    }

    public boolean isReloadVideo() {
        return isReloadVideo;
    }

    public void setIsReloadVideo(boolean isReloadVideo) {
        this.isReloadVideo = isReloadVideo;
    }

    public BusinessInfoBean getBusinessInfoBean() {
        return businessInfoBean;
    }

    public void setBusinessInfoBean(BusinessInfoBean businessInfoBean) {
        this.businessInfoBean = businessInfoBean;
    }

    public List<VideoInfoBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoInfoBean> videoList) {
        this.videoList = videoList;
    }

    public List<ImageInfoBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageInfoBean> imageList) {
        this.imageList = imageList;
    }

}
