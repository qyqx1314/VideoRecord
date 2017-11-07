package com.banger.videorecord.mouble.product.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.BaseResult;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.AppException;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.record.activity.CameraVideoActivity;
import com.banger.videorecord.mouble.record.activity.ImageListDisplayActivity_;
import com.banger.videorecord.mouble.record.activity.PictureViewerActivity_;
import com.banger.videorecord.mouble.record.activity.VideoDisplayActivity;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.DisplayImgEntity;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageListInfo;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.StorageUtils;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.util.VideoUtils;
import com.banger.videorecord.widget.MySelectDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhusiliang on 16/7/6.
 */
@EActivity(R.layout.activity_edit_video)
public class EditVideoActivity extends Activity {
    public final static String TAG = "EditVideoActivity";
    @ViewById
    TextView activity_title;
    @ViewById
    ImageView img_go_back;
    @ViewById
    TextView txt_save;//保存
    @ViewById
    TextView user_name;//用户名
    @ViewById
    TextView txt_note_call;//证件类型
    @ViewById
    TextView txt_note_id;//证件号
    @ViewById
    TextView txt_product_name;//产品名
    @ViewById
    RelativeLayout rl_product;//产品名下拉
    @ViewById
    TextView product_num;//产品编号
    @ViewById
    TextView firm_name;//业务名
    @ViewById
    TextView product_type;//产品类型
    @ViewById
    EditText txt_buy_money;//购买金额
    @ViewById
    TextView txt_play_video;//环节一的拍摄视频的文字
    @ViewById
    TextView txt_play_video2;//环节一的拍摄视频的文字
    @ViewById
    RelativeLayout rl_money;//金额列表
    @ViewById
    EditText note_content;//备注
    @ViewById
    RelativeLayout layout_take_video1;//环节一播放视频
    @ViewById
    RelativeLayout ring_take;//环节一拍摄凭证
    @ViewById
    RelativeLayout take_video;//环节二拍摄视频
    @ViewById
    RelativeLayout ring_take2;//环节二拍摄凭证
    @ViewById
    Button btn_choose_product;//
    // 天机产品@ViewById
    @ViewById
    LinearLayout layout_product;//天机产品
    //  获取到图片后生成预览的六张照片
    @ViewById
    ImageView img_one;
    @ViewById
    ImageView img_two;
    @ViewById
    ImageView img_three;
    @ViewById
    ImageView img_four;
    @ViewById
    ImageView img_five;
    @ViewById
    ImageView img_six;
    //  生成视频缩略图隐藏的布局
    @ViewById
    RelativeLayout rl_show_video1;
    @ViewById
    RelativeLayout rl_show_video2;

    //切换回来的拍照的布局
    @ViewById
    RelativeLayout rl_take_video1;
    @ViewById
    RelativeLayout rl_take_video2;
    //  生成视频的缩略图
    @ViewById
    ImageView img_video1_cut;
    @ViewById
    ImageView img_video2_cut;
    //  如果照片大于三个之后加的背景
    @ViewById
    RelativeLayout rl_img_three;
    @ViewById
    RelativeLayout rl_img_six;
    @App
    AppContext appContext;
    //视频的信息集合类，存储大小，时间等信息 环节1和环节2的视频类
    private VideoInfoBean videoFirst;
    private VideoInfoBean videoSecond;
    //  环节1和环节2的图像的集合
    private List<ImageInfoBean> imgList;
    private List<ImageInfoBean> imgList2;
    //用来存库的视频实体类
    private BusinessInfoBean businessInfoBean;
    //每个产品的唯一标识符
    private String biz = "";
    //每一笔业务的业务标识
    private String bizNo = "";
    private AlertDialog dialog;
    private LocalRecordBean localRecordBean;
    private VideoInfoBean videoInfoBean;
    final int RG_REQUEST = 0; //判断回调函数的值
    private String s1 = "确认放弃并删除当前业务吗？";
    private String s2 = "当前业务有拍摄的凭证，返回将删除记录丢失凭证，确认删除？";

    @AfterViews
    void initViews() {
        activity_title.setText("编辑录像记录");
        localRecordBean = (LocalRecordBean) getIntent().getSerializableExtra("localRecordBean");
        imgList = new ArrayList<>();
        imgList2 = new ArrayList<>();
        if (localRecordBean != null) {
            initData();
        }
    }

    private void initData() {
        bizNo = localRecordBean.getBizNo();
        biz = localRecordBean.getBiz();
        //拿到数据库的内容，显示产品内容
        List<BusinessInfoBean> businessInfoBeanList = DBVideoUtils.findAllBusinessByBiz(biz);
        if (businessInfoBeanList != null && businessInfoBeanList.size() > 0) {
            businessInfoBean = businessInfoBeanList.get(0);
            product_num.setText(businessInfoBean.getProductId() + "");
            firm_name.setText(businessInfoBean.getBizType());
            product_type.setText(businessInfoBean.getProductType());
            txt_product_name.setText(businessInfoBean.getProductName());
//            user_name.setText(businessInfoBean.getUserName());
//            txt_note_id.setText(businessInfoBean.getCertNum());
//            int certType = businessInfoBean.getCertType();
//            txt_note_call.setText(Constants.ID_TYPE[certType - 1]);
            if (businessInfoBean.getBuyMoney() == -1) {
                txt_buy_money.setText("");
            } else {
                txt_buy_money.setText("" + businessInfoBean.getBuyMoney());
            }

            note_content.setText(businessInfoBean.getNote());
        }
        //显示两个视频
        List<VideoInfoBean> list = DBVideoUtils.findAllByBiz(biz);
        if (list != null && list.size() > 0) {
            for (VideoInfoBean videoInfoBean : list) {
                if (videoInfoBean.getVideoProcess() == 1) {
                    if (videoFirst == null) {
                        videoFirst = new VideoInfoBean();
                    }
                    videoFirst = videoInfoBean;
                    if (!TextUtils.isEmpty(videoFirst.getFilePath())) {
                        if (FileUtils.isExistFile(videoFirst.getFilePath())) {
                            rl_show_video1.setVisibility(View.VISIBLE);
                            Bitmap bitmap = VideoUtils.getVideoThumb(videoFirst.getFilePath());
                            img_video1_cut.setImageBitmap(bitmap);
                        } else {
                            DBVideoUtils.deleteVideo(videoFirst.getId());
                            videoFirst = null;
                        }

                    } else {
                        DBVideoUtils.deleteVideo(videoFirst.getId());
                        videoFirst = null;
                    }
                } else {
                    videoSecond = videoInfoBean;
                    if (!TextUtils.isEmpty(videoSecond.getFilePath())) {
                        if (FileUtils.isExistFile(videoSecond.getFilePath())) {
                            rl_show_video2.setVisibility(View.VISIBLE);
                            Bitmap bitmap = VideoUtils.getVideoThumb(videoSecond.getFilePath());
                            img_video2_cut.setImageBitmap(bitmap);
                        } else {
                            DBVideoUtils.deleteVideo(videoSecond.getId());
                            videoSecond = null;
                        }
                    } else {
                        DBVideoUtils.deleteVideo(videoSecond.getId());
                        videoSecond = null;
                    }
                }
            }

        }
        //显示下面的图片
        imgList = DBVideoUtils.findAllImageByProcess(biz, 1);
        imgList2 = DBVideoUtils.findAllImageByProcess(biz, 2);
        removeNoneImage(imgList);
        removeNoneImage(imgList2);
        //处理多张图片
        initImage();
    }

    //环节一拍摄视频 或者播放视频
    @Click
    void layout_take_video1() {//播放视频
        if (videoFirst != null) {
            Intent intent = new Intent(EditVideoActivity.this, VideoDisplayActivity.class);
            intent.putExtra("filePath", videoFirst.getFilePath());
            intent.putExtra("process", 1);
            startActivityForResult(intent, Constants.DISPLAY_VIDEO);
        } else {//拍摄时判断可用空间大于50M
            if (StorageUtils.getInstance().getAvailableExternalMemorySize() > 50 * 1024 * 1024) {
                Intent intent = new Intent(EditVideoActivity.this, CameraVideoActivity.class);
                intent.putExtra("state", 1);
                intent.putExtra("round", 1);
                initVideoInfo(1);
                intent.putExtra("videoBean", videoInfoBean);
                intent.putExtra("biz", biz);
                intent.putExtra("videoName", "" + businessInfoBean.getProductId() + System.currentTimeMillis());
                startActivityForResult(intent, Constants.ADD_VIDEO);

            } else {
                ToastUtil.showShortToast(EditVideoActivity.this, "剩余空间不足，请清理后重试");
            }
        }
    }

    //环节二拍摄视频,或者是播放视频
    @Click
    void take_video() {
        if (videoSecond != null) {
            Intent intent = new Intent(EditVideoActivity.this, VideoDisplayActivity.class);
            intent.putExtra("filePath", videoSecond.getFilePath());
            intent.putExtra("process", 2);
            startActivityForResult(intent, Constants.DISPLAY_VIDEO);
        } else {
            if (StorageUtils.getInstance().getAvailableExternalMemorySize() > 50 * 1024 * 1024) {
                Intent intent = new Intent(EditVideoActivity.this, CameraVideoActivity.class);
                intent.putExtra("state", 1);
                intent.putExtra("round", 2);
                initVideoInfo(2);
                intent.putExtra("videoBean", videoInfoBean);
                intent.putExtra("biz", biz);
                intent.putExtra("videoName", "" + businessInfoBean.getProductId() + System.currentTimeMillis());
                startActivityForResult(intent, Constants.ADD_VIDEO);
            } else {
                ToastUtil.showShortToast(EditVideoActivity.this, "剩余空间不足，请清理后重试");
            }
        }
    }

    //保存的按钮
    @Click
    void txt_save() {//保存
        saveToServer();
    }


    //    点击返回
    @Click
    void img_go_back() {
        boolean isNoImage = false;
        if ((imgList == null || imgList.size() == 0) && (imgList2 == null || imgList2.size() == 0)) {
            isNoImage = true;
        }
        if (videoFirst == null && videoSecond == null && isNoImage) {
            MySelectDialog mySelectDialog = new MySelectDialog(EditVideoActivity.this, s1, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
                @Override
                public void button1(Dialog dialog) {
                    deleteAllByBiz();
                    dialog.dismiss();
                    finish();
                }

                @Override
                public void button2(Dialog dialog) {
                    dialog.dismiss();
                }
            });
            mySelectDialog.show();
        } else if (videoFirst == null && videoSecond == null && !isNoImage) {
            MySelectDialog mySelectDialog = new MySelectDialog(EditVideoActivity.this, s2, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
                @Override
                public void button1(Dialog dialog) {
                    deleteAllByBiz();
                    dialog.dismiss();
                    finish();
                }

                @Override
                public void button2(Dialog dialog) {
                    dialog.dismiss();
                }
            });
            mySelectDialog.show();
        } else {
            finish();
        }
    }

    //环节一拍照凭证
    @Click
    void ring_take() {
        Intent intent = new Intent(EditVideoActivity.this, CameraVideoActivity.class);
        intent.putExtra("state", 2);
        intent.putExtra("round", 1);
        intent.putExtra("biz", biz);
        intent.putExtra("bizNo", bizNo);
        intent.putExtra("imgName", "" + businessInfoBean.getProductId() + System.currentTimeMillis());
        startActivityForResult(intent, Constants.ADD_PHOTO);
    }

    //环节二拍照凭证
    @Click
    void ring_take2() {
        Intent intent = new Intent(EditVideoActivity.this, CameraVideoActivity.class);
        intent.putExtra("state", 2);
        intent.putExtra("round", 2);
        intent.putExtra("biz", biz);
        intent.putExtra("bizNo", bizNo);
        intent.putExtra("imgName", "" + businessInfoBean.getProductId() + System.currentTimeMillis());
        startActivityForResult(intent, Constants.ADD_PHOTO);
    }


    //  预览第一张图片
    @Click
    void img_one() {
        showPhoto(0, 1);
    }

    //  预览第二张图片
    @Click
    void img_two() {
        showPhoto(1, 1);
    }

    //  预览第三张图片 如果大于三张就跳查看页面 如果是三张就直接显示图片
    @Click
    void img_three() {
        if (imgList.size() > 3) {
            Intent intent = new Intent(this, ImageListDisplayActivity_.class);
            DisplayImgEntity entity = new DisplayImgEntity();
            entity.setImgList(imgList);
            entity.setRingNum(1);
            intent.putExtra("entity", entity);

//            startActivity(intent);
            startActivityForResult(intent, RG_REQUEST);
        } else {
            showPhoto(2, 1);
        }
    }

    //环节2的照片预览一
    @Click
    void img_four() {
        showPhoto(0, 2);
    }

    //环节2的照片预览二
    @Click
    void img_five() {
        showPhoto(1, 2);
    }

    //环节2的照片预览三
    @Click
    void img_six() {
        if (imgList2.size() > 3) {
            Intent intent = new Intent(this, ImageListDisplayActivity_.class);
            DisplayImgEntity entity = new DisplayImgEntity();
            entity.setImgList(imgList2);
            entity.setRingNum(2);
            intent.putExtra("entity", entity);
            startActivityForResult(intent, RG_REQUEST);
        } else {
            showPhoto(2, 2);
        }

    }


    private void removeNoneImage(List<ImageInfoBean> list) {
        if (list != null && list.size() > 0) {
            for (ImageInfoBean bean : list) {
                if (TextUtils.isEmpty(bean.getFilePath())) {
                    DBVideoUtils.deleteImage(bean.getId());
                    list.remove(bean);
                } else {
                    if (!FileUtils.isExistFile(bean.getFilePath())) {
                        DBVideoUtils.deleteImage(bean.getId());
                        list.remove(bean);
                    }
                }
            }
        }
    }

    /**
     * 初始化video，将用户信息和产品存放到videoBean中，最后的存库
     *
     * @param progress 判断是哪个环节
     */
    private void initVideoInfo(int progress) {
        if (videoInfoBean == null) {
            videoInfoBean = new VideoInfoBean();
        }
//        videoInfoBean.setBiz(biz);
        videoInfoBean.setBizNo(bizNo);
        videoInfoBean.setVideoProcess(progress);
        videoInfoBean.setState(1);
//        videoInfoBean.setUserName(user_name.getText().toString());
        videoInfoBean.setBizType(businessInfoBean.getBizType());
        videoInfoBean.setProductType(businessInfoBean.getProductType());
        videoInfoBean.setProductName(businessInfoBean.getProductName());
        videoInfoBean.setCreateTime(DateUtil.getNowTime("yyyyMMdd HH:mm:ss"));
        videoInfoBean.setBusinessNo("123456");
//        videoInfoBean.setCertType(businessInfoBean.getCertType());
//        videoInfoBean.setCertNum(businessInfoBean.getCertNum());
        videoInfoBean.setProductId(businessInfoBean.getProductId());
        videoInfoBean.setBuyType(1);
        videoInfoBean.setDueDate("2017-12-12 10:00:00");
    }


    void saveToServer() {
        if (videoFirst == null && videoSecond == null) {
            ToastUtil.showShortToast(this, "环节一和环节二录像不能同时为空，请录制后保存");
            return;
        }
        int price;
        if (TextUtils.isEmpty(txt_buy_money.getText().toString())) {
            price = -1;
        } else {
            price = Integer.parseInt(txt_buy_money.getText().toString());
        }
        System.out.println("zzzz price" + price);
        businessInfoBean.setNote(note_content.getText().toString());
        businessInfoBean.setBuyMoney(price);
        businessInfoBean.setCreateTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
        DBVideoUtils.updateBizInfo(businessInfoBean.getId(), businessInfoBean);
        Intent intent = new Intent();
        intent.setAction(Constants.ADD_BROADCAST);
        EditVideoActivity.this.sendBroadcast(intent);
        finish();
//        save(ss);
    }

    //  保存函数
    void save(String param) {
        dialog = UiHelper.getInstance().createProgress(EditVideoActivity.this);
        HttpTool httpTool = RetrofitUtils.createApi(EditVideoActivity.this, HttpTool.class);
        httpTool.uploadForStr(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(BaseResult base) {
                        dialog.dismiss();
                        if (base.getResult() == 0) {
                            Intent intent = new Intent();
                            intent.setAction(Constants.ADD_BROADCAST);
                            EditVideoActivity.this.sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.showShortToast(EditVideoActivity.this, base.getError());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //处理添加图片的处理
        if (requestCode == Constants.ADD_PHOTO && data != null) {
            ArrayList<ImageInfoBean> imgBeans = (ArrayList<ImageInfoBean>) data.getSerializableExtra("imgList");
            if (imgBeans != null&&imgBeans.size()>0) {
                if (imgBeans.get(0).getProcess() == 1) {
                    imgList.addAll(imgBeans);
                } else {
                    imgList2.addAll(imgBeans);
                }
                initImage();
            }

        }
        //处理添加视频的处理
        if (requestCode == Constants.ADD_VIDEO && data != null) {
            VideoInfoBean videoInfoBean1 = (VideoInfoBean) data.getSerializableExtra("videoBean");
            if (videoInfoBean1 != null) {
                if (videoInfoBean1.getVideoProcess() == 1) {
                    videoFirst = videoInfoBean1;
                } else {
                    videoSecond = videoInfoBean1;
                }
                initVideo();
            }
        }
        //预览视频之后的回调
        if (requestCode == Constants.DISPLAY_VIDEO && data != null) {
            int process = data.getIntExtra("process", 1);
            if (process == 1) {
                List<VideoInfoBean> list = DBVideoUtils.findAllByBizProcess(biz, process);
                if (list != null && list.size() > 0) {
                    DBVideoUtils.deleteVideo(list.get(0).getId());
                }
                videoFirst = null;

            } else {
                List<VideoInfoBean> list = DBVideoUtils.findAllByBizProcess(biz, process);
                if (list != null && list.size() > 0) {
                    DBVideoUtils.deleteVideo(list.get(0).getId());
                }
                videoSecond = null;
            }
            initVideo();
        }

        if (requestCode == RG_REQUEST && data != null) {
            ImageListInfo bean = (ImageListInfo) data.getSerializableExtra("img");
//            Log.e("qqq", "onActivityResult: " + bean.getImgList().size() + "==" + bean.getRingNum());
            if (null != bean) {
                if (bean.getRingNum() == 1) {
                    imgList = bean.getImgList();
                } else {
                    imgList2 = bean.getImgList();
                }
                initImage();
            }

        }
    }

    //处理视频拍完的后续操作
    private void initVideo() {
        if (videoFirst != null) {
            rl_show_video1.setVisibility(View.VISIBLE);
            Bitmap bitmap = VideoUtils.getVideoThumb(videoFirst.getFilePath());
            if (bitmap != null) {
                img_video1_cut.setImageBitmap(bitmap);
            }
        } else {
            rl_show_video1.setVisibility(View.GONE);
            rl_take_video1.setVisibility(View.VISIBLE);
        }
        if (videoSecond != null) {
            rl_show_video2.setVisibility(View.VISIBLE);
            Bitmap bitmap = VideoUtils.getVideoThumb(videoSecond.getFilePath());
            if (bitmap != null) {
                img_video2_cut.setImageBitmap(bitmap);
            }

        } else {
            rl_show_video2.setVisibility(View.GONE);
            rl_take_video2.setVisibility(View.VISIBLE);
        }
    }

    //处理查询出来的图片,环节1和环节2处理类似
    private void initImage() {
        if (imgList != null && imgList.size() > 0) {
            try {
                if (imgList.size() == 1) {
                    img_one.setVisibility(View.VISIBLE);
                    img_two.setVisibility(View.GONE);
                    img_three.setVisibility(View.GONE);
                    rl_img_three.setBackgroundResource(R.color.white);
                    appContext.getImageLoader().displayImage("file://" + imgList.get(0).getFilePath(), img_one);
                }
                if (imgList.size() >= 2) {
                    if (imgList.size() == 2) {
                        img_one.setVisibility(View.VISIBLE);
                        img_two.setVisibility(View.VISIBLE);
                        img_three.setVisibility(View.GONE);
                        rl_img_three.setBackgroundResource(R.color.white);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(0).getFilePath(), img_one);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(1).getFilePath(), img_two);
                    }
                    if (imgList.size() == 3) {
                        img_one.setVisibility(View.VISIBLE);
                        img_two.setVisibility(View.VISIBLE);
                        img_three.setVisibility(View.VISIBLE);
                        rl_img_three.setBackgroundResource(R.color.white);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(0).getFilePath(), img_one);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(1).getFilePath(), img_two);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(2).getFilePath(), img_three);
                    }
                    if (imgList.size() > 3) {
                        img_one.setVisibility(View.VISIBLE);
                        img_two.setVisibility(View.VISIBLE);
                        img_three.setVisibility(View.VISIBLE);
                        rl_img_three.setBackgroundResource(R.color.ring_bg);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(0).getFilePath(), img_one);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(1).getFilePath(), img_two);
                        img_three.setImageDrawable(getResources().getDrawable(R.drawable.image_more_pressed));
                    }
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
        } else {

            img_one.setVisibility(View.GONE);
            img_two.setVisibility(View.GONE);
            img_three.setVisibility(View.GONE);
            rl_img_three.setBackgroundResource(R.color.white);
        }

        if (imgList2 != null && imgList2.size() > 0) {
            try {
                if (imgList2.size() == 1) {
                    img_four.setVisibility(View.VISIBLE);
                    img_five.setVisibility(View.GONE);
                    img_six.setVisibility(View.GONE);
                    rl_img_six.setBackgroundResource(R.color.white);
                    appContext.getImageLoader().displayImage("file://" + imgList2.get(0).getFilePath(), img_four);
                }
                if (imgList2.size() >= 2) {
                    if (imgList2.size() == 2) {
                        img_four.setVisibility(View.VISIBLE);
                        img_five.setVisibility(View.VISIBLE);
                        img_six.setVisibility(View.GONE);
                        rl_img_six.setBackgroundResource(R.color.white);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(0).getFilePath(), img_four);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(1).getFilePath(), img_five);
                    }
                    if (imgList2.size() == 3) {
                        img_four.setVisibility(View.VISIBLE);
                        img_five.setVisibility(View.VISIBLE);
                        img_six.setVisibility(View.VISIBLE);
                        rl_img_six.setBackgroundResource(R.color.white);
//                        rl_img_six.setBackgroundResource(R.color.ring_bg);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(0).getFilePath(), img_four);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(1).getFilePath(), img_five);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(2).getFilePath(), img_six);
                    }
                    if (imgList2.size() > 3) {
                        img_four.setVisibility(View.VISIBLE);
                        img_five.setVisibility(View.VISIBLE);
                        img_six.setVisibility(View.VISIBLE);
                        rl_img_six.setBackgroundResource(R.color.ring_bg);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(0).getFilePath(), img_four);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(1).getFilePath(), img_five);
                        img_six.setImageDrawable(getResources().getDrawable(R.drawable.image_more_pressed));
                    }
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
        } else {
            img_four.setVisibility(View.GONE);
            img_five.setVisibility(View.GONE);
            img_six.setVisibility(View.GONE);
            rl_img_six.setBackgroundResource(R.color.white);
        }
    }

    //展示图片
    private void showPhoto(int position, int type) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        if (type == 1) {
//            File file = new File(imgList.get(position).getFilePath());
//            intent.setDataAndType(Uri.fromFile(file), "image/*");
//        } else {
//            File file = new File(imgList2.get(position).getFilePath());
//            intent.setDataAndType(Uri.fromFile(file), "image/*");
//
//        }
//        startActivity(intent);
        Intent intent = new Intent(EditVideoActivity.this, PictureViewerActivity_.class);
        Bundle bundle = new Bundle();
        DisplayImgEntity entity = new DisplayImgEntity();
        if (type == 1) {
            entity.setImgList(imgList);
            entity.setRingNum(1);
        } else {
            entity.setImgList(imgList2);
            entity.setRingNum(2);
        }
        bundle.putSerializable("entity", entity);
        bundle.putInt("Position", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, RG_REQUEST);
    }

    //删除数据库的文件
    private void deleteAllByBiz() {
        DBVideoUtils.deleteBusinessInfo(businessInfoBean.getId());
        File xmlFile = new File(businessInfoBean.getXmlFilePath());
        if (xmlFile.exists()) {
            xmlFile.delete();
        }
        if (imgList != null && imgList.size() > 0) {
            for (ImageInfoBean bean : imgList) {
                DBVideoUtils.deleteImage(bean.getId());
                File file = new File(bean.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        if (imgList2 != null && imgList2.size() > 0) {
            for (ImageInfoBean bean : imgList2) {
                DBVideoUtils.deleteImage(bean.getId());
                File file = new File(bean.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    //监听物理返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean isNoImage = false;
            if ((imgList == null || imgList.size() == 0) && (imgList2 == null || imgList2.size() == 0)) {
                isNoImage = true;
            }
            if (videoFirst == null && videoSecond == null && isNoImage) {
                MySelectDialog mySelectDialog = new MySelectDialog(EditVideoActivity.this, s1, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
                    @Override
                    public void button1(Dialog dialog) {
                        deleteAllByBiz();
                        finish();
                        dialog.dismiss();
                    }

                    @Override
                    public void button2(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
                mySelectDialog.show();
            } else if (videoFirst == null && videoSecond == null && !isNoImage) {
                MySelectDialog mySelectDialog = new MySelectDialog(EditVideoActivity.this, s2, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
                    @Override
                    public void button1(Dialog dialog) {
                        deleteAllByBiz();
                        finish();
                        dialog.dismiss();
                    }

                    @Override
                    public void button2(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
                mySelectDialog.show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
