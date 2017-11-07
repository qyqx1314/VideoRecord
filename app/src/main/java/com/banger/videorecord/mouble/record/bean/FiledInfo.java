package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhusiliang on 16/9/26.
 */
public class FiledInfo implements Serializable {
    private String fieldCNName;//自定义中文字段名，左边的文本
    private String fieldType;//自定义类型
    private int isRequire;//是否是必填 0非必填 1必填
    private String fieldColName;//自定义字段列名 右边的文字
    private List<String> options;
    private String value;
    private String name;

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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getFieldCNName() {
        return fieldCNName;
    }

    public void setFieldCNName(String fieldCNName) {
        this.fieldCNName = fieldCNName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(int isRequire) {
        this.isRequire = isRequire;
    }

    public String getFieldColName() {
        return fieldColName;
    }

    public void setFieldColName(String fieldColName) {
        this.fieldColName = fieldColName;
    }

    @Override
    public String toString() {
        return "FiledInfo{" +
                "fieldCNName='" + fieldCNName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", isRequire=" + isRequire +
                ", fieldColName='" + fieldColName + '\'' +
                ", options=" + options +
                '}';
    }
}
