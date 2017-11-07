package com.banger.videorecord.mouble.product.activity;

import android.app.AlertDialog;
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
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.bean.ProductClass;
import com.banger.videorecord.mouble.product.business.ProductBusinessImp;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.setting.bean.ProductListResult;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.GsonUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.MyListView;
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

@EActivity(R.layout.activity_product_type_list)
public class ProductTypeListActivity extends AppCompatActivity {
    public static String TAG = "ProductTypeListActivity";
    @ViewById
    ImageView img_go_back;
    @ViewById
    TextView activity_title;
    @ViewById(R.id.listView)
    MyListView mListView;
    @ViewById(R.id.listView2)
    MyListView mListView2;
    @ViewById
    View line;
    private CenterAdapter adapter;
    private CenterAdapter adapter2;
    private ResIdBean resIdBean;
    private ResIdBean resIdBean2;
    private ArrayList<ProdClass> list;
    private ArrayList<ProductDetailInfo> productDetailList;
    @Bean
    ProductBusinessImp productBusinessImp;
    @Bean
    HttpParams httpParams;
    @App
    AppContext appContext;
    private BusinessDataInfo businessDataInfo;
    //区分是本地查库还是访问网络
    private boolean isNative = false;
    private AlertDialog dialog;

    @AfterViews
    void initViews() {
        activity_title.setText("产品详情");
        list = new ArrayList<>();
        productDetailList = new ArrayList<>();
        businessDataInfo = (BusinessDataInfo) getIntent().getSerializableExtra(Constants.Business_Info);
        Log.e(TAG, "initViews: ====="+businessDataInfo.getBizType().getProcessInfo());
        isNative = getIntent().getBooleanExtra("isNative", false);
        if (appContext.getLoginState() == 1) {
            initNative1();
            initNative2();
        } else {
            list = businessDataInfo.getProdClass();
            if (list == null || list.size() == 0) {
                list = new ArrayList<>();
                initNative1();
                initNative2();
                return;
            }
            getProductList();
        }

        //获取大类产品列表

    }

    @Click
    void img_go_back() {
        finish();
    }

    private void initListView1() {
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                productBusinessImp.bindProductType(view, resIdBean);
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                productBusinessImp.setDataProductType(resIdBean, position);
            }

            @Override
            public void setListener(Object object, Object data) {

            }
        }, resIdBean);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductTypeListActivity.this, ProductDetailListActivity_.class);
                intent.putExtra("businessType", businessDataInfo.getBizType());
                intent.putExtra("product", list.get(position));
                intent.putExtra(Constants.Business_Info, businessDataInfo);
                startActivityForResult(intent, Constants.ADD_PRODUCT);
            }
        });
    }

    private void initListView2() {
        adapter2 = new CenterAdapter(new CenterAdapterInf() {
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
        }, resIdBean2);
        mListView2.setAdapter(adapter2);
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("product", productDetailList.get(position));
                intent.putExtra(Constants.Business_Info,businessDataInfo);
                setResult(Constants.ADD_PRODUCT, intent);
                finish();
            }
        });

    }


    private void initNative1() {
        List<ProductClass> prodClassList = DBVideoUtils.findAllProductType(businessDataInfo.getBizType().getTypeId());
        Log.e(TAG, "initNative1: ==="+prodClassList.size());
        if(null!=list&&list.size()>0){
            list.clear();
        }
        if (prodClassList != null && prodClassList.size() > 0) {
            changeToList(prodClassList);
        }
        initAdapter();
        initListView1();
    }

    private void initNative2() {
        List<ProductDetailInfo> productDetailInfoList1 = DBVideoUtils.findProductDetailo(businessDataInfo.getBizType().getTypeId());
        Log.e(TAG, "initNative2: ===="+productDetailInfoList1.size());
        productDetailList = (ArrayList<ProductDetailInfo>) productDetailInfoList1;
        initAdapter();
        initListView2();

    }

    private void initAdapter() {
        resIdBean = new ResIdBean();
        resIdBean.setList(list);
        resIdBean.setLayoutId(R.layout.item_business_type);
        resIdBean.setContext(ProductTypeListActivity.this);

        resIdBean2 = new ResIdBean();
        resIdBean2.setList(productDetailList);
        resIdBean2.setLayoutId(R.layout.item_product_detail);
        resIdBean2.setContext(ProductTypeListActivity.this);
    }


    //获取产品详情list
    private void getProductList() {
        dialog = UiHelper.getInstance().createProgress(ProductTypeListActivity.this);
        HttpTool httpTool = RetrofitUtils.createApi(ProductTypeListActivity.this, HttpTool.class);
        int classId = 0;
        httpTool.getProductList(httpParams.getProductDetailList(appContext, businessDataInfo.getBizType().getTypeId(), classId, 1, 20))
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
                        dialog.dismiss();
                        initNative1();
                        initNative2();
                    }

                    @Override
                    public void onNext(ProductListResult productListResult) {
                        Log.e(TAG, "onNext: " + productListResult.toString());
                        Log.e(TAG, "projosn=="+ GsonUtil.getInstance().toJson(productListResult.getData()));
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        productDetailList = productListResult.getData();
                        if (productDetailList == null || productDetailList.size() == 0) {
                            line.setVisibility(View.GONE);
                        } else {
                            line.setVisibility(View.VISIBLE);
                        }
                        initAdapter();
                        initListView1();
                        initListView2();
                        if (productDetailList != null && list.size() > 0) {
                            DBVideoUtils.saveProductDetail(productDetailList);
                        }
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADD_PRODUCT && data != null) {
            ProductDetailInfo info = (ProductDetailInfo) data.getSerializableExtra("product");
            businessDataInfo= (BusinessDataInfo) data.getSerializableExtra(Constants.Business_Info);
            if (info != null&&businessDataInfo!=null) {
                Intent intent = new Intent();
                intent.putExtra("product", info);
                intent.putExtra(Constants.Business_Info, businessDataInfo);
                setResult(Constants.ADD_PRODUCT, intent);
                finish();
            }

        }
    }

    private void changeToList(List<ProductClass> productClassList) {
        for (ProductClass productClass : productClassList) {
            ProdClass prodClass = new ProdClass();
            prodClass.setClassId(productClass.getClassId());
            prodClass.setTypeId(productClass.getTypeId());
            prodClass.setClassName(productClass.getName());
            list.add(prodClass);
        }
    }

    //产品搜索
    @Click
    void img_setting() {
        Intent intent = new Intent(ProductTypeListActivity.this, ProductSearchActivity_.class);
        if(businessDataInfo!=null){
            intent.putExtra(Constants.Business_Info, businessDataInfo);
        }
        startActivityForResult(intent, Constants.ADD_PRODUCT);
    }
}
