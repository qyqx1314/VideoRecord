package com.microcredit.adapter.widget;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.microcredit.adapter.bean.ResIdBean;
import com.microcredit.adapter.bus.CenterAdapterInf;

/**
 * $desc$
 * author：jiumin
 * create on 2016/3/16 10:43
 */
public class CenterAdapter extends BaseAdapter {
    private static final String TAG = "CenterAdapter";
    private ResIdBean resIdBean;
    private CenterAdapterInf centerAdapterInf;

    public CenterAdapter(CenterAdapterInf centerAdapterInf, ResIdBean resIdBean) {
        super();
        this.resIdBean = resIdBean;
        this.centerAdapterInf = centerAdapterInf;
    }

    @Override
    public int getCount() {
        return resIdBean.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return resIdBean.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (null == convertView) {
                convertView = LayoutInflater.from(resIdBean.context).inflate(resIdBean.layoutId, null);
                //绑定资源
                centerAdapterInf.bind(convertView,resIdBean);
                if (null != convertView) {
                    convertView.setTag(resIdBean.adpterObject);
                }
            } else {
                resIdBean.adpterObject = convertView.getTag();
            }
            if (null != resIdBean.adpterObject) {
                centerAdapterInf.setData(resIdBean,position);
                centerAdapterInf.setListener(resIdBean.adpterObject,getItem(position));
            }
            return convertView;
        } catch (Exception e) {
            Log.e(TAG, "getView: e is" + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
