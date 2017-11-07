package com.banger.videorecord.http.util;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;

import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.GsonUtil;
import com.banger.videorecord.util.SharedPrefsUtil;

import java.io.File;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhujm on 2016/5/5.
 */
public class RetrofitUtils {

    private static Retrofit singleton;

    public static Retrofit getSingleton() {
        return singleton;
    }

    public static <T> T createApi(Context context, Class<T> clazz) {
        if (singleton == null) {
            synchronized (RetrofitUtils.class) {
                if (singleton == null) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(HttpConfig.getInstance().getBaseUrl(context));
                    Log.e("aaa", "createApi: " + HttpConfig.getInstance().getBaseUrl(context));
                    builder .client(OkHttpUtils.getInstance(context));
                    //增加返回值为Oservable<T>的支持
                    builder .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
                    //增加返回值为String的支持
//                    builder.addConverterFactory(ScalarsConverterFactory.create());
                    //增加返回值为Gson的支持(以实体类返回)
                    builder.addConverterFactory(GsonConverterFactory.create(GsonUtil.newInstance()));
                    singleton = builder.build();
                }else {
                    Log.e("RetrofitUtils", "createApi: " );
                }
            }
        }else if (SharedPrefsUtil.getInstance().getBooleanValue(context, Constants.RESETURI,false)){
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(HttpConfig.getInstance().getBaseUrl(context));
            Log.e("aaa", "createApi: " + HttpConfig.getInstance().getBaseUrl(context));
            builder .client(OkHttpUtils.getInstance(context));
            //增加返回值为Oservable<T>的支持
            builder .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            //增加返回值为String的支持
//                    builder.addConverterFactory(ScalarsConverterFactory.create());
            //增加返回值为Gson的支持(以实体类返回)
            builder.addConverterFactory(GsonConverterFactory.create(GsonUtil.newInstance()));
            singleton = builder.build();
        }
        return singleton.create(clazz);
    }
}
