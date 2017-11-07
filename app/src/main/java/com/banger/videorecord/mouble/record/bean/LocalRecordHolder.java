package com.banger.videorecord.mouble.record.bean;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.mouble.record.widget.RoundProgressBar;

import java.util.List;

/**
 * Created by Xuchaowen on 2016/6/11.
 * 本地记录holder
 */
public class LocalRecordHolder {

    private TextView userName;//姓名
    private TextView productNum;//产品编号
    private CheckBox isSlected;//是否选中
    private TextView videoType;//录像的业务类型
    private TextView manageType;//理财类型
    private TextView manageName;//理财业务名
    private TextView time;//时间
    private TextView errorToast;//异常提示
    //环节一
    private LinearLayout ll_ring1;//环节一布局
    private ImageView up_bg;//重新上传和等待上传标题
    private RoundProgressBar upProgress;//上传进度
    private TextView upState;//上传状态文字描述
    private RelativeLayout rl_my;//上传信息布局

    private TextView ring1VideoName;//环节一视频名
    private TextView ring1VideoTime;//视频一时长
    private TextView ring1VideoSize;//环节一视频大小

    //环节二
    private LinearLayout ll_ring2;//环节一布局
    private TextView ring2VideoName;//环节二视频名
    private TextView ring2VideoTime;//视频二时长
    private TextView ring2VideoSize;//环节二视频大小
    private ImageView ring2_iv;//环节二重新上传和等待上传标题
    private RoundProgressBar upProgress2;//环节二上传进度
    private TextView upState2;//环节二上传状态文字描述
    private RelativeLayout ring2_rl;//环节二上传信息布局

    private TextView picCount;//照片数
    private TextView haveUpPic;//已经上传数
    private TextView picTotal;//照片总数
    private RelativeLayout re_up;//照片上传信息布局
    private LinearLayout ll_pic;//凭证布局


    public LinearLayout getLl_ring2() {
        return ll_ring2;
    }

    public void setLl_ring2(LinearLayout ll_ring2) {
        this.ll_ring2 = ll_ring2;
    }

    public LinearLayout getLl_ring1() {
        return ll_ring1;
    }

    public void setLl_ring1(LinearLayout ll_ring1) {
        this.ll_ring1 = ll_ring1;
    }


    public LinearLayout getLl_pic() {
        return ll_pic;
    }

    public void setLl_pic(LinearLayout ll_pic) {
        this.ll_pic = ll_pic;
    }

    public ImageView getRing2_iv() {
        return ring2_iv;
    }

    public void setRing2_iv(ImageView ring2_iv) {
        this.ring2_iv = ring2_iv;
    }

    public RelativeLayout getRing2_rl() {
        return ring2_rl;
    }

    public void setRing2_rl(RelativeLayout ring2_rl) {
        this.ring2_rl = ring2_rl;
    }

    public RoundProgressBar getUpProgress2() {
        return upProgress2;
    }

    public void setUpProgress2(RoundProgressBar upProgress2) {
        this.upProgress2 = upProgress2;
    }

    public TextView getUpState2() {
        return upState2;
    }

    public void setUpState2(TextView upState2) {
        this.upState2 = upState2;
    }

    public RelativeLayout getRe_up() {
        return re_up;
    }

    public void setRe_up(RelativeLayout re_up) {
        this.re_up = re_up;
    }

    public TextView getRing1VideoName() {
        return ring1VideoName;
    }

    public void setRing1VideoName(TextView ring1VideoName) {
        this.ring1VideoName = ring1VideoName;
    }

    public TextView getRing1VideoTime() {
        return ring1VideoTime;
    }

    public void setRing1VideoTime(TextView ring1VideoTime) {
        this.ring1VideoTime = ring1VideoTime;
    }

    public TextView getRing1VideoSize() {
        return ring1VideoSize;
    }

    public void setRing1VideoSize(TextView ring1VideoSize) {
        this.ring1VideoSize = ring1VideoSize;
    }

    public TextView getRing2VideoName() {
        return ring2VideoName;
    }

    public void setRing2VideoName(TextView ring2VideoName) {
        this.ring2VideoName = ring2VideoName;
    }

    public TextView getRing2VideoTime() {
        return ring2VideoTime;
    }

    public void setRing2VideoTime(TextView ring2VideoTime) {
        this.ring2VideoTime = ring2VideoTime;
    }

    public TextView getRing2VideoSize() {
        return ring2VideoSize;
    }

    public void setRing2VideoSize(TextView ring2VideoSize) {
        this.ring2VideoSize = ring2VideoSize;
    }

    public TextView getPicCount() {
        return picCount;
    }

    public void setPicCount(TextView picCount) {
        this.picCount = picCount;
    }

    public TextView getHaveUpPic() {
        return haveUpPic;
    }

    public void setHaveUpPic(TextView haveUpPic) {
        this.haveUpPic = haveUpPic;
    }

    public TextView getPicTotal() {
        return picTotal;
    }

    public void setPicTotal(TextView picTotal) {
        this.picTotal = picTotal;
    }

    public ImageView getUp_bg() {
        return up_bg;
    }

    public void setUp_bg(ImageView up_bg) {
        this.up_bg = up_bg;
    }



    public RoundProgressBar getUpProgress() {
        return upProgress;
    }

    public void setUpProgress(RoundProgressBar upProgress) {
        this.upProgress = upProgress;
    }


    public RelativeLayout getRl_my() {
        return rl_my;
    }

    public void setRl_my(RelativeLayout rl_my) {
        this.rl_my = rl_my;
    }



    public TextView getUpState() {
        return upState;
    }

    public void setUpState(TextView upState) {
        this.upState = upState;
    }

    public TextView getUserName() {
        return userName;
    }

    public void setUserName(TextView userName) {
        this.userName = userName;
    }

    public CheckBox getIsSlected() {
        return isSlected;
    }

    public void setIsSlected(CheckBox isSlected) {
        this.isSlected = isSlected;
    }

    public TextView getVideoType() {
        return videoType;
    }

    public void setVideoType(TextView videoType) {
        this.videoType = videoType;
    }

    public TextView getManageType() {
        return manageType;
    }

    public void setManageType(TextView manageType) {
        this.manageType = manageType;
    }

    public TextView getManageName() {
        return manageName;
    }

    public void setManageName(TextView manageName) {
        this.manageName = manageName;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }
    public TextView getProductNum() {
        return productNum;
    }

    public void setProductNum(TextView productNum) {
        this.productNum = productNum;
    }
    public TextView getErrorToast() {
        return errorToast;
    }

    public void setErrorToast(TextView errorToast) {
        this.errorToast = errorToast;
    }
}
