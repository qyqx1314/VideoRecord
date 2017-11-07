package com.banger.videorecord.mouble.record.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.bean.XmlMessageBean;
import com.banger.videorecord.mouble.record.util.FormatUtil;
import com.banger.videorecord.mouble.record.widget.RoundProgressBar;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.ViewHolder;

import java.util.List;

/**
 * Created by zhusiliang on 16/9/29.
 */
public class UpQueueNewAdapter extends BaseAdapter {

    private Context mContext;
    private List<XmlMessageBean> recordBeanList;
    private OnFailClickListener failClickListener;

    public UpQueueNewAdapter(Context mContext, List<XmlMessageBean> xmlMessageBeans, OnFailClickListener failClickListener) {
        this.mContext = mContext;
        this.recordBeanList = xmlMessageBeans;
        this.failClickListener = failClickListener;
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
        ViewHolder holder = ViewHolder.getViewHolder(mContext, R.layout.item_up_queue_new, position, convertView, parent);
        XmlMessageBean xmlMessageBean = recordBeanList.get(position);
        BusinessInfoBean businessInfoBean = xmlMessageBean.getBusinessInfoBean();
        //产品部分的控件
        TextView txt_user_name = holder.getView(R.id.txt_user_name);
        TextView txt_biz_type = holder.getView(R.id.txt_biz_type);
        TextView txt_product_type = holder.getView(R.id.txt_product_type);
        TextView txt_product_name = holder.getView(R.id.txt_product_name);
        TextView txt_create_time = holder.getView(R.id.txt_create_time);

        //环节的布局
        LinearLayout layout_videos = holder.getView(R.id.layout_videos);
        initVideoView(mContext, layout_videos, xmlMessageBean);
        //凭证部分的控件
        TextView txt_pic_count = holder.getView(R.id.txt_pic_count);
        TextView txt_pic_up = holder.getView(R.id.txt_pic_up);
        TextView txt_pic_total = holder.getView(R.id.txt_pic_total);
        ImageView img_reload_image = holder.getView(R.id.img_reload_image);
        RelativeLayout layout_reload_image = holder.getView(R.id.layout_reload_image);
        //适配器各个参数的赋值********************************
        //产品部分的控件赋值
        txt_biz_type.setText(businessInfoBean.getBizType());
        if (businessInfoBean.getProductType() == null || businessInfoBean.getProductType().equals("null")) {
            txt_product_type.setText("");
        } else {
            txt_product_type.setText(businessInfoBean.getProductType());
        }
        txt_product_name.setText(businessInfoBean.getProductName());
        txt_create_time.setText(FormatUtil.formatTime(businessInfoBean.getCreateTime()));
        txt_user_name.setText(businessInfoBean.getUserName());
        //凭证部分的控件的赋值
        txt_pic_count.setText(xmlMessageBean.getImageList().size() + "张");
        txt_pic_total.setText(xmlMessageBean.getImageList().size() + "");

        //上传状态的改变
        //上传凭证 图片多少的显示
        int num = 0;
        int doneNum = 0;
        if (xmlMessageBean.getImageList() != null && xmlMessageBean.getImageList().size() > 0) {
            for (ImageInfoBean imageInfoBean : xmlMessageBean.getImageList()) {
                if (imageInfoBean.getState() == 3) {
                    num++;
                }
                if (imageInfoBean.getState() > 2) {
                    doneNum++;
                }
            }
            if (xmlMessageBean.getImageList().size() == doneNum && doneNum > num) {
                layout_reload_image.setVisibility(View.VISIBLE);
            } else {
                layout_reload_image.setVisibility(View.GONE);
            }
            txt_pic_up.setText("" + num);
        }

        //重新上传的回调添加
        MyOnclickListener myOnclickListener = new MyOnclickListener(xmlMessageBean, 0);
        img_reload_image.setOnClickListener(myOnclickListener);


        return holder.getConvertView();
    }

    /**
     * 状态转成字符串
     */
    private void stateToText(TextView textView, int state) {
        if (state == 1) {
            textView.setText("等待上传");
        } else if (state == 2) {
            textView.setText("正在上传");
        } else if (state == 3) {
            textView.setText("上传成功");
        } else if (state == 4) {
            textView.setText("重新上传");
        }
    }


    //自定义点击事件，将列表对象，回调给接口，在接口中处理逻辑
    class MyOnclickListener implements View.OnClickListener {
        private XmlMessageBean xmlMessageBean;
        private int process;

        public MyOnclickListener(XmlMessageBean xmlMessageBean, int process) {
            this.xmlMessageBean = xmlMessageBean;
            this.process = process;
        }

        @Override
        public void onClick(View v) {
            failClickListener.videoFailCallBack(xmlMessageBean, process);
        }
    }

    /**
     * @param context
     * @param layout
     * @param bean    动态添加视频上传环节的布局，以及相关的点击事件
     */
    private void initVideoView(Context context, LinearLayout layout, XmlMessageBean bean) {
        List<VideoInfoBean> list = bean.getVideoList();
        if (list != null && list.size() > 0) {
            if (layout.getChildCount() > 0) {
                layout.removeAllViews();
            }
            for (VideoInfoBean videoInfoBean : list) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_list_video_info, null);
                TextView txt_round = (TextView) view.findViewById(R.id.txt_round);
                TextView txt_video_name = (TextView) view.findViewById(R.id.txt_video_name);
                TextView txt_video_time = (TextView) view.findViewById(R.id.txt_video_time);
                TextView txt_video_size = (TextView) view.findViewById(R.id.txt_video_size);
                TextView txt_video_upState = (TextView) view.findViewById(R.id.txt_video_upState);
                ImageView img_reload_video = (ImageView) view.findViewById(R.id.img_reload_video);
                RoundProgressBar progressBar_video = (RoundProgressBar) view.findViewById(R.id.progressBar_video);
                //环节名称，等服务器弄好
                txt_round.setText(videoInfoBean.getProcessName());
                txt_video_name.setText(videoInfoBean.getFileName());
                txt_video_time.setText("" + videoInfoBean.getRecTime());
                txt_video_size.setText(FileUtils.changeLongToSize(videoInfoBean.getFileSize()));
                progressBar_video.setProgress(videoInfoBean.getProgressPercent());

                stateToText(txt_video_upState, videoInfoBean.getState());
                if (videoInfoBean.getState() == 4) {
                    progressBar_video.setVisibility(View.GONE);
                    img_reload_video.setVisibility(View.VISIBLE);
                } else {
                    progressBar_video.setVisibility(View.VISIBLE);
                    img_reload_video.setVisibility(View.GONE);
                }
                //重新上传的点击事件
                MyOnclickListener myOnclickListener = new MyOnclickListener(bean, 0);
                img_reload_video.setOnClickListener(myOnclickListener);
                layout.addView(view);
            }
        }
    }

    //适配器传入的回调接口
    public interface OnFailClickListener {
        void videoFailCallBack(XmlMessageBean bean, int process);
    }

}
