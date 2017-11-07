package com.banger.videorecord.mouble.product.business;

import android.view.View;

import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.product.bean.ProductClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.microcredit.adapter.bean.ResIdBean;

/**
 * Created by zhusiliang on 16/6/18.
 */
public interface ProductBusinessInt {
    void bindBusinessType(View view, ResIdBean resIdBean);
    void setDataBusinessType(ResIdBean resIdBean, int position);

    void bindProductType(View view, ResIdBean resIdBean);
    void setDataProductType(ResIdBean resIdBean, int position);

    void bindProduct(View view, ResIdBean resIdBean);
    void setDataProduct(ResIdBean resIdBean, int position);

    void bindSearchProduct(View view, ResIdBean resIdBean);
    void setDataSearchProduct(ResIdBean resIdBean, int position);
    void updateProductDB(ProductDetailInfo productDetailInfo);
    boolean bizTypeIsChanged(ProductDetailInfo productDetailInfo, BizType bizType);
    boolean ProductClassIsChanged(ProductDetailInfo productDetailInfo, ProductClass productClass);
    boolean ProductDetailIsChanged(ProductDetailInfo productDetailInfo, ProductDetailInfo productDetailDB);
    void updateProductDetail(ProductDetailInfo productDetailInfo);
}
