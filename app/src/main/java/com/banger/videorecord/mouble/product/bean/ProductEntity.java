package com.banger.videorecord.mouble.product.bean;


import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/23.
 */
public class ProductEntity implements Serializable {
    //产品详情 用户购买的产品
    private ProductDetailInfo productDetailInfo;
    //证件类型
    private int certType;
    //证件号码
    private String  certNum;
    //用户姓名
    private String  userName;
    //购买金额
    private int   buyNum;
    //购买类型
    private int   buyType;
    //到期时间
    private String dueDate;
    //拍照环节
    private int videoProcess;
    //每一笔业务的唯一上传标识
    private String bizNo;

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public int getVideoProcess() {
        return videoProcess;
    }

    public void setVideoProcess(int videoProcess) {
        this.videoProcess = videoProcess;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getBuyType() {
        return buyType;
    }

    public void setBuyType(int buyType) {
        this.buyType = buyType;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public ProductDetailInfo getProductDetailInfo() {
        return productDetailInfo;
    }

    public void setProductDetailInfo(ProductDetailInfo productDetailInfo) {
        this.productDetailInfo = productDetailInfo;
    }

    public int getCertType() {
        return certType;
    }

    public void setCertType(int certType) {
        this.certType = certType;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
