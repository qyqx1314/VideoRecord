package com.banger.videorecord.mouble.product.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhusiliang on 16/6/15.
 */
public class BusinessTypeInfo  implements Serializable{
    private String  businessName;
    private ArrayList<ProductTypeInfo> list;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public ArrayList<ProductTypeInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ProductTypeInfo> list) {
        this.list = list;
    }
}
