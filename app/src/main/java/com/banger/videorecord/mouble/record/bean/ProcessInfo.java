package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import org.litepal.crud.DataSupport;

/**
 * Created by zhusiliang on 16/9/27.
 */
public class ProcessInfo extends DataSupport implements Serializable {
    private String processName;
    private String processId;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "processName='" + processName + '\'' +
                ", processId='" + processId + '\'' +
                '}';
    }
}
