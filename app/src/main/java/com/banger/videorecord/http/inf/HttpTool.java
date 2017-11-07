package com.banger.videorecord.http.inf;

import com.banger.videorecord.bean.result.BaseResult;
import com.banger.videorecord.bean.result.BizTypeResult;
import com.banger.videorecord.bean.result.LoginResult;
import com.banger.videorecord.bean.result.UpVersionResult;
import com.banger.videorecord.bean.result.Version;
import com.banger.videorecord.mouble.record.bean.CustomerInfoResult;
import com.banger.videorecord.mouble.setting.bean.ProductListResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhujm on 2016/6/14.
 * 接口
 */
public interface HttpTool {
    /**
     * @param params
     * @return 用户登录接口
     */
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=UTF-8")
    @POST("doUserLoginIn.html")
    Observable<LoginResult> userLogin(@Query("param") String params);

    //http://../mobile/compareAPKVersion.html
    @POST("compareAPKVersion.html")
    Observable<Version> updateVersion(@Query("param") String params);

    @POST("doUserLoginIn.html")
    Observable<LoginResult> userLogins(@Query("params") String params, @Query("params") String
            paramss);

    /**
     * @param jsonString 查询参数
     * @return 产品分类查询接口
     */
    @POST("getBizTypeAndProdClass.html")
    Observable<BizTypeResult> getBiz(@Query("param") String jsonString);

    /**
     * @param jsonString
     * @return 查询产品列表详情接口
     */
    @POST("queryProductPageList.html")
    Observable<ProductListResult> getProductList(@Query("param") String jsonString);

    /**
     * @param xmlString
     * @return 返回上传是否成功
     * 上传视频文件的接口
     */
    @POST("uploadVideoRecord.html")
    Observable<BaseResult> uploadVideo(@Query("param") String xmlString);

    /**
     * @param xmlString
     * @return 业务记录上传接口
     */
    @POST("uploadRecordInfo.html")
    Observable<BaseResult> uploadRecordInfo(@Query("param") String xmlString);

    /**
     * @param xmlString
     * @return 脱机记录上传接口
     */
    @POST("uploadVideoRecordInfo.html")
    Observable<BaseResult> uploadVideoRecordInfo(@Query("param") String xmlString);

    /**
     * @param params 参数集合
     * @return 上传当前设备信息
     */
    @POST("uploadDeviceState.html")
    Observable<LoginResult> devicestate(@Query("param") String params);

    /**
     * 获取升级信息
     *
     * @param params 参数
     * @return UpVersionResult
     */
    @POST("compareAPKVersion.html")
    Observable<UpVersionResult> upData(@Query("param") String params);

    /**
     * 文件上传
     *
     * @param file 上传参数
     * @return FileUploadResult
     * uploadVideoRecordInfo.html
     */
    @Multipart
    @POST("uploadVideoRecordInfo.html")
    Observable<BaseResult> upload(@Part("description") RequestBody description,
                                  @Part MultipartBody.Part file);

    @POST("uploadVideoRecordInfo.html")
    Observable<BaseResult> uploadForStr(@Query("param") String params);

    /**
     * 上传一张图片
     *
     * @param description
     * @param imgs
     * @return
     */
    @Multipart
    @POST("uploadVideoImage.html")
    Observable<BaseResult> uploadImage(@Part("fileName") String description,
                                       @Part("file\"; filename=\"image.png\"") RequestBody imgs,
                                       @Part("file\"; filename=\"image.png\"") RequestBody imgs1,
                                       @Part("file\"; filename=\"image.png\"") RequestBody imgs2,
                                       @Part("file\"; filename=\"image.png\"") RequestBody imgs3,
                                       @Part("file\"; filename=\"image.png\"") RequestBody imgs4);
    @Multipart
    @POST("file/uploadVideoImage.html")
    Observable<BaseResult> uploadImageFile(@Part MultipartBody.Part file,
                                            @Part("fileName") RequestBody fileName,@Part
                                                   ("bizNo") RequestBody bizNo);
    @Multipart
    @POST("file/uploadVideoImage.html")
    Observable<BaseResult> uploadImageFile(@Part MultipartBody.Part file);
    @Multipart
    @POST("uploadVideoImage.html")
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part MultipartBody.Part imgs,
                             @Part MultipartBody.Part imgs1);

    /**
     * 简便写法
     *
     * @param description 简介
     * @param imgs1       图片
     * @return
     */
    @Multipart
    @POST("uploadVideoImage.html")
    Call<String> uploadImage(@Part("description") String description, @PartMap
    Map<String, RequestBody> imgs1);


    /**
     * @param jsonString
     * @return 查询产品列表详情接口
     */
    @POST("getCustomerBaseInfo.html")
    Observable<CustomerInfoResult> getCustomerInfo(@Query("param") String jsonString);

}
