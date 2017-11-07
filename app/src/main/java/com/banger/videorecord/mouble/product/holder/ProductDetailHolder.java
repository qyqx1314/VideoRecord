package com.banger.videorecord.mouble.product.holder;

import android.widget.TextView;

/**
 * Created by zhusiliang on 16/6/21.
 */
public class ProductDetailHolder {

    private TextView txtProductName;//产品名称
    private TextView txtProductCode;//产品编号
    private TextView txtProductType;//产品类型
    private TextView txtBizType;//业务类型

    public TextView getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(TextView txtProductName) {
        this.txtProductName = txtProductName;
    }

    public TextView getTxtProductCode() {
        return txtProductCode;
    }

    public void setTxtProductCode(TextView txtProductCode) {
        this.txtProductCode = txtProductCode;
    }

    public TextView getTxtProductType() {
        return txtProductType;
    }

    public void setTxtProductType(TextView txtProductType) {
        this.txtProductType = txtProductType;
    }

    public TextView getTxtBizType() {
        return txtBizType;
    }

    public void setTxtBizType(TextView txtBizType) {
        this.txtBizType = txtBizType;
    }
}
