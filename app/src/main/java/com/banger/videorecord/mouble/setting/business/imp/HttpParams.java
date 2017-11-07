package com.banger.videorecord.mouble.setting.business.imp;

import android.util.Log;

import com.banger.videorecord.bean.param.BaseParams;
import com.banger.videorecord.bean.param.DeviceParams;
import com.banger.videorecord.bean.param.LoginParam;
import com.banger.videorecord.bean.param.ProductMessageParam;
import com.banger.videorecord.bean.param.ProductTypeParams;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.GsonUtil;
import com.banger.videorecord.util.SharedPrefsUtil;

import org.androidannotations.annotations.EBean;

/**
 * 得到网络请求参数
 * Created by zhujm on 2016/6/21.
 */
@EBean
public class HttpParams implements com.banger.videorecord.mouble.setting.business.inf.HttpParams{
    private static final String TAG = "HttpParams";
    @Override
    public String loginParams(AppContext appContext, String name, String password) {
        LoginParam params = new LoginParam();
        params.setIp(appContext.getLocalIpAddress());
        params.setImei(appContext.getIMEI());
        params.setCurrentVersionName(appContext.getCurrentVersionName());
        params.setAccount(name);
        params.setPassword(password);
        Log.e(TAG, "loginParams: " + GsonUtil.getInstance().toJson(params));
        System.out.println("zzzz"+GsonUtil.getInstance().toJson(params));
        return  GsonUtil.getInstance().toJson(params);
    }
    @Override
    public String versionUpdateParams(AppContext appContext) {
        LoginParam params = new LoginParam();
        params.setIp(appContext.getLocalIpAddress());
        params.setImei(appContext.getIMEI());
        params.setCurrentVersionName(appContext.getCurrentVersionName());
        Log.e(TAG, "versionUpdateParams: "+GsonUtil.getInstance().toJson(params) );
        return  GsonUtil.getInstance().toJson(params);
    }

    @Override
    public String sendDevice(AppContext appContext) {
        DeviceParams params = new DeviceParams();
        params.setIp(appContext.getLocalIpAddress());
        params.setImei(appContext.getIMEI());
        params.setCurrentVersionName(appContext.getCurrentVersionName());
        params.setBrand(appContext.getVendor());
        params.setSdk(appContext.getSDKVersion());
        return  GsonUtil.getInstance().toJson(params);
    }
    @Override
    public String getUodateData(AppContext appContext) {
        BaseParams params = new BaseParams();
        params.setIp(appContext.getLocalIpAddress());
        params.setImei(appContext.getIMEI());
        params.setCurrentVersionName(appContext.getCurrentVersionName());
        return  GsonUtil.getInstance().toJson(params);
    }
    @Override
    public String getProductType(AppContext appContext) {
        ProductTypeParams params = new ProductTypeParams();
        params.setIp(appContext.getLocalIpAddress());
        params.setImei(appContext.getIMEI());
        params.setCurrentVersionName(appContext.getCurrentVersionName());
        params.setAccount(SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.USER_NAME, "admin"));
        Log.e(TAG, "ip= "+params.getIp()+"imei="+params.getImei()+"cc="+params.getCurrentVersionName()+"a="+params.getAccount());
        return  GsonUtil.getInstance().toJson(params);
    }
    @Override
    public String getProductMessage(AppContext appContext) {
        ProductMessageParam params = new ProductMessageParam();
        params.setIp(appContext.getLocalIpAddress());
        params.setImei(appContext.getIMEI());
        params.setCurrentVersionName(appContext.getCurrentVersionName());
        params.setAccount(SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.USER_NAME,""));
//        params.setBizType();
        return  GsonUtil.getInstance().toJson(params);
    }

    @Override
    public String getProductDetailList(AppContext appContext, int bizTypeId,int classId,int pageNum,int pageSize) {
        ProductMessageParam params=new ProductMessageParam();
        params.setAccount(SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.USER_NAME, "admin"));
        params.setPageNum(pageNum);
        params.setPageSize(pageSize);
        params.setProductName("");
        params.setProductCode("");
        params.setBizType(bizTypeId);
        params.setClassType(classId);
//        params.setUpdateDate("2016-06-13");
//        params.setUpdateDateEnd("2016-06-13");
        return GsonUtil.getInstance().toJson(params);
    }

    @Override

    public String getProductSearchList(AppContext appContext, int bizTypeId,int pageNum, int pageSize, String productName) {
        ProductMessageParam params=new ProductMessageParam();
        params.setAccount(SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.USER_NAME, "admin"));
        params.setPageNum(pageNum);
        params.setPageSize(pageSize);
//        params.setProductCode(productCode);
        params.setProductName(productName);
        params.setBizType(bizTypeId);
        Log.e(TAG, "getProductSearchList: =="+bizTypeId);
//        if(classId!=-1){
            params.setClassType(-1);
//        }
//        params.setUpdateDate("2016-06-13");
//        params.setUpdateDateEnd("2016-06-13");
        return GsonUtil.getInstance().toJson(params);
    }
    @Override
    public String getProductOffSearchList(AppContext appContext,int pageNum, int pageSize, String productName,String productCode,int bizType,int classType,String updateDate,String updateDateEnd) {
        ProductMessageParam params=new ProductMessageParam();
        params.setAccount(SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.USER_NAME, "admin"));
        params.setPageNum(pageNum);
        params.setPageSize(pageSize);
        params.setProductCode(productCode);
        params.setProductName(productName);
        params.setBizType(bizType);
        if(classType==0){
            params.setClassType(-1);
        }else {
            params.setClassType(classType);
        }
        params.setUpdateDate(updateDate);
        params.setUpdateDateEnd(updateDateEnd);
        return GsonUtil.getInstance().toJson(params);
    }

}
