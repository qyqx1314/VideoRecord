package com.banger.videorecord.mouble.record.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.record.activity.AddVideoActivity;
import com.banger.videorecord.mouble.record.activity.AddVideoNewActivity;
import com.banger.videorecord.mouble.record.activity.CameraVideoActivity;
import com.banger.videorecord.mouble.record.activity.ImageListDisplayActivity_;
import com.banger.videorecord.mouble.record.activity.PictureViewerActivity_;
import com.banger.videorecord.mouble.record.activity.VideoDisplayActivity;
import com.banger.videorecord.mouble.record.bean.AddVideoBean;
import com.banger.videorecord.mouble.record.bean.AddVideoHolder;
import com.banger.videorecord.mouble.record.bean.ClickBean;
import com.banger.videorecord.mouble.record.bean.DisplayImgEntity;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.MediaInfoBean;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.VideoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by zhusiliang on 16/9/27.
 */
public class AddVideoItem extends LinearLayout {
    Context context;
    View mContentView;
    private AddVideoBean addVideoBean;
    private Activity activity;
    private AppContext appContext;
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public AddVideoItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddVideoItem(Context context, AddVideoBean addVideoBean) {
        super(context, null);
        this.context = context;
        this.appContext = (AppContext)context.getApplicationContext();
        this.addVideoBean = addVideoBean;
        initViews();
    }

    public void initViews() {
        mContentView = LayoutInflater.from(this.context).inflate(R.layout.item_media_info, null);
        TextView txt_round = (TextView) mContentView.findViewById(R.id.txt_round);
        RelativeLayout layout_take_video = (RelativeLayout) mContentView.findViewById(R.id
                .rl_take_video);
        RelativeLayout rl_show_video = (RelativeLayout) mContentView.findViewById(R.id
                .rl_show_video);
//        RelativeLayout rl_take_video = (RelativeLayout) mContentView.findViewById(R.id
//                .rl_take_video);
        RelativeLayout rl_pic_take = (RelativeLayout) mContentView.findViewById(R.id.rl_pic_take);
        ImageView img_video_cut = (ImageView) mContentView.findViewById(R.id.img_video_cut);
        ImageView img_one = (ImageView) mContentView.findViewById(R.id.img_one);
        ImageView img_two = (ImageView) mContentView.findViewById(R.id.img_two);
        ImageView img_three = (ImageView) mContentView.findViewById(R.id.img_three);
        RelativeLayout rl_three = (RelativeLayout) mContentView.findViewById(R.id.rl_img_three);
        AddVideoHolder addVideoHolder = new AddVideoHolder();
        addVideoHolder.setImg_one(img_one);
        addVideoHolder.setImg_two(img_two);
        addVideoHolder.setImg_three(img_three);
        addVideoHolder.setImg_video_cut(img_video_cut);
        addVideoHolder.setTxt_round(txt_round);
        addVideoHolder.setLayout_take_video(layout_take_video);
        addVideoHolder.setRl_show_video(rl_show_video);
        addVideoHolder.setRl_pic_take(rl_pic_take);
        addVideoHolder.setRl_img_three(rl_three);
        addVideoBean.setAddVideoHolder(addVideoHolder);
        setOnclickListener();
        txt_round.setText(addVideoBean.getInfo().getProcessName());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        mContentView.setLayoutParams(params);
        mContentView.setTag(addVideoHolder);
        addView(mContentView);
        showVideo();
        showImage();
    }

    class MyOnclickListener implements OnClickListener {
        private Context context;
        private AddVideoBean bean;
        private int type;
        private int postion;

        public MyOnclickListener(Context context, AddVideoBean bean, int type, int postion) {
            this.context = context;
            this.bean = bean;
            this.type = type;
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case 1:
                    if (bean != null && bean.getVideo() != null &&
                            bean.getVideo() != null && bean.getVideo().getFilePath() != null && !"".equals
                            (bean.getVideo().getFilePath())) {
                        Intent intent = new Intent(context, VideoDisplayActivity.class);
                        intent.putExtra("filePath", bean.getVideo()
                                .getFilePath());
                        intent.putExtra("videoBean", bean.getVideo());
                        getActivity().startActivityForResult(intent, Constants
                                .DISPLAY_VIDEO);
                    } else {
                        Intent intent = new Intent(context, CameraVideoActivity.class);
                        intent.putExtra("state", 1);
                        intent.putExtra("videoBean", bean.getVideo());
                        intent.putExtra("bizNo", bean.getBizNo());
                        getActivity().startActivityForResult(intent, Constants
                                .ADD_VIDEO);
                    }
                    break;
                case 2:
                    if (bean.getImages() != null) {
                        if (postion == 2 && bean.getImages().size() > 3) {
                            Intent intent = new Intent(context, ImageListDisplayActivity_.class);
                            DisplayImgEntity entity = new DisplayImgEntity();
                            entity.setImgList(bean.getImages());
                            entity.setRingNum(1);
                            intent.putExtra("entity", entity);
                            getActivity().startActivityForResult(intent, 0);
                        } else {
                            showPhoto(postion, context, bean
                                    .getImages());
                        }
                    }
                    break;
                case 3:
                    Intent intent = new Intent(context, CameraVideoActivity.class);
                    intent.putExtra("state", 2);
                    intent.putExtra("process", bean.getInfo());
                    intent.putExtra("bizNo", bean.getBizNo());
                    getActivity().startActivityForResult(intent, Constants
                            .ADD_PHOTO);
                    break;
            }
        }
    }

    //展示图片
    private void showPhoto(int position, Context context, List<ImageInfoBean> images) {
        Intent intent = new Intent(context, PictureViewerActivity_.class);
        Bundle bundle = new Bundle();
        DisplayImgEntity entity = new DisplayImgEntity();
        entity.setImgList(images);
        entity.setRingNum(Integer.parseInt(addVideoBean.getInfo().getProcessId()));
        bundle.putSerializable("entity", entity);
        bundle.putInt("Position", position);
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, 0);
    }

    /**
     * 讲传入的数字
     */
    private void setOnclickListener() {
        addVideoBean.getAddVideoHolder().getLayout_take_video().setOnClickListener(new MyOnclickListener(context,
                addVideoBean, 1, 0));
        addVideoBean.getAddVideoHolder().getRl_show_video().setOnClickListener(new MyOnclickListener(context,
                addVideoBean, 1, 0));
        addVideoBean.getAddVideoHolder().getRl_pic_take().setOnClickListener(new MyOnclickListener(context,
                addVideoBean, 3, 0));
        addVideoBean.getAddVideoHolder().getImg_one().setOnClickListener(new MyOnclickListener(context, addVideoBean,
                2, 0));
        addVideoBean.getAddVideoHolder().getImg_two().setOnClickListener(new MyOnclickListener(context, addVideoBean,
                2, 1));
        addVideoBean.getAddVideoHolder().getImg_three().setOnClickListener(new MyOnclickListener(context,
                addVideoBean, 2, 2));

    }

    public void showVideo() {
        if (addVideoBean.getVideo() != null && !TextUtils.isEmpty(addVideoBean.getVideo().getFilePath())) {
            Bitmap bitmap = VideoUtils.getVideoThumb(addVideoBean.getVideo().getFilePath());
            if (bitmap != null) {
                addVideoBean.getAddVideoHolder().getRl_show_video().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getLayout_take_video().setVisibility(View.GONE);
                addVideoBean.getAddVideoHolder().getImg_video_cut().setImageBitmap(bitmap);
            }else {

            addVideoBean.getAddVideoHolder().getRl_show_video().setVisibility(View.GONE);
            addVideoBean.getAddVideoHolder().getLayout_take_video().setVisibility(View.VISIBLE);
            }
        } else {
            addVideoBean.getAddVideoHolder().getRl_show_video().setVisibility(View.GONE);
            addVideoBean.getAddVideoHolder().getLayout_take_video().setVisibility(View.VISIBLE);
        }
    }


    public void showImage() {
        System.out.println("zzzz img size+" + addVideoBean.getImages().size());
        if (addVideoBean.getImages() != null && addVideoBean.getImages().size() > 0) {
            if (addVideoBean.getImages().size() == 1) {
                addVideoBean.getAddVideoHolder().getImg_one().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getImg_two().setVisibility(View.GONE);
                addVideoBean.getAddVideoHolder().getImg_three().setVisibility(View.GONE);
                addVideoBean.getAddVideoHolder().getImg_three().setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(0).getFilePath(), addVideoBean.getAddVideoHolder().getImg_one(),appContext.getMemoryOptions());
            }
            if (addVideoBean.getImages().size() == 2) {
                addVideoBean.getAddVideoHolder().getImg_one().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getImg_two().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getImg_three().setVisibility(View.GONE);
//                rl_img_three.setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(0).getFilePath(), addVideoBean.getAddVideoHolder().getImg_one(),appContext.getMemoryOptions());
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(1).getFilePath(), addVideoBean.getAddVideoHolder().getImg_two(),appContext.getMemoryOptions());
            }

            if (addVideoBean.getImages().size() == 3) {
                addVideoBean.getAddVideoHolder().getImg_one().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getImg_two().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getImg_three().setVisibility(View.VISIBLE);
//                rl_img_three.setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(0).getFilePath(), addVideoBean.getAddVideoHolder().getImg_one(),appContext.getMemoryOptions());
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(1).getFilePath(), addVideoBean.getAddVideoHolder().getImg_two(),appContext.getMemoryOptions());
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(2).getFilePath(), addVideoBean.getAddVideoHolder().getImg_three(),appContext.getMemoryOptions());
            }
            if (addVideoBean.getImages().size() > 3) {
                addVideoBean.getAddVideoHolder().getImg_one().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getImg_two().setVisibility(View.VISIBLE);
                addVideoBean.getAddVideoHolder().getImg_three().setVisibility(View.VISIBLE);
//                rl_img_three.setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(0).getFilePath(), addVideoBean.getAddVideoHolder().getImg_one(),appContext.getMemoryOptions());
                ImageLoader.getInstance().displayImage("file://" + addVideoBean.getImages().get(1).getFilePath(), addVideoBean.getAddVideoHolder().getImg_two(),appContext.getMemoryOptions());
                addVideoBean.getAddVideoHolder().getRl_img_three().setBackgroundResource(R.color.ring_bg);
                addVideoBean.getAddVideoHolder().getImg_three().setImageDrawable(context.getResources().getDrawable(R.drawable.image_more_pressed));
            }
        }
    }

}
