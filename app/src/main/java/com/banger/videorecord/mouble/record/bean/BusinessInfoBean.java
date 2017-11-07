package com.banger.videorecord.mouble.record.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/28.
 */
public class BusinessInfoBean extends DataSupport implements Serializable {
    private int id;
    private String bizNo;
    //业务类型和Id
    private String bizType;
    private int bizTypeId;
    //产品类型和Id
    private String productType;
    private int productTypeId;
    //产品名称
    private String productName;
    //产品id
    private int productId;
    //创建时间
    private String createTime;
    //业务号
    private String businessNo;
    //购买金额
    private int buyMoney;
    //购买类型
    private int buyType;
    //产品到期时间
    private String  dueDate;
    // 1是没上传，2是上传中 3是上传中  4是上传失败
    private int state;
    private String account;
    //备注
    private String note;
    private String xmlFilePath;
    //用户信息，自定义添加的字段
    private String userInfo;
    //产品的额外信息
    private String productInfo;
    //环节信息，及该项目会有几个环节的录像和图片
    private String mediaInfo;
    //视频自定义字段
    private String mediaExtraInfo;
    //用户必填信息 之前删掉的  rlgl的逻辑
    private String  userName;
    private int  certType;
    private String  certNumber;
    private String  mobile;

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getMediaExtraInfo() {
        return mediaExtraInfo;
    }

    public void setMediaExtraInfo(String mediaExtraInfo) {
        this.mediaExtraInfo = mediaExtraInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCertType() {
        return certType;
    }

    public void setCertType(int certType) {
        this.certType = certType;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMediaInfo() {
        return mediaInfo;
    }

    public void setMediaInfo(String mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    public String getXmlFilePath() {
        return xmlFilePath;
    }

    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBizType() {
        return bizType;
    }

    public int getBizTypeId() {
        return bizTypeId;
    }

    public void setBizTypeId(int bizTypeId) {
        this.bizTypeId = bizTypeId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(int buyMoney) {
        this.buyMoney = buyMoney;
    }

    public int getBuyType() {
        return buyType;
    }

    public void setBuyType(int buyType) {
        this.buyType = buyType;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "BusinessInfoBean{" +
                "id=" + id +
                ", bizNo='" + bizNo + '\'' +
                ", bizType='" + bizType + '\'' +
                ", bizTypeId=" + bizTypeId +
                ", productType='" + productType + '\'' +
                ", productTypeId=" + productTypeId +
                ", productName='" + productName + '\'' +
                ", productId=" + productId +
                ", createTime='" + createTime + '\'' +
                ", businessNo='" + businessNo + '\'' +
                ", buyMoney=" + buyMoney +
                ", buyType=" + buyType +
                ", dueDate='" + dueDate + '\'' +
                ", state=" + state +
                ", account='" + account + '\'' +
                ", note='" + note + '\'' +
                ", xmlFilePath='" + xmlFilePath + '\'' +
                ", userInfo='" + userInfo + '\'' +
                ", productInfo='" + productInfo + '\'' +
                ", mediaInfo='" + mediaInfo + '\'' +
                ", mediaExtraInfo='" + mediaExtraInfo + '\'' +
                ", userName='" + userName + '\'' +
                ", certType=" + certType +
                ", certNumber='" + certNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
