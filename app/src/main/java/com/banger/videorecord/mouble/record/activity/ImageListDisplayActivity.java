package com.banger.videorecord.mouble.record.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.record.adapter.DisplayImgAdapter;
import com.banger.videorecord.mouble.record.bean.DisplayImgEntity;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageListInfo;
import com.banger.videorecord.mouble.record.bean.VideoEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_image_list_display)
public class ImageListDisplayActivity extends AppCompatActivity {
    @ViewById
    TextView activity_title;
    @ViewById
    ImageView img_go_back;
    @ViewById
    GridView grid_img_display;
    private int height;
    private DisplayImgAdapter adapter;
    private List<ImageInfoBean> imgLists;
    private AppContext appContext;
    final int RG_REQUEST = 0; //判断回调函数的值
    ImageListInfo bean;
    int ringNum;//环节数
    @AfterViews()
    void initViews() {
        activity_title.setText("图片展示");
        appContext= (AppContext) getApplication();
        initData();
        initAdapter();
    }

    @Click
    void img_go_back() {
        sendList();
    }


    //传递对象
    private void sendList() {
        if(null!=bean){
            Intent intent = new Intent();
            intent.putExtra("img", bean);
            setResult(RG_REQUEST, intent);
            finish();
        }else{
            finish();
        }

    }
    private void initData() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        height = screenWidth / 4;
        imgLists = new ArrayList<>();
        DisplayImgEntity entity = (DisplayImgEntity) getIntent().getSerializableExtra("entity");
        imgLists = entity.getImgList();
        ringNum = entity.getRingNum();

        Log.e("ringNum", "initData: "+ringNum);
    }

    private void initAdapter() {
        adapter = new DisplayImgAdapter(this, appContext,imgLists, height);
        grid_img_display.setAdapter(adapter);
        grid_img_display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                File file = new File(imgLists.get(position).getFilePath());
//                intent.setDataAndType(Uri.fromFile(file), "image/*");
//                startActivity(intent);
                Intent intent = new Intent(ImageListDisplayActivity.this,PictureViewerActivity_.class);
                Bundle bundle=new Bundle();
                DisplayImgEntity entity = new DisplayImgEntity();
                entity.setImgList(imgLists);
                entity.setRingNum(ringNum);
                bundle.putSerializable("entity", entity);
                bundle.putInt("Position", position);
                intent.putExtras(bundle);
                startActivityForResult(intent,RG_REQUEST);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            sendList();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       bean= (ImageListInfo) data.getSerializableExtra("img");
        Log.e("qqq", "onActivityResult11: "+bean.getImgList().size()+"=="+bean.getRingNum());
        if (requestCode == RG_REQUEST && data != null && null!=bean) {
            imgLists=bean.getImgList();
            initAdapter();
        }
    }
}
