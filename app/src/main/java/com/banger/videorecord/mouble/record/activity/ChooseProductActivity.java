package com.banger.videorecord.mouble.record.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;


import com.banger.videorecord.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Xuchaowen on 2016/6/7.
 * 选择产品页面
 */
@EActivity(R.layout.activity_choose_product)
public class ChooseProductActivity extends Activity {

    @ViewById
    TextView activity_title;


    @AfterViews
    void initViews(){
        activity_title.setText("选择产品");
    }

    @Click
    void classify1(){//业务1
        Intent intent=new Intent(ChooseProductActivity.this,AddVideo2Activity_.class);
        startActivity(intent);
    }

    @Click
    void classify2(){//业务2

    }

    @Click
    void img_go_back(){
        finish();
    }

}
