package com.banger.videorecord.mouble.setting.activity;

/**
 * Created by qyqx on 2016/9/27.
 */
public class appBean {

    private String name;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "appBean{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
