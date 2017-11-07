package com.banger.videorecord.mouble.product.holder;

import android.widget.TextView;

/**
 * Created by Xuchaowen on 2016/7/11.
 */
public class ProductSearchHolder {

    private TextView txtProductName;//产品名称
    private TextView productNum;//产品编号
    private TextView bizType;//业务大类

    public TextView getProductClass() {
        return productClass;
    }

    public void setProductClass(TextView productClass) {
        this.productClass = productClass;
    }

    public TextView getProductNum() {
        return productNum;
    }

    public void setProductNum(TextView productNum) {
        this.productNum = productNum;
    }

    public TextView getBizType() {
        return bizType;
    }

    public void setBizType(TextView bizType) {
        this.bizType = bizType;
    }

    private TextView productClass;//产品分类
    public TextView getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(TextView txtProductName) {
        this.txtProductName = txtProductName;
    }
}
