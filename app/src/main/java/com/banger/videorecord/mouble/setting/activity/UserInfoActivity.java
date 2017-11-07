package com.banger.videorecord.mouble.setting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.Version;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.HttpConfig;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.record.activity.AddVideoNewActivity;
import com.banger.videorecord.mouble.record.activity.AddVideoNewActivity_;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.widget.MySelectItemDialog;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StorageUtils;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.widget.MyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xuchaowen on 2016/6/13.
 * 我的界面
 */
@EActivity(R.layout.activity_user_info)
public class UserInfoActivity extends Activity {
    private static final String TAG = "UserInfoActivity";
    @ViewById
    TextView text_save;//存储空间
    @ViewById
    RelativeLayout version_up;//版本更新
    @ViewById
    RelativeLayout layout_para_setting;//参数设置
    @ViewById
    RelativeLayout save_state;//存储状态
    @ViewById
    RelativeLayout ll_my;
    @ViewById
    Button  out_Login;
    @ViewById
    TextView txt_name;
    @ViewById
    TextView version_num;//版本号
    @Bean(com.banger.videorecord.mouble.setting.business.imp.HttpParams.class)
    com.banger.videorecord.mouble.setting.business.inf.HttpParams httpParams;
    @App
    AppContext appContext;
    //    String[] items = new String[]{ "手机内存", "SD卡"};
    StorageUtils storageUtils;
    SharedPrefsUtil sp;
    final int RG_REQUEST = 0; //判断回调函数的值
    boolean state = false;
    private MyDialog myDialog;
    private Dialog dialog;
    int loginState;
    @AfterViews
    void initViews() {
        loginState=appContext.getLoginState();
        if(loginState==0){
            ll_my.setVisibility(View.VISIBLE);
            out_Login.setText("退出登录");
        }else{
            ll_my.setVisibility(View.GONE);
            out_Login.setText("切换在线");
        }
        sp = SharedPrefsUtil.getInstance();
        txt_name.setText(sp.getStringValue(UserInfoActivity.this, Constants.USER_NAME, ""));
        version_num.setText(appContext.getCurrentVersionName());
        int location = sp.getIntValue(UserInfoActivity.this, Constants.STORAGE_PHONE, 0);
//        ToastUtil.showShortToast(UserInfoActivity.this,""+location);
        storageUtils = StorageUtils.getInstance();
        if (location == 0) {
            text_save.setText("手机 剩余" + storageUtils.getRomAvailableSize(UserInfoActivity.this));
        } else if (location == 1) {
            String outPath = sp.getStringValue(UserInfoActivity.this, Constants.STORAGE_OUTPATH, null);
            if (outPath.length() != 0) {
                File file = new File(outPath);
                text_save.setText("拓展卡 剩余" + storageUtils.getMemoryInfo(file, UserInfoActivity.this, 2));
            }
//            File file=new File(storageUtils.getExtSDCardPaths().get(1));
        }
    }

    @Click
    void layout_para_setting() {//参数设置
        Intent intent = new Intent();
        intent.setClass(UserInfoActivity.this, VideoSettingActivity_.class);
        startActivity(intent);

    }

    @Click
    void rl_offline() {//离线数据

        Intent intent=new Intent(UserInfoActivity.this,OffLineDataActivity_.class);
        startActivity(intent);

    }

    @Click
    void rl_about() {//关于
//        Intent intent=new Intent(UserInfoActivity.this, AddVideoNewActivity_.class);
//        startActivity(intent);
    }

    @Click
    void out_Login() {//退出登录
        String str ="";
        if(loginState==0){
             str = "您确定要注销当前用户登录吗？";
        }else{
             str = "您确定要切换到在线模式吗？";
        }
        List list = DBVideoUtils.findAllByStateAccount(2, SharedPrefsUtil.getInstance().getStringValue(UserInfoActivity.this, Constants.USER_NAME, ""));
        //正在上传
//        String str = "您确定要注销当前用户登录吗？";

        if (null != list && list.size() > 0) {
            str = "正在上传录像记录，注销用户可能会影响上传数据的完整性，您确认要注销当前用户吗？";
            state = true;
        }
        showDia(str);
    }

    private void showDia(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setMessage(str);
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(loginState==1){
                    SharedPrefsUtil.getInstance().putIntValue(UserInfoActivity.this, Constants.LOGIN_STATE, 0);
                }

                SharedPrefsUtil.getInstance().putStringValue(UserInfoActivity.this, Constants.USER_NAME, "");
                SharedPrefsUtil.getInstance().putStringValue(UserInfoActivity.this, Constants.USER_PWD, "");
                finish();
                if (state) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                } else {
                    Intent intent = new Intent(UserInfoActivity.this, LoginActivity_.class);
                    intent.putExtra(Constants.RESETLOGIN,1);
                    startActivity(intent);
                }
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

    //    @Click
//    void save_state(){//存储状态
//        Intent intent=new Intent(UserInfoActivity.this,StorageStateActivity_.class);
//        startActivityForResult(intent,RG_REQUEST);
//    }
    @Click
    void version_up() {
        dialog =  UiHelper.getInstance().createProgress(UserInfoActivity.this);
        dialog.show();
        HttpTool httpTool = RetrofitUtils.createApi(UserInfoActivity.this, HttpTool.class);
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
                    }

                    @Override
                    public void onNext(Version version) {
                        Log.e(TAG, "onNext: " + version.toString());
                        Log.e(TAG, "onNext: "+HttpConfig.getInstance().getTcpUpVideo(appContext) );
                        if (null != version && version.getResult() == 0) {
                            if (!"false".equals(version.getNeedUpgrade())){

                                UiHelper.getInstance().updateDialog(UserInfoActivity.this,version.getRemark(),"软件更新提示","更新","取消",version,new UiHelper.OnCustomDialogListener(){
                                    @Override
                                    public void cancle(Dialog dialog, Version version) {
                                        if (version.getNeedUpgrade().equals("true")){
                                            android.os.Process.killProcess(android.os.Process.myPid());
                                            System.exit(0);
                                        }
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void confirm(Dialog dialog, Version version) {
                                        if (!"".equals(version.getUpgradeUrl())){
                                            appContext.openBrowser(UserInfoActivity.this, HttpConfig.getInstance().getTcpUpVideo(appContext)+version.getUpgradeUrl());
                                        }else {
                                            ToastUtil.showShortToast(UserInfoActivity.this,"下载信息异常，请检查下载路径配置");
                                        }
                                    }
                                });
                            }else {
                                ToastUtil.showShortToast(UserInfoActivity.this,"当前已是最新版本");
                            }
                        }else {
                            ToastUtil.showShortToast(UserInfoActivity.this,version.getError());
                        }
                    }
                });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                initViews();
                break;
            default:
                break;
        }
    }
}
