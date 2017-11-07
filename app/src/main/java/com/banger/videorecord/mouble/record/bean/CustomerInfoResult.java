package com.banger.videorecord.mouble.record.bean;

import com.banger.videorecord.bean.result.BaseResult;

import java.io.Serializable;

/**
 * Created by zhusiliang on 16/9/27.
 */
public class CustomerInfoResult extends BaseResult implements Serializable {
    CustomerInfoBean data;

    public CustomerInfoBean getData() {
        return data;
    }

    public void setData(CustomerInfoBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CustomerInfoResult{" +
                "data=" + data +
                '}';
    }
}
