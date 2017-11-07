package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/9/30.
 */
public class ProductExtraInfo implements Serializable {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
