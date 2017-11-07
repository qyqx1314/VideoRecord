package com.banger.videorecord.bean.result;
/**
 * Created by Xuchaowen on 2016/6/15.
 * 版本升级返回
 */
public class UpVersionResult extends BaseResult {
    String needUpgrade="";
    String version="";

    public String getNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(String needUpgrade) {
        this.needUpgrade = needUpgrade;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public String toString() {
        return "UpVersionResult{" +
                "needUpgrade='" + needUpgrade +
                ", version='" + version +
                ", result=" + this.getResult() +
                ", error='" + this.getError() +
                '}';
    }
}
