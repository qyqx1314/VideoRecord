package com.banger.videorecord.mouble.setting.bean;
import com.banger.videorecord.bean.result.BaseResult;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhusiliang on 16/6/18.
 */
public class ProductListResult extends BaseResult implements Serializable{
    private ArrayList<ProductDetailInfo> data;
    private PageBean page;

    public ArrayList<ProductDetailInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ProductDetailInfo> data) {
        this.data = data;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "ProductListResult{" +
                "data=" + data +
                ", page=" + page +
                '}';
    }
}
