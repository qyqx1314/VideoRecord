package com.banger.videorecord.mouble.setting.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by zhujm on 2016/7/14.
 */
public class OfflineImportHistory extends DataSupport {
    String offlineName;
    String importTime;

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getOfflineName() {
        return offlineName;
    }

    public void setOfflineName(String offlineName) {
        this.offlineName = offlineName;
    }
}
