package com.banger.videorecord.mouble.setting.bean;

import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/7/13.
 */
public class OffDataListBean implements Serializable {
    private ArrayList<ProductDetailInfo> list;
    private String offDownTime;//离线下载时间
    private List<ProductDetailInfo> businessList;//搜索的处理结果
    private String productCode;
    private String productName;
    private int classType;
    private int bizType;
    private String startTime;
    private String endTime;

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }



    public String getOffDownTime() {
        return offDownTime;
    }
    public List<ProductDetailInfo> getBusinessList() {
        return businessList;
    }

    public void setBusinessList(List<ProductDetailInfo> businessList) {
        this.businessList = businessList;
    }

    public void setOffDownTime(String offDownTime) {
        this.offDownTime = offDownTime;
    }
    public ArrayList<ProductDetailInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ProductDetailInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "OffDataListBean{" +
                "list=" + list +
                ", offDownTime='" + offDownTime + '\'' +
                ", businessList=" + businessList +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", classType=" + classType +
                ", bizType=" + bizType +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
