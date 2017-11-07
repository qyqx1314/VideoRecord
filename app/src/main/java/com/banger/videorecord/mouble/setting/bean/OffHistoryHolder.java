package com.banger.videorecord.mouble.setting.bean;

import android.widget.TextView;

import com.banger.videorecord.mouble.record.widget.RoundProgressBar;

/**
 * Created by Xuchaowen on 2016/7/13.
 * 离线数据历史
 */
public class OffHistoryHolder {

    private TextView time;//处理时间
    private TextView downloadState;//下载状态
//    private RoundProgressBar progress;//进度条

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(TextView downloadState) {
        this.downloadState = downloadState;
    }

//    public RoundProgressBar getProgress() {
//        return progress;
//    }
//
//    public void setProgress(RoundProgressBar progress) {
//        this.progress = progress;
//    }
}
