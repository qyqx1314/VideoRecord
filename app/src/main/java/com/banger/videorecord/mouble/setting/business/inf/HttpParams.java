package com.banger.videorecord.mouble.setting.business.inf;
import com.banger.videorecord.helper.AppContext;

/**
 * 接口参数封装
 * Created by zhujm on 2016/6/21.
 */

public interface HttpParams {
     String loginParams(AppContext appContext,String name, String password);
     String sendDevice(AppContext appContext);
     String getUodateData(AppContext appContext);
     String getProductType(AppContext appContext);
     String getProductMessage(AppContext appContext);
     String getProductDetailList(AppContext appContext,int bizTypeId,int classId,int pageNum,int pageSize);
     String getProductSearchList(AppContext appContext,int bizTypeId,int pageNum,int pageSize,String productName);
     String versionUpdateParams(AppContext appContext);
     String getProductOffSearchList(AppContext appContext,int pageNum, int pageSize, String productName,String productCode,int bizType,int classType,String updateDate,String updateDateEnd);
}
