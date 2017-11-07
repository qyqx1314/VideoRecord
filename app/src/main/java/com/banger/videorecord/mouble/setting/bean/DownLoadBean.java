package com.banger.videorecord.mouble.setting.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Xuchaowen on 2016/7/17.
 */
public class DownLoadBean  extends DataSupport {

    private int count;//几条
    private String downTime;//下载时间

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
