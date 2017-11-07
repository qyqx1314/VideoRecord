package com.banger.videorecord.mouble.setting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.BizTypeResult;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.product.bean.ProductClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.widget.MySelectItemDialog;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.setting.bean.OffDataListBean;
import com.banger.videorecord.mouble.setting.bean.ProductListResult;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.MyDatePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xuchaowen on 2016/7/13.
 * 离线数据搜索
 */
@EActivity(R.layout.activity_offdata_search)
public class OffDataSearchActivity extends Activity {
    @ViewById
    TextView note_call;//业务类型
    @ViewById
    TextView product_down;//产品分类
    @ViewById
    EditText product_name;//产品名
    @ViewById
    EditText product_num;//产品编号
    @ViewById
    TextView start_time;//开始时间
    @ViewById
    TextView end_time;//截止时间
    @ViewById
    TextView activity_title;
    @ViewById
    TextView txt_save;//取消
    int type=-1;//时间选择
    int bizNum=-1;//选择选项
    int selectType=-1;
    String chooseTime;
    List<BusinessDataInfo> buzList;
    ArrayList<ProdClass> list=new ArrayList<>();
    List<ProductDetailInfo> businessList = new ArrayList<>();
    private ArrayList<ProductDetailInfo> productDetailList;
    OffDataListBean offBean=new OffDataListBean();;
    String[] item1;
    String[] item2;
//    private AlertDialog dialog;
    @Bean(HttpParams.class)
    HttpParams httpParams;
    @App
    AppContext appContext;
    @AfterViews
    void initViews(){
        activity_title.setText("搜索");
        txt_save.setText("取消");
//        dialog= UiHelper.getInstance().createProgress(OffDataSearchActivity.this);
        businessList = DBVideoUtils.findAllProduct();
        Log.e("sss", "initViews: " + businessList.size());
    }
    @Click
    void img_go_back(){
        finish();
    }
    @Click
    void txt_save(){
        finish();
    }

    @Click
    void layout_pro(){//业务分类
        selectType=1;
        product_down.setText("");
        getBiz();
    }
    @Click
    void layout_type(){//产品名称
        if(note_call.getText().toString().length()>0){
        list=buzList.get(bizNum).getProdClass();
        showTypeDia();
//        getProductList(buzList.get(bizNum).getBizType().getTypeId());
        }else{
            ToastUtil.showShortToast(OffDataSearchActivity.this,"请先选择业务分类");
        }
    }

    /**
     * 产品分类
     */
    private void showTypeDia(){

        if(null!=list&&list.size()>0){
            selectType=2;
            item2=new String[list.size()];
            for(int i=0;i<list.size();i++) {
                if(list.get(i).getClassName().length()!=0){
                    item2[i] = list.get(i).getClassName();
                }else{
                    ToastUtil.showShortToast(OffDataSearchActivity.this,"没有子分类");
                }
            }
            showDia(item2);
        }else{
            ToastUtil.showShortToast(OffDataSearchActivity.this,"没有子分类");
        }

    }

    @Click
    void ll_start(){//开始
        type=1;
        showDateDia();
    }

    @Click
    void end_time1(){//截止
        type=2;
        showDateDia();
    }


    private void showDia(String[] items) {
        MySelectItemDialog dialog = new MySelectItemDialog(OffDataSearchActivity.this, new MyDialogListener());
        dialog.updataDialog(items);
        dialog.show();
    }

    class MyDialogListener implements MySelectItemDialog.OnCustomDialogListener {
        @Override
        public void returnSelector(int index) {
            if(selectType==1){
                bizNum=index;
                note_call.setText(item1[index]);
                offBean.setBizType(buzList.get(index).getBizType().getTypeId());
            }else if(selectType==2){
                product_down.setText(item2[index]);
//                offBean.setClassType(productDetailList.get(index).getProductClass());
                offBean.setClassType(list.get(index).getClassId());
            }
        }
    }
    private void showDateDia(){
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        long time = System.currentTimeMillis();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(OffDataSearchActivity.this, new MyDateChangeListener(), year, month, day);
        myDatePickerDialog.show();
    }

    //日期选择框
    class MyDateChangeListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String month=Integer.toString(monthOfYear+1);
            if(month.length()==1){
                chooseTime = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
            }else{
                chooseTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            }

            if(type==1){
                start_time.setText(chooseTime);
            }else if(type==2){
                end_time.setText(chooseTime);
            }
        }
    }

    @Click
    void btn_search(){//搜索
        String biz=note_call.getText().toString().trim();
        String productClass=product_down.getText().toString().trim();
        String product=product_name.getText().toString().trim();
        String productNum=product_num.getText().toString().trim();
        String start=start_time.getText().toString().trim();
        String end=end_time.getText().toString().trim();
        if(biz.length()==0&&productClass.length()==0&&product.length()==0&&productNum.length()==0&&start.length()==0&&end.length()==0){
            ToastUtil.showShortToast(OffDataSearchActivity.this,"搜索条件不能为空");
        }else{

            if(start.length()!=0 && end.length()!=0 && start.compareTo(end)>0){
                ToastUtil.showShortToast(OffDataSearchActivity.this,"截止日期不能大于开始日期");
            }else{

                offBean.setProductName(product);
                offBean.setProductCode(productNum);
                offBean.setStartTime(start);
                offBean.setEndTime(end);
                Intent intent = new Intent();
                intent.putExtra("search", offBean);
                intent.setAction(Constants.OFF_SEARCH);
                this.sendBroadcast(intent);
                finish();
            }

        }
    }

    //获取业务类型列表，数据请求接口以及回调
    void getBiz() {
//        dialog.show();
        HttpTool httpTool = RetrofitUtils.createApi(OffDataSearchActivity.this, HttpTool.class);
        httpTool.getBiz(httpParams.getProductType(appContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BizTypeResult>() {
                    @Override
                    public void onCompleted() {
//                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(BizTypeResult bizTypeResult) {
                        if (buzList == null) {
                            buzList = new ArrayList<>();
                        }
                        buzList = bizTypeResult.getData();


                        if(null!=buzList&&buzList.size()>0){
                            item1=new String[buzList.size()];
                            for(int i=0;i<buzList.size();i++) {
                                if(buzList.get(i).getBizType().getTypeName().length()!=0){
                                    item1[i] = buzList.get(i).getBizType().getTypeName();
                                }else{
                                    ToastUtil.showShortToast(OffDataSearchActivity.this,"无当前业务分类");
                                }
                                Log.e("sss", "buzList: ====" + buzList.size() + "==" + item1[i]);
                            }
                            showDia(item1);
                        }else{
                            ToastUtil.showShortToast(OffDataSearchActivity.this,"当前无业务分类");
                        }

                    }
                });
    }

    //获取产品详情list
    private void getProductList(int typeId) {
//        dialog.show();
        HttpTool httpTool = RetrofitUtils.createApi(OffDataSearchActivity.this, HttpTool.class);
        int classId=-1;
        Log.e("qqq", "typeId======: "+typeId);
        httpTool.getProductList(httpParams.getProductDetailList(appContext, typeId, classId, 1, 100))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductListResult>() {
                    @Override
                    public void onCompleted() {
//                       dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(ProductListResult productListResult) {
                        Log.e("qqq", "onNext: " + productListResult.toString());
                        if (productDetailList == null) {
                            productDetailList = new ArrayList<>();
                        }
                        productDetailList = productListResult.getData();
                        Log.e("qqq", "onNext+=======:productDetailList "+productDetailList.size());
//                        dialog.dismiss();

//        ProductClassList=DBVideoUtils.findAllProductType(buzList.get(bizNum).getTypeId());
                            if(null!=productDetailList&&productDetailList.size()>0){
                                selectType=2;
                                item2=new String[productDetailList.size()];
                                for(int i=0;i<productDetailList.size();i++) {
                                    if(productDetailList.get(i).getProductClassName().length()!=0){
                                        item2[i] = productDetailList.get(i).getProductClassName();
                                    }else{
                                        ToastUtil.showShortToast(OffDataSearchActivity.this,"没有子分类");
                                    }
                                }
                                showDia(item2);
                            }else{
                                ToastUtil.showShortToast(OffDataSearchActivity.this,"没有子分类");
                            }
                    }
                });
    }
}
