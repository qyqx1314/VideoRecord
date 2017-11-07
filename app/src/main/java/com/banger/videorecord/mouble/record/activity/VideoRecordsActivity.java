package com.banger.videorecord.mouble.record.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.adapter.FragmentTabAdapterForList;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.record.fragment.LocalRecordFragment;
import com.banger.videorecord.mouble.record.fragment.LocalRecordFragment_;
import com.banger.videorecord.mouble.record.fragment.UpRecordFragment;
import com.banger.videorecord.mouble.record.fragment.UpRecordFragment_;
import com.banger.videorecord.mouble.record.fragment.UpSuccessFragment;
import com.banger.videorecord.mouble.record.fragment.UpSuccessFragment_;
import com.banger.videorecord.mouble.record.widget.CustomViewPager;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StorageUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_video_records)
public class VideoRecordsActivity extends FragmentActivity implements  LocalRecordFragment.onItemLongClickListener,LocalRecordFragment.onUpDeleteClickListener,UpSuccessFragment.onSuccessItemLongClickListener,UpSuccessFragment.onDeleteClickListener {
    // 一个tab页面对应一个Fragment
    private List<Fragment> mFragmentList =  new ArrayList<Fragment>();
    private static final String TAG = "VideoRecordsActivity";
    private View[] viewDatas;
    private View[] views;
    @ViewById
    TextView record_local;
    @ViewById
    TextView record_up;
    @ViewById
    TextView record_suc;

    @ViewById
    FrameLayout record_tab_content;
    @ViewById
    View record_local_;
    @ViewById
    View record_up_;
    @ViewById
    View record_suc_;
    @ViewById
    ImageView img_setting;//搜索
    @ViewById
    TextView txt_cancel,txt_save;//取消和全选
    @App
    AppContext appContext;
    int postion = 0;
    StorageUtils storageUtils;
    SharedPrefsUtil sp;
    int state=0;//全选，取消状态
    boolean flag;//全选全不选的标记
//    @ColorRes
//    Color royalblue;
//    @ColorRes
//    Color transparent;
    private LocalRecordFragment localRecordFragment;//本地
    private UpRecordFragment upRecordFragment;//上传队列
    private UpSuccessFragment upSuccessFragment;//上传成功
    private  FragmentTabAdapterForList tabAdapter;
    private  int loginState = 0;
    @AfterViews
    void initViews(){
        txt_save.setTag(true);
        loginState = appContext.getLoginState();
        localRecordFragment = new LocalRecordFragment_();

        upSuccessFragment = new UpSuccessFragment_();
        if(localRecordFragment.state==2){
            img_setting.setVisibility(View.GONE);
            txt_cancel.setVisibility(View.VISIBLE);
            txt_save.setVisibility(View.VISIBLE);
        }
        mFragmentList.add(localRecordFragment);
        if (loginState==0){
            upRecordFragment = new UpRecordFragment_();
            mFragmentList.add(upRecordFragment);
        }
        mFragmentList.add(upSuccessFragment);
        if (loginState==0){
            record_up.setVisibility(View.VISIBLE);
            record_up_.setVisibility(View.VISIBLE);
            viewDatas= new View[]{record_local, record_up, record_suc};
            views = new View[]{record_local_, record_up_, record_suc_};
        }else {
            record_up.setVisibility(View.GONE);
            record_up_.setVisibility(View.GONE);
            viewDatas= new View[]{record_local, record_suc};
            views = new View[]{record_local_, record_suc_};
        }

        tabAdapter = new FragmentTabAdapterForList(VideoRecordsActivity.this, mFragmentList, R.id.record_tab_content, viewDatas);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapterForList.OnRgsExtraCheckedChangedListener() {
            public void OnRgsExtraCheckedChanged(View view, int index) {
                changedTextColor(index);
            }
        });
    }
    void changedTextColor(int index) {
            switch (index) {
                case 0:
                    changedBg(record_local_);
                    break;
                case 1:
                    if (loginState==0){
                        changedBg(record_up_);
                    }else {
                        changedBg(record_suc_);
                    }
                    break;
                case 2:
                    changedBg(record_suc_);
                    break;
        }
    }
    void changedBg(View view ) {
        for (View li : views) {
            if (li == view) {
                view.setBackgroundResource(R.color.common_blue);
//                view.setBackgroundColor(getResources().getColor(R.color.royalblue));
            } else {
//                li.setBackgroundColor(getResources().getColor(R.color.transparent));
                li.setBackgroundResource(R.color.transparent);
            }
        }
    }
    @Override
    public void onItemLongClick() {//调用接口方法
        tabAdapter.setStateClick(false);
        img_setting.setVisibility(View.GONE);
        txt_cancel.setVisibility(View.VISIBLE);
        txt_save.setVisibility(View.VISIBLE);
    }
    @Click
    void img_setting() {//搜索
        Intent intent=new Intent(VideoRecordsActivity.this,VideoSearchActivity_.class);
        startActivity(intent);
    }


    @Click
    void txt_cancel(){//取消
        doCancel();
        txt_save.setTag(false);
        setTextTag();

    }

    private void doCancel(){
        img_setting.setVisibility(View.VISIBLE);//visible
        txt_cancel.setVisibility(View.GONE);
        txt_save.setVisibility(View.GONE);
        tabAdapter.setStateClick(true);
        Intent intent = new Intent();
        intent.setAction(Constants.TABVIDEORECEIVEBROADCAST);
        intent.putExtra(Constants.TABVIDEOACTIVITYTYPE, 1);
        if(localRecordFragment.isVisible()){
            localRecordFragment.cancelBtn();
        }else if(upSuccessFragment.isVisible()){
            upSuccessFragment.cancelBtn();
        }
        VideoRecordsActivity.this.sendBroadcast(intent);
    }
    @Click
    void txt_save(){//全选
        setTextTag();
        if(state == 2){
            if(localRecordFragment.isVisible()){
                localRecordFragment.allSelect();
            }else if(upSuccessFragment.isVisible()){
                upSuccessFragment.allSelect();
            }
        }else if(state == 1){
            if(localRecordFragment.isVisible()){
                localRecordFragment.allNotSelect();
            }else if(upSuccessFragment.isVisible()){
                upSuccessFragment.allNotSelect();
            }
        }
    }

    private void setTextTag(){

        flag = (boolean) txt_save.getTag();//点击变为false
        if (!flag) {//没有点击
            txt_save.setText("全选");
            txt_save.setTag(true);
            state = 1;
        } else {
            txt_save.setText("全不选");
            txt_save.setTag(false);
            state = 2;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown: "+event.getKeyCode() );

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            showDia();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 提示确认退出
     */
    private void showDia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoRecordsActivity.this);
        builder.setMessage("确定要退出应用吗？");
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    /**
     * 上传成功长按接口
     */
    @Override
    public void onSuccessItemLongClick() {
        tabAdapter.setStateClick(false);
        img_setting.setVisibility(View.GONE);
        txt_cancel.setVisibility(View.VISIBLE);
        txt_save.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpClick() {
        doCancel();
    }

    @Override
    public void onDeleteClick() {
        doCancel();
    }

    @Override
    public void onDeleteClickListen() {
        doCancel();
    }

}
