package com.banger.videorecord.mouble.setting.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.adapter.FragmentTabAdapterForList;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.record.bean.ImageListInfo;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.business.CameraBus;
import com.banger.videorecord.mouble.record.fragment.LocalRecordFragment;
import com.banger.videorecord.mouble.record.fragment.LocalRecordFragment_;
import com.banger.videorecord.mouble.record.fragment.UpSuccessFragment;
import com.banger.videorecord.mouble.record.fragment.UpSuccessFragment_;
import com.banger.videorecord.mouble.setting.bean.OffDataListBean;
import com.banger.videorecord.mouble.setting.fragment.DownLoadHistoryFragment;
import com.banger.videorecord.mouble.setting.fragment.DownLoadHistoryFragment_;
import com.banger.videorecord.mouble.setting.fragment.LeadHistoryFragment;
import com.banger.videorecord.mouble.setting.fragment.LeadHistoryFragment_;
import com.banger.videorecord.mouble.setting.fragment.OffDataListFragment;
import com.banger.videorecord.mouble.setting.fragment.OffDataListFragment_;
import com.banger.videorecord.mouble.setting.fragment.OffPackageListFragment;
import com.banger.videorecord.mouble.setting.fragment.OffPackageListFragment_;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/7/12.
 * 离线数据
 */
@EActivity(R.layout.activity_offline_data)
public class OffLineDataActivity extends FragmentActivity {

    private List<Fragment> mFragmentList =  new ArrayList<Fragment>();
    private static final String TAG = "VideoRecordsActivity";
    private View[] viewDatas;
    private View[] views;
    @ViewById
    TextView record_local;
    @ViewById
    TextView record_suc;
    @ViewById
    FrameLayout record_tab_content;
    @ViewById
    View record_local_;//离线
    @ViewById
    View record_suc_;//历史下载
    @ViewById
    ImageView img_setting;//搜索
    @App
    AppContext appContext;

    int netType=-1;//离线数据状态,0在线，1离线

    private OffDataListFragment offDataListFragment;//离线数据
    private DownLoadHistoryFragment downLoadHistoryFragment;//下载历史
    private OffPackageListFragment offPackageListFragment;//离线数据包列表
    private LeadHistoryFragment leadHistoryFragment;//导入历史
    private FragmentTabAdapterForList tabAdapter;
    @AfterViews
    void initViews(){
        netType=appContext.getLoginState();
        offDataListFragment = new OffDataListFragment_();
        downLoadHistoryFragment = new DownLoadHistoryFragment_();
        offPackageListFragment=new OffPackageListFragment_();
        leadHistoryFragment=new LeadHistoryFragment_();
        if(netType==1){
            if(mFragmentList.size()>0){
                mFragmentList.clear();
            }
            mFragmentList.add(offPackageListFragment);
            mFragmentList.add(leadHistoryFragment);
            record_local.setText("数据列表");
            record_suc.setText("导入历史");
            img_setting.setVisibility(View.GONE);
        }else{
            if(mFragmentList.size()>0){
                mFragmentList.clear();
            }
            mFragmentList.add(offDataListFragment);
            mFragmentList.add(downLoadHistoryFragment);
            record_local.setText("数据列表");
            record_suc.setText("下载历史");
            img_setting.setVisibility(View.VISIBLE);
        }
        viewDatas= new View[]{record_local, record_suc};
        views = new View[]{record_local_,record_suc_};
        tabAdapter = new FragmentTabAdapterForList(OffLineDataActivity.this, mFragmentList, R.id.record_tab_content, viewDatas);
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
                changedBg(record_suc_);
                break;
        }
    }
    void changedBg(View view ) {
        for (View li : views) {
            if (li == view) {
                view.setBackgroundResource(R.color.royalblue);
            } else {
                li.setBackgroundResource(R.color.transparent);
            }
        }
    }
    @Click
    void img_setting(){//离线数据搜索
        Intent intent=new Intent(OffLineDataActivity.this,OffDataSearchActivity_.class);
        startActivity(intent);
    }

    @Click
    void img_go_back(){
        finish();
    }

}
