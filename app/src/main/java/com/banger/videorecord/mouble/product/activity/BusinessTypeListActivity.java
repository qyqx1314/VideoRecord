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
import com.banger.videorecord.bean.result.BizTypeResult;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.bean.ListProducts;
import com.banger.videorecord.mouble.product.bean.ProcessBiz;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.product.bean.ProductClass;
import com.banger.videorecord.mouble.product.bean.VideoMoreData;
import com.banger.videorecord.mouble.product.business.ProductBusinessImp;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.GsonUtil;
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
 * @author zhusiliang
 *         选择产品选取业务分类的页面
 */
@EActivity(R.layout.activity_business_type_list)
public class BusinessTypeListActivity extends AppCompatActivity {
    public static String TAG = "BusinessTypeListActivity";
    @ViewById
    ImageView img_go_back;
    @ViewById
    TextView activity_title;
    @ViewById
    RefreshLayout layout_refresh;
    @ViewById(R.id.listView)
    ListView mListView;
    @ViewById
    ImageView img_setting;
    private CenterAdapter adapter;
    private ResIdBean resIdBean;
    private ArrayList<BusinessDataInfo> list;
    @Bean
    ProductBusinessImp productBusinessImp;
    @Bean(HttpParams.class)
    HttpParams httpParams;
    @App
    AppContext appContext;
    //判断是在线的还是脱机的
    private boolean isNative = false;
    private AlertDialog dialog;

    @AfterViews
    void initViews() {

        img_setting.setVisibility(View.GONE);
        activity_title.setText("选择产品");
        isNative = getIntent().getBooleanExtra("isNative", false);

        list = new ArrayList<>();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(Constants.Business_Info, list.get(position));

                if (!isNative) {
                    if (list.get(position).getProdClass() == null || list.get(position).getProdClass().size() == 0) {
                        intent.setClass(BusinessTypeListActivity.this, ProductDetailListActivity_.class);
                        intent.putExtra("businessType", list.get(position).getBizType());
                    } else {
                        intent.setClass(BusinessTypeListActivity.this, ProductTypeListActivity_.class);
                    }
                } else {//脱机
                    List<ProductClass> prodClassList = DBVideoUtils.findAllProductType(list.get(position).getBizType().getTypeId());
                    if (prodClassList == null || prodClassList.size() == 0) {
                        intent.setClass(BusinessTypeListActivity.this, ProductDetailListActivity_.class);
                        intent.putExtra("businessType", list.get(position).getBizType());
                    } else {
                        intent.setClass(BusinessTypeListActivity.this, ProductTypeListActivity_.class);
                    }
                }
                startActivityForResult(intent, Constants.ADD_PRODUCT);
            }
        });
        if (appContext.getLoginState() == 1) {
//            initNative();
            getDataOffline();

        } else {
            if (!appContext.isNetworkConnected()) {
//                initNative();
                getDataOffline();
                return;
            }
                getBiz();
        }


    }


    private void initNative() {
        layout_refresh.setEnabled(false);
//        getDataOffline();
        List<BizType> bizTypeList = DBVideoUtils.findAllBizType();
        if (bizTypeList != null && bizTypeList.size() > 0) {
            isNative = true;
            dataToList(bizTypeList);
            initAdapter();
            initListView();
        }
    }


    @Click
    void img_go_back() {
        finish();
    }


    //给封装类复制，用于列表展示
    private void initAdapter() {
        resIdBean = new ResIdBean();
        resIdBean.setList(list);
        resIdBean.setLayoutId(R.layout.item_business_type);
        resIdBean.setContext(BusinessTypeListActivity.this);

    }

    //初始化列表的数据以及列表的点击事件
    private void initListView() {
        adapter = new CenterAdapter(new CenterAdapterInf() {
            @Override
            public void bind(View view, ResIdBean resIdBean) {
                productBusinessImp.bindBusinessType(view, resIdBean);
            }

            @Override
            public void setData(ResIdBean resIdBean, int position) {
                productBusinessImp.setDataBusinessType(resIdBean, position);
            }

            @Override
            public void setListener(Object object, Object data) {

            }
        }, resIdBean);
        mListView.setAdapter(adapter);
        layout_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (list != null) {
                    list.clear();
                }
                getBiz();
            }
        });

    }

    //离线获取数据
    void getDataOffline(){

        List<BizType> bizType=DBVideoUtils.findAllBizType();
//        List<ProcessInfo> process=DBVideoUtils.findAllpro();
        layout_refresh.setEnabled(false);
        Log.e(TAG, "getDataOffline: =========="+bizType.size());
        BizTypeResult bizTypeResult1=new BizTypeResult();
        ArrayList<BusinessDataInfo> data=new ArrayList<>();
        if(bizType!=null && bizType.size()>0){
            isNative = true;
            for(int i=0;i<bizType.size();i++){
            BusinessDataInfo bizInfo=new BusinessDataInfo();//中层
                BizType typeInfo=bizType.get(i);
                bizInfo.setBizType(typeInfo);
                String josnPro=typeInfo.getProcessInfo();
                ProcessBiz pb = GsonUtil.getInstance().json2Bean(josnPro, ProcessBiz.class);
                bizInfo.setProcesses(pb.getProcessinfos());
                String josnVideo=typeInfo.getVideoMoreData();
                VideoMoreData vd=GsonUtil.getInstance().json2Bean(josnVideo,VideoMoreData.class);
                bizInfo.setTemplateFields(vd.getVideoMore());
                Log.e(TAG, "getDataOffline: ==========" + bizInfo.getProcesses().size());
                data.add(bizInfo);
                bizTypeResult1.setData(data);
                }

            }

        if(list.size()>0){
            list=null;
        }
        list = data;
        Log.e(TAG, "data============"+list.size());
//        dataToList(bizType);
        initAdapter();
        initListView();
        }



    //获取业务类型列表，数据请求接口以及回调
    void getBiz() {
        dialog = UiHelper.getInstance().createProgress(BusinessTypeListActivity.this);
        dialog.show();
        HttpTool httpTool = RetrofitUtils.createApi(BusinessTypeListActivity.this, HttpTool.class);
        httpTool.getBiz(httpParams.getProductType(appContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BizTypeResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                        layout_refresh.setRefreshing(false);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        layout_refresh.setRefreshing(false);
                        dialog.dismiss();
//                        initNative();
                        getDataOffline();
                    }

                    @Override
                    public void onNext(BizTypeResult bizTypeResult) {
                        layout_refresh.setRefreshing(false);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list = bizTypeResult.getData();
                            initAdapter();
                            initListView();
                        DBVideoUtils.saveBizType(list);
                        dialog.dismiss();
                    }
                });
    }

    //讲查库得到的数据，传递给list，任何初始化列表
    private void dataToList(List<BizType> bizTypes) {
        for (BizType bizType : bizTypes) {
            BusinessDataInfo info = new BusinessDataInfo();
            info.setBizType(bizType);
            list.add(info);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADD_PRODUCT && data != null) {
            ProductDetailInfo info = (ProductDetailInfo) data.getSerializableExtra("product");
            BusinessDataInfo businessDataInfo = (BusinessDataInfo) data.getSerializableExtra(Constants.Business_Info);

            if (info != null&&businessDataInfo!=null) {
                Intent intent = new Intent();
                intent.putExtra("product", info);
                intent.putExtra(Constants.Business_Info,businessDataInfo);
                System.out.println("zzzz+ sss" + businessDataInfo.getProcesses().size());
                setResult(Constants.ADD_PRODUCT, intent);
                finish();
            }

        }
    }

    //产品搜索
    @Click
    void img_setting() {
        Intent intent = new Intent(BusinessTypeListActivity.this, ProductSearchActivity_.class);
        startActivityForResult(intent, Constants.ADD_PRODUCT);
    }
}
