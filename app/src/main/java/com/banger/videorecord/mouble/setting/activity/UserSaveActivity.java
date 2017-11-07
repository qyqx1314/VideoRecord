package com.banger.videorecord.mouble.setting.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.activity.VideoRecordActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Created by Xuchaowen on 2016/6/7.
 * 用户新增页面
 */
@EActivity(R.layout.activity_user_save)
public class UserSaveActivity extends Activity {
    private static final String TAG = "UserSaveActivity";
    @ViewById
    TextView activity_title;

    @ViewById
    EditText txtUserName;

    @AfterViews
    void initViews(){
        activity_title.setText("用户");
    }

    @Click
    void img_go_back(){
        finish();
    }

    @Click
    void use_save(){//保存
    Intent intent =new Intent(UserSaveActivity.this,VideoRecordActivity_.class);
    startActivity(intent);

    }

}
