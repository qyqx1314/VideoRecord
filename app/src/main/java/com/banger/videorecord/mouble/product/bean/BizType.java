package com.banger.videorecord.mouble.product.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/18.
 */
public class BizType extends DataSupport implements Serializable {
    private int typeId;
    private String typeName;
    private int id;
    private String processInfo;//环节字段
    private String videoMoreData;//环节额外字段

    public String getVideoMoreData() {
        return videoMoreData;
    }

    public void setVideoMoreData(String videoMoreData) {
        this.videoMoreData = videoMoreData;
    }



    public String getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(String processInfo) {
        this.processInfo = processInfo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    @Override
    public String toString() {
        return "BizType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", id=" + id +
                ", processInfo='" + processInfo + '\'' +
                ", videoMoreData='" + videoMoreData + '\'' +
                '}';
    }
}
