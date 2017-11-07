package com.banger.videorecord.http.util;

import android.content.Context;

import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.SharedPrefsUtil;

/**
 * Created by zhujm on 2016/5/5.
 */

public class HttpConfig {

    private static HttpConfig httpConfig = null;
    SharedPrefsUtil sp = SharedPrefsUtil.getInstance();

    protected HttpConfig() {
    }

    public static synchronized HttpConfig getInstance() {
        if (httpConfig == null) {
            httpConfig = new HttpConfig();
        }
        return httpConfig;
    }

    public String getBaseIp(Context context) {
        String ip = sp.getStringValue(context, Constants.LOGIN_IP, "192.168.1.75");
        return "http://" + ip;
    }

    public String getBaseIpNoHttp(Context context) {
        String ip = sp.getStringValue(context, Constants.LOGIN_IP, "192.168.1.75");
        return ip;
    }

    public String getTcpUpVideo(Context context) {
        String port = sp.getStringValue(context, Constants.LOGIN_PORT, "80");
        String node = sp.getStringValue(context, Constants.LOGIN_NODE, "");
        if (node.length() != 0) {
            return getBaseIp(context) + ":" + port + "/" + node + "/";
        }
        return getBaseIp(context) + ":" + port+"/";
    }

    public String getTcpUrlRoot(Context context) {

        return getTcpUpVideo(context) + "mobile/";
    }

    public String getBaseUrl(Context context) {

        return getTcpUrlRoot(context);
    }
    public static final String RESPONSE_CACHE = "/haha";
    public static final long RESPONSE_CACHE_SIZE = 200;
    public static final int HTTP_CONNECT_TIMEOUT = 20;
    public static final int HTTP_READ_TIMEOUT = 20;
    public static String TOKEN = "";
}
