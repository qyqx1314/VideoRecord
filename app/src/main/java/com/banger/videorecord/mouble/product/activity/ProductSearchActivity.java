package com.banger.videorecord.mouble.product.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.product.business.ProductBusinessImp;
import com.banger.videorecord.mouble.record.activity.AddVideoActivity;
import com.banger.videorecord.mouble.record.activity.AddVideoActivity_;
import com.banger.videorecord.mouble.record.activity.AddVideoNewActivity_;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.setting.bean.ProductListResult;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.ToastUtil;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;
import com.microcredit.adapter.myview.RefreshLayout;
import com.microcredit.adapter.widget.CenterAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xuchaowen on 2016/7/11.
 * 产品搜索界面
 */
@EActivity(R.layout.activiy_product_search)
public class ProductSearchActivity extends Activity {
    @ViewById
    EditText input_product;//输入框
    @ViewById
    ListView product_list;
    @ViewById
    RelativeLayout search_clear;//清除搜索
    @Bean(HttpParams.class)
    HttpParams httpParams;
    private ArrayList<ProductDetailInfo> list=new ArrayList<>();
    @Bean
    ProductBusinessImp productBusinessImp;
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    private ProdClass prodClass;
    private BusinessDataInfo businessDataInfo;
//    private AlertDialog dialog;
    @App
    AppContext appContext;
    int netType = -1;
    private int page = 1;
    String inPut;
    private boolean isLoading = false;
    @ViewById
    RefreshLayout layout_refresh;
    int typeId;

    @AfterViews
    void initViews() {
        businessDataInfo=(BusinessDataInfo)getIntent().getSerializableExtra(Constants.Business_Info);
    }

    private void initListView() {

        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                productBusinessImp.bindSearchProduct(view, resIdBean);

            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                productBusinessImp.setDataSearchProduct(resIdBean, position);
            }

            @Override
            public void setListener(Object object, Object data) {

            }
        }, resIdBean);

        product_list.setAdapter(adapter);
        product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ProductSearchActivity.this, AddVideoNewActivity_.class);
                intent.putExtra("product", list.get(position));
                intent.putExtra(Constants.Business_Info, businessDataInfo);
                Log.e("aa", "onItemClick: "+businessDataInfo.toString() );
                setResult(Constants.ADD_PRODUCT, intent);
                finish();

            }
        });


        if(list.size()==0){
            layout_refresh.setVisibility(View.GONE);
        }else{
            layout_refresh.setVisibility(View.VISIBLE);

            layout_refresh
                    .setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
                            android.R.color.holo_blue_light, android.R.color.holo_orange_dark);

            layout_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                ToastUtil.showShortToast(ProductDetailListActivity.this, "下拉刷新");
                    page = 1;
                    getProductList(inPut);
                    layout_refresh.setRefreshing(false);
                }
            });
            layout_refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
                @Override
                public void onLoad() {
                    if(netType==0){
                        page++;
                        isLoading = true;
                        getProductList(inPut);
                    }else{
                        layout_refresh.setLoading(false);
                    }

                }
            });
        }

    }

    private void initAdapter() {
        resIdBean = new ResIdBean();
        resIdBean.setList(list);
        resIdBean.setLayoutId(R.layout.item_product_search);
        resIdBean.setContext(ProductSearchActivity.this);

    }

    @Click
    void img_go_back() {
        finish();
    }

    @Click
    void txt_search() {//搜索
        netType=appContext.getLoginState();
        inPut = input_product.getText().toString().trim();
        typeId=businessDataInfo.getBizType().getTypeId();
        if (inPut.length() != 0) {
            if (null!=list && list.size() > 0) {
                list.clear();
                adapter.notifyDataSetChanged();
                adapter=null;
            }

            if (netType == 1) {
                list= (ArrayList<ProductDetailInfo>) DBVideoUtils.findProductByIdNum(inPut);
                if(list.size()==0){
                    ToastUtil.showShortToast(ProductSearchActivity.this, "没有查到该产品");
                }else{
                    initAdapter();
                    initListView();
                }
            } else {
                getProductList(inPut);
            }

        } else {
            list.clear();
            adapter.notifyDataSetChanged();
            adapter=null;
            ToastUtil.showShortToast(ProductSearchActivity.this, "搜索条件不能为空");
        }
    }

    @Click
    void input_product() {

        search_clear.setVisibility(View.VISIBLE);
    }

    @Click
    void search_clear() {
        input_product.setText("");
    }

    //获取产品详情list
    private void getProductList(String inPut) {
//        int classId = -1;
//        if (prodClass != null) {
//            classId = prodClass.getClassId();
//        }
//        dialog = UiHelper.getInstance().createProgress(ProductSearchActivity.this);
        HttpTool httpTool = RetrofitUtils.createApi(ProductSearchActivity.this, HttpTool.class);
//        Log.e("id", "getProductList: "+typeId );
        httpTool.getProductList(httpParams.getProductSearchList(appContext,typeId,page, 10, inPut))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductListResult>() {
                    @Override
                    public void onCompleted() {
//                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dialog.dismiss();
                        layout_refresh.setRefreshing(false);
                        if (isLoading) {
                            layout_refresh.setLoading(false);
                            isLoading = false;
                        }
                        layout_refresh.setEnabled(false);
                    }

                    @Override
                    public void onNext(ProductListResult productListResult) {
                        Log.e("ProductListResult", "onNext: "+productListResult.toString());
//                        dialog.dismiss();
                        layout_refresh.setRefreshing(false);
                        if (isLoading) {
                            layout_refresh.setLoading(false);
                            isLoading = false;
                        }
//                        if (list == null) {
//                            list = new ArrayList<>();
//                        }

//                        if (list.size() > 0) {
//                            list.clear();
//                            adapter.notifyDataSetChanged();
//                        }

                        if (page == 1) {
                            list = productListResult.getData();
                        } else {
                            list.addAll(productListResult.getData());
                            if (productListResult.getData() == null || productListResult.getData().size() == 0) {
//                                ToastUtil.showShortToast(ProductSearchActivity.this, "已经是最后一页了");
                                return;
                            }
                        }

                        if (list.size() == 0) {
                            ToastUtil.showShortToast(ProductSearchActivity.this, "没有查到该产品");
                        } else {
                            if(adapter==null){
                                initAdapter();
                                initListView();
                            }else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}
