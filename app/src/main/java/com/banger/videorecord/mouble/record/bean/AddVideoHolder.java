package com.banger.videorecord.mouble.record.bean;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by zhusiliang on 16/9/27.
 */
public class AddVideoHolder {
    TextView txt_round;
    RelativeLayout layout_take_video;
    RelativeLayout rl_show_video;
    RelativeLayout rl_pic_take;
    RelativeLayout rl_img_three;
    ImageView img_video_cut;
    ImageView img_one;
    ImageView img_two;
    ImageView img_three;


    public RelativeLayout getRl_img_three() {
        return rl_img_three;
    }

    public void setRl_img_three(RelativeLayout rl_img_three) {
        this.rl_img_three = rl_img_three;
    }

    public TextView getTxt_round() {
        return txt_round;
    }

    public void setTxt_round(TextView txt_round) {
        this.txt_round = txt_round;
    }

    public RelativeLayout getLayout_take_video() {
        return layout_take_video;
    }

    public void setLayout_take_video(RelativeLayout layout_take_video) {
        this.layout_take_video = layout_take_video;
    }

    public RelativeLayout getRl_show_video() {
        return rl_show_video;
    }

    public void setRl_show_video(RelativeLayout rl_show_video) {
        this.rl_show_video = rl_show_video;
    }

    public RelativeLayout getRl_pic_take() {
        return rl_pic_take;
    }

    public void setRl_pic_take(RelativeLayout rl_pic_take) {
        this.rl_pic_take = rl_pic_take;
    }

    public ImageView getImg_video_cut() {
        return img_video_cut;
    }

    public void setImg_video_cut(ImageView img_video_cut) {
        this.img_video_cut = img_video_cut;
    }

    public ImageView getImg_one() {
        return img_one;
    }

    public void setImg_one(ImageView img_one) {
        this.img_one = img_one;
    }

    public ImageView getImg_two() {
        return img_two;
    }

    public void setImg_two(ImageView img_two) {
        this.img_two = img_two;
    }

    public ImageView getImg_three() {
        return img_three;
    }

    public void setImg_three(ImageView img_three) {
        this.img_three = img_three;
    }
}
