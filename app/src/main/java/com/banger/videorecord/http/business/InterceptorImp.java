package com.banger.videorecord.http.business;

import android.util.Log;

import com.banger.videorecord.http.util.HttpConfig;
import com.banger.zeromq.util.StringUtil;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhujm on 2016/5/5.
 * 那个 if 判断意思是，如果你的 token 是空的，就是还没有请求到 token，
 * 比如对于登陆请求，是没有 token 的，只有等到登陆之后才有 token，这时候就不进行附着上 token。
 * 另外，如果你的请求中已经带有验证 header 了，比如你手动设置了一个另外的 token，
 * 那么也不需要再附着这一个 token.
 * header 的 key 通常是 Authorization，如果你的不是这个，可以修改
 */
public class InterceptorImp implements Interceptor {
    private static final String TAG = "InterceptorImp";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (HttpConfig.TOKEN == null ||"".equals(HttpConfig.TOKEN) || alreadyHasAuthorizationHeader(originalRequest)) {
            Log.e(TAG, "intercept: "+originalRequest );
            return chain.proceed(originalRequest);
        }
        Request authorised = originalRequest.newBuilder()
                .header("Authorization", HttpConfig.TOKEN)
                .build();
        return chain.proceed(authorised);
    }
    boolean alreadyHasAuthorizationHeader(Request originalRequest){
        Log.e(TAG, "alreadyHasAuthorizationHeader: "+originalRequest );
        if (com.banger.videorecord.util.StringUtil.isEmpty(originalRequest.header("Authorization"))){
            return true;
        }
        return false;
    }
}
