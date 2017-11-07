package com.banger.videorecord.mouble.record.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.product.activity.BusinessTypeListActivity_;
import com.banger.videorecord.mouble.record.fragment.LocalRecordFragment;
import com.banger.videorecord.mouble.record.util.ImageUtils;
import com.banger.videorecord.mouble.setting.activity.LoginActivity_;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StorageUtils;
import com.banger.videorecord.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Xuchaowen on 2016/6/16.
 * 录像记录管理页签
 */
@EActivity(R.layout.activity_tab_video)
public class TabVideoRecordActivity extends TabActivity {

    private static final String TAG = "TabVideoRecordActivity";
    @ViewById
    LinearLayout frame_ll;//页签父布局
    @ViewById
    TabWidget tabs;//页签
    @ViewById
    ImageView tab_add;
    @ViewById
    RelativeLayout internal_bg;//内存展示
    @ViewById
    TextView internal;//内存大小
    @ViewById
    ProgressBar save_progress;
    @App
    AppContext app;

    private TabHost tab ;
    private LayoutInflater layoutInflater ;
    private MyBroadcastReciver myBroadcastReciver;
    @AfterViews
    void initViews(){
        init();
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.e(TAG, "onTabChanged: " + (tabId));
            }
        });
    }

    private void init(){
        receiveBroadcast(Constants.TABVIDEORECEIVEBROADCAST);
        StorageUtils storageUtils = StorageUtils.getInstance();
        if(storageUtils.getAvailableExternalMemorySize() < 50 * 1024 * 1024){
            save_progress.setProgressDrawable(getResources().getDrawable(R.drawable.pro_bar_red));
        }
        internal.setText("已占用" + storageUtils.getRomUsedSize(TabVideoRecordActivity.this) + ",可用" + storageUtils.getRomAvailableSize(TabVideoRecordActivity.this));
        save_progress.setProgress(storageUtils.getRomUsedPrecent());

        tab = getTabHost();
        layoutInflater = LayoutInflater.from(this);
        int count = Constants.HOME_TAB_ITEM_CLASS_ARRAY.length;
        TabHost.TabSpec tabSpec =null;
        for(int i=0;i<3;i++){
            tabSpec = tab.newTabSpec(Constants.HOME_TAB_ITEM_TAG_ARRAY[i]);
            tabSpec.setIndicator(getTabItemView(i));
            tabSpec.setContent(getTabItemIntent(i));
            tab.addTab(tabSpec);
            tab.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
        }
    }

    @Click
    void tab_add(){
        Intent intent = new Intent(TabVideoRecordActivity.this,AddVideoNewActivity_.class);
        startActivity(intent);
    }
    private void receiveBroadcast(String broadcast) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(broadcast);
        myBroadcastReciver = new MyBroadcastReciver();
        this.registerReceiver(myBroadcastReciver, intentFilter);
    }

    private View getTabItemView(int index){
        //载入新的资源
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageview);
        if (imageView != null)
        {
            imageView.setImageResource(Constants.HOME_TAB_ITEM_IMAGEVIEW_ARRAY[index]);
        }
        TextView textView = (TextView) view.findViewById(R.id.textview);
        if (index!=1)
        textView.setText(Constants.HOME_TAB_ITEM_TEXTVIEW_ARRAY[index]);
        return view;
    }
    //更改页面
    private Intent getTabItemIntent(int index)
    {
        Intent intent = new Intent(this, Constants.HOME_TAB_ITEM_CLASS_ARRAY[index]);
        return intent;
    }


    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Constants.TABVIDEORECEIVEBROADCAST)){
                   switch (intent.getIntExtra(Constants.TABVIDEOACTIVITYTYPE,0)){
                       case 1:
                           //显示
                           frame_ll.setVisibility(View.VISIBLE);
                           break;
                       case 0:
                            //隐藏
                           frame_ll.setVisibility(View.GONE);
                           break;
                       case 2:
                           finish();
                           android.os.Process.killProcess(android.os.Process.myPid());
                           System.exit(0);
                           break;

                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myBroadcastReciver);
    }
}
