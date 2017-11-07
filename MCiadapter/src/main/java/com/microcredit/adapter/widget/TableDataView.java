package com.microcredit.adapter.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;

/**
 * @author zhujm
 * @deprecated 用于展示表格类数据
 */
public class TableDataView extends LinearLayout {

    private ResIdBean resIdBean = null;
    private CenterAdapterInf centerAdapterInf;



    public TableDataView(Context context, ResIdBean resIdBean, CenterAdapterInf centerAdapterInf) {
        super(context);
        // TODO Auto-generated constructor stub
        this.resIdBean = resIdBean;
        this.centerAdapterInf = centerAdapterInf;
        init(context);
    }
    public void init(Context context) {
        View mContentView = LayoutInflater.from(context).inflate(
                resIdBean.getLayoutId(), null);
        //绑定资源
        centerAdapterInf.bind(mContentView, resIdBean);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        addView(mContentView, lp);
        if (null != resIdBean.adpterObject) {
            centerAdapterInf.setData(resIdBean,0);
            centerAdapterInf.setListener(resIdBean,null);
        }
    }
}
