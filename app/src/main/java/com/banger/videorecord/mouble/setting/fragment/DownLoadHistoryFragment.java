package com.banger.videorecord.mouble.setting.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.setting.bean.DownLoadBean;
import com.banger.videorecord.mouble.setting.bean.OffDataListBean;
import com.banger.videorecord.mouble.setting.bean.OffHistoryHolder;
import com.banger.videorecord.mouble.setting.bean.OfflineImportHistory;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;
import com.microcredit.adapter.widget.CenterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by Xuchaowen on 2016/7/12.
 * 下载历史
 */
@EFragment(R.layout.fragment_download_history)
public class DownLoadHistoryFragment extends Fragment {

    @ViewById
    ListView history_list;
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    ArrayList<DownLoadBean>  datalist = new ArrayList<>();
    @AfterViews
    void initViews(){
        intData();
        initListViews();
        initAdapter();
    }

    private void intData() {
        datalist.addAll(DataSupport.findAll(DownLoadBean.class));
    }


    private void initAdapter() {
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                OffHistoryHolder holder = new OffHistoryHolder();
                resIdBean.setAdpterObject(holder);
                holder.setTime((TextView) view.findViewById(R.id.off_time));
                holder.setDownloadState((TextView) view.findViewById(R.id.down_state));
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                OffHistoryHolder holder  = (OffHistoryHolder) resIdBean.getAdpterObject();
                DownLoadBean bean = (DownLoadBean)resIdBean.getList().get(position);
                holder.getTime().setText(bean.getDownTime());
                holder.getDownloadState().setText("下载成功");
            }

            @Override
            public void setListener(Object object, Object data) {
            }
        }, resIdBean);

        history_list.setAdapter(adapter);
    }


    private void initListViews() {
        resIdBean = new ResIdBean();
        resIdBean.setLayoutId(R.layout.item_off_download);
        resIdBean.setContext(getActivity());
        resIdBean.setList(datalist);
    }


    @Override
    public void onResume() {
        super.onResume();
        datalist.clear();
        intData();
        adapter.notifyDataSetInvalidated();
    }
}
