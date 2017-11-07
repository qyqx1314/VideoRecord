package com.banger.videorecord.mouble.setting.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.DataBase;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.TestWheelDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import common.wheel.widget.WheelView;

@EActivity(R.layout.activity_video_setting)
public class VideoSettingActivity extends AppCompatActivity {
    private static final String TAG = "VideoSettingActivity";
    @ViewById
    TextView activity_title;
    @ViewById
    ImageView img_go_back;
    @ViewById
    TextView txt_save;
    @ViewById
    TextView txt_radio;
    @ViewById
    RelativeLayout layout_wheel;
    @ViewById
    LinearLayout layout_video_radio;
    private int type = 1;
    private String [] planets = new String[]{"省流","标清","高清"};
    @AfterViews
    void initView() {
        activity_title.setText("参数设置");
//        txt_save.setVisibility(View.GONE);
        type = SharedPrefsUtil.getInstance().getIntValue(this, "videoType", 1);
        txt_radio.setText(planets[type]);
    }

    @Click
    void layout_video_radio() {

        UiHelper.getInstance().showDialog("取消","确定",this, planets, type, new com.banger.videorecord.widget.WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
//                Log.e(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                type = selectedIndex;
            }
        }, new UiHelper.OnCustomListener() {
            @Override
            public void confirm(com.banger.videorecord.widget.WheelView.OnWheelViewListener onWheelViewListener, AlertDialog dialog) {
                dialog.dismiss();
                //不为0 也就是滑动了。没滑动无需设置
                if (onWheelViewListener.selectedIndex!=0){
                    type = onWheelViewListener.selectedIndex-1;
                    txt_radio.setText(planets[type]);
                }
            }
            @Override
            public void cancle(AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }
    @Click
    void txt_save() {
        SharedPrefsUtil.getInstance().putIntValue(VideoSettingActivity.this, "videoType", type);
        finish();
    }
    @Click
    void img_go_back(){
        finish();
    }
}
