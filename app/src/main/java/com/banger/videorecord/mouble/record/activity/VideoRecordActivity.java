package com.banger.videorecord.mouble.record.activity;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.adapter.FragmentAdapter;
import com.banger.videorecord.mouble.record.fragment.LocalRecordFragment;
import com.banger.videorecord.mouble.record.fragment.LocalRecordFragment_;
import com.banger.videorecord.mouble.record.fragment.UpRecordFragment;
import com.banger.videorecord.mouble.record.fragment.UpRecordFragment_;
import com.banger.videorecord.mouble.record.fragment.UpSuccessFragment;
import com.banger.videorecord.mouble.record.fragment.UpSuccessFragment_;
import com.banger.videorecord.mouble.record.widget.CustomViewPager;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StorageUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/6/7.
 * 录像记录页面
 */
@EActivity(R.layout.activity_video_record)
public class VideoRecordActivity extends FragmentActivity implements  LocalRecordFragment.onItemLongClickListener,LocalRecordFragment.onUpDeleteClickListener,UpSuccessFragment.onSuccessItemLongClickListener,UpSuccessFragment.onDeleteClickListener {
    private static final String TAG = "VideoRecordActivity";
    @ViewById
    ImageView img_setting;//设置
    @ViewById
    CustomViewPager id_page_vp;

    @ViewById
    TextView txt_cancel,txt_save;//取消和全选
    @ViewById(R.id.sliding_tabs)
    TabLayout tabLayout;
    boolean clickState = true;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private LocalRecordFragment localRecordFragment;//本地
    private UpRecordFragment upRecordFragment;//上传队列
    private UpSuccessFragment upSuccessFragment;//上传成功
    int postion = 0;
    StorageUtils storageUtils;
    SharedPrefsUtil sp;
    int state=0;//全选，取消状态
//    boolean flag;//全选全不选的标记
    private View tabView0,tabView1,tabView2;
    @AfterViews
    void initViews() {
        txt_save.setTag(true);
        sp=SharedPrefsUtil.getInstance();
        storageUtils=StorageUtils.getInstance();

        localRecordFragment = new LocalRecordFragment_();
        upRecordFragment = new UpRecordFragment_();
        upSuccessFragment = new UpSuccessFragment_();
        if(localRecordFragment.state==2){
            img_setting.setVisibility(View.GONE);
            txt_cancel.setVisibility(View.VISIBLE);
            txt_save.setVisibility(View.VISIBLE);
        }
        mFragmentList.add(localRecordFragment);
        mFragmentList.add(upRecordFragment);
        mFragmentList.add(upSuccessFragment);

        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(),VideoRecordActivity.this, mFragmentList);

        id_page_vp.setAdapter(mFragmentAdapter);
        tabLayout.setupWithViewPager(id_page_vp);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setEnabled(false);
        tabLayout.setClickable(false);
        id_page_vp.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(new tabSelect());
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(mFragmentAdapter.getTabView(i));
//                if (tab.getCustomView() != null) {
//                    View tabView = (View) tab.getCustomView().getParent();
//                    tabView.setTag(i);
//                    tabView.setOnClickListener(null);
//                    switch (i){
//                        case 0:
//                            tabView0 = tabView;
//                            break;
//                        case 1:
//                            tabView1 =tabView;
//                            break;
//
//                        case 2:
//                            tabView2 = tabView;
//                            break;
//                    }
//
//                }
//            }
//        }

    }
    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();
            if (!clickState) {
                if (pos ==(int)tabView0.getTag()&&(pos!=postion)){
                    switch (postion){
                        case 1:
                                tabView1.performClick();
                            break;
                        case 2:
                                tabView2.performClick();
                            break;
                    }
                }else if (pos ==(int)tabView1.getTag()&&(pos!=postion)){
                    switch (postion){
                        case 0:
                                tabView0.performClick();
                            break;
                        case 2:
                                tabView2.performClick();
                            break;
                    }
                }else if (pos ==(int)tabView2.getTag()&&(pos!=postion)){
                    switch (postion){
                        case 0:
                                tabView0.performClick();
                            break;
                        case 1:
                                tabView1.performClick();
                            break;
                    }
                }
                if (id_page_vp.getCurrentItem()!=pos){
                Toast.makeText(VideoRecordActivity.this, "请操作完成在切换", Toast.LENGTH_SHORT).show();
                }
                //TODO 跳转到登录界面
                TabLayout.Tab tab = tabLayout.getTabAt(pos);
                if (tab != null) {
                    tab.isSelected();
                    Log.e(TAG, "onClick: ---->>"+  tab.isSelected());
                }
            } else {
                TabLayout.Tab tab = tabLayout.getTabAt(pos);
                if (tab != null) {
                    tab.select();
                }
            }
        }
    };
    @Override
    public void onItemLongClick() {//调用接口方法
        //禁止左右滑动
        id_page_vp.setScanScroll(false);
        clickState = false;
        img_setting.setVisibility(View.GONE);
        txt_cancel.setVisibility(View.VISIBLE);
        txt_save.setVisibility(View.VISIBLE);
    }



    class tabSelect implements TabLayout.OnTabSelectedListener{
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.e(TAG, "onTabSelected: "+clickState+"----"+ tab.getPosition());
            if (clickState){
                switch (tab.getPosition()){
                    case 0:
                        id_page_vp.setCurrentItem(0);
                        postion = 0;
                        break;
                    case 1:
                        id_page_vp.setCurrentItem(1);
                        postion = 1;
                        break;

                    case 2:
                        id_page_vp.setCurrentItem(2);
                        postion = 2;
                        break;
                }
            }else {

            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }

    @Click
    void img_setting() {//搜索
        Intent intent=new Intent(VideoRecordActivity.this,VideoSearchActivity_.class);
        startActivity(intent);
    }


    @Click
    void txt_cancel(){//取消
        doCancel();
//        txt_save.setTag(true);

    }

    private void doCancel(){
        //开启左右滑动
        id_page_vp.setScanScroll(true);
        tabLayout.setClickable(true);
        clickState = true;
        clickState = true;
        img_setting.setVisibility(View.GONE);//visible
        txt_cancel.setVisibility(View.GONE);
        txt_save.setVisibility(View.GONE);

        Intent intent = new Intent();
        intent.setAction(Constants.TABVIDEORECEIVEBROADCAST);
        intent.putExtra(Constants.TABVIDEOACTIVITYTYPE, 1);
        if(localRecordFragment.isVisible()){
            localRecordFragment.cancelBtn();
        }else if(upSuccessFragment.isVisible()){
            upSuccessFragment.cancelBtn();
        }
        VideoRecordActivity.this.sendBroadcast(intent);
    }
    @Click
    void txt_save(){//全选
        boolean flag = (boolean) txt_save.getTag();//点击变为false
        if (!flag) {//没有点击
            txt_save.setText("全选");
            txt_save.setTag(true);
            state = 1;
        } else {
            txt_save.setText("全不选");
            txt_save.setTag(false);
            state = 2;
        }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoRecordActivity.this);
        builder.setMessage("确定要退出应用吗？");
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        //开启左右滑动
        id_page_vp.setScanScroll(false);
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
