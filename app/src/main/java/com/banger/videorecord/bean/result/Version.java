package com.banger.videorecord.bean.result;

/**
 * Created by zhujm on 2016/7/13.
 */
public class Version extends BaseResult {
    String needUpgrade;
    String version;
    String upgradeUrl;
    String remark = "000";

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

    public String getUpgradeUrl() {
        return upgradeUrl;
    }

    public void setUpgradeUrl(String upgradeUrl) {
        this.upgradeUrl = upgradeUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Override
    public String toString() {
        return super.toString()+"--needUpgrade---"+getNeedUpgrade()+"--version--"+getVersion()+"--upgradeUrl--"+getUpgradeUrl()+"--remark--"+getRemark();
    }
}
