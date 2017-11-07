package com.banger.videorecord.mouble.setting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.LoginResult;
import com.banger.videorecord.bean.result.Version;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.HttpConfig;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.record.activity.TabVideoRecordActivity_;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.bean.XmlMessageBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by qyqx on 2016/7/25.
 * 登录等待
 */

@EActivity(R.layout.activity_waiting)
public class WaitingActivity extends Activity {

    @ViewById
    ImageView lv_waiting;

    AlphaAnimation alpha;
    String name;
    String pwd;
    int logState;
    private static final String TAG = "WaitingActivity";

    private AlertDialog dialog;
    @Bean(HttpParams.class)
    HttpParams httpParams;
    @App
    AppContext appContext;
    private List<XmlMessageBean> xmlMessageBeanList = new ArrayList<>();


    @AfterViews
    void initViews() {
        Window window = WaitingActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.WHITE);
        }


        SharedPrefsUtil sp = SharedPrefsUtil.getInstance();
        name = sp.getStringValue(WaitingActivity.this, Constants.USER_NAME, "");
        pwd = sp.getStringValue(WaitingActivity.this, Constants.USER_PWD, "");
        logState = appContext.getLoginState();

        startAnimation();
    }

    private void startAnimation() {

        alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);
        lv_waiting.setAnimation(alpha);
        alpha.startNow();

        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                alpha.cancel();
//                if (logState == 0) {
//                    updateVersion();
//                } else {
                    Intent intent = new Intent(WaitingActivity.this, LoginActivity_.class);
                    startActivity(intent);
                    finish();
//                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private void updateVersion() {
        {
            dialog = UiHelper.getInstance().createProgress(WaitingActivity.this);
            HttpTool httpTool = RetrofitUtils.createApi(WaitingActivity.this, HttpTool.class);
            httpTool.updateVersion(httpParams.versionUpdateParams(appContext))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Version>() {
                        @Override
                        public void onCompleted() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            jumpToNext();
                        }

                        @Override
                        public void onNext(Version version) {
                            Log.e(TAG, "onNext: " + version.toString());
                            if (null != version && version.getResult() == 0) {
                                /**
                                 *true 	强制升级
                                 need	提示升级
                                 false	不需要升级
                                 */
                                if (!"false".equals(version.getNeedUpgrade())) {

                                    Intent intent = new Intent(WaitingActivity.this, LoginActivity_.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    jumpToNext();
                                }
                            } else {
                                jumpToNext();
                            }
                        }
                    });
        }
    }

    private void jumpToNext() {

        if (logState == 0 && name.length() != 0 && pwd.length() != 0) {
//            handler.post(runnable);
            login(name, pwd);
        } else {
            Intent intent = new Intent(WaitingActivity.this, LoginActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 登录接口
     *
     * @param name     用户姓名
     * @param password 用户密码
     */
    void login(String name, String password) {
        dialog = UiHelper.getInstance().createProgress(WaitingActivity.this);
        try {
            HttpTool httpTool = RetrofitUtils.createApi(WaitingActivity.this, HttpTool.class);
            httpTool = RetrofitUtils.createApi(WaitingActivity.this, HttpTool.class);
            httpTool.userLogin(httpParams.loginParams(appContext, name, password))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LoginResult>() {
                        @Override
                        public void onCompleted() {
                            Log.e(TAG, "onCompleted: ");
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            Log.e(TAG, "onError: " + e.getMessage());
                            //服务器异常或网络不通
                            ToastUtil.showShortToast(WaitingActivity.this, "网络异常，请重试");
                            Intent intent = new Intent(WaitingActivity.this, LoginActivity_.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onNext(LoginResult responseBody) {
                            if(responseBody.getLoginDate().equals(SharedPrefsUtil.getInstance().getStringValue(WaitingActivity.this, Constants.LOGIN_TIME_OVER, ""))){
                                Log.e(TAG, "onNext: " + responseBody.toString());
                                ToastUtil.showShortToast(WaitingActivity.this, "登录成功");
                                Intent intent = new Intent(WaitingActivity.this, TabVideoRecordActivity_.class);
                                SharedPrefsUtil.getInstance().putIntValue(WaitingActivity.this, Constants.LOGIN_STATE, 0);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(WaitingActivity.this, LoginActivity_.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "login: " + e.getMessage());
            ToastUtil.showShortToast(WaitingActivity.this, "请检查ip和端口号");
        }
    }

    //数据同步
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<String> xmlList = new ArrayList<>();
            if (appContext.getLoginState() == 0) {
                xmlList = FileUtils.getXmlList(Constants.ONLINE_XML_PATH);
            } else {
                xmlList = FileUtils.getXmlList(Constants.OFFLINE_XML_PATH);
            }
            if (xmlList != null && xmlList.size() > 0) {
                for (String xmlFile : xmlList) {
                    System.out.println("zzzz file" + xmlFile);
                    File file = new File(xmlFile);
                    if (file.exists()) {
                        try {
                            InputStream inputStream = new FileInputStream(file);
                            XmlMessageBean xmlMessageBean = XmlUtils.praseXml(inputStream);
                            if (xmlMessageBean != null) {
                                xmlMessageBeanList.add(xmlMessageBean);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (xmlMessageBeanList != null && xmlMessageBeanList.size() > 0) {
                    List<BusinessInfoBean> list = DBVideoUtils.findAllBizInfo();
                    //将xml读出来的信息，如果没有的话存库
                    for (XmlMessageBean bean : xmlMessageBeanList) {

                        if (list != null && list.size() > 0) {
                            boolean isExit = false;
                            for (BusinessInfoBean businessInfoBean : list) {
                                if (bean.getBusinessInfoBean() != null) {
                                    if (bean.getBusinessInfoBean().getBizNo().equals(businessInfoBean.getBizNo())) {
                                        isExit = true;
                                    }
                                }
                            }
                            if (!isExit) {
                                saveDataToDb(bean);
                            }
                        } else {
                            saveDataToDb(bean);
                        }
                    }
                    //将数据库有，但是xml文件不存在的数据删除
                    if (list != null && list.size() > 0) {
                        List<BusinessInfoBean> deleteList = new ArrayList<>();
                        for (BusinessInfoBean businessInfoBean : list) {
                            boolean isExit = false;
                            for (XmlMessageBean xmlMessageBean : xmlMessageBeanList) {
                                if (xmlMessageBean.getBusinessInfoBean().getBizNo()!=null&&xmlMessageBean.getBusinessInfoBean().getBizNo().equals(businessInfoBean.getBizNo())) {
                                    isExit = true;
                                }
                            }
                            if (!isExit) {
                                deleteList.add(businessInfoBean);
                            }
                        }
                        //删除xml没有，但是数据有的数据
                        if (deleteList != null && deleteList.size() > 0) {
                            for (BusinessInfoBean businessInfoBean : deleteList) {
                                DBVideoUtils.deleteBusinessInfo(businessInfoBean.getId());
                            }
                        }
                    }
                } else {
                    DBVideoUtils.deleteBusinessAll();
                }
            } else {
                DBVideoUtils.deleteBusinessAll();
            }
            Message message = Message.obtain();
            message.what = Constants.GET_XML;
            handler.sendMessage(message);
        }
    };

    //将xml获取到的数据存到数据库
    private void saveDataToDb(XmlMessageBean bean) {
        if (bean.getBusinessInfoBean() != null) {
            bean.getBusinessInfoBean().saveThrows();
        }
        if (bean.getVideoList() != null && bean.getVideoList().size() > 0) {
            for (VideoInfoBean videoInfoBean : bean.getVideoList()) {
                videoInfoBean.saveThrows();
            }
        }
        if (bean.getImageList() != null && bean.getVideoList().size() > 0) {
            for (ImageInfoBean imageInfoBean : bean.getImageList()) {
                imageInfoBean.saveThrows();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what == Constants.GET_XML) {
//                ToastUtil.showShortToast(WaitingActivity.this, "登录成功");
//                Intent intent = new Intent(WaitingActivity.this, TabVideoRecordActivity_.class);
//                startActivity(intent);
//                finish();
//            }
        }
    };

}
