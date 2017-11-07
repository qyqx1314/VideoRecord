package com.banger.videorecord.bean.result;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusiliang on 16/6/18.
 * 产品类型
 */
public class BizTypeResult  extends BaseResult implements Serializable{

    private ArrayList<BusinessDataInfo> data;

    public ArrayList<BusinessDataInfo> getData() {
        return data;
    }

    public void setData(ArrayList<BusinessDataInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BizTypeResult{" +
                "data=" + data +
                '}';
    }
}
