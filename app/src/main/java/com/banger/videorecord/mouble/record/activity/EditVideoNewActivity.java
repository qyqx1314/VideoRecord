package com.banger.videorecord.mouble.record.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.record.bean.AddVideoBean;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.CustomerInfoBean;
import com.banger.videorecord.mouble.record.bean.FiledInfo;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;
import com.banger.videorecord.mouble.record.bean.ProductExtraInfo;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.mouble.record.widget.AddCustomerItem;
import com.banger.videorecord.mouble.record.widget.AddProductItem;
import com.banger.videorecord.mouble.record.widget.AddVideoItem;
import com.banger.videorecord.mouble.record.widget.MySelectItemDialog;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.GsonUtil;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.MySelectDialog;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_edit_video_new)
public class EditVideoNewActivity extends AppCompatActivity {
    public static String TAG = "AddVideoNewActivity";
    @ViewById
    TextView activity_title;
    @ViewById
    ImageView img_go_back;
    @ViewById
    TextView txt_save;//保存
    @ViewById
    TextView txt_product_name;//产品名称
    @ViewById
    LinearLayout layout_add_info;//用户自定义字段的布局
    @ViewById
    Button btn_choose_product;//新增产品的button
    @ViewById
    RelativeLayout rl_product;//产品选择的对话框
    @ViewById
    TextView txt_product_num;//产品编号
    @ViewById
    TextView txt_firm_name;//产品分类
    @ViewById
    TextView txt_product_type;//产品类型
    @ViewById
    LinearLayout layout_add_media;//用户自定义字段的布局
    @ViewById
    LinearLayout layout_hide;//用户自定义字段的布局
    @ViewById
    LinearLayout layout_product_hide;//用户自定义字段的布局
    @ViewById
    ImageView img_pull_down;
    @ViewById
    LinearLayout layout_product;//产品的布局
    @ViewById
    EditText txt_buy_money;//购买金额
    @ViewById
    EditText txt_note_content;//购买金额
    //必填字段  user_name
    @ViewById
    EditText txt_user_name;//用户姓名
    @ViewById
    TextView txt_video_round;//环节的额外字段
    @ViewById
    RelativeLayout layout_id_type;//证件类型的外部layout
    @ViewById
    TextView txt_id_type;//证件类型
    @ViewById
    EditText txt_id_code;//证件号码
    @ViewById
    EditText txt_user_mobile;//用户电话
    @ViewById
    LinearLayout layout_video_extra;
    @Bean(HttpParams.class)
    HttpParams httpParams;
    @App
    AppContext appContext;
    private String bizNo;//唯一标示号
    private File xmlFile;//存储的xml文件地址
    //  最后返回的产品信息
    private List<AddVideoBean> addVideoBeanList;
    private VideoInfoBean videoInfoBean;
    private BusinessInfoBean businessInfoBean;
    private int certType = 0;
    private LocalRecordBean localRecordBean;
    //用户信息的额外信息
    private CustomerInfoBean data;
    private List<FiledInfo> userExtraInfoList;
    private List<ProductExtraInfo> productExtraInfoList;
    private List<FiledInfo> mediaExtraInfoList;
    //环节字段
    private List<ProcessInfo> processList;
    final int RG_REQUEST = 0; //判断回调函数的值
    private String s1 = "确认放弃并删除当前业务吗？";
    private String s2 = "当前业务有拍摄的凭证，返回将删除记录丢失凭证，确认删除？";

    @AfterViews
    void initView() {
        activity_title.setText("编辑录像记录");
        localRecordBean = (LocalRecordBean) getIntent().getSerializableExtra("localRecordBean");
        Log.e(TAG, "initView: ===" + localRecordBean.getMediaInfo() + "==1111" + localRecordBean.getMediaExtraInfo());
        if (localRecordBean != null) {
            initData();
        }
    }

    //选择证件类型
    @Click
    void layout_id_type() {
        MySelectItemDialog selectItemDialog = new MySelectItemDialog(EditVideoNewActivity.this, new MySelectItemDialog.OnCustomDialogListener() {
            @Override
            public void returnSelector(int index) {
                txt_id_type.setText(Constants.ID_TYPE[index]);
                certType = index + 1;
            }
        });
        selectItemDialog.updataDialog(Constants.ID_TYPE);
        selectItemDialog.show();
    }

    //保存按钮
    @Click
    void txt_save() {
        String result = judgeInfo();
        if (TextUtils.isEmpty(result)) {
            saveBusinessInfo();
            writeToSdCard();
            sendBro();
            finish();
        } else {
            ToastUtil.showShortToast(EditVideoNewActivity.this, result);
        }
    }

    @Click
    void img_go_back() {
        if (!isHaveVideo() && !isHaveImage()) {
            MySelectDialog mySelectDialog = new MySelectDialog(EditVideoNewActivity.this, s1, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
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
        } else if (!isHaveVideo() && isHaveImage()) {
            MySelectDialog mySelectDialog = new MySelectDialog(EditVideoNewActivity.this, s2, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
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

    @Click
    void img_pull_down() {
        img_pull_down.setSelected(!img_pull_down.isSelected());
        if (img_pull_down.isSelected()) {
            layout_product_hide.setVisibility(View.GONE);
        } else {
            layout_product_hide.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void rl_product(){

    }

    private void sendBro() {
        Intent intent = new Intent();
        intent.setAction(Constants.ADD_BROADCAST);
        EditVideoNewActivity.this.sendBroadcast(intent);
    }

    private void initData() {
        bizNo = localRecordBean.getBizNo();
        initFile();
        List<BusinessInfoBean> busList = DBVideoUtils.findAllBusinessByBiz(bizNo);
        if (busList != null && busList.size() > 0) {
            businessInfoBean = busList.get(0);
            userExtraInfoList = new ArrayList<>();
            productExtraInfoList = new ArrayList<>();
            mediaExtraInfoList = new ArrayList<>();
            processList = new ArrayList<>();
            initUserInfo();
            initProductInfo();
            initMediaInfo();

        }
    }

    /**
     * 初始化存入的xml文件
     */
    private void initFile() {
        if (appContext.getLoginState() == 0) {
            File file = new File(Constants.ONLINE_XML_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            xmlFile = new File(file, bizNo + ".xml");

        } else {
            File file = new File(Constants.OFFLINE_XML_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            xmlFile = new File(file, bizNo + ".xml");
        }
    }


    //初始化产品和额外字段
    private void initUserInfo() {
        txt_user_name.setText(businessInfoBean.getUserName());
        txt_id_type.setText(Constants.ID_TYPE[businessInfoBean.getCertType()]);
        txt_id_code.setText(businessInfoBean.getCertNumber());
        txt_user_mobile.setText(businessInfoBean.getMobile());
        txt_note_content.setText(businessInfoBean.getNote());
        if (businessInfoBean.getBuyMoney() == -1) {
            txt_buy_money.setText("");
        } else {
            txt_buy_money.setText("" + businessInfoBean.getBuyMoney());
        }
        if (!TextUtils.isEmpty(businessInfoBean.getUserInfo())) {
            data = GsonUtil.getInstance().json2Bean(businessInfoBean.getUserInfo(), CustomerInfoBean.class);
            if (data != null) {
                userExtraInfoList = data.getTemplateFields();
                if (userExtraInfoList != null && userExtraInfoList.size() > 0) {
                    addExtraInfoToView(layout_add_info, userExtraInfoList);
                }
            }
        }
    }

    //初始化产品信息
    private void initProductInfo() {
        txt_firm_name.setText(businessInfoBean.getBizType());
        txt_product_type.setText(businessInfoBean.getProductType());
        txt_product_num.setText(businessInfoBean.getProductId() + "");
        txt_product_name.setText(businessInfoBean.getProductName());
        txt_note_content.setText(businessInfoBean.getNote());
        Type type = new TypeToken<List<ProductExtraInfo>>() {
        }.getType();
        if (!TextUtils.isEmpty(businessInfoBean.getProductInfo())) {
            productExtraInfoList = GsonUtil.getInstance().json2List(businessInfoBean.getProductInfo(), type);
            if (productExtraInfoList != null && productExtraInfoList.size() > 0) {
                addExtraInfoToView2(layout_product_hide, productExtraInfoList);
            }
        }

    }

    //初始化录像信息
    private void initMediaInfo() {
        //初始化额外信息
        Type type = new TypeToken<List<FiledInfo>>() {
        }.getType();
        if (!TextUtils.isEmpty(businessInfoBean.getMediaExtraInfo())) {
            mediaExtraInfoList = GsonUtil.getInstance().json2List(businessInfoBean.getMediaExtraInfo(), type);
            Log.e(TAG, "mediaExtraInfoList.size()==: " + mediaExtraInfoList.size());
            if (mediaExtraInfoList != null && mediaExtraInfoList.size() > 0) {
                addExtraInfoToView(layout_video_extra, mediaExtraInfoList);
            } else {
                txt_video_round.setVisibility(View.GONE);
            }
        }
        //初始化环节信息
        if (!TextUtils.isEmpty(businessInfoBean.getMediaInfo())) {
            type = new TypeToken<List<ProcessInfo>>() {
            }.getType();
            processList = GsonUtil.getInstance().json2List(businessInfoBean.getMediaInfo(), type);
            Log.e(TAG, "processList.size()==== " + processList.size());
            if (processList != null && processList.size() > 0) {
                addMediaView(processList);
            }
        }
    }


    //添加产品额外字段的封装方法
    private void addExtraInfoToView(LinearLayout layout, List<FiledInfo> list) {
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
        for (FiledInfo info : list) {
            AddCustomerItem item = new AddCustomerItem(EditVideoNewActivity.this, info);
            layout.addView(item);
        }
    }

    //添加产品额外字段的封装方法
    private void addExtraInfoToView2(LinearLayout layout, List<ProductExtraInfo> list) {
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
        for (ProductExtraInfo info : list) {
            AddProductItem item = new AddProductItem(EditVideoNewActivity.this, info);
            layout.addView(item);
        }
        layout.setVisibility(View.GONE);
        img_pull_down.setSelected(true);
    }


    private void addMediaView(List<ProcessInfo> list) {
        //环节额外字段布局的添加
        addVideoBeanList = new ArrayList<>(list.size());
        if (layout_add_media.getChildCount() > 0) {
            layout_add_media.removeAllViews();
        }
        if (list != null && list.size() > 0) {
            for (ProcessInfo info : list) {
                AddVideoBean bean = new AddVideoBean();
                //初始化视频信息
                List<VideoInfoBean> videoList = DBVideoUtils.findVideoByProcess(bizNo, info.getProcessId());
                videoList=isVideoExist(videoList);
                if (videoList == null || videoList.size() == 0) {
                    initVideoInfo(info);
                    bean.setVideo(videoInfoBean);
                } else {
                        bean.setVideo(videoList.get(0));
                }
                //初始化图像信息
                List<ImageInfoBean> imgList = DBVideoUtils.findImageByProcess(bizNo, info.getProcessId());
//                List<ImageInfoBean> imgListFor =  new ArrayList<>();
                if (imgList == null || imgList.size() == 0) {
                    imgList = new ArrayList<>();
                }else {
                    imgList = isEx(imgList);
                }
                bean.setImages(imgList);

                bean.setInfo(info);
                bean.setBizNo(bizNo);
                bean.setContext(EditVideoNewActivity.this);
                AddVideoItem addVideoItem = new AddVideoItem(EditVideoNewActivity.this, bean);
                addVideoItem.setActivity(EditVideoNewActivity.this);
                layout_add_media.addView(addVideoItem);
                addVideoBeanList.add(bean);
            }
        }
    }

    private List<ImageInfoBean> isEx(List<ImageInfoBean> imgList){
        for (int i=0;i<imgList.size();i++){
            ImageInfoBean bean = imgList.get(i);
            if (!FileUtils.isExists(bean.getFilePath())){
                DBVideoUtils.deleteImage(bean.getId());
                imgList.remove(i);
                isEx(imgList);
                break;
            }
        }
        return imgList;
    }

    private List<VideoInfoBean> isVideoExist(List<VideoInfoBean> videoList){
        for (int i=0;i<videoList.size();i++){
            VideoInfoBean bean = videoList.get(i);
            if (!FileUtils.isExists(bean.getFilePath())){
                DBVideoUtils.deleteVideo(bean.getId());
                videoList.remove(i);
                isVideoExist(videoList);
                break;
            }
        }
        return videoList;
    }

    /**
     * 初始化传入到拍照页面的视频信息（产品信息以及环节信息）
     *
     * @param info
     */
    private void initVideoInfo(ProcessInfo info) {
        videoInfoBean = new VideoInfoBean();
        videoInfoBean.setBizNo(bizNo);
        videoInfoBean.setProcessName(info.getProcessName());
        videoInfoBean.setProcessId(info.getProcessId());
        videoInfoBean.setState(1);
        videoInfoBean.setBizType(businessInfoBean.getBizType() + "");
        videoInfoBean.setProductType(businessInfoBean.getProductType());
        videoInfoBean.setProductName(businessInfoBean.getProductName());
        videoInfoBean.setCreateTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
        videoInfoBean.setBusinessNo("123456");
        videoInfoBean.setProductId(businessInfoBean.getProductId());
        videoInfoBean.setBuyType(1);
        videoInfoBean.setDueDate("2017-12-12 10:00:00");
    }

    /**
     * 判断相关的必填项是否都有值
     *
     * @return
     */
    private String judgeInfo() {
        String result = "";
        if (TextUtils.isEmpty(txt_user_name.getText().toString())) {
            return "用户姓名不能为空，请重新输入";
        }
        if (TextUtils.isEmpty(txt_user_name.getText().toString())) {
            return "证件类型不能为空，请选择证件类型";
        }
        if (TextUtils.isEmpty(txt_id_code.getText().toString())) {
            return "证件号码不能为空，请重新输入";
        }
        if (TextUtils.isEmpty(txt_user_mobile.getText().toString())) {
            return "用户手机不能为空，请重新输入";
        } else {
            if (txt_user_mobile.getText().toString().length() != 11) {
                return "用户手机格式不对，请重新输入";
            }
        }
        //判断用户信息的额外字段是否有数值
        if (userExtraInfoList != null && userExtraInfoList.size() > 0) {
            for (FiledInfo info : userExtraInfoList) {
                if (TextUtils.isEmpty(info.getFieldColName()) && info.getIsRequire() == 1) {
                    result = info.getFieldCNName() + "不能为空,请重新输入";
                    break;
                }
            }
        }
        //判断视频额外字段的必填项是否有数值
        if (mediaExtraInfoList != null && mediaExtraInfoList.size() > 0) {
            for (FiledInfo info : mediaExtraInfoList) {
                if (TextUtils.isEmpty(info.getFieldColName()) && info.getIsRequire() == 1) {
                    result = info.getFieldCNName() + "不能为空,请重新输入";
                    break;
                }
            }
        }
        //判断是否有视频录入
        boolean isHaveVideo = false;
        if (addVideoBeanList != null && addVideoBeanList.size() > 0) {
            for (AddVideoBean bean : addVideoBeanList) {
                try {
                    if (bean.getVideo() != null && !TextUtils.isEmpty(bean.getVideo().getFilePath())) {
                        isHaveVideo = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (!isHaveVideo) {
            return "视频不能为空，请录入";
        }
        return result;
    }

    /**
     * @return 判断是否有视频
     */
    private boolean isHaveVideo() {
        boolean result = false;
        List<VideoInfoBean> videoList = DBVideoUtils.findAllByBiz(bizNo);
        if (videoList != null && videoList.size() > 0) {
            result = true;
        }
        return result;
    }

    /**
     * @return 判断是否有图片
     */
    private boolean isHaveImage() {
        boolean result = false;
        List<ImageInfoBean> imageList = DBVideoUtils.findAllByBizImage(bizNo);
        if (imageList != null && imageList.size() > 0) {
            result = true;
        }
        return result;
    }


    /**
     * 保存业务信息
     */
    private void saveBusinessInfo() {
        if (businessInfoBean == null) {
            businessInfoBean = new BusinessInfoBean();
        }
        businessInfoBean.setBizNo(bizNo);
        if (TextUtils.isEmpty(txt_buy_money.getText().toString())) {
            businessInfoBean.setBuyMoney(-1);
        } else {
            businessInfoBean.setBuyMoney(Integer.parseInt(txt_buy_money.getText().toString()));
        }
        businessInfoBean.setBuyType(1);
        businessInfoBean.setDueDate("2017-1-11 10:00:00");
        businessInfoBean.setState(1);
        businessInfoBean.setAccount(SharedPrefsUtil.getInstance().getStringValue(EditVideoNewActivity.this, Constants.USER_NAME, "admin"));
        businessInfoBean.setNote(txt_note_content.getText().toString());
        businessInfoBean.setXmlFilePath(xmlFile.getAbsolutePath());
        //新增的用户自定义字段
        if (data == null) {
            data = new CustomerInfoBean();
        } else {
            data.setTemplateFields(userExtraInfoList);
        }
        businessInfoBean.setUserInfo(GsonUtil.getInstance().toJson(data));
        //新增的产品自定义字段
        businessInfoBean.setProductInfo(GsonUtil.getInstance().toJson(productExtraInfoList));
        //新增的用户环节自定义字段
//        businessInfoBean.setMediaInfo(GsonUtil.getInstance().toJson(bizInfo.getProcesses()));
        //新增的录像自定义字段
        businessInfoBean.setMediaExtraInfo(GsonUtil.getInstance().toJson(mediaExtraInfoList));

        //之前删掉的 现在又要的字段
        businessInfoBean.setCertNumber(txt_id_code.getText().toString());
        businessInfoBean.setCertType(certType);
        businessInfoBean.setUserName(txt_user_name.getText().toString());
        businessInfoBean.setMobile(txt_user_mobile.getText().toString());
//        businessInfoBean.saveThrows();
        DBVideoUtils.updateBizInfo(businessInfoBean.getId(), businessInfoBean);
//        Log.e(TAG, "saveBusinessInfo: =="+businessInfoBean.getId() );
    }

    //将视频信息和业务信息存到xml中
    private void writeToSdCard() {
        if (FileUtils.isSdState()) {
            List<VideoInfoBean> list = new ArrayList<>();
            List<ImageInfoBean> imgLists = new ArrayList<>();
            for (AddVideoBean addVideoBean : addVideoBeanList) {
                if (addVideoBean.getVideo() != null && !TextUtils.isEmpty(addVideoBean.getVideo().getFilePath())) {
                    list.add(addVideoBean.getVideo());
                }
                if (addVideoBean.getImages() != null) {
                    imgLists.addAll(addVideoBean.getImages());
                }
            }
            //将相关信息覆盖写到xml中
            String xml = XmlUtils.changeBusinessToXml(businessInfoBean, list, imgLists, appContext);
            FileUtils.writeFile(xmlFile.getAbsolutePath(), xml, true);
        }

    }


    //删除数据库的文件
    private void deleteAllByBiz() {
        DBVideoUtils.deleteBusinessInfo(businessInfoBean.getId());
        File xmlFile = new File(businessInfoBean.getXmlFilePath());
        if (xmlFile.exists()) {
            xmlFile.delete();
        }

        List<ImageInfoBean> imageList = DBVideoUtils.findAllByBizImage(bizNo);
        if (imageList != null && imageList.size() > 0) {
            for (ImageInfoBean bean : imageList) {
                DBVideoUtils.deleteImage(bean.getId());
                File file = new File(bean.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //选择录像之后的回调
        if (requestCode == Constants.ADD_VIDEO && data != null) {
            VideoInfoBean bean = (VideoInfoBean) data.getSerializableExtra("videoBean");
            if (bean != null) {
                //加载环节布局
                if (processList != null && processList.size() >= 0) {
                    addMediaView(processList);
                }
            }
        }
        //选择拍照之后的回调
        if (requestCode == Constants.ADD_PHOTO && data != null) {
            //加载环节布局
            if (processList != null && processList.size() >= 0) {
                addMediaView(processList);
            }
        }
        //删除拍照的回调
        //预览视频之后的回调
        if (requestCode == Constants.DISPLAY_VIDEO && data != null) {
            //加载环节布局
            if (processList != null && processList.size() >= 0) {
                addMediaView(processList);
            }
        }

        if (requestCode == RG_REQUEST && data != null) {
            if (processList != null && processList.size() >= 0) {
                addMediaView(processList);
            }
        }

    }


    //监听物理返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isHaveVideo() && !isHaveImage()) {
                MySelectDialog mySelectDialog = new MySelectDialog(EditVideoNewActivity.this, s1, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
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
            } else if (!isHaveVideo() && isHaveImage()) {
                MySelectDialog mySelectDialog = new MySelectDialog(EditVideoNewActivity.this, s2, "删除", "取消", new MySelectDialog.OnCustomDialogListener() {
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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        txt_save();
    }
}
