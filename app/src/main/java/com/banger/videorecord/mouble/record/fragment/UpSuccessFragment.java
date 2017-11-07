package com.banger.videorecord.mouble.record.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.record.adapter.VideoRecordAdapter;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.InitUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.microcredit.adapter.myview.RefreshLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Xuchaowen on 2016/6/8.
 * 上传成功界面
 */
@EFragment(R.layout.fragment_up_success)
public class UpSuccessFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    private static final String TAG = "UpSuccessFragment";
    @ViewById(R.id.success_list)
    ListView success_list;
    @ViewById(R.id.swipe_layout)
    RefreshLayout myRefreshListView;//刷新
    @ViewById
    RelativeLayout rl_delete;
    LocalRecordBean localRecordBean;
    VideoRecordAdapter adapter;
    @App
    AppContext app;
    List<LocalRecordBean> recordBeanList = new ArrayList<>();
    List<BusinessInfoBean> businessList = new ArrayList<>();
    List<LocalRecordBean> selectlist = new ArrayList<>();
    BusinessInfoBean bean;
    onSuccessItemLongClickListener listener;
    onDeleteClickListener deleteClickListener;
    MyBroadcastReciver myBroadcastReciver = new MyBroadcastReciver();
    int logState;
    @AfterViews
    void initViews() {
        logState=app.getLoginState();
        initDta();
//        InitUtils.initLocalList(recordBeanList);
        adapter = new VideoRecordAdapter(getActivity(), recordBeanList);
        Log.e("bbb", "recordBeanList:=== "+recordBeanList.size());
        if (null != success_list) {
            success_list.setAdapter(adapter);
        }
        success_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                listener.onSuccessItemLongClick();
                banFresh(true);

                for (int i = 0; i < recordBeanList.size(); i++) {
                    localRecordBean = recordBeanList.get(i);
                    localRecordBean.setIsVisible(true);
                }
                adapter.notifyDataSetChanged();

                rl_delete.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setAction(Constants.TABVIDEORECEIVEBROADCAST);
                intent.putExtra(Constants.TABVIDEOACTIVITYTYPE, 0);
                getActivity().sendBroadcast(intent);
                return false;
            }
        });

        if (null != myRefreshListView) {
            myRefreshListView
                    .setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
                            android.R.color.holo_blue_light, android.R.color.holo_orange_dark);
            myRefreshListView.setOnRefreshListener(this);
            myRefreshListView.setOnLoadListener(this);
        }
    }

    private void initDta() {

        receiveBroadcast(Constants.UP_SUCCESS_BROADCAST);
        if (null != recordBeanList&& recordBeanList.size()!=0) {
            recordBeanList.clear();
        }

        if(null!=businessList&&businessList.size()!=0){
            businessList.clear();
        }

        String account = SharedPrefsUtil.getInstance().getStringValue(getActivity(), Constants.USER_NAME, "admin");

        if(logState==0){
            businessList=DBVideoUtils.findAllByStateAccount(3,account);
        }else{
            businessList=DBVideoUtils.findAllByStateAccount(3,"");
        }

//        for(BusinessInfoBean bean:businessList1){
//            if(logState==0 && bean.getAccount().length()!=0){
//                businessList.add(bean);
//            }else if(logState==1 && bean.getAccount().length()==0){
//                businessList.add(bean);
//            }
//        }

        Log.e("bbb", "businessList:=== "+businessList.size());

        if (null != businessList) {
            for (int i = 0; i < businessList.size(); i++) {
                bean = businessList.get(i);
                List<String> pathList=new ArrayList<>();
                if(pathList.size()!=0){
                    pathList.clear();
                }
                List<VideoInfoBean> videoList1=new ArrayList<>();
                videoList1=DBVideoUtils.findAllByBiz(bean.getBizNo());
                FileUtils.saveVideoPath(pathList,videoList1);

                List<ImageInfoBean> imageInfoBeanList1=new ArrayList<>();
                imageInfoBeanList1= DBVideoUtils.findAllByBizImage(bean
                        .getBizNo());
                FileUtils.saveImagePath(pathList, imageInfoBeanList1);

                    localRecordBean = new LocalRecordBean();
                    localRecordBean.setUserName(bean.getUserName());
                    localRecordBean.setBiz(bean.getBizNo());//业务号得到文件名
                    localRecordBean.setVideoType(bean.getBizType());
                    localRecordBean.setManageType(bean.getProductType());
                    localRecordBean.setManageName(bean.getProductName());
                    localRecordBean.setTime(bean.getCreateTime());
                    localRecordBean.setState(bean.getState());
                    localRecordBean.setId(bean.getId());
                    localRecordBean.setPathList(pathList);
                    recordBeanList.add(localRecordBean);
                }
            }

    }

    /**
     * 长按接口
     */
    public interface onSuccessItemLongClickListener {

        public void onSuccessItemLongClick();

    }

    /**
     * 删除接口
     */
    public interface onDeleteClickListener {

        public void onDeleteClickListen();

    }

    public void allSelect() {//全选

        if (null != adapter.isSelect) {
            adapter.isSelect.clear();
        }
        for (int i = 0; i < recordBeanList.size(); i++) {
            localRecordBean = recordBeanList.get(i);
            localRecordBean.setIsVisible(true);
            localRecordBean.setIsSlected(true);
            adapter.isSelect.add(localRecordBean);
        }
//        ToastUtil.showShortToast(getActivity(), "" + selectlist.size());
        adapter.notifyDataSetChanged();

    }

    public void allNotSelect() {//全不选

        for (int i = 0; i < recordBeanList.size(); i++) {
            localRecordBean = recordBeanList.get(i);
            localRecordBean.setIsVisible(true);
            localRecordBean.setIsSlected(false);
            adapter.isSelect.clear();
        }
        adapter.notifyDataSetChanged();

    }

    public void cancelBtn() {//取消

        banFresh(false);
        for (int i = 0; i < recordBeanList.size(); i++) {
            localRecordBean = recordBeanList.get(i);
            localRecordBean.setIsVisible(false);
            localRecordBean.setIsSlected(false);
        }

        rl_delete.setVisibility(View.GONE);
//        internal_bg.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 这个方法是用来确认当前的Activity容器是否已经继承了该接口，如果没有将抛出异常
        try {
            listener = (onSuccessItemLongClickListener) activity;
            deleteClickListener = (onDeleteClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    private void banFresh(boolean isFrashing) {
        if (isFrashing) {
            myRefreshListView.setEnabled(false);
            myRefreshListView.setOnLoadListener(null);
        } else {
            myRefreshListView.setEnabled(true);
            myRefreshListView.setOnLoadListener(this);
        }
    }

    @Click
    void rl_delete() {//删除
        selectlist = adapter.isSelect;//选中
        if (selectlist.size() > 0) {
            showDeleteDia();
        } else {
            ToastUtil.showShortToast(getActivity(), "当前没有选中删除项");
        }
    }


    private void deleteXml(String path){
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    private void deleteFiles() {

        selectlist = adapter.isSelect;//选中
        if (selectlist.size() > 0) {
            for (int i = 0; i < selectlist.size(); i++) {
                localRecordBean = selectlist.get(i);//先删除文件，再删除表记录，
//                File file = new File(Constants.CLASS_PATH + localRecordBean.getBiz());
//                if (file.exists()) {
//                    try {
//                        FileUtils.deteleDatas(file);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                if(logState==0){
                    deleteXml(Constants.ONLINE_XML_PATH + localRecordBean.getBiz() + ".xml");

                }else{
                    deleteXml(Constants.OFFLINE_XML_PATH + localRecordBean.getBiz() + ".xml");
                }
                List<String>pathlist=localRecordBean.getPathList();
                for(int j=0;j<pathlist.size();j++) {
                    File file1 = new File(pathlist.get(j));
                    if (file1.exists()) {
                        file1.delete();
                    }
                }

                for (BusinessInfoBean bean : businessList) {
                    if (bean.getBizNo() == localRecordBean.getBiz()) {
                        DBVideoUtils.deleteBusinessInfo(bean.getId());
                    }
                }
                recordBeanList.remove(localRecordBean);
            }
            ToastUtil.showShortToast(getActivity(), "删除成功");
            deleteClickListener.onDeleteClickListen();
        }
        adapter.notifyDataSetChanged();
        initViews();

    }

    private void showDeleteDia() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("删除记录将同时删除视频文件和凭证，删除后无法恢复，您确定要删除当前记录吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFiles();
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

    private void receiveBroadcast(String broadcast) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(broadcast);
        if (null != myBroadcastReciver && null != intentFilter) {
            getActivity().registerReceiver(myBroadcastReciver, intentFilter);
        }
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.UP_SUCCESS_BROADCAST)) {
                initViews();
            }
        }
    }

    @Override
    public void onLoad() {

//       initViews();
        // 加载完后调用该方法
        ToastUtil.showShortToast(getActivity(),"没有更多记录了");
        myRefreshListView.setLoading(false);

    }

    @Override
    public void onRefresh() {
        initViews();
        // 更新完后调用该方法结束刷新
        myRefreshListView.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myBroadcastReciver);
    }
}
