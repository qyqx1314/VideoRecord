package com.banger.videorecord.mouble.record.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordHolder;
import com.banger.videorecord.mouble.record.util.FormatUtil;
import com.banger.videorecord.mouble.record.widget.RoundProgressBar;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/6/18.
 */
public class VideoRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<LocalRecordBean> recordBeanList;
    public  List<LocalRecordBean> isSelect=new ArrayList<>();
    private LocalRecordBean bean;
    private int progress = 0;

    public VideoRecordAdapter(Context mContext,List<LocalRecordBean> recordBeanList){
        this.mContext=mContext;
        this.recordBeanList=recordBeanList;
    }

    @Override
    public int getCount() {
        return recordBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LocalRecordHolder holder = null;
        if (null == convertView) {
            holder=new LocalRecordHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_local_record, null);
            holder.setUserName((TextView) convertView.findViewById(R.id.tv_name));
            holder.setTime((TextView) convertView.findViewById(R.id.manage_time));
            holder.setIsSlected((CheckBox) convertView.findViewById(R.id.check_box));
            holder.setVideoType((TextView) convertView.findViewById(R.id.manage));
            holder.setManageType((TextView) convertView.findViewById(R.id.manage_type));
            holder.setManageName((TextView) convertView.findViewById(R.id.manage_name));
            convertView.setTag(holder);
        }else{
            holder = (LocalRecordHolder)convertView.getTag();
        }
        bean= (LocalRecordBean) getItem(position);
        holder.getUserName().setText(bean.getUserName());
        holder.getTime().setText(FormatUtil.formatTime(bean.getTime()));
        holder.getVideoType().setText(bean.getVideoType());
        holder.getManageType().setText(bean.getManageType());
        holder.getManageName().setText(bean.getManageName());


        if(bean.isVisible()){//是否显示checkbox
            holder.getIsSlected().setVisibility(View.VISIBLE);
//            holder.getRl_my().setVisibility(View.GONE);
            final LocalRecordHolder finalHolder = holder;
            holder.getIsSlected().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalHolder.getIsSlected().isChecked()){
                        finalHolder.getIsSlected().setChecked(true);
                        bean= (LocalRecordBean) getItem(position);
                        bean.setIsSlected(true);
                        isSelect.add(bean);
                    }else{
                        finalHolder.getIsSlected().setChecked(false);
                        bean= (LocalRecordBean) getItem(position);
                        bean.setIsSlected(false);
                        isSelect.remove(bean);
                    }
                }
            });

        }else{
            holder.getIsSlected().setVisibility(View.GONE);
        }

        if(bean.isSlected()){//是否选中
            holder.getIsSlected().setChecked(true);
        }else{
            holder.getIsSlected().setChecked(false);
        }


        return convertView;
    }

}
