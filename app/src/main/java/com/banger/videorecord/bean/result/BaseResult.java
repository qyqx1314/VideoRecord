package com.banger.videorecord.bean.result;

/**
 * Created by zhusiliang on 16/6/18.
 */
public class BaseResult{
    private int result;
    private String error;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "result:"+getResult()+"error:"+getError();
    }
}
