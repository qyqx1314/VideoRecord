package com.banger.videorecord.mouble.record.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.BizTypeResult;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.activity.EditVideoActivity_;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordHolder;
import com.banger.videorecord.mouble.record.business.ContentCallBack;
import com.banger.videorecord.mouble.record.business.ContentClickListener;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.mouble.record.widget.MySelectItemDialog;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;
import com.microcredit.adapter.widget.CenterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xuchaowen on 2016/6/21.
 * 首页搜索页面
 */
@EActivity(R.layout.activity_video_search)
public class VideoSearchActivity extends Activity {
    @ViewById
    TextView txt_save;//取消
    @ViewById
    TextView activity_title;
    @ViewById
    EditText user_name;//客户姓名
    @ViewById
    EditText note_id;//证件号码
    @ViewById
    LinearLayout ll_search;//搜索框
    @ViewById
    ListView search_list;
    ArrayList<LocalRecordBean> list = new ArrayList<>();
    List<BusinessInfoBean> businessList = new ArrayList<>();
    LocalRecordBean localRecordBean;
    BusinessInfoBean bean;
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    @AfterViews
    void initViews(){
        txt_save.setText("取消");
        activity_title.setText("搜索");
    }

    @Click
    void img_go_back(){
        finish();
    }

    @Click
    void btn_search(){//搜索
        String userName=user_name.getText().toString().trim();//客户姓名
        String noteId=note_id.getText().toString().trim();//证件号码
        if(userName.length()==0 && noteId.length()==0){
            ToastUtil.showShortToast(VideoSearchActivity.this,"搜索条件不能为空");
        }else{
            businessList=DBVideoUtils.findAllByIdName(userName,noteId);

            if (null != list) {
                if(businessList.size()!=0){
                    ll_search.setVisibility(View.GONE);
                    search_list.setVisibility(View.VISIBLE);
                    initData();
                    initListViews();
                    initAdapter();
                }else{
                    ToastUtil.showShortToast(VideoSearchActivity.this,"当前搜索没有结果，请检查搜索条件");
                }

            }
        }
    }

    private void initAdapter() {
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                LocalRecordHolder holder = new LocalRecordHolder();
                resIdBean.setAdpterObject(holder);
                holder.setUserName((TextView) view.findViewById(R.id.tv_name));
                holder.setTime((TextView) view.findViewById(R.id.manage_time));
                holder.setVideoType((TextView) view.findViewById(R.id.manage));
                holder.setManageType((TextView) view.findViewById(R.id.manage_type));
                holder.setManageName((TextView) view.findViewById(R.id.manage_name));
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                LocalRecordHolder holder = (LocalRecordHolder) resIdBean.getAdpterObject();
                LocalRecordBean bean = (LocalRecordBean) resIdBean.getList().get(position);
                holder.getUserName().setText(bean.getUserName());
                holder.getTime().setText(bean.getTime());
                holder.getVideoType().setText(bean.getVideoType());
                holder.getManageType().setText(bean.getManageType());
                holder.getManageName().setText(bean.getManageName());
            }

            @Override
            public void setListener(Object object, Object data) {
                LocalRecordHolder holder = (LocalRecordHolder) object;
                LocalRecordBean bean = (LocalRecordBean) data;
            }
        }, resIdBean);

        search_list.setAdapter(adapter);
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoSearchActivity.this, EditVideoActivity_.class);
                intent.putExtra("localRecordBean", list.get(position));
                startActivity(intent);
            }
        });

    }

    private void initData() {
        for (int i = 0; i < businessList.size(); i++) {
            bean = businessList.get(i);
            if (bean.getState() == 1 ) {
                localRecordBean = new LocalRecordBean();
                localRecordBean.setUserName(bean.getUserName());
                localRecordBean.setBiz(bean.getBizNo());//业务号得到文件名
                localRecordBean.setBizNo(bean.getBizNo());
                localRecordBean.setVideoType(bean.getBizType());
                localRecordBean.setManageType(bean.getProductType());
                localRecordBean.setManageName(bean.getProductName());
                localRecordBean.setTime(bean.getCreateTime());
                localRecordBean.setState(bean.getState());
                localRecordBean.setId(bean.getId());
//                localRecordBean.setRing1State(1);
//                localRecordBean.setRing2State(1);
                list.add(localRecordBean);
            }
        }
    }

    private void initListViews() {
        resIdBean = new ResIdBean();
        resIdBean.setLayoutId(R.layout.item_local_record);
        resIdBean.setContext(VideoSearchActivity.this);
        resIdBean.setList(list);
    }

    @Click
    void txt_save(){//取消
        finish();
    }
}
