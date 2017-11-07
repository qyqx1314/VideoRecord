package com.banger.videorecord.bean.param;

import com.google.gson.annotations.SerializedName;

/**
 * 登录参数
 * Created by zhujm on 2016/6/14.
 */
public class LoginParam extends BaseParams{
    //帐号
    @SerializedName("account")
    private String account;
    //密码
    @SerializedName("password")
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
