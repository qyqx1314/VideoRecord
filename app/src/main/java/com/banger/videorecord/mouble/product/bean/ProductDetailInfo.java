package com.banger.videorecord.mouble.product.bean;

import com.banger.videorecord.mouble.record.bean.FiledInfo;
import com.banger.videorecord.mouble.record.bean.ProductExtraInfo;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhusiliang on 16/6/18.
 */
public class ProductDetailInfo  extends DataSupport implements Serializable {
    private int productId;
    private String productCode;
    private String productName;
    private int bizType;
    private String bizTypeName;
    private int productClass;
    private String productClassName;
    private String updateDate;
    private int id;
    //新增的自定义字段
    private List<ProductExtraInfo> templateFields;

    public List<ProductExtraInfo> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(List<ProductExtraInfo> templateFields) {
        this.templateFields = templateFields;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public int getProductClass() {
        return productClass;
    }

    public void setProductClass(int productClass) {
        this.productClass = productClass;
    }

    public String getProductClassName() {
        return productClassName;
    }

    public void setProductClassName(String productClassName) {
        this.productClassName = productClassName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


    @Override
    public String toString() {
        return "ProductDetailInfo{" +
                "productId='" + productId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", bizType='" + bizType + '\'' +
                ", bizTypeName='" + bizTypeName + '\'' +
                ", productClass='" + productClass + '\'' +
                ", productClassName='" + productClassName + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
