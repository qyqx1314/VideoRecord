package com.banger.videorecord.mouble.product.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/29.
 */
public class ProductClass extends DataSupport implements Serializable {
    private int classId;
    private int typeId;
    private String name;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductClass{" +
                "classId=" + classId +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
