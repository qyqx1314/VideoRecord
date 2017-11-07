package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhusiliang on 16/9/27.
 *
 * 该实体类是用来存储每一个环节的媒体信息，一个录像 多张图片
 */
public class MediaInfoBean implements Serializable {
    private VideoInfoBean video;
    private List<ImageInfoBean> images;

    public VideoInfoBean getVideo() {
        return video;
    }

    public void setVideo(VideoInfoBean video) {
        this.video = video;
    }

    public List<ImageInfoBean> getImages() {
        return images;
    }

    public void setImages(List<ImageInfoBean> images) {
        this.images = images;
    }
}
