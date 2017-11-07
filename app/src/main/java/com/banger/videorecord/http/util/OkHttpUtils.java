package com.banger.videorecord.http.util;

import android.content.Context;
import android.util.Log;

import com.banger.videorecord.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by zhujm on 2016/5/5.
 * OkHttpClient自定义工具类
 */
public class OkHttpUtils {
    private static OkHttpClient singleton;

    public static OkHttpClient getInstance(Context context) {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    File cacheDir = new File(context.getCacheDir(), HttpConfig.RESPONSE_CACHE);
                    singleton = new OkHttpClient();

                    Builder builder =singleton.newBuilder();
//                    builder.addInterceptor(new InterceptorImp());
//                    builder.addNetworkInterceptor(new InterceptorImp());
//                    builder.authenticator(new AuthenticatorImp());
                    builder.retryOnConnectionFailure(true);
                    builder.connectTimeout(HttpConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                    builder.readTimeout(HttpConfig.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
                    builder.cache(new Cache(cacheDir, HttpConfig.RESPONSE_CACHE_SIZE));
//                    builder.addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request request = chain.request()
//                                    .newBuilder()
//                                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
//                                    .addHeader("Accept-Encoding", "gzip, deflate")
//                                    .addHeader("Connection", "keep-alive")
//                                    .addHeader("Accept", "*/*")
////                                    .addHeader("Cookie", "add cookies here")
//                                    .build();
//                            return chain.proceed(request);
//                        }
//                    });
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor  logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Timber.tag("okhttp").d(message);
                                Log.e("okhttp", "log: " + message);
                            }
                        });
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                        builder.addInterceptor(logging);
//                        singleton.interceptors().add(logging);
                    }
                }
            }
        }

        return singleton;
    }

}
