package com.banger.videorecord.mouble.product.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.business.ProductBusinessImp;
import com.banger.videorecord.mouble.product.business.ProductBusinessInt;
import com.banger.videorecord.mouble.record.activity.AddVideoNewActivity_;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.setting.bean.ProductListResult;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.GsonUtil;
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
 * Created by zhusiliang on 16/6/21.
 */
@EActivity(R.layout.activity_product_detail_list)
public class ProductDetailListActivity extends AppCompatActivity {
    public static String TAG = "ProductDetailListActivity";
    @ViewById
    ImageView img_go_back;
    @ViewById
    TextView activity_title;
    @ViewById
    RefreshLayout layout_refresh;
    @ViewById(R.id.listView)
    ListView mListView;
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    private ArrayList<ProductDetailInfo> list;
    @Bean
    ProductBusinessImp productBusinessImp;
    @Bean
    HttpParams httpParams;
    @App
    AppContext appContext;
    @Bean(ProductBusinessImp.class)
    ProductBusinessInt productBus;
    private ProdClass prodClass;
    private BizType bizType;
    private boolean isNative = false;
    private int page = 1;
    private boolean isLoading = false;
    //新增传递过来的大类
    private BusinessDataInfo businessDataInfo;

    @AfterViews
    void initViews() {
        activity_title.setText("选择产品");
        prodClass = (ProdClass) getIntent().getSerializableExtra("product");
        bizType = (BizType) getIntent().getSerializableExtra("businessType");
        businessDataInfo = (BusinessDataInfo) getIntent().getSerializableExtra(Constants.Business_Info);
        isNative = getIntent().getBooleanExtra("isNative", false);
        if (appContext.getLoginState() == 1) {
            initNative();
        } else {
            if (!appContext.isNetworkConnected()) {
                initNative();
                return;
            }
            getProductList();
        }
    }

    @Click
    void img_go_back() {
        finish();
    }



    private void initNative() {
        layout_refresh.setEnabled(false);
        if (prodClass != null) {
            List<ProductDetailInfo> productDetailInfoList = DBVideoUtils.findProductDetail(bizType.getTypeId(), prodClass.getClassId());
            list = (ArrayList<ProductDetailInfo>) productDetailInfoList;
        } else {
            List<ProductDetailInfo> productDetailInfoList = DBVideoUtils.findProductDetail(bizType.getTypeId());
            list = (ArrayList<ProductDetailInfo>) productDetailInfoList;
        }
        if (list != null && list.size() > 0) {
            initAdapter();
            initListView();
        }
    }

    private void initAdapter() {
        resIdBean = new ResIdBean();
        resIdBean.setList(list);
        resIdBean.setLayoutId(R.layout.item_product_detail);
        resIdBean.setContext(ProductDetailListActivity.this);
    }

    private void initListView() {
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                productBusinessImp.bindProduct(view, resIdBean);
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                productBusinessImp.setDataProduct(resIdBean, position);
            }

            @Override
            public void setListener(Object object, Object data) {

            }
        }, resIdBean);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductDetailListActivity.this, AddVideoNewActivity_.class);
                intent.putExtra("product", list.get(position));
                intent.putExtra(Constants.Business_Info, businessDataInfo);
                setResult(Constants.ADD_PRODUCT, intent);
                finish();
//                startActivity(intent);
            }
        });
        layout_refresh
                .setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_green_dark,
                        android.R.color.holo_blue_light, android.R.color.holo_orange_dark);
        if(appContext.getLoginState()==1){
            layout_refresh.setEnabled(false);
        }
        layout_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (list != null) {
                    list.clear();
                }
                    page = 1;
                    getProductList();
                    layout_refresh.setRefreshing(false);

            }
        });
        layout_refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                isLoading = true;
                getProductList();
            }
        });

    }

    //获取产品详情list
    private void getProductList() {
        HttpTool httpTool = RetrofitUtils.createApi(ProductDetailListActivity.this, HttpTool.class);
        int classId = -1;
        if (prodClass != null) {
            classId = prodClass.getClassId();
        }
        httpTool.getProductList(httpParams.getProductDetailList(appContext, bizType.getTypeId(), classId, page, 10))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductListResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        layout_refresh.setRefreshing(false);
                        if (isLoading) {
                            layout_refresh.setLoading(false);
                            isLoading = false;
                        }
                        initNative();
                    }

                    @Override
                    public void onNext(ProductListResult productListResult) {
                        Log.e(TAG, "projosn=="+ GsonUtil.getInstance().toJson(productListResult.getData()));
                        layout_refresh.setRefreshing(false);
                        if (isLoading) {
                            layout_refresh.setLoading(false);
                            isLoading = false;
                        }
                        Log.e(TAG, "onNext: " + productListResult.toString());
                        if (list == null) {
                            list = new ArrayList<>();
                        }
//                        if (page>1&&productListResult.getData() == null || productListResult.getData().size() == 0) {
//                            ToastUtil.showShortToast(ProductDetailListActivity.this, "已经是最后一页了");
//                            return;
//                        }
                        if (page == 1) {
                            list = productListResult.getData();
                        } else {
                            list.addAll(productListResult.getData());
                        }
                         if(adapter==null){
                             initAdapter();
                             initListView();
                         }else {
                             adapter.notifyDataSetChanged();
                         }


                        saveDatabase(productListResult.getData());
                        List<ProductDetailInfo> productDetailInfoList = DBVideoUtils.findAllProduct();
                        Log.e(TAG, "onNext: ====="+productDetailInfoList.get(0).toString());

//                        if (list != null && list.size() > 0) {
//                            DBVideoUtils.saveProductDetail(list);
//                        }

                    }
                });
    }

    private void saveDatabase(ArrayList<ProductDetailInfo> productDetailList) {
        if (productDetailList != null && productDetailList.size() > 0) {
            for (ProductDetailInfo productDetailInfo : productDetailList) {
                productBus.updateProductDB(productDetailInfo);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADD_PRODUCT && data != null) {
            ProductDetailInfo info = (ProductDetailInfo) data.getSerializableExtra("product");
            if (info != null) {
                Intent intent = new Intent();
                intent.putExtra("product", info);
                setResult(Constants.ADD_PRODUCT, intent);
                finish();
            }
        }
    }

    //产品搜索
    @Click
    void img_setting() {
        Intent intent = new Intent(ProductDetailListActivity.this, ProductSearchActivity_.class);
        startActivityForResult(intent, Constants.ADD_PRODUCT);
    }
}

