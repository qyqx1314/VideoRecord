package com.banger.videorecord.mouble.product.bean;

import com.banger.videorecord.mouble.record.bean.FiledInfo;

import java.util.List;

/**
 * Created by qyqx on 2016/9/29.
 */
public class VideoMoreData {

    private List<FiledInfo> videomore;
    public List<FiledInfo> getVideoMore() {
        return videomore;
    }

    public void setVideoMore(List<FiledInfo> videoMore) {
        this.videomore = videoMore;
    }

    @Override
    public String toString() {
        return "VideoMoreData{" +
                "videoMore=" + videomore +
                '}';
    }
}
