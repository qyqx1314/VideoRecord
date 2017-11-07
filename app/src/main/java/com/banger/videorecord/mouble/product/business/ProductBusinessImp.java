package com.banger.videorecord.mouble.product.business;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.product.bean.BizType;
import com.banger.videorecord.mouble.product.bean.ProductClass;
import com.banger.videorecord.mouble.product.holder.BusinessHolder;
import com.banger.videorecord.mouble.product.holder.ProductDetailHolder;
import com.banger.videorecord.mouble.product.holder.ProductSearchHolder;
import com.banger.videorecord.mouble.product.holder.ProductTypeHolder;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.product.bean.ProdClass;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.microcredit.adapter.bean.ResIdBean;

import org.androidannotations.annotations.EBean;

import java.util.List;

/**
 * Created by zhusiliang on 16/6/18.
 */
@EBean
public class ProductBusinessImp implements ProductBusinessInt {
    private static final String TAG = "ProductBusinessImp";

    @Override
    public void bindBusinessType(View view, ResIdBean resIdBean) {
        BusinessHolder holder = new BusinessHolder();
        holder.setTxtName((TextView) view.findViewById(R.id.txt_name));
        resIdBean.setAdpterObject(holder);
    }

    @Override
    public void setDataBusinessType(ResIdBean resIdBean, int position) {
        BusinessHolder holder = (BusinessHolder) resIdBean.getAdpterObject();
        BusinessDataInfo info = (BusinessDataInfo) resIdBean.getList().get(position);
        holder.getTxtName().setText("" + info.getBizType().getTypeName());
    }

    @Override
    public void bindProductType(View view, ResIdBean resIdBean) {
        ProductTypeHolder holder = new ProductTypeHolder();
        holder.setTxtName((TextView) view.findViewById(R.id.txt_name));
        resIdBean.setAdpterObject(holder);

    }

    @Override
    public void setDataProductType(ResIdBean resIdBean, int position) {
        ProductTypeHolder holder = (ProductTypeHolder) resIdBean.getAdpterObject();
        ProdClass info = (ProdClass) resIdBean.getList().get(position);
        holder.getTxtName().setText(info.getClassName());
    }

    @Override
    public void bindProduct(View view, ResIdBean resIdBean) {
        ProductDetailHolder holder = new ProductDetailHolder();
        holder.setTxtProductName((TextView) view.findViewById(R.id.txt_name));
        holder.setTxtProductCode((TextView) view.findViewById(R.id.txt_code));
        holder.setTxtBizType((TextView) view.findViewById(R.id.txt_biz_type));
        holder.setTxtProductType((TextView) view.findViewById(R.id.txt_product_type));
        resIdBean.setAdpterObject(holder);
    }

    @Override
    public void setDataProduct(ResIdBean resIdBean, int position) {
        ProductDetailHolder holder = (ProductDetailHolder) resIdBean.getAdpterObject();
        ProductDetailInfo info = (ProductDetailInfo) resIdBean.getList().get(position);
        holder.getTxtProductName().setText(info.getProductName());
        holder.getTxtProductCode().setText(info.getProductCode());
        holder.getTxtBizType().setText(info.getBizTypeName());
        holder.getTxtProductType().setText(info.getProductClassName());
    }

    @Override
    public void bindSearchProduct(View view, ResIdBean resIdBean) {
        ProductSearchHolder holder = new ProductSearchHolder();
        holder.setTxtProductName((TextView) view.findViewById(R.id.product_name));
        holder.setProductNum((TextView) view.findViewById(R.id.product_num));
        holder.setBizType((TextView) view.findViewById(R.id.manage));
        holder.setProductClass((TextView) view.findViewById(R.id.manage_type));
        resIdBean.setAdpterObject(holder);
    }

    @Override
    public void setDataSearchProduct(ResIdBean resIdBean, int position) {
        ProductSearchHolder holder = (ProductSearchHolder) resIdBean.getAdpterObject();
        ProductDetailInfo info = (ProductDetailInfo) resIdBean.getList().get(position);
        holder.getTxtProductName().setText("" + info.getProductName());
        holder.getProductNum().setText(info.getProductCode());
        holder.getBizType().setText(info.getBizTypeName());
        holder.getProductClass().setText(info.getProductClassName());
    }

    @Override
    public void updateProductDB(ProductDetailInfo productDetailInfo) {
        List<BizType> list = DBVideoUtils.findAllType(productDetailInfo.getBizType());
        //更新大类
        if (null != list && list.size() > 0) {
            if (bizTypeIsChanged(productDetailInfo, list.get(0))) {
                Log.e(TAG, "updateProductDB: 更新大类 ");
                boolean state = DBVideoUtils.updateType(productDetailInfo.getBizType(), productDetailInfo.getBizTypeName(), list.get(0).getId());
                //更新成功
                if (state) {
                    Log.e(TAG, "updateProductDB: 更新成功 ");
                }
            }
        } else {
            //新增大类
            Log.e(TAG, "updateProductDB: 新增大类 ");
            BizType bizType = new BizType();
            bizType.setTypeId(productDetailInfo.getBizType());
            bizType.setTypeName(productDetailInfo.getBizTypeName());
            bizType.save();
        }
        //更新子类
        if (productDetailInfo.getProductClass() != 0) {
            Log.e(TAG, "updateProductDB: 有子类 ");
            List<ProductClass> productClassList = DBVideoUtils.findAllProductClass(productDetailInfo.getBizType(), productDetailInfo.getProductClass());
            if (null != productClassList && productClassList.size() > 0) {
                if (ProductClassIsChanged(productDetailInfo, productClassList.get(0))) {
                    Log.e(TAG, "updateProductDB: 更新子类 ");
                    //更新子类
                    String name = "";
                    if (null!=productDetailInfo.getProductClassName()){
                        name = productDetailInfo.getProductClassName();
                    }
                    boolean stateClass = DBVideoUtils.updateProductClass(productDetailInfo.getBizType(), productDetailInfo.getProductClass(), name, productClassList.get(0).getId());
                    //更新成功
                    if (stateClass) {
                        Log.e(TAG, "updateProductDB: 更新子类成功 ");

                    } else {
                        Log.e(TAG, "updateProductDB: 更新子类失败 ");
                    }
                }
            } else {
                Log.e(TAG, "updateProductDB: 新增子类 ");
                //新增子类
                ProductClass productClass = new ProductClass();
                productClass.setClassId(productDetailInfo.getProductClass());
                productClass.setName(productDetailInfo.getProductClassName());
                productClass.setTypeId(productDetailInfo.getBizType());
                productClass.save();
            }
        }
//        else {
//            Log.e(TAG, "updateProductDB: 更新产品详情 ");
//            //大类里没有子类直接更新产品详情
//            updateProductDetail(productDetailInfo);
//        }
        updateProductDetail(productDetailInfo);
    }

    @Override
    public boolean bizTypeIsChanged(ProductDetailInfo productDetailInfo, BizType bizType) {
        if (productDetailInfo.getBizType() == bizType.getTypeId()
                && productDetailInfo.getBizTypeName().equals(bizType.getTypeName())) {
            Log.e(TAG, "bizTypeIsChanged: 不需要更新大类");
            return false;
        }
        return true;
    }

    @Override
    public boolean ProductClassIsChanged(ProductDetailInfo productDetailInfo, ProductClass productClass) {
        if (productDetailInfo.getBizType() == productClass.getTypeId()
                && productDetailInfo.getProductClass() == productClass.getClassId()) {
            if ( null != productDetailInfo.getProductClassName() && null != productClass.getName()
                    &&productDetailInfo.getProductClassName().equals(productClass.getName())){
                Log.e(TAG, "bizTypeIsChanged: 不需要更新子类");
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public boolean ProductDetailIsChanged(ProductDetailInfo productDetailInfo, ProductDetailInfo productDetailDB) {

        if (productDetailInfo.getBizType() == productDetailDB.getBizType()
                && productDetailInfo.getProductId() == productDetailDB.getProductId()
                && productDetailInfo.getProductClass() == productDetailDB.getProductClass()
                && productDetailInfo.getProductCode().equals(productDetailDB.getProductCode())
                && productDetailInfo.getProductName().equals(productDetailDB.getProductName())
                && productDetailInfo.getBizTypeName().equals(productDetailDB.getBizTypeName())
                && productDetailInfo.getUpdateDate().equals(productDetailDB.getUpdateDate())) {
            if (null != productDetailInfo.getProductClassName() && null != productDetailDB.getProductClassName()
                    && productDetailInfo.getProductClassName().equals(productDetailDB.getProductClassName())) {
                Log.e(TAG, "bizTypeIsChanged: 不需要更新产品详情");
                return false;
            }else if (null == productDetailInfo.getProductClassName() && null == productDetailDB.getProductClassName()){
                return false;
            }else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void updateProductDetail(ProductDetailInfo productDetailInfo) {


        //更新产品详情
        List<ProductDetailInfo> productDetailInfoList = DBVideoUtils.findProductDs(productDetailInfo.getBizType(), productDetailInfo.getProductClass(), productDetailInfo.getProductId());

        if (null != productDetailInfoList && productDetailInfoList.size() > 0) {
            //更新
            if (ProductDetailIsChanged(productDetailInfo, productDetailInfoList.get(0))) {
                Log.e(TAG, "updateProductDB: 更新产品详情 ");
                boolean stateProduct = DBVideoUtils.updateProductDs(productDetailInfo, productDetailInfoList.get(0).getId());
                if (stateProduct) {
                    //更新成功
                    Log.e(TAG, "updateProductDB: 更新产品详情成功 ");
                } else {
                    //更新失败
                    Log.e(TAG, "updateProductDB: 更新产品详情失败 ");
                }
            }
        } else {
            //新增
            Log.e(TAG, "updateProductDB: 新增产品详情 ");
            productDetailInfo.save();
        }
    }

}
