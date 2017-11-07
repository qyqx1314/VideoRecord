package com.banger.videorecord.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * $desc$
 * author：zhujm
 * create on 2016/1/12 14:50
 */
public class SharedPrefsUtil {
    public final static String SETTING = "baihang_microcredit_setting";
    private static com.banger.videorecord.util.SharedPrefsUtil sharedPrefsUtil = null;

    protected SharedPrefsUtil() {

    }

    public static synchronized com.banger.videorecord.util.SharedPrefsUtil getInstance() {
        if (sharedPrefsUtil == null) {
            sharedPrefsUtil = new com.banger.videorecord.util.SharedPrefsUtil();
        }
        return sharedPrefsUtil;
    }

    public void putIntValue(Context context, String key, int value) {
        Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.putInt(key, value);
        sp.commit();
    }

    public void putBooleanValue(Context context, String key, boolean value) {
        Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.putBoolean(key, value);
        sp.commit();
    }

    public  void putStringValue(Context context, String key, String value) {
        Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.commit();
    }

    public  int getIntValue(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }

    public boolean getBooleanValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public  String getStringValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
}
