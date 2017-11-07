package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusiliang on 16/6/22.
 */
public class DisplayImgEntity implements Serializable {
    private ArrayList<VideoEntity> list;
    private List<ImageInfoBean> imgList;

    public int getRingNum() {
        return ringNum;
    }

    public void setRingNum(int ringNum) {
        this.ringNum = ringNum;
    }

    private int ringNum;

    public List<ImageInfoBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImageInfoBean> imgList) {
        this.imgList = imgList;
    }

    public ArrayList<VideoEntity> getList() {
        return list;
    }

    public void setList(ArrayList<VideoEntity> list) {
        this.list = list;
    }
}
