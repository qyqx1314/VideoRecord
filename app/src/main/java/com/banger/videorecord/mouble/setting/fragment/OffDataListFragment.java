package com.banger.videorecord.mouble.setting.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.activity.EditVideoActivity_;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordHolder;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.setting.activity.OffLineDataActivity;
import com.banger.videorecord.mouble.setting.bean.DownLoadBean;
import com.banger.videorecord.mouble.setting.bean.OffDataListBean;
import com.banger.videorecord.mouble.setting.bean.ProductListResult;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;
import com.microcredit.adapter.myview.RefreshLayout;
import com.microcredit.adapter.widget.CenterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xuchaowen on 2016/7/12.
 * 离线数据列表
 */
@EFragment(R.layout.fragment_off_data)
public class OffDataListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @ViewById
    ListView offData_list;
    @ViewById(R.id.swipe_layout)
    RefreshLayout myRefreshListView;//刷新
    @ViewById
    TextView internal;//记录数据条目
    @ViewById
    ImageView iv_down;//下载
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    OffDataListBean offDataListBean = new OffDataListBean();
    MyBroadcastReciver myBroadcastReciver;
    ArrayList<ProductDetailInfo> prolist=new ArrayList<>();
    private int page = 1;
//    private AlertDialog dialog;
    @Bean(HttpParams.class)
    HttpParams httpParams;
    @App
    AppContext appContext;
    OffDataListBean bean;
    private boolean isLoading = false;

    @AfterViews
    void initViews() {
        receiveBroadcast(Constants.OFF_SEARCH);
        internal.setText("共0条数据");
        myRefreshListView.setVisibility(View.GONE);

    }


    private void initAdapter() {
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                LocalRecordHolder holder = new LocalRecordHolder();
                resIdBean.setAdpterObject(holder);
                holder.setUserName((TextView) view.findViewById(R.id.product_name));
                holder.setProductNum((TextView) view.findViewById(R.id.product_num));
                holder.setTime((TextView) view.findViewById(R.id.manage_time));
                holder.setVideoType((TextView) view.findViewById(R.id.manage));
                holder.setManageType((TextView) view.findViewById(R.id.manage_type));
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                LocalRecordHolder holder = (LocalRecordHolder) resIdBean.getAdpterObject();
//                LocalRecordBean bean = (LocalRecordBean) resIdBean.getList().get(position);
                ProductDetailInfo bean = (ProductDetailInfo) resIdBean.getList().get(position);
                holder.getUserName().setText(bean.getProductName());
                holder.getProductNum().setText(bean.getProductCode());
                holder.getTime().setText(bean.getUpdateDate());
                holder.getVideoType().setText(bean.getBizTypeName());
                holder.getManageType().setText(bean.getProductClassName());
            }

            @Override
            public void setListener(Object object, Object data) {

            }
        }, resIdBean);

        offData_list.setAdapter(adapter);

        if (null != myRefreshListView) {
            if (prolist.size() == 0) {
                myRefreshListView.setVisibility(View.GONE);
            } else {
                myRefreshListView.setVisibility(View.VISIBLE);
                myRefreshListView.setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
                        android.R.color.holo_blue_light, android.R.color.holo_orange_dark);
                myRefreshListView.setOnRefreshListener(this);
                if (prolist.size() >= 10) {
                    myRefreshListView.setOnLoadListener(this);
                } else {
                    myRefreshListView.setOnLoadListener(null);
                }
            }

        }
    }

    @Click
    void rl_down() {//下载
        DBVideoUtils.saveProductDetail(prolist);
        ToastUtil.showShortToast(getActivity(), "下载成功");
        if (prolist.size() != 0) {
            for (int i = 0; i < prolist.size(); i++) {
                DownLoadBean bean = new DownLoadBean();
                bean.setCount(prolist.size());
                bean.setDownTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
                bean.save();
            }
            prolist.clear();
            adapter.notifyDataSetChanged();
            initListViews();
        }

    }

    private void initListViews() {
        resIdBean = new ResIdBean();
        resIdBean.setLayoutId(R.layout.item_off_data);
        resIdBean.setContext(getActivity());
        resIdBean.setList(prolist);
//        internal.setText("共" + ((page - 1) * 10 + prolist.size()) + "条数据");

    }

    private void receiveBroadcast(String broadcast) {
        IntentFilter intentFilter = new IntentFilter();
        myBroadcastReciver = new MyBroadcastReciver();
        intentFilter.addAction(broadcast);
        if (null != myBroadcastReciver && null != intentFilter) {
            getActivity().registerReceiver(myBroadcastReciver, intentFilter);
        }
    }

    @Override
    public void onLoad() {
        page++;
        isLoading = true;
        getProductList(offDataListBean);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getProductList(offDataListBean);
        myRefreshListView.setRefreshing(false);
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.OFF_SEARCH)) {
                offDataListBean = (OffDataListBean) intent.getSerializableExtra("search");
                if (offDataListBean != null) {
                    Log.e("ccc", "onReceive: " + offDataListBean.toString());
                    if (null!=prolist && prolist.size() > 0) {
                        prolist.clear();
                        adapter.notifyDataSetChanged();
                        adapter=null;
                    }
                    getProductList(offDataListBean);
                }
            }
        }
    }


    //获取产品详情list
    private void getProductList(OffDataListBean bean) {
//        dialog = UiHelper.getInstance().createProgress(getActivity());
        HttpTool httpTool = RetrofitUtils.createApi(getActivity(), HttpTool.class);
        httpTool.getProductList(httpParams.getProductOffSearchList(appContext, page, 10, bean.getProductName(), bean.getProductCode(), bean.getBizType(), bean.getClassType(), bean.getStartTime(), bean.getEndTime()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductListResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e("qqq", "onCompleted: ");
//                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        myRefreshListView.setRefreshing(false);
                        if (isLoading) {
                            myRefreshListView.setLoading(false);
                            isLoading = false;
                        }
                        myRefreshListView.setEnabled(false);
//                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(ProductListResult productListResult) {
                        myRefreshListView.setRefreshing(false);
                        if (isLoading) {
                            myRefreshListView.setLoading(false);
                            isLoading = false;
                        }
//                        dialog.dismiss();
//                        if (prolist == null) {
//                            prolist = new ArrayList<>();
//                        }

//                        if (prolist.size() > 0) {
//                            prolist.clear();
//                            adapter.notifyDataSetChanged();
//                        }

                        if (page == 1) {
                            prolist = productListResult.getData();
                        } else {
                            prolist.addAll(productListResult.getData());
                            if (productListResult.getData() == null || productListResult.getData().size() == 0) {
//                                ToastUtil.showShortToast(getActivity(), "已经是最后一页了");
                                return;
                            }
                        }

                        internal.setText("共"+prolist.size() + "条数据");
                        if (prolist.size() == 0) {
                            ToastUtil.showShortToast(getActivity(), "没有查到该产品");
                        }
//                        initListViews();
//                        initAdapter();
                        if(adapter==null){
                            initListViews();
                            initAdapter();
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myBroadcastReciver);
    }
}
