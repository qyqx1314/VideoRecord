package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Xuchaowen on 2016/7/12.
 */
public class ImageListInfo implements Serializable {

    public ArrayList<ImageInfoBean> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<ImageInfoBean> imgList) {
        this.imgList = imgList;
    }

    ArrayList<ImageInfoBean> imgList;

    public int getRingNum() {
        return ringNum;
    }

    public void setRingNum(int ringNum) {
        this.ringNum = ringNum;
    }

    int ringNum;

}
