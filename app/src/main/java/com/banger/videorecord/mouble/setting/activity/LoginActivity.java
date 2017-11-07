package com.banger.videorecord.mouble.setting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.LoginResult;
import com.banger.videorecord.bean.result.Version;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.HttpConfig;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.product.business.ProductBusinessImp;
import com.banger.videorecord.mouble.product.business.ProductBusinessInt;
import com.banger.videorecord.mouble.record.activity.TabVideoRecordActivity_;
import com.banger.videorecord.mouble.record.bean.BusinessInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.bean.XmlMessageBean;
import com.banger.videorecord.mouble.record.business.CameraBus;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.util.XmlUtils;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.GsonUtil;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.util.ZipUtil;
import com.banger.videorecord.widget.MyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xuchaowen on 2016/6/7.
 * 登录页面
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    @ViewById
    EditText txtUserName;
    @ViewById
    EditText txtUserPassword;
    @ViewById
    LinearLayout ll_name;//用户名
    @ViewById
    LinearLayout ll_pwd;//密码
    @ViewById
    Button btnLogin;//登录
    @ViewById
    Button btn_come_in;//进入
    @ViewById
    TextView txt_online;
    @ViewById
    TextView btn_offline;
    @App
    AppContext appContext;
    private String name;
    private String pwd;

    @Bean(HttpParams.class)
    HttpParams httpParams;

    @Bean(ProductBusinessImp.class)
    ProductBusinessInt productBus;

    private AlertDialog dialog;
    private boolean aotu = false;
    private List<XmlMessageBean> xmlMessageBeanList;
    int state = 0;//在线离线状态
    private boolean isData = false;
    private boolean isLogin = false;

    @AfterViews
    void initViews() {
        xmlMessageBeanList = new ArrayList<>();
        Intent intent = getIntent();
        int resetLogin = 0;
        if (null != intent) {
            resetLogin = intent.getIntExtra(Constants.RESETLOGIN, 0);
        }
        if (resetLogin == 0 && appContext.getLoginState()==0) {
            updateVersion();
        }
    }


    void loginUser() {

        SharedPrefsUtil sp = SharedPrefsUtil.getInstance();
        String name1 = sp.getStringValue(LoginActivity.this, Constants.USER_NAME, "");
        String pwd1 = sp.getStringValue(LoginActivity.this, Constants.USER_PWD, "");
        if (name1.length() != 0 && pwd1.length() != 0) {
            txtUserName.setText(name1);
            txtUserName.setSelection(name1.length());
            txtUserPassword.setText(pwd1);
            txtUserPassword.setSelection(pwd1.length());
            aotu = true;
            btnLogin.performClick();

        }
        getPermissions();
    }

    @Click
    void txt_online() {//在线
        btn_offline.setTextColor(Color.argb(60, 71, 176, 237));   //文字透明度
        txt_online.setTextColor(Color.argb(255, 71, 176, 237));   //文字透明度
        ll_name.setVisibility(View.VISIBLE);
        ll_pwd.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btn_come_in.setVisibility(View.GONE);
        appContext.setLoginState(0);
    }

    @Click
    void btn_offline() {//离线
        txt_online.setTextColor(Color.argb(60, 71, 176, 237));   //文字透明度
        btn_offline.setTextColor(Color.argb(255, 71, 176, 237));   //文字透明度
        ll_name.setVisibility(View.INVISIBLE);
        ll_pwd.setVisibility(View.GONE);
        btnLogin.setVisibility(View.INVISIBLE);
        btn_come_in.setVisibility(View.VISIBLE);
        appContext.setLoginState(1);

//        String jsonString ="{\"name\":\"android\",\"version\":\"Beta1.0\"}";
//        appBean app=GsonUtil.getInstance().json2Bean(jsonString,appBean.class);
//        Log.e(TAG, "app======"+app.toString());
    }

    @Click
    void btn_come_in() {//进入
        state = 1;
//        Intent intent=new Intent(LoginActivity.this,FileListActivity.class);

        if (null != dialog) {
            dialog.show();
        } else {
            dialog = UiHelper.getInstance().createProgress(LoginActivity.this);
        }
        isLogin = true;
        appContext.setLoginState(1);
        handler.post(runnable);
    }

    @Click
    void txtSetting() {//设置

        Intent intent = new Intent(LoginActivity.this, LoginSettingActivity_.class);
        startActivity(intent);
//        sendDevice();
    }

    @Click
    void btnLogin() {//登录
        appContext.setLoginState(0);
        name = txtUserName.getText().toString().trim();
        pwd = txtUserPassword.getText().toString().trim();

        state = 0;
        String ip = SharedPrefsUtil.getInstance().getStringValue(LoginActivity.this, Constants.LOGIN_IP, "");
        String port = SharedPrefsUtil.getInstance().getStringValue(LoginActivity.this, Constants.LOGIN_PORT, "");
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
            ToastUtil.showShortToast(LoginActivity.this, "请先设置IP和端口");
            return;
        }
        if (name.length() != 0 && pwd.length() != 0) {

            if (!aotu) {
                handler.post(runnable);
            } else {
                isData = true;
            }
            if (null != dialog) {
                dialog.show();
            } else {
                dialog = UiHelper.getInstance().createProgress(LoginActivity.this);
            }
            login(name, pwd);
        } else {
            ToastUtil.showShortToast(LoginActivity.this, "用户名和密码不能为空");
        }
//        Log.e(TAG, "btnLogin:=========== " + appContext.getIp() + "===" + appContext.getPort() + "===" + appContext.getNode());
    }

    /**
     * 登录接口
     *
     * @param name     用户姓名
     * @param password 用户密码
     */
    void login(String name, String password) {
        Log.e(TAG, "login: 111");
        try {
            HttpTool httpTool = RetrofitUtils.createApi(LoginActivity.this, HttpTool.class);
            httpTool = RetrofitUtils.createApi(LoginActivity.this, HttpTool.class);
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
                            ToastUtil.showShortToast(LoginActivity.this, "网络异常，请重试");
                        }

                        @Override
                        public void onNext(LoginResult responseBody) {
                            Log.e(TAG, "onNext: " + responseBody.toString());
                            //判断是否过期
                            if (responseBody.getResult() == 0) {
                                isLogin = true;
                                if (appContext.loginTimeOver(responseBody, aotu)) {
                                    //保存用户名和密码
                                    saveData(responseBody);
                                    if (isData && isLogin) {
                                        state = 0;
                                        ToastUtil.showShortToast(LoginActivity.this, "登录成功");
                                        Intent intent = new Intent(LoginActivity.this, TabVideoRecordActivity_.class);
                                        SharedPrefsUtil.getInstance().putIntValue(LoginActivity.this, Constants.LOGIN_STATE, 0);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    //开启线程，匹配xml和数据库的数据
                                    aotu = false;
                                    txtUserName.setText("");
                                    txtUserPassword.setText("");
                                    SharedPrefsUtil.getInstance().putStringValue(LoginActivity.this, Constants.USER_PWD, "");
                                    ToastUtil.showShortToast(LoginActivity.this, "登录信息过期，请重新登录");
                                }

                            } else {
                                ToastUtil.showShortToast(LoginActivity.this, responseBody.getError());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "login: " + e.getMessage());
            ToastUtil.showShortToast(LoginActivity.this, "请检查ip和端口号");
        }
//        finally {
//            ToastUtil.showShortToast(LoginActivity.this, "登录异常");
//        }
    }

    /**
     * 保存账户和密码
     */
    private void saveData(LoginResult loginResult) {
        SharedPrefsUtil sp = SharedPrefsUtil.getInstance();
        sp.putStringValue(LoginActivity.this, Constants.USER_NAME, name);
        sp.putStringValue(LoginActivity.this, Constants.USER_PWD, pwd);
        sp.putStringValue(LoginActivity.this, Constants.LOGIN_TIME_OVER, loginResult.getLoginDate());
//        sp.putStringValue(LoginActivity.this, Constants.LOGIN_TIME_OVER, "2016-07-25");
    }

    /**
     * 获取照相权限
     */
    void getPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
//                     // your code using Camera2 API here - is api 21 or higher
            boolean state = CameraBus.newInstance().hasPermissionsGranted(Constants.RECORD_PERMISSIONS, LoginActivity.this);
            if (!state) {
                requestPermissions(Constants.RECORD_PERMISSIONS, Constants.REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }
        }
    }

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
                    try {
                        List<BusinessInfoBean> list = DBVideoUtils.findAllBizInfo();
                        //将xml读出来的信息，如果没有的话存库
                        for (XmlMessageBean bean : xmlMessageBeanList) {
                            boolean isExit = false;
                            if (list != null && list.size() > 0) {
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
                                    if (xmlMessageBean.getBusinessInfoBean().getBizNo().equals(businessInfoBean.getBizNo())) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void changeXmlToDb(List<XmlMessageBean> xmlList, List<BusinessInfoBean> bizList) {
        //
        if (xmlList != null && xmlList.size() > 0) {
            for (XmlMessageBean bean : xmlList) {
                //如果数据库有，则循环数据库
                if (bizList != null && bizList.size() > 0) {
                    boolean isExit = false;
                    for (BusinessInfoBean businessInfoBean : bizList) {
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
        } else {
            DBVideoUtils.deleteBusinessAll();
        }
        //讲xml有的数据但是数据库没有的数据删除
        if (bizList != null && bizList.size() > 0) {
            List<BusinessInfoBean> deleteList = new ArrayList<>();
            for (BusinessInfoBean businessInfoBean : bizList) {
                boolean isExit = false;
                for (XmlMessageBean xmlMessageBean : xmlList) {
                    if (xmlMessageBean.getBusinessInfoBean().getBizNo().equals(businessInfoBean.getBizNo())) {
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
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constants.GET_XML) {
                isData = true;
                if (isData && isLogin) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (appContext.getLoginState() == 1) {
                        SharedPrefsUtil sp = SharedPrefsUtil.getInstance();
                        sp.putStringValue(LoginActivity.this, Constants.USER_NAME, "");
                        sp.putStringValue(LoginActivity.this, Constants.USER_PWD, "");
                        Intent intent = new Intent(LoginActivity.this, TabVideoRecordActivity_.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.showShortToast(LoginActivity.this, "登录成功");
                        appContext.setLoginState(0);
                        Intent intent = new Intent(LoginActivity.this, TabVideoRecordActivity_.class);
                        SharedPrefsUtil.getInstance().putIntValue(LoginActivity.this, Constants.LOGIN_STATE, 0);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        }
    };

    /**
     * 权限获取的回调，针对6.0
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onRequestPermissionsResult: ");
                initNativePath();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            showLog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 提示确认退出
     */
    private void showLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("确定要退出应用吗？");
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void updateVersion() {
        {
            dialog = UiHelper.getInstance().createProgress(LoginActivity.this);
            HttpTool httpTool = RetrofitUtils.createApi(LoginActivity.this, HttpTool.class);
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
                            loginUser();
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
//                            if ("true".equals(version.getNeedUpgrade())) {
//
//                            } else if ("need".equals(version.getNeedUpgrade())) {
//
//                            } else
                                if (!"false".equals(version.getNeedUpgrade())) {
                                    UiHelper.getInstance().updateDialog(LoginActivity.this, version.getRemark(), "软件更新提示", "更新", "取消", version, new UiHelper.OnCustomDialogListener() {
                                        @Override
                                        public void cancle(Dialog dialog, Version version) {
                                            if (version.getNeedUpgrade().equals("true")) {
                                                android.os.Process.killProcess(android.os.Process.myPid());
                                                System.exit(0);
                                            } else {
                                                loginUser();
                                            }
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void confirm(Dialog dialog, Version version) {
                                            if (!"".equals(version.getUpgradeUrl())) {
                                                appContext.openBrowser(LoginActivity.this, HttpConfig.getInstance().getTcpUpVideo(appContext) + version.getUpgradeUrl());
                                            } else {
                                                ToastUtil.showShortToast(LoginActivity.this, "下载信息异常，请检查下载路径配置");
                                            }
                                        }
                                    });
                                } else {
                                    loginUser();
                                }
                            } else {
                                loginUser();
                            }
                        }
                    });
        }
    }

    private void Unzip() {
        try {
            ZipUtil.unzip(Constants.CLASS_PATH + "products.zip", Constants.CLASS_PATH + "products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始化文件路径
    private void initNativePath() {
        File file = new File(Constants.OFFLINE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
//        file = new File(Constants.XML_PATH);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        file = new File(Constants.FILE_PATH);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
    }
}
