package com.banger.videorecord.mouble.record.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/6/11.
 * 本地记录对象,按照业务分
 */
public class LocalRecordBean implements Serializable {

    private int id;
    private String userName;//姓名
    private String videoType;//录像的业务类型
    private String manageType;//理财类型
    private String manageName;//理财业务名
    private String time;//时间
    private String dueDate;//到期时间

    //环节信息，及该项目会有几个环节的录像和图片
    private String mediaInfo;
    //视频自定义字段
    private String mediaExtraInfo;
    private String UserInfo;//用户额外字段

    private String biz;//唯一表示的业务Id
    private String bizNo;//传递给后台的biz，25位
    private int state;//文件状态 1没上传2正上传3上传成功4上传失败
    private int location;//文件存储位置 0本地 1sd卡


    private boolean isVisiblePro;//是否显示progressbar
    private int upId;//上传状态 2表示上传中 3表示等待上传 4表示重新上传
    private String path;//视频文件路径
    private int progress;//环节一进度条进度
    private int progress2;//环节二进度
    private boolean isSlected;//是否选中
    private boolean isVisible;//是否显示勾选框
    private String xmlPath;//xml文件路
    private String deviceId;//设备ID

    //判断是否在上传中 1代表环节1  2代表环节2
    private boolean isUpdate1=false;
    private boolean isUpdate2=false;
    //判断是否上传成功  1代表环节1  2代表环节2
    private boolean isSuccess1=false;
    private boolean isSuccess2=false;
    //判断是否上传结束 1代表环节1 2代表环节2
    private boolean isEnd1=false;
    private boolean isEnd2=false;
    //图片是否上传完成
    private boolean isImageDone=false;
    //图片是否上传中
    private boolean isImageUpdate=false;
    //图片上传成功和失败的路径
    private List<String> ring1PicList;//环节一图片路径集合
    private List<String> ring2PicList;//环节二图片路径集合
    private List<String> ring1PicListFail;//环节一图片上传失败路径集合
    private List<String> ring2PicListFail;//环节二图片上传失败路径集合
    //判断是重新上传图片的标识
    private boolean isReloadImage;
    private boolean isReloadVideo;
    //判断图片是否有上传失败
    private boolean isImageFail=false;
    //统计成功上传了多少张
    private int numSuccess=0;
    private int numSum=0;
    private String productNum;//产品号

    private int errorState;//异常状态0无异常 1视频缺失 2图片缺失 3均缺失
    List<String> pathList;//资源文件path集合
    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public String getMediaInfo() {
        return mediaInfo;
    }

    public String getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(String userInfo) {
        UserInfo = userInfo;
    }


    public void setMediaInfo(String mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    public String getMediaExtraInfo() {
        return mediaExtraInfo;
    }

    public void setMediaExtraInfo(String mediaExtraInfo) {
        this.mediaExtraInfo = mediaExtraInfo;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getErrorState() {
        return errorState;
    }

    public void setErrorState(int errorState) {
        this.errorState = errorState;
    }



    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }


    public int getNumSum() {
        return numSum;
    }

    public void setNumSum(int numSum) {
        this.numSum = numSum;
    }

    public boolean isImageFail() {
        return isImageFail;
    }

    public void setIsImageFail(boolean isImageFail) {
        this.isImageFail = isImageFail;
    }

    public int getNumSuccess() {
        return numSuccess;
    }

    public void setNumSuccess(int numSuccess) {
        this.numSuccess = numSuccess;
    }

    public boolean isImageUpdate() {
        return isImageUpdate;
    }

    public void setIsImageUpdate(boolean isImageUpdate) {
        this.isImageUpdate = isImageUpdate;
    }

    public boolean isReloadImage() {
        return isReloadImage;
    }

    public void setIsReloadImage(boolean isReloadImage) {
        this.isReloadImage = isReloadImage;
    }

    public boolean isReloadVideo() {
        return isReloadVideo;
    }

    public void setIsReloadVideo(boolean isReloadVideo) {
        this.isReloadVideo = isReloadVideo;
    }

    public List<String> getRing1PicListFail() {
        return ring1PicListFail;
    }

    public void setRing1PicListFail(List<String> ring1PicListFail) {
        this.ring1PicListFail = ring1PicListFail;
    }

    public List<String> getRing2PicListFail() {
        return ring2PicListFail;
    }

    public void setRing2PicListFail(List<String> ring2PicListFail) {
        this.ring2PicListFail = ring2PicListFail;
    }

    public boolean isImageDone() {
        return isImageDone;
    }

    public void setIsImageDone(boolean isImageDone) {
        this.isImageDone = isImageDone;
    }

    public List<String> getRing2PicList() {
        return ring2PicList;
    }

    public void setRing2PicList(List<String> ring2PicList) {
        this.ring2PicList = ring2PicList;
    }

    public List<String> getRing1PicList() {
        return ring1PicList;
    }

    public void setRing1PicList(List<String> ring1PicList) {
        this.ring1PicList = ring1PicList;
    }



    public boolean isEnd1() {
        return isEnd1;
    }

    public void setIsEnd1(boolean isEnd1) {
        this.isEnd1 = isEnd1;
    }


    public int getProgress2() {
        return progress2;
    }

    public void setProgress2(int progress2) {
        this.progress2 = progress2;
    }


    public boolean isEnd2() {
        return isEnd2;
    }

    public void setIsEnd2(boolean isEnd2) {
        this.isEnd2 = isEnd2;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public boolean isSuccess1() {
        return isSuccess1;
    }

    public void setIsSuccess1(boolean isSuccess1) {
        this.isSuccess1 = isSuccess1;
    }

    public boolean isSuccess2() {
        return isSuccess2;
    }

    public void setIsSuccess2(boolean isSuccess2) {
        this.isSuccess2 = isSuccess2;
    }

    public boolean isUpdate1() {
        return isUpdate1;
    }

    public void setIsUpdate1(boolean isUpdate1) {
        this.isUpdate1 = isUpdate1;
    }

    public boolean isUpdate2() {
        return isUpdate2;
    }

    public void setIsUpdate2(boolean isUpdate2) {
        this.isUpdate2 = isUpdate2;
    }


    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getUpId() {
        return upId;
    }

    public void setUpId(int upId) {
        this.upId = upId;
    }


    public boolean isVisiblePro() {
        return isVisiblePro;
    }

    public void setIsVisiblePro(boolean isVisiblePro) {
        this.isVisiblePro = isVisiblePro;
    }


    public boolean isSlected() {
        return isSlected;
    }

    public void setIsSlected(boolean isSlected) {
        this.isSlected = isSlected;
    }


    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LocalRecordBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", videoType='" + videoType + '\'' +
                ", manageType='" + manageType + '\'' +
                ", manageName='" + manageName + '\'' +
                ", time='" + time + '\'' +
                ", biz='" + biz + '\'' +
                ", state=" + state +
                ", location=" + location +
                ", isVisiblePro=" + isVisiblePro +
                ", upId=" + upId +
                ", path='" + path + '\'' +
                ", progress=" + progress +
                ", isSlected=" + isSlected +
                ", isVisible=" + isVisible +
                ", xmlPath='" + xmlPath + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
