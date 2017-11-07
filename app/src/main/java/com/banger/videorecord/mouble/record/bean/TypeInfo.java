package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/9/26.
 */
public class TypeInfo implements Serializable {
    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TypeInfo{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
