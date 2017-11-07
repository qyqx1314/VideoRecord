package com.banger.videorecord.mouble.setting.bean;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.mouble.record.widget.RoundProgressBar;

/**
 * Created by Xuchaowen on 2016/7/13.
 */
public class OffPackageListHodler {

    private TextView dataPackageName;//数据包名

    public RelativeLayout getOffDownload() {
        return offDownload;
    }

    public void setOffDownload(RelativeLayout offDownload) {
        this.offDownload = offDownload;
    }

    private RelativeLayout offDownload;//离线下载
    private RoundProgressBar progress;

    private TextView dataName;//导入数据包名
    private TextView leadTime;//导入时间

    public TextView getDataName() {
        return dataName;
    }

    public void setDataName(TextView dataName) {
        this.dataName = dataName;
    }


    public TextView getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(TextView leadTime) {
        this.leadTime = leadTime;
    }



    public TextView getDataPackageName() {
        return dataPackageName;
    }

    public void setDataPackageName(TextView dataPackageName) {
        this.dataPackageName = dataPackageName;
    }

    public RoundProgressBar getProgress() {
        return progress;
    }

    public void setProgress(RoundProgressBar progress) {
        this.progress = progress;
    }
}
