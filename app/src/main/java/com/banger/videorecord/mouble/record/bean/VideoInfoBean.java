package com.banger.videorecord.mouble.record.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/6/27.
 */
public class VideoInfoBean extends DataSupport implements Serializable {
    private int id;
    //上传到服务器的唯一标识
    private String bizNo;
    //业务类型
    private String bizType;
    //产品类型
    private String productType;
    //产品名称
    private String productName;
    //产品id
    private int productId;
    //创建时间
    private String createTime;
    //存储状态 1代表没有上传  2代表正在上传  3代表上传成功
    private int state;
    //存储位置0代表内置存储卡 1代表外置存储卡
    private int location;
    //*********************************这一块是record的用于存储用户信息和产品信息
    //业务号
    private String businessNo;
    //购买金额
    private int buyMoney;
    //购买类型
    private int buyType;
    //产品到期时间
    private String  dueDate;
    //*********************************这一块是video的用于存储视频的相关信息以及本机信息
    //流水号，这里就时文件名（不带后缀）
    private String recordNo;
    //文件名（不带后缀）
    private String fileId;
    //录像开始时间  格式 2012-12-12 12:12:12
    private String beginTime;
    //录像结束时间  格式 2012-12-12 12:12:12
    private String endTime;
    //时长
    private int  recTime;
    //文件名
    private String fileName;
    //文件名
    private String filePath;
    //文件大小
    private long fileSize;
    //账号，没有取员工号
    private String account;
    //录像环节
    private int  videoProcess;
    //录像上传进度
    private int  progressPercent;
    //新增的环节id
    private String processId;
    //新增环节名称
    private String processName;

    //nc删了又要加上的字段  rlgl
    private String  userName;
    private String  certType;
    private String  certNumber;
    private String  mobile;
    //录像的额外信息
    private String extraInfo;


    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
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

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
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

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getRecTime() {
        return recTime;
    }

    public void setRecTime(int recTime) {
        this.recTime = recTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getVideoProcess() {
        return videoProcess;
    }

    public void setVideoProcess(int videoProcess) {
        this.videoProcess = videoProcess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }


    public String getBizType() {
        return bizType;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "VideoInfoBean{" +
                ", bizType='" + bizType + '\'' +
                ", productType='" + productType + '\'' +
                ", productName='" + productName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", state=" + state +
                ", location=" + location +
                '}';
    }
}
