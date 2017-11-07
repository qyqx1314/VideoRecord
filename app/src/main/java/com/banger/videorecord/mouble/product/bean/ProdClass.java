package com.banger.videorecord.mouble.product.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/18.
 */
public class ProdClass extends DataSupport implements Serializable{
    private int classId;
    private String className;
    private int typeId;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "ProdClass{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", typeId=" + typeId +
                '}';
    }
}
