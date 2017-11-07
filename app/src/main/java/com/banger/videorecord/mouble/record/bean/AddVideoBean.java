package com.banger.videorecord.mouble.record.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.banger.videorecord.R;
import com.banger.videorecord.util.VideoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhujm on 2016/9/28.
 */
public class AddVideoBean {
    private AddVideoHolder addVideoHolder;
    private VideoInfoBean video;
    private List<ImageInfoBean> images;
    private String  bizNo;
    private ProcessInfo info;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AddVideoHolder getAddVideoHolder() {
        return addVideoHolder;
    }

    public void setAddVideoHolder(AddVideoHolder addVideoHolder) {
        this.addVideoHolder = addVideoHolder;
    }

    public ProcessInfo getInfo() {
        return info;
    }

    public void setInfo(ProcessInfo info) {
        this.info = info;
    }

    public VideoInfoBean getVideo() {
        return video;
    }

    public void setVideo(VideoInfoBean video) {
        this.video = video;
    }

    public List<ImageInfoBean> getImages() {
        return images;
    }

    public void setImages(List<ImageInfoBean> images) {
        this.images = images;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public void showVideo(){
        if(video!=null){
            addVideoHolder.getRl_show_video().setVisibility(View.VISIBLE);
            addVideoHolder.getLayout_take_video().setVisibility(View.GONE);
            System.out.println("zzzz file+" + video.getFilePath());
            Bitmap bitmap = VideoUtils.getVideoThumb(video.getFilePath());
            if (bitmap != null) {
                addVideoHolder.getImg_video_cut().setImageBitmap(bitmap);
            }
        }else {
            addVideoHolder.getRl_show_video().setVisibility(View.GONE);
            addVideoHolder.getLayout_take_video().setVisibility(View.VISIBLE);
        }


    }
    public void showImage(){
        if(images!=null&&images.size()>0){
            if(images.size()==1){
                addVideoHolder.getImg_one().setVisibility(View.VISIBLE);
                addVideoHolder.getImg_two().setVisibility(View.GONE);
                addVideoHolder.getImg_three().setVisibility(View.GONE);
                addVideoHolder.getImg_three().setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + images.get(0).getFilePath(), addVideoHolder.getImg_one());
            }
            if(images.size()==2){
                addVideoHolder.getImg_one().setVisibility(View.VISIBLE);
                addVideoHolder.getImg_two().setVisibility(View.VISIBLE);
                addVideoHolder.getImg_three().setVisibility(View.GONE);
//                rl_img_three.setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + images.get(0).getFilePath(), addVideoHolder.getImg_one());
                ImageLoader.getInstance().displayImage("file://" + images.get(1).getFilePath(), addVideoHolder.getImg_two());
            }

            if(images.size()==3){
                addVideoHolder.getImg_one().setVisibility(View.VISIBLE);
                addVideoHolder.getImg_two().setVisibility(View.VISIBLE);
                addVideoHolder.getImg_three().setVisibility(View.VISIBLE);
//                rl_img_three.setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + images.get(0).getFilePath(),addVideoHolder.getImg_one());
                ImageLoader.getInstance().displayImage("file://" + images.get(1).getFilePath(), addVideoHolder.getImg_two());
                ImageLoader.getInstance().displayImage("file://" + images.get(2).getFilePath(), addVideoHolder.getImg_three());
            }
            if(images.size()>3){
                addVideoHolder.getImg_one().setVisibility(View.VISIBLE);
                addVideoHolder.getImg_two().setVisibility(View.VISIBLE);
                addVideoHolder.getImg_three().setVisibility(View.VISIBLE);
                System.out.println("zzzz img+"+images.get(0).getFilePath());
                System.out.println("zzzz img+"+images.get(1).getFilePath());
//                rl_img_three.setBackgroundResource(R.color.white);
                ImageLoader.getInstance().displayImage("file://" + images.get(0).getFilePath(),addVideoHolder.getImg_one());
                ImageLoader.getInstance().displayImage("file://" + images.get(1).getFilePath(), addVideoHolder.getImg_two());
                addVideoHolder.getRl_img_three().setBackgroundResource(R.color.ring_bg);
                addVideoHolder.getImg_three().setImageDrawable(context.getResources().getDrawable(R.drawable.image_more_pressed));
            }
        }
    }
}
