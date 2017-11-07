package com.banger.videorecord.mouble.record.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.AppException;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.VideoEntity;
import com.banger.videorecord.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusiliang on 16/6/22.
 */
public class DisplayImgAdapter extends BaseAdapter {
    private Context context;
    private List<ImageInfoBean> list;
    private int height;
    private AppContext appContext;

    public DisplayImgAdapter(Context context,AppContext appContext,List<ImageInfoBean> list,int height){
        this.context=context;
        this.list=list;
        this.height=height;
        this.appContext= appContext;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=ViewHolder.getViewHolder(context, R.layout.item_img_display,position,convertView,parent);
        View view=holder.getConvertView();
        ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
        layoutParams.height=height;
        layoutParams.width=height;
        view.setLayoutParams(layoutParams);
        ImageView img_display=holder.getView(R.id.img_display);
        try {
//            appContext.getImageLoader().displayImage("file://" + list.get(0).getFilePath(), img_display);
            appContext.getImageLoader().displayImage("file://"+list.get(position).getFilePath(),img_display,appContext.getPreviewOptions());
        } catch (AppException e) {
            e.printStackTrace();
        }
        return holder.getConvertView();
    }
}
