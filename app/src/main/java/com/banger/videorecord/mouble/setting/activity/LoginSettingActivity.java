package com.banger.videorecord.mouble.setting.activity;

import android.app.Activity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit2.Retrofit;

/**
 * Created by Xuchaowen on 2016/6/8.
 * 登陆设置界面
 */
@EActivity(R.layout.activity_setting)
public class LoginSettingActivity extends Activity {

    @ViewById
    TextView activity_title;
    @ViewById
    EditText txtIp;//ip
    @ViewById
    EditText txtPort;//端口
    @ViewById
    EditText txtNode;//节点
    String ip;
    String port;
    String node;
    String addIp;
    String addPort;
    String addNode;
    SharedPrefsUtil sp = SharedPrefsUtil.getInstance();

    @AfterViews
    void initViews() {
        activity_title.setText("登录设置");
        txtIp.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        txtPort.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        addIp = sp.getStringValue(LoginSettingActivity.this, Constants.LOGIN_IP, "");
        addPort = sp.getStringValue(LoginSettingActivity.this, Constants.LOGIN_PORT, "");
        addNode=sp.getStringValue(LoginSettingActivity.this,Constants.LOGIN_NODE,"");
        txtIp.setText(addIp);
        if (addIp.length() != 0) {
            txtIp.setSelection(addIp.length());
        }
        txtPort.setText(addPort);
        if (addPort.length() != 0) {
            txtPort.setSelection(addPort.length());
        }
        txtNode.setText(addNode);
        if (addNode.length() != 0) {
            txtNode.setSelection(addNode.length());
        }
    }

    @Click
    void img_go_back() {
        finish();
    }

    @Click
    void txt_save() {//保存
        ip = txtIp.getText().toString().trim();
        port = txtPort.getText().toString().trim();
        node = txtNode.getText().toString().trim();
        if (ip.length() != 0 && port.length() != 0) {
            sp.putStringValue(LoginSettingActivity.this, Constants.LOGIN_IP, ip);
            sp.putStringValue(LoginSettingActivity.this, Constants.LOGIN_PORT, port);
            sp.putStringValue(LoginSettingActivity.this, Constants.LOGIN_NODE, node);
            ToastUtil.showShortToast(LoginSettingActivity.this, "保存成功");
            SharedPrefsUtil.getInstance().putBooleanValue(LoginSettingActivity.this, Constants.RESETURI, true);
        } else {
            ToastUtil.showShortToast(LoginSettingActivity.this, "IP和端口不能为空");
        }
        finish();
    }

}
