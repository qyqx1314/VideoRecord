package com.banger.videorecord.mouble.product.bean;

import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujm on 2016/7/12.
 */
public class ListProducts {

    List<ProductDetailInfo> product;//产品
    ArrayList<BusinessDataInfo> bizandring;//大类和录像环节

    public ArrayList<BusinessDataInfo> getBizandring() {
        return bizandring;
    }

    public void setBizandring(ArrayList<BusinessDataInfo> bizandring) {
        this.bizandring = bizandring;
    }

    public List<ProductDetailInfo> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDetailInfo> product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ListProducts{" +
                "product=" + product +
                '}';
    }
}
