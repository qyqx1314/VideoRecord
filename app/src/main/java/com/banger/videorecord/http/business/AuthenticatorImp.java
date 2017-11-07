package com.banger.videorecord.http.business;

import android.util.Log;


import com.banger.videorecord.http.util.HttpConfig;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by zhujm on 2016/5/5.
 * 如果你需要在遇到诸如 401 Not Authorised 的时候进行刷新 token，可以使用 Authenticator，
 * 这是一个专门设计用于当验证出现错误的时候，进行询问获取处理的拦截器
 */
public class AuthenticatorImp implements Authenticator {
    private static final String TAG = "AuthenticatorImp";
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        refreshToken(response);
        return response.request().newBuilder()
                .addHeader("Authorization", HttpConfig.TOKEN)
                .build();
    }
    void refreshToken(Response response){
        Log.e(TAG, "refreshToken: "+response );
        HttpConfig.TOKEN =response.header("Authorization");
    }
}
