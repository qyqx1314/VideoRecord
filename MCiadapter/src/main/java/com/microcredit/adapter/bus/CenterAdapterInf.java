package com.microcredit.adapter.bus;

import android.view.View;

import com.microcredit.adapter.bean.ResIdBean;


/**
 * 业务抽象类
 * author：jiumin
 * create on 2016/3/16 10:55
 */
public interface CenterAdapterInf {

    //绑定资源
    public void bind(View view,ResIdBean resIdBean);
    //填充内容
    public void setData(ResIdBean resIdBean,int position);
    //用于列表展示
    public void setListener(Object object,Object data);
}
