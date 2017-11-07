package com.banger.videorecord.mouble.setting.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.business.ContentCallBack;
import com.banger.videorecord.mouble.record.business.ContentClickListener;
import com.banger.videorecord.mouble.record.widget.RoundProgressBar;
import com.banger.videorecord.mouble.setting.bean.OffPackageListHodler;
import com.banger.videorecord.mouble.setting.bean.OfflineImportHistory;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;
import com.microcredit.adapter.widget.CenterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/7/13.
 */
@EFragment(R.layout.fragment_lead_history)
public class LeadHistoryFragment extends Fragment {
    private static final String TAG = "LeadHistoryFragment";
    @ViewById
    ListView lead_list;
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    ArrayList<OfflineImportHistory> list=new ArrayList<>();
    @AfterViews
    void initViews(){
        initData();
        initListViews();
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                OffPackageListHodler holder = new OffPackageListHodler();
                resIdBean.setAdpterObject(holder);
                holder.setDataName((TextView) view.findViewById(R.id.package_name));
                holder.setLeadTime((TextView) view.findViewById(R.id.lead_time));
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                OffPackageListHodler holder  = (OffPackageListHodler) resIdBean.getAdpterObject();
                OfflineImportHistory bean = (OfflineImportHistory) resIdBean.getList().get(position);
                holder.getDataName().setText(bean.getOfflineName());
                holder.getLeadTime().setText(bean.getImportTime());
            }

            @Override
            public void setListener(Object object, Object data) {
                OffPackageListHodler holder = (OffPackageListHodler) object;
//                LocalRecordBean bean = (LocalRecordBean) data;
            }
        }, resIdBean);

        lead_list.setAdapter(adapter);
//        ViewTreeObserver observer = lead_list.getViewTreeObserver();
//        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                Log.e(TAG, "onPreDraw: " );
//                return true;
//            }
//        });
    }

    private void initData() {
        list.addAll(DataSupport.findAll(OfflineImportHistory.class));
    }

    private void initListViews() {
        resIdBean = new ResIdBean();
        resIdBean.setLayoutId(R.layout.item_lead_history);
        resIdBean.setContext(getActivity());
        resIdBean.setList(list);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        initData();
        adapter.notifyDataSetInvalidated();
        Log.e(TAG, "onResume: " );
    }
}
