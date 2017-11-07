package com.banger.videorecord.mouble.record.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.mouble.product.activity.EditVideoActivity_;
import com.banger.videorecord.mouble.record.activity.EditVideoNewActivity_;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordHolder;
import com.banger.videorecord.mouble.record.bean.UpdateListInfo;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.bean.XmlMessageBean;
import com.banger.videorecord.mouble.record.business.ContentCallBack;
import com.banger.videorecord.mouble.record.business.ContentClickListener;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.FormatUtil;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StorageUtils;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.MySelectDialog;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;
import com.microcredit.adapter.myview.RefreshLayout;
import com.microcredit.adapter.widget.CenterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_local_record)
public class LocalRecordFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    private static final String TAG = "LocalRecordFragment";
    @ViewById
    TextView txt_record, txt_my;
    @ViewById
    TextView internal;//内存大小
    @ViewById
    ProgressBar save_progress;
    @ViewById(R.id.swipe_layout)
    RefreshLayout myRefreshListView;//刷新
    @ViewById
    LinearLayout rl_deal;//删除和上传父控件
    @ViewById
    TextView no_toast;
    @ViewById
    RelativeLayout rl_record, rl_my;//删除和上传
    @ViewById
    LinearLayout ll_record, ll_my;//删除和上传父控件
    @ViewById(R.id.local_list)
    ListView local_list;
    ArrayList<LocalRecordBean> list = new ArrayList<>();
    List<BusinessInfoBean> businessList = new ArrayList<>();
    List<VideoInfoBean> videoList = new ArrayList<>();//视频信息
    public ArrayList<LocalRecordBean> isSelect = new ArrayList<>();//选中的条目集合
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    public int state = 1;//1表示为普通状态，2表示设置状态
    LocalRecordBean localRecordBean;
    private UpdateListInfo updateListInfo;
    BusinessInfoBean bean;
    onItemLongClickListener listener;
    onUpDeleteClickListener upDeleteClickListener;
    MyBroadcastReciver myBroadcastReciver = new MyBroadcastReciver();
    @App
    AppContext app;
    int logState;

    @AfterViews
    void initViews() {
        logState=app.getLoginState();
        updateListInfo = new UpdateListInfo();
        if (app.getLoginState() == 0) {
            ll_my.setVisibility(View.VISIBLE);
        } else {
            ll_my.setVisibility(View.GONE);
        }
        init();
        initListViews();
        initAdapter();
        //刷新
        if (null != myRefreshListView) {
            myRefreshListView
                    .setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
                            android.R.color.holo_blue_light, android.R.color.holo_orange_dark);
            myRefreshListView.setOnRefreshListener(this);
            myRefreshListView.setOnLoadListener(this);
        }

            local_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), EditVideoNewActivity_.class);
                    intent.putExtra("localRecordBean", list.get(position));
                    startActivity(intent);
                }
            });
        }

    private void initAdapter() {
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                LocalRecordHolder holder = new LocalRecordHolder();
                resIdBean.setAdpterObject(holder);
                holder.setUserName((TextView) view.findViewById(R.id.tv_name));
                holder.setTime((TextView) view.findViewById(R.id.manage_time));
                holder.setIsSlected((CheckBox) view.findViewById(R.id.check_box));
                holder.setVideoType((TextView) view.findViewById(R.id.manage));
                holder.setManageType((TextView) view.findViewById(R.id.manage_type));
                holder.setManageName((TextView) view.findViewById(R.id.manage_name));
                holder.setErrorToast((TextView) view.findViewById(R.id.error_text));
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                LocalRecordHolder holder = (LocalRecordHolder) resIdBean.getAdpterObject();
                LocalRecordBean bean = (LocalRecordBean) resIdBean.getList().get(position);
                holder.getUserName().setText(bean.getUserName());
                if (bean.isVisible()) {
                    holder.getIsSlected().setVisibility(View.VISIBLE);
                } else {
                    holder.getIsSlected().setVisibility(View.GONE);
                }
                if (bean.isSlected()) {
                    holder.getIsSlected().setChecked(true);
                } else {
                    holder.getIsSlected().setChecked(false);
                }
                holder.getTime().setText(FormatUtil.formatTime(bean.getTime()));
                holder.getVideoType().setText(bean.getVideoType());
                Log.e(TAG, "setData: getVideoType is--" + bean.getVideoType() + "----" + bean.getManageName() + bean.getUserName());
                holder.getManageType().setText(bean.getManageType());
                holder.getManageName().setText(bean.getManageName());

                int errorState=bean.getErrorState();
                if(errorState==0){
                    holder.getErrorToast().setVisibility(View.GONE);
                }else{
                    holder.getErrorToast().setVisibility(View.VISIBLE);
                    switch (errorState) {
                        case 1:
                            holder.getErrorToast().setText("视频文件缺失");
                            break;
                        case 2:
                            holder.getErrorToast().setText("图片文件缺失");
                            break;
                        case 3:
                            holder.getErrorToast().setText("视频及图片文件缺失");
                            break;
                    }

                }

            }

            @Override
            public void setListener(Object object, Object data) {
                LocalRecordHolder holder = (LocalRecordHolder) object;
                LocalRecordBean bean = (LocalRecordBean) data;

                ContentClickListener addCheckListener = new ContentClickListener(getActivity(), bean, holder, new ContentCallBack() {
                    @Override
                    public void Click(Context context, Object bean, Object holder) {
                        LocalRecordBean recordBean = (LocalRecordBean) bean;
                        LocalRecordHolder localRecordHolder = (LocalRecordHolder) holder;
                        if (localRecordHolder.getIsSlected().isChecked()) {
                            localRecordHolder.getIsSlected().setChecked(true);
                            recordBean.setIsSlected(true);
                            isSelect.add(recordBean);

                        } else {
                            localRecordHolder.getIsSlected().setChecked(false);
                            recordBean.setIsSlected(false);
                            isSelect.remove(recordBean);
                        }
                    }
                });
                holder.getIsSlected().setOnClickListener(addCheckListener);
            }
        }, resIdBean);
        if (null != local_list) {
            local_list.setAdapter(adapter);
    }


        local_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemLongClick();
                banFresh(true);
                for (int i = 0; i < list.size(); i++) {
                    localRecordBean = list.get(i);
                    if(localRecordBean.getErrorState()==0){//文件状态
                        localRecordBean.setIsVisible(true);
                    }else{
                        localRecordBean.setIsVisible(false);
                    }
                }
                adapter.notifyDataSetChanged();
                state = 2;
                rl_deal.setVisibility(View.VISIBLE);
                myRefreshListView.setEnabled(false);//禁掉下拉刷新
                myRefreshListView.cancelLongPress();

                txt_record.setText("删除");
                txt_my.setText("上传");
//                add_record.setVisibility(View.INVISIBLE);

                Intent intent = new Intent();
                intent.setAction(Constants.TABVIDEORECEIVEBROADCAST);
                intent.putExtra(Constants.TABVIDEOACTIVITYTYPE, 0);
                getActivity().sendBroadcast(intent);
                return false;
            }
        });

    }

    private void init() {
        receiveBroadcast(Constants.ADD_BROADCAST);
        if (null != list) {
            list.clear();
        }
        if(null != businessList){
            businessList.clear();
        }
       String account = SharedPrefsUtil.getInstance().getStringValue(getActivity(), Constants.USER_NAME, "admin");

        if(logState==0){
            businessList=DBVideoUtils.findAllByStateAccount(1,account);
        }else{
            businessList=DBVideoUtils.findAllByStateAccount(1,"");
        }


        if (null != businessList&&businessList.size()>0) {
            for (int i = 0; i < businessList.size(); i++) {
                bean = businessList.get(i);
                boolean isVideo=true,isImage=true;
                List<String> pathList = new ArrayList<>();
                if (pathList.size() != 0) {
                    pathList.clear();
                }
                List<VideoInfoBean> videoList1 = DBVideoUtils.findAllByBiz(bean.getBizNo());
                if (null!=videoList1&&videoList1.size()>0){
                     isVideo = FileUtils.isExitFile(videoList1);
                }
                FileUtils.saveVideoPath(pathList, videoList1);

                List<ImageInfoBean> imageInfoBeanList = DBVideoUtils.findAllByBizImage(bean
                        .getBizNo());

                if (null!=imageInfoBeanList&&imageInfoBeanList.size()>0){
                     isImage = FileUtils.isExitFileImage(imageInfoBeanList);
                }
                FileUtils.saveImagePath(pathList, imageInfoBeanList);
                 //   Log.e("aaaa", "bean.getAccount():==" + bean.getAccount() + "----getUserName" + bean.getUserName());
                    localRecordBean = new LocalRecordBean();
                    localRecordBean.setBiz(bean.getBizNo());//业务号得到文件名
                    localRecordBean.setVideoType(bean.getBizType());
                    localRecordBean.setManageType(bean.getProductType());
                    localRecordBean.setManageName(bean.getProductName());
                    localRecordBean.setBizNo(bean.getBizNo());
                    localRecordBean.setId(bean.getId());
                    localRecordBean.setUserName(bean.getUserName());
                    localRecordBean.setTime(bean.getCreateTime());
                    localRecordBean.setState(bean.getState());
                    localRecordBean.setDueDate(bean.getDueDate());
                    localRecordBean.setXmlPath(bean.getXmlFilePath());
                    localRecordBean.setUserInfo(bean.getUserInfo());
                    localRecordBean.setMediaExtraInfo(bean.getMediaExtraInfo());
                    localRecordBean.setMediaInfo(bean.getMediaInfo());
//                    localRecordBean.setUserName(bean.getUserName());
//                    localRecordBean.setBiz(bean.getBiz());//业务号得到文件名
//                    localRecordBean.setRing1State(1);
//                    localRecordBean.setRing2State(1);
                    localRecordBean.setPathList(pathList);

                    if (isVideo && isImage) {
                        localRecordBean.setErrorState(0);
                    }

                    if (isImage && !isVideo) {
                            localRecordBean.setErrorState(1);
                    }

                    if (isVideo && !isImage) {
                            localRecordBean.setErrorState(2);
                    }

                    if (!isImage && !isVideo) {
                            localRecordBean.setErrorState(3);
                    }
                    list.add(localRecordBean);
                }
        }
    }


    /**
     * 长按接口
     */
    public interface onItemLongClickListener {

         void onItemLongClick();

    }

    /**
     * 删除和上传接口
     */
    public interface onUpDeleteClickListener {

         void onUpClick();

         void onDeleteClick();

    }

    /**
     * 全选
     */
    public void allSelect() {

        if (null != isSelect) {
            isSelect.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            localRecordBean = list.get(i);
            localRecordBean.setIsVisible(true);
            localRecordBean.setIsSlected(true);
            isSelect.add(localRecordBean);
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * 全不选
     */
    public void allNotSelect() {

        for (int i = 0; i < list.size(); i++) {
            localRecordBean = list.get(i);
            localRecordBean.setIsVisible(true);
            localRecordBean.setIsSlected(false);
            isSelect.clear();
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * 取消
     */
    public void cancelBtn() {

        banFresh(false);
        if (isSelect != null) {
            isSelect.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            localRecordBean = list.get(i);
            localRecordBean.setIsVisible(false);
            localRecordBean.setIsSlected(false);
        }

        rl_deal.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 这个方法是用来确认当前的Activity容器是否已经继承了该接口，如果没有将抛出异常
        try {
            listener = (onItemLongClickListener) activity;
            upDeleteClickListener = (onUpDeleteClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Click
    void rl_record() {//删除
        if (isSelect.size() > 0) {
            showDeleteDia();
        } else {
            ToastUtil.showShortToast(getActivity(), "当前没有选中删除项");
        }

    }

    /**
     * 删除选中文件
     */
    private void deleteFile() {

        if (isSelect.size() > 0) {
            for (int i = 0; i < isSelect.size(); i++) {
                localRecordBean = isSelect.get(i);//先删除文件，再删除表记录，
                Log.e(TAG, "addUpload: =" + localRecordBean.getManageName() + "==" + localRecordBean.getVideoType() + "==" + localRecordBean.getManageType());
                for (BusinessInfoBean bean : businessList) {
                    if (bean.getBizNo() == localRecordBean.getBiz()) {
                        DBVideoUtils.deleteBusinessInfo(bean.getId());
                    }
                }
                CutClipFile(localRecordBean);
                list.remove(localRecordBean);
            }
            ToastUtil.showShortToast(getActivity(), "删除成功");
            upDeleteClickListener.onDeleteClick();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 上传,将选中的数据传到上传队列,判断选中的文件是否存在
     */
    @Click
    void rl_my() {
        int netState = app.getNetworkType();
        switch (netState) {
            case 0:
                ToastUtil.showShortToast(getActivity(), "网络连接异常，请检查");
                break;
            case 1:
                addUpload();
                break;
            default:
                showDia();
                break;
        }
    }

    /**
     * 禁止刷新
     *
     * @param isFrashing
     */
    private void banFresh(boolean isFrashing) {
        if (isFrashing) {
            myRefreshListView.setEnabled(false);
            myRefreshListView.setOnLoadListener(null);
        } else {
            myRefreshListView.setEnabled(true);
            myRefreshListView.setOnLoadListener(this);
        }
    }

    /**
     * 提示网络问题
     */
    private void showDia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否使用移动网络继续上传?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addUpload();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //添加队列，不上传，等待wifi连接时上传
                addUpload();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showDeleteDia() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("删除记录将同时删除视频文件和凭证，删除后无法恢复，您确定要删除当前记录吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile();
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
     * 添加下载
     */
    private void addUpload() {
        if (isSelect.size() > 0) {
            for (int i = 0; i < isSelect.size(); i++) {
                LocalRecordBean localRecordBean = isSelect.get(i);
                for (BusinessInfoBean bean : businessList) {
                    if (bean.getBizNo() == localRecordBean.getBizNo()) {
                        bean.setState(2);
                        DBVideoUtils.updateBusinessInfo(bean.getId(), bean);
                        videoList = DBVideoUtils.findAllByBiz(bean.getBizNo());//获取视频信息
                    }
                }
                list.remove(localRecordBean);
            }

            if (updateListInfo.getList().size() > 0) {
                updateListInfo.getList().clear();
            }
            updateListInfo.getList().addAll(isSelect);
            adapter.notifyDataSetChanged();
            Intent intent = new Intent();
            intent.putExtra("updateListInfo", updateListInfo);
            intent.setAction(Constants.UP_BROADCAST);
            getActivity().sendBroadcast(intent);
            upDeleteClickListener.onUpClick();
            isSelect.clear();
            initViews();
        } else {
            ToastUtil.showShortToast(getActivity(), "当前没有选中上传项");
        }

    }

    private void deleteXml(String path){
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param localRecordBean
     */
    @Background
    void CutClipFile(LocalRecordBean localRecordBean) {

        if(logState==0){
            deleteXml(Constants.ONLINE_XML_PATH + localRecordBean.getBiz() + ".xml");
        }else{
            deleteXml(Constants.OFFLINE_XML_PATH + localRecordBean.getBiz() + ".xml");
        }

        List<String>pathlist=localRecordBean.getPathList();
        for(int i=0;i<pathlist.size();i++){
            File file1=new File(pathlist.get(i));
            if(file1.exists()){
                file1.delete();
            }
        }

        updateUi();
    }

    @UiThread
    void updateUi() {
        initViews();
    }

    private void initListViews() {
        resIdBean = new ResIdBean();
        resIdBean.setLayoutId(R.layout.item_local_record);
        resIdBean.setContext(getActivity());
        resIdBean.setList(list);
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
            if (action.equals(Constants.ADD_BROADCAST)) {
                initViews();
            }
        }
    }


    @Override
    public void onLoad() {
//        page++;
//        isLoading = true;
//        init(page);
//        initListViews();
//        initAdapter()
//        adapter.notifyDataSetChanged();
        // 加载完后调用该方法
        ToastUtil.showShortToast(getActivity(),"没有更多记录了");
        myRefreshListView.setLoading(false);


    }

    @Override
    public void onRefresh() {
//        page = 1;
        initViews();
//        adapter.notifyDataSetChanged();
        myRefreshListView.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myBroadcastReciver);
    }

}
