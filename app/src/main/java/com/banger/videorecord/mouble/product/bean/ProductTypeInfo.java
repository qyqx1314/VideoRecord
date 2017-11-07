package com.banger.videorecord.mouble.product.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhusiliang on 16/6/15.
 */
public class ProductTypeInfo implements Serializable{
    private String  name;
    private ArrayList<ProductDetailInfo> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProductDetailInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ProductDetailInfo> list) {
        this.list = list;
    }
}
