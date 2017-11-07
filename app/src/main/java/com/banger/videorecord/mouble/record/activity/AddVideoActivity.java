package com.banger.videorecord.mouble.record.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.banger.videorecord.mouble.product.activity.BusinessTypeListActivity_;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.DisplayImgEntity;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageListInfo;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.mouble.record.widget.MySelectItemDialog;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StorageUtils;
import com.banger.videorecord.util.StringUtil;
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
 * Created by Xuchaowen on 2016/6/7.
 * 新增录像界面
 */
@EActivity(R.layout.activity_add_video)
public class AddVideoActivity extends Activity {
    public static String TAG = "AddVideoActivity";
    @ViewById
    TextView activity_title;
    @ViewById
    ImageView img_go_back;
    @ViewById
    TextView txt_save;//保存
    @ViewById
    EditText user_name;//用户名
    @ViewById
    TextView note_call;//证件类型
    @ViewById
    EditText note_id;//证件号
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
    TextView txt_buy_money;//购买金额
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
    //  最后返回的产品信息
    private ProductDetailInfo productDetailInfo;
    //视频的信息集合类，存储大小，时间等信息 环节1和环节2的视频类
    private VideoInfoBean videoFirst;
    private VideoInfoBean videoSecond;
    //  环节1和环节2的图像的集合
    private List<ImageInfoBean> imgList;
    private List<ImageInfoBean> imgList2;
    //选好产品之后，就不能再选产品了
    private boolean isProduct = false;
    //用来存库的视频实体类
    private VideoInfoBean videoInfoBean;
    private BusinessInfoBean businessInfoBean;
    String[] item1 = Constants.ID_TYPE;
    //每个产品的唯一标识符
    private String biz = "";
    //每一笔业务的业务标识
    private String bizNo = "";
    private AlertDialog dialog;
    private int certType = 0;
    private File xmlFile;
    final int RG_REQUEST = 0; //判断回调函数的值


    @AfterViews
    void initViews() {
        activity_title.setText("新增录像");
        biz = "biz" + System.currentTimeMillis();
        bizNo = DateUtil.getNowTime("yyyyMMddHHmmssSSS") + "12345678";
        imgList = new ArrayList<>();
        imgList2 = new ArrayList<>();
        initFile();
        appContext.getOptions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    //选择证件类型
    @Click
    void layout_pro() {
        showDia(item1);
    }

    //    点击返回
    @Click
    void img_go_back() {
        if (productDetailInfo != null) {
            MySelectDialog mySelectDialog = new MySelectDialog(AddVideoActivity.this, "已添加数据，确定不保存记录退出么？", "确定", "取消", new MySelectDialog.OnCustomDialogListener() {
                @Override
                public void button1(Dialog dialog) {
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
    }


    //环节一拍摄视频 或者播放视频
    @Click
    void layout_take_video1() {//播放视频
        if (videoFirst != null) {
            Intent intent = new Intent(AddVideoActivity.this, VideoDisplayActivity.class);
            intent.putExtra("filePath", videoFirst.getFilePath());
            intent.putExtra("process", 1);
            startActivityForResult(intent, Constants.DISPLAY_VIDEO);
        } else {//拍摄时判断可用空间大于50M
            if (StorageUtils.getInstance().getAvailableExternalMemorySize() > 50 * 1024 * 1024) {
                if (TextUtils.isEmpty(isConfirm())) {
                    Intent intent = new Intent(AddVideoActivity.this, CameraVideoActivity.class);
                    intent.putExtra("state", 1);
                    intent.putExtra("round", 1);
                    initVideoInfo(1);
                    intent.putExtra("videoBean", videoInfoBean);
                    intent.putExtra("biz", biz);
                    intent.putExtra("videoName", "video" + System.currentTimeMillis());
                    startActivityForResult(intent, Constants.ADD_VIDEO);
                } else {
                    ToastUtil.showShortToast(AddVideoActivity.this, isConfirm());
                }
            } else {
                ToastUtil.showShortToast(AddVideoActivity.this, "剩余空间不足，请清理后重试");
            }
        }
    }

    //环节二拍摄视频,或者是播放视频
    @Click
    void take_video() {
        if (videoSecond != null) {
            Intent intent = new Intent(AddVideoActivity.this, VideoDisplayActivity.class);
            intent.putExtra("filePath", videoSecond.getFilePath());
            intent.putExtra("process", 2);
            startActivityForResult(intent, Constants.DISPLAY_VIDEO);
        } else {
            if (StorageUtils.getInstance().getAvailableExternalMemorySize() > 50 * 1024 * 1024) {
                if (TextUtils.isEmpty(isConfirm())) {
                    Intent intent = new Intent(AddVideoActivity.this, CameraVideoActivity.class);
                    intent.putExtra("state", 1);
                    intent.putExtra("round", 2);
                    initVideoInfo(2);
                    intent.putExtra("videoBean", videoInfoBean);
                    intent.putExtra("biz", biz);
                    intent.putExtra("videoName", "video" + System.currentTimeMillis());
                    startActivityForResult(intent, Constants.ADD_VIDEO);
                } else {
                    ToastUtil.showShortToast(AddVideoActivity.this, isConfirm());
                }
            } else {
                ToastUtil.showShortToast(AddVideoActivity.this, "剩余空间不足，请清理后重试");
            }
        }
    }


    //环节一拍照凭证
    @Click
    void ring_take() {
        if (TextUtils.isEmpty(isConfirm())) {
            Intent intent = new Intent(AddVideoActivity.this, CameraVideoActivity.class);
            intent.putExtra("state", 2);
            intent.putExtra("round", 1);
            intent.putExtra("biz", biz);
            intent.putExtra("bizNo", bizNo);
            intent.putExtra("imgName", "img" + +System.currentTimeMillis());
            startActivityForResult(intent, Constants.ADD_PHOTO);
        } else {
            ToastUtil.showShortToast(AddVideoActivity.this, isConfirm());
        }
    }

    //环节二拍照凭证
    @Click
    void ring_take2() {
        if (TextUtils.isEmpty(isConfirm())) {
            Intent intent = new Intent(AddVideoActivity.this, CameraVideoActivity.class);
            intent.putExtra("state", 2);
            intent.putExtra("round", 2);
            intent.putExtra("biz", biz);
            intent.putExtra("bizNo", bizNo);
            intent.putExtra("imgName", "img" + System.currentTimeMillis());
            startActivityForResult(intent, Constants.ADD_PHOTO);
        } else {
            ToastUtil.showShortToast(AddVideoActivity.this, isConfirm());
        }
    }

    //选择产品的button
    @Click
    void btn_choose_product() {
        Intent intent = new Intent(AddVideoActivity.this, BusinessTypeListActivity_.class);
        intent.putExtra("isNative", false);
        startActivityForResult(intent, Constants.ADD_PRODUCT);
    }

    //重新选择产品布局
    @Click
    void rl_product() {
        if (isProduct) {
            ToastUtil.showShortToast(AddVideoActivity.this, "产品数据已经生成，不能更改");
            return;
        }
        Intent intent = new Intent(AddVideoActivity.this, BusinessTypeListActivity_.class);
        intent.putExtra("isNative", false);
        startActivityForResult(intent, Constants.ADD_PRODUCT);
    }


    //保存的按钮
    @Click
    void txt_save() {//保存
        if (TextUtils.isEmpty(isConfirm())) {
            if (!StringUtil.isLegel(user_name.getText().toString())) {
                ToastUtil.showShortToast(AddVideoActivity.this, "用户姓名不能包含非法字符，请重新输入");
                return;
            }
            if (!StringUtil.isegal(note_id.getText().toString())) {
                ToastUtil.showShortToast(AddVideoActivity.this, "证件号码只能是数字和字母，请重新输入");
                return;
            }
            if (productDetailInfo == null) {
                ToastUtil.showShortToast(AddVideoActivity.this, "产品不能为空，请先选择产品");
                return;
            }
            if (videoFirst == null && videoSecond == null) {
                ToastUtil.showShortToast(AddVideoActivity.this, "环节1和环节2视频不能同时空，请重新拍摄");
                return;
            }
            if (videoFirst == null) {
                MySelectDialog mySelectDialog = new MySelectDialog(AddVideoActivity.this, "环节1录像为空，确定保存记录吗？", "确定", "取消", new MySelectDialog.OnCustomDialogListener() {
                    @Override
                    public void button1(Dialog dialog) {
                        saveToServer();
                        dialog.dismiss();
                    }
                    @Override
                    public void button2(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
                mySelectDialog.show();
            }

            if (videoSecond == null) {
                MySelectDialog mySelectDialog = new MySelectDialog(AddVideoActivity.this, "环节2录像为空，确定保存记录吗？", "确定", "取消", new MySelectDialog.OnCustomDialogListener() {
                    @Override
                    public void button1(Dialog dialog) {
                        saveToServer();
                        dialog.dismiss();
                    }

                    @Override
                    public void button2(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
                mySelectDialog.show();
            }
            if (videoFirst != null && videoSecond != null) {
                saveToServer();
            }
        } else {
            ToastUtil.showShortToast(AddVideoActivity.this, isConfirm());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    void saveToServer() {
        initBusinessInfo();
        writeToSdCard();
        Intent intent = new Intent();
        intent.setAction(Constants.ADD_BROADCAST);
        AddVideoActivity.this.sendBroadcast(intent);
        finish();
//        save(ss);
    }

    //  保存函数
    void save(String param) {
        dialog = UiHelper.getInstance().createProgress(AddVideoActivity.this);
        dialog.show();
        HttpTool httpTool = RetrofitUtils.createApi(AddVideoActivity.this, HttpTool.class);
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
                    }

                    @Override
                    public void onNext(BaseResult base) {

                        if (base.getResult() == 0) {
                            Intent intent = new Intent();
                            intent.setAction(Constants.ADD_BROADCAST);
                            AddVideoActivity.this.sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.showShortToast(AddVideoActivity.this, base.getError());
                        }
                    }
                });
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
//            startActivity(intent);
            startActivityForResult(intent, RG_REQUEST);
        } else {
            showPhoto(2, 2);
        }

    }

    private void writeToSdCard() {
        if (FileUtils.isSdState()) {
            List<VideoInfoBean> list = new ArrayList<>();
            if (videoFirst != null) {
                list.add(videoFirst);
            }
            if (videoSecond != null) {
                list.add(videoSecond);
            }
            List<ImageInfoBean> imgLists = new ArrayList<>();
            imgLists.addAll(imgList);
            imgLists.addAll(imgList2);
            if (appContext.getLoginState() == 0) {
                String xml = XmlUtils.changeBusinessToXml(businessInfoBean, list, imgLists, appContext);
                FileUtils.writeFile(xmlFile.getAbsolutePath(), xml, true);
            } else {
                String xml = XmlUtils.changeBusinessToXml(businessInfoBean, list, imgLists, appContext);
                FileUtils.writeFile(xmlFile.getAbsolutePath(), xml, true);
            }

        }

    }

    private void initFile() {
        if (appContext.getLoginState() == 0) {
            File file = new File(Constants.ONLINE_XML_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            xmlFile = new File(file, biz + ".xml");
        } else {
            File file = new File(Constants.OFFLINE_XML_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            xmlFile = new File(file, biz + ".xml");
        }
    }

    private String isConfirm() {
        if (TextUtils.isEmpty(user_name.getText().toString())) {
            return "姓名不能为空，请重新输入";
        }
        if (TextUtils.isEmpty(note_call.getText().toString())) {
            return "证件类型不能为空，请选择证件类型";
        }

        if (TextUtils.isEmpty(note_id.getText().toString())) {
            return "证件号码不能为空，请重新输入";
        }
        if (certType == 1 && note_id.getText().toString().length() != 18) {
            return "身份证号码格式不对，请重新输入";
        }
        return "";
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
        videoInfoBean.setBizNo(bizNo);
        videoInfoBean.setVideoProcess(progress);
        videoInfoBean.setState(1);
//        videoInfoBean.setUserName(user_name.getText().toString());
        videoInfoBean.setBizType(productDetailInfo.getBizTypeName());
        videoInfoBean.setProductType(productDetailInfo.getProductClassName());
        videoInfoBean.setProductName(productDetailInfo.getProductName());
        videoInfoBean.setCreateTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
        videoInfoBean.setBusinessNo("123456");
//        videoInfoBean.setCertType(certType);
//        videoInfoBean.setCertNum(note_id.getText().toString());
        videoInfoBean.setProductId(productDetailInfo.getProductId());
//        videoInfoBean.setBuyMoney(Integer.parseInt(txt_buy_money.getText().toString()));
        videoInfoBean.setBuyType(1);
        videoInfoBean.setDueDate("2017-12-12 10:00:00");
    }


    /**
     * 保存业务信息
     */
    private void initBusinessInfo() {
        if (businessInfoBean == null) {
            businessInfoBean = new BusinessInfoBean();
        }
        businessInfoBean.setBizNo(bizNo);
//        businessInfoBean.setUserName(user_name.getText().toString());
        businessInfoBean.setBizType(productDetailInfo.getBizTypeName());
        businessInfoBean.setProductType(productDetailInfo.getProductClassName());
        businessInfoBean.setProductName(productDetailInfo.getProductName());
        businessInfoBean.setCreateTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
//        businessInfoBean.setCertType(certType);
//        businessInfoBean.setCertNum(note_id.getText().toString());
        businessInfoBean.setProductId(productDetailInfo.getProductId());
        if (TextUtils.isEmpty(txt_buy_money.getText().toString())) {
            businessInfoBean.setBuyMoney(0);
        } else {
            businessInfoBean.setBuyMoney(Integer.parseInt(txt_buy_money.getText().toString()));
        }
        businessInfoBean.setBuyType(1);
        businessInfoBean.setDueDate("2017-1-11 10:00:00");
        businessInfoBean.setState(1);
        businessInfoBean.setAccount(SharedPrefsUtil.getInstance().getStringValue(AddVideoActivity.this, Constants.USER_NAME, "admin"));
        businessInfoBean.setNote(note_content.getText().toString());
        businessInfoBean.setXmlFilePath(xmlFile.getAbsolutePath());
        System.out.println("zzzz" + xmlFile.getAbsolutePath());
        businessInfoBean.saveThrows();
    }

    //展示图片
    private void showPhoto(int position, int type) {
        Intent intent = new Intent(AddVideoActivity.this, PictureViewerActivity_.class);
        Bundle bundle = new Bundle();
        DisplayImgEntity entity = new DisplayImgEntity();
//        intent.setAction(Intent.ACTION_VIEW);
        if (type == 1) {
            entity.setImgList(imgList);
            entity.setRingNum(1);
//            File file = new File(imgList.get(position).getFilePath());
//            intent.setDataAndType(Uri.fromFile(file), "image/*");
        } else {
            entity.setImgList(imgList2);
            entity.setRingNum(2);
//            File file = new File(imgList2.get(position).getFilePath());
//            intent.setDataAndType(Uri.fromFile(file), "image/*");
        }
        bundle.putSerializable("entity", entity);
        bundle.putInt("Position", position);
        intent.putExtras(bundle);
//        startActivity(intent);
        startActivityForResult(intent, RG_REQUEST);
    }

    private void showDia(String[] items) {
        MySelectItemDialog dialog = new MySelectItemDialog(AddVideoActivity.this, new MyDialogListener());
        dialog.updataDialog(items);
        dialog.show();
    }

    class MyDialogListener implements MySelectItemDialog.OnCustomDialogListener {
        @Override
        public void returnSelector(int index) {
            note_call.setText(item1[index]);
            certType = index + 1;
        }
    }

    //处理选择产品之后后面的逻辑
    private void initProduct() {
        if (productDetailInfo == null) {
            productDetailInfo = new ProductDetailInfo();
        }
        layout_product.setVisibility(View.VISIBLE);
        btn_choose_product.setVisibility(View.GONE);
        product_type.setText(productDetailInfo.getProductClassName());
        firm_name.setText(productDetailInfo.getBizTypeName());
        product_num.setText(productDetailInfo.getProductCode());
        txt_product_name.setText(productDetailInfo.getProductName());

    }


    //处理返回回来的照片,环节1和环节2处理类似
    private void initImage() {

        if (imgList != null && imgList.size() > 0) {
            try {
                Log.e("qqq", "initImage: " + imgList.size());
//                File file=new File(imgList.get(0).getFilePath());
//                if(file.exists()){
                if (imgList.size() == 1) {
                    img_one.setVisibility(View.VISIBLE);
                    img_two.setVisibility(View.GONE);
                    img_three.setVisibility(View.GONE);
                    rl_img_three.setBackgroundResource(R.color.white);
                    appContext.getImageLoader().displayImage("file://" + imgList.get(0).getFilePath(), img_one);
                }
//                }
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
                        appContext.getImageLoader().displayImage("file://" + imgList.get(0).getFilePath(), img_one);
                        appContext.getImageLoader().displayImage("file://" + imgList.get(1).getFilePath(), img_two);
                        rl_img_three.setBackgroundResource(R.color.ring_bg);
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
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(0).getFilePath(), img_four);
                        appContext.getImageLoader().displayImage("file://" + imgList2.get(1).getFilePath(), img_five);
                        rl_img_six.setBackgroundResource(R.color.ring_bg);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//       选择产品的回调
        if (requestCode == Constants.ADD_PRODUCT && data != null) {
            productDetailInfo = (ProductDetailInfo) data.getSerializableExtra("product");
            if (productDetailInfo != null) {
                initProduct();
            }
        }
//        选择图片或者视频的回调
        if (requestCode == Constants.ADD_PHOTO && data != null) {
            ArrayList<ImageInfoBean> imageInfoBeans = (ArrayList<ImageInfoBean>) data.getSerializableExtra("imgList");
            if (imageInfoBeans != null && imageInfoBeans.size() > 0) {
                isProduct = true;
                if (imageInfoBeans.get(0).getProcess() == 1) {
                    imgList.addAll(imageInfoBeans);
                } else {
                    imgList2.addAll(imageInfoBeans);
                }
                initImage();
            }
        }
        //选择录像之后的回调
        if (requestCode == Constants.ADD_VIDEO && data != null) {
            VideoInfoBean videoInfoBean = (VideoInfoBean) data.getSerializableExtra("videoBean");
            if (videoInfoBean != null) {
                if (videoInfoBean.getVideoProcess() == 1) {
                    videoFirst = videoInfoBean;
                } else {
                    videoSecond = videoInfoBean;
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
            Log.e("qqq", "onActivityResult: " + bean.getImgList().size() + "==" + bean.getRingNum());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (productDetailInfo != null) {
                MySelectDialog mySelectDialog = new MySelectDialog(AddVideoActivity.this, "已添加数据，确定不保存记录退出么？", "确定", "取消", new MySelectDialog.OnCustomDialogListener() {
                    @Override
                    public void button1(Dialog dialog) {
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
