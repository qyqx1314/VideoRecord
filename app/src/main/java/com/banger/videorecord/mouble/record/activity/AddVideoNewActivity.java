package com.banger.videorecord.mouble.record.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.activity.BusinessTypeListActivity_;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.record.bean.AddVideoBean;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.CustomerInfoBean;
import com.banger.videorecord.mouble.record.bean.CustomerInfoResult;
import com.banger.videorecord.mouble.record.bean.FiledInfo;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;
import com.banger.videorecord.mouble.record.bean.ProductExtraInfo;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.mouble.record.widget.AddCustomerItem;
import com.banger.videorecord.mouble.record.widget.AddProductItem;
import com.banger.videorecord.mouble.record.widget.AddVideoItem;
import com.banger.videorecord.mouble.record.widget.MySelectItemDialog;
import com.banger.videorecord.mouble.setting.bean.BusinessDataInfo;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.GsonUtil;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.MySelectDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
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
 * @author zhusiliang
 *         该界面是新改的添加录音录像文件的
 */
@EActivity(R.layout.activity_add_video_new)
public class AddVideoNewActivity extends AppCompatActivity {
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
    RelativeLayout layout_id_type;//证件类型的外部layout
    @ViewById
    TextView txt_id_type;//证件类型
    @ViewById
    TextView txt_video_round;//环节的额外字段
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
    private File infoFile;
    //  最后返回的产品信息
    private ProductDetailInfo productDetailInfo;
    private AlertDialog dialog;
    private BusinessDataInfo bizInfo;//从产品传递过来的业务信息 包含录像环节
    private CustomerInfoResult customerInfoResult;
    private List<AddVideoBean> addVideoBeanList;
    private VideoInfoBean videoInfoBean;
    private BusinessInfoBean businessInfoBean;
    private int certType = 0;
    final int RG_REQUEST = 0; //判断回调函数的值
    private boolean isHaveMedia = false;

    @AfterViews
    void initViews() {
        activity_title.setText("新增录像");
        bizNo = "bizNo" + DateUtil.getNowTime("yyyyMMddHHmmssSSS");
        initFile();
        if (appContext.getLoginState() == 0) {
            getCustomerInfo();
        } else {
            String string = FileUtils.ReadTxtFile(Constants.INFO_PATH + "info.xml");
            customerInfoResult = new CustomerInfoResult();
            CustomerInfoBean customerInfoBean = GsonUtil.getInstance().json2Bean(string, CustomerInfoBean.class);
            customerInfoResult.setData(customerInfoBean);
            if (customerInfoResult.getData() != null &&
                    customerInfoResult.getData().getTemplateFields() != null &&
                    customerInfoResult.getData().getTemplateFields().size() > 0) {
                addExtraInfoToView(layout_add_info, customerInfoResult.getData().getTemplateFields());

            }

        }

    }

    @Click
    void img_go_back() {
        if (productDetailInfo != null) {
            MySelectDialog mySelectDialog = new MySelectDialog(AddVideoNewActivity.this, "已添加数据，确定不保存记录退出么？", "确定", "取消", new MySelectDialog.OnCustomDialogListener() {
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

    //选择产品
    @Click
    void btn_choose_product() {
        Intent intent = new Intent(AddVideoNewActivity.this, BusinessTypeListActivity_.class);
        intent.putExtra("isNative", false);
        startActivityForResult(intent, Constants.ADD_PRODUCT);
    }




    //选择产品
    @Click
    void rl_product() {
        if (isHaveMedia) {
            ToastUtil.showShortToast(AddVideoNewActivity.this, "已有多媒体信息，不允许修改产品信息");
        } else {
            Intent intent = new Intent(AddVideoNewActivity.this, BusinessTypeListActivity_.class);
            intent.putExtra("isNative", false);
            startActivityForResult(intent, Constants.ADD_PRODUCT);
        }
    }

    //选择证件类型
    @Click
    void layout_id_type() {
        MySelectItemDialog selectItemDialog = new MySelectItemDialog(AddVideoNewActivity.this, new MySelectItemDialog.OnCustomDialogListener() {
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
            finish();
        } else {
            ToastUtil.showShortToast(AddVideoNewActivity.this, result);
        }
    }

    @Click
    void layout_hide() {
        img_pull_down.setSelected(!img_pull_down.isSelected());
        if (img_pull_down.isSelected()) {
            layout_product_hide.setVisibility(View.GONE);
        } else {
            layout_product_hide.setVisibility(View.VISIBLE);
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

        File file = new File(Constants.INFO_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        infoFile = new File(file, "info.xml");
    }

    /**
     * 获取自定义用户信息
     */
    private void getCustomerInfo() {
        dialog = UiHelper.getInstance().createProgress(AddVideoNewActivity.this);
        dialog.show();
        HttpTool httpTool = RetrofitUtils.createApi(AddVideoNewActivity.this, HttpTool.class);
        httpTool.getCustomerInfo(httpParams.getProductType(appContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CustomerInfoResult>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(CustomerInfoResult result) {
                        Log.e(TAG, "onNext: " + result.toString());
                        if (result != null) {
                            customerInfoResult = result;
                            if (customerInfoResult.getData() != null &&
                                    customerInfoResult.getData().getTemplateFields() != null &&
                                    customerInfoResult.getData().getTemplateFields().size() > 0) {
                                addExtraInfoToView(layout_add_info, customerInfoResult.getData().getTemplateFields());
                            }
                            //将这些信息写到文件中
                            String info = GsonUtil.getInstance().toJson(customerInfoResult.getData());
                            FileUtils.writeFile(infoFile.getAbsolutePath(), info, true);
                        }
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 获取产品信息后，赋值相关的控件
     */
    private void initProduct() {
        if (productDetailInfo == null) {
            productDetailInfo = new ProductDetailInfo();
        }
        layout_product.setVisibility(View.VISIBLE);
        btn_choose_product.setVisibility(View.GONE);
        txt_product_type.setText(productDetailInfo.getProductClassName());
        txt_firm_name.setText(productDetailInfo.getBizTypeName());
        txt_product_num.setText(productDetailInfo.getProductCode());
        txt_product_name.setText(productDetailInfo.getProductName());
    }

    /**
     * 添加下面拍照和录制视频的布局
     *
     * @param list
     */
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
                if (videoList == null || videoList.size() == 0) {
                    initVideoInfo(info);
                    bean.setVideo(videoInfoBean);
                } else {
                    bean.setVideo(videoList.get(0));
                }
                //初始化图像信息
                List<ImageInfoBean> imgList = DBVideoUtils.findImageByProcess(bizNo, info.getProcessId());
                if (imgList == null || imgList.size() == 0) {
                    imgList = new ArrayList<>();
                }
                bean.setImages(imgList);
                bean.setInfo(info);
                bean.setBizNo(bizNo);
                bean.setContext(AddVideoNewActivity.this);
                AddVideoItem addVideoItem = new AddVideoItem(AddVideoNewActivity.this, bean);
                addVideoItem.setActivity(AddVideoNewActivity.this);
                layout_add_media.addView(addVideoItem);
                addVideoBeanList.add(bean);
            }
        }

    }

    private void addExtraInfoToView(LinearLayout layout, List<FiledInfo> list) {
        //录像的额外字段布局添加
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
        for (FiledInfo info : list) {
            AddCustomerItem item = new AddCustomerItem(AddVideoNewActivity.this, info);
            layout.addView(item);
        }
    }

    private void addExtraInfoToView2(LinearLayout layout, List<ProductExtraInfo> list) {
        //产品额外字段布局添加
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
        for (ProductExtraInfo info : list) {
            AddProductItem item = new AddProductItem(AddVideoNewActivity.this, info);
            layout.addView(item);
        }
        layout.setVisibility(View.GONE);
        img_pull_down.setSelected(true);
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
        videoInfoBean.setBizType(productDetailInfo.getBizType() + "");
        videoInfoBean.setProductType(productDetailInfo.getProductClassName());
        videoInfoBean.setProductName(productDetailInfo.getProductName());
        videoInfoBean.setCreateTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
        videoInfoBean.setBusinessNo("123456");
        videoInfoBean.setProductId(productDetailInfo.getProductId());
        videoInfoBean.setBuyType(1);
        videoInfoBean.setDueDate("2017-12-12 10:00:00");
        //新增的额外信息字段
        if (bizInfo.getTemplateFields() != null) {
            videoInfoBean.setExtraInfo(GsonUtil.getInstance().toJson(bizInfo.getTemplateFields()));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //       选择产品的回调
        if (requestCode == Constants.ADD_PRODUCT && data != null) {
            productDetailInfo = (ProductDetailInfo) data.getSerializableExtra("product");
            bizInfo = (BusinessDataInfo) data.getSerializableExtra(Constants.Business_Info);
            Log.e(TAG, "onActivityResult: ====" + productDetailInfo.toString() + "===" + bizInfo.toString());
            if (productDetailInfo != null) {
                initProduct();
                if (productDetailInfo.getTemplateFields() != null && productDetailInfo.getTemplateFields().size() > 0) {
                    //加载产品的额外字段
                    addExtraInfoToView2(layout_product_hide, productDetailInfo.getTemplateFields());
                }
            }
            //加载环节布局
            if (bizInfo.getProcesses() != null && bizInfo.getProcesses().size() >= 0) {
                addMediaView(bizInfo.getProcesses());
            }
            //加载额外字段布局
            if (bizInfo.getTemplateFields() != null && bizInfo.getTemplateFields().size() > 0) {
                addExtraInfoToView(layout_video_extra, bizInfo.getTemplateFields());
            } else {
                txt_video_round.setVisibility(View.GONE);
            }
        }
        //选择录像之后的回调
        if (requestCode == Constants.ADD_VIDEO && data != null) {
            VideoInfoBean bean = (VideoInfoBean) data.getSerializableExtra("videoBean");
            if (bean != null) {
                isHaveMedia = true;
                //加载环节布局
                if (bizInfo.getProcesses() != null && bizInfo.getProcesses().size() >= 0) {
                    addMediaView(bizInfo.getProcesses());
                }
            }
        }
        //选择拍照之后的回调
        if (requestCode == Constants.ADD_PHOTO && data != null) {
            isHaveMedia = true;
            //加载环节布局
            if (bizInfo.getProcesses() != null && bizInfo.getProcesses().size() >= 0) {
                addMediaView(bizInfo.getProcesses());
            }
        }
        //删除拍照的回调
        //预览视频之后的回调
        if (requestCode == Constants.DISPLAY_VIDEO && data != null) {
            //加载环节布局
            if (bizInfo.getProcesses() != null && bizInfo.getProcesses().size() >= 0) {
                addMediaView(bizInfo.getProcesses());
            }
        }

        //图片浏览回调
        if (requestCode == RG_REQUEST && data != null) {
            if (bizInfo.getProcesses() != null && bizInfo.getProcesses().size() >= 0) {
                addMediaView(bizInfo.getProcesses());
            }
        }
    }


    /**
     * 判断相关的必填项是否都有值
     *
     * @return
     */
    private String judgeInfo() {
        String result = "";
        if (productDetailInfo == null) {
            return "产品不能为空，请先选择产品";
        }
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
        if (customerInfoResult.getData().getTemplateFields() != null && customerInfoResult.getData().getTemplateFields().size() > 0) {
            for (FiledInfo info : customerInfoResult.getData().getTemplateFields()) {
                System.out.println("kkkk+value+"+info.getValue()+"bool"+info.getIsRequire());
                if (TextUtils.isEmpty(info.getValue()) && info.getIsRequire() == 1) {
                    return  info.getFieldCNName()+"不能为空,请重新输入";
                }
            }
        }
        //判断视频额外字段的必填项是否有数值
        if (bizInfo.getTemplateFields() != null && bizInfo.getTemplateFields().size() > 0) {
            for (FiledInfo info : bizInfo.getTemplateFields()) {
                if (TextUtils.isEmpty(info.getValue()) && info.getIsRequire() == 1) {
                    return  info.getFieldCNName()+"不能为空,请重新输入";
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
     * 保存业务信息
     */
    private void saveBusinessInfo() {
        if (businessInfoBean == null) {
            businessInfoBean = new BusinessInfoBean();
        }
        businessInfoBean.setBizNo(bizNo);
        businessInfoBean.setBizType(productDetailInfo.getBizTypeName());
        businessInfoBean.setBizTypeId(productDetailInfo.getBizType());
        businessInfoBean.setProductType(productDetailInfo.getProductClassName());
        businessInfoBean.setProductTypeId(productDetailInfo.getProductClass());
        businessInfoBean.setProductName(productDetailInfo.getProductName());
        businessInfoBean.setCreateTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
        businessInfoBean.setProductId(productDetailInfo.getProductId());
        if (TextUtils.isEmpty(txt_buy_money.getText().toString())) {
            businessInfoBean.setBuyMoney(0);
        } else {
            businessInfoBean.setBuyMoney(Integer.parseInt(txt_buy_money.getText().toString()));
        }
        businessInfoBean.setBuyType(1);
        businessInfoBean.setDueDate("2017-1-11 10:00:00");
        businessInfoBean.setState(1);
        businessInfoBean.setAccount(SharedPrefsUtil.getInstance().getStringValue(AddVideoNewActivity.this, Constants.USER_NAME, "admin"));
        businessInfoBean.setNote(txt_note_content.getText().toString());
        businessInfoBean.setXmlFilePath(xmlFile.getAbsolutePath());
        //新增的用户自定义字段
        businessInfoBean.setUserInfo(GsonUtil.getInstance().toJson(customerInfoResult.getData()));
        //新增的产品自定义字段
        businessInfoBean.setProductInfo(GsonUtil.getInstance().toJson(productDetailInfo.getTemplateFields()));
        //新增的用户环节自定义字段
        businessInfoBean.setMediaInfo(GsonUtil.getInstance().toJson(bizInfo.getProcesses()));
        //新增的录像自定义字段
        businessInfoBean.setMediaExtraInfo(GsonUtil.getInstance().toJson(bizInfo.getTemplateFields()));

        //之前删掉的 现在又要的字段
        businessInfoBean.setCertNumber(txt_id_code.getText().toString());
        businessInfoBean.setCertType(certType);
        businessInfoBean.setUserName(txt_user_name.getText().toString());
        businessInfoBean.setMobile(txt_user_mobile.getText().toString());
        businessInfoBean.saveThrows();
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

            if (appContext.getLoginState() == 0) {
                String xml = XmlUtils.changeBusinessToXml(businessInfoBean, list, imgLists, appContext);
                FileUtils.writeFile(xmlFile.getAbsolutePath(), xml, true);
            } else {
                String xml = XmlUtils.changeBusinessToXml(businessInfoBean, list, imgLists, appContext);
                FileUtils.writeFile(xmlFile.getAbsolutePath(), xml, true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        img_go_back();
    }


}
