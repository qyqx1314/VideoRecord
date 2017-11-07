package com.microcredit.adapter.bean;

import android.content.Context;

import java.util.ArrayList;


/**
 * 资源文件id
 * author：jiumin
 * create on 2016/3/16 11:06
 */
public class ResIdBean {
    //布局文件id
    public int layoutId;
    //Context
    public Context context;
    //Viewholder 适配器资源
    public Object adpterObject;
    //数据
    public ArrayList list;
    //数据
    public Object dataObject;

    public Object getDataObject() {
        return dataObject;
    }

    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Object getAdpterObject() {
        return adpterObject;
    }

    public void setAdpterObject(Object adpterObject) {
        this.adpterObject = adpterObject;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }
}
