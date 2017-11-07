package com.banger.videorecord.mouble.record.business;

import android.content.Context;
import android.view.View;

/**
 * Created by Xuchaowen on 2016/6/12.
 */
public class ContentClickListener implements View.OnClickListener {

    private ContentCallBack contentCallBack;
    private Context context;
    private Object bean;
    private Object holder;
    public ContentClickListener(Context context,Object bean, Object holder,ContentCallBack contentCallBack){
        this.contentCallBack=contentCallBack;
        this.context=context;
        this.bean=bean;
        this.holder=holder;
    }

    @Override
    public void onClick(View v) {
        contentCallBack.Click(context,bean,holder);
    }
}
