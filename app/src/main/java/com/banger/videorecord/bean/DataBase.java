package com.banger.videorecord.bean;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/24.
 */
public class DataBase implements Serializable {
    //通用的数据对应的id
    private int id;
    //数据对应的具体值
    private String value;
    //额外字段用来拓展或者存储其他信息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
