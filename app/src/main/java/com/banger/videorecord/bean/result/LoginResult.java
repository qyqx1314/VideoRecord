package com.banger.videorecord.bean.result;

/**
 * Created by zhujm on 2016/6/14.
 */
public class LoginResult extends BaseResult {
    String userName ="";
    String loginDate="";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    @Override
    public String toString() {
        return "userName:" + userName + "result:" + this.getResult() + "error:" + this.getError()+ "loginDate:" + this.getLoginDate();
    }
}
