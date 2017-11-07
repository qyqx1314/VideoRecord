package com.banger.videorecord.mouble.record.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.AppException;
import com.banger.videorecord.mouble.product.bean.ProductDetailInfo;
import com.banger.videorecord.mouble.record.bean.DisplayImgEntity;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageListInfo;
import com.banger.videorecord.mouble.record.bean.VideoEntity;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Xuchaowen on 2016/7/8.
 * 照片预览
 */
@EActivity(R.layout.activity_picture_viewer)
public class PictureViewerActivity extends Activity {
    private static final String TAG = "PictureViewerActivity";
    @ViewById
    TextView activity_title;
    @ViewById
    Gallery gallery;
    @App
    AppContext appContext;
    /**
     * 装ImageView数组
     */
    private ImageAdapter adapter;
    public static int ScreenW;
    public static int ScreenH;
    int pageCount;//当前页数
    int position;
    int ringNum;//环节数
    DisplayImgEntity entity;
    ArrayList<ImageInfoBean> imgList;
    final int RG_REQUEST = 0; //判断回调函数的值
    ImageListInfo bean;

    @AfterViews
    void initViews() {
        Window window = PictureViewerActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }

        DisplayMetrics dm = new DisplayMetrics();//获取手机屏幕的宽高
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ScreenW = dm.widthPixels;
        ScreenH = dm.heightPixels;
        activity_title.setText("凭证");
        entity = (DisplayImgEntity) getIntent().getSerializableExtra("entity");
        position = getIntent().getIntExtra("Position", 10000);
        if (null != entity) {
            imgList = (ArrayList<ImageInfoBean>) entity.getImgList();
            ringNum = entity.getRingNum();
        }
        adapter = new ImageAdapter(PictureViewerActivity.this, imgList);

        if (null != gallery) {
            gallery.setAdapter(adapter);
            gallery.setSelection(position);
        }
//        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.e(TAG, "onItemSelected:parent is: "+parent );
//                Log.e(TAG, "onItemSelected:view is: "+view );
//                Log.e(TAG, "onItemSelected:position ls: "+position );
//                Log.e(TAG, "onItemSelected:id is: "+id );
//                if (position !=0&&position!=imgList.size()){
//                    ImageAdapter imageAdapter = (ImageAdapter)parent.getItemAtPosition(position-1);
//                    if (null!=imageAdapter.imageView){
//                        Log.e(TAG, "onItemSelected: " );
//                        BitmapDrawable bitmap = (BitmapDrawable)imageAdapter.imageView.getDrawable();
//                        bitmap.getBitmap().recycle();
//                        System.gc();
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.e(TAG, "onNothingSelected:parent is: "+parent );
//            }
//        });
    }


    @Click
    void img_go_back() {
        sendList();
    }

    @Click
    void rl_delete() {//删除
        showDeleteDia();

    }

    private void showDeleteDia() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PictureViewerActivity.this);
        builder.setMessage("您确定要删除当前凭证吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFiles();
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

    private void deleteFiles() {

        File file = new File(imgList.get(pageCount).getFilePath());
        file.delete();
        DBVideoUtils.deleteImageById(imgList.get(pageCount).getId());
        imgList.remove(imgList.get(pageCount));
        bean = new ImageListInfo();
        bean.setImgList(imgList);
        bean.setRingNum(ringNum);
        adapter.notifyDataSetChanged();

        if (imgList.size() == 0) {
            sendList();
        }
        ToastUtil.showShortToast(PictureViewerActivity.this, "删除成功");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            sendList();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //传递对象
    private void sendList() {
            Intent intent = new Intent();
            bean = new ImageListInfo();
            bean.setImgList(imgList);
            bean.setRingNum(ringNum);
            intent.putExtra("img", bean);
            setResult(RG_REQUEST, intent);
            finish();
    }

    public class ImageAdapter extends BaseAdapter {

        private Context context;
        ArrayList<ImageInfoBean> imgList;
        LayoutInflater inflater;
        ImageView imageView;
        public ImageAdapter(Context context, ArrayList<ImageInfoBean> imgList) {
            this.context = context;
            this.imgList = imgList;
            this.inflater = LayoutInflater.from(context);
        }

        /* 使用在res/values/attrs.xml中的定义 * 的Gallery属性.*/
//            TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
//      /*取得Gallery属性的Index id*/
//            mGalleryItemBackground = a.getResourceId( R.styleable.Gallery_android_galleryItemBackground, 0);
//      /*让对象的styleable属性能够反复使用*/
//            a.recycle();
        public int getCount() {
            return imgList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            pageCount = position;
            ImageView i = new ImageView(PictureViewerActivity.this);
      /*设定图片给imageView对象*/
            try {
                if (null != imgList.get(position)) {
                    appContext.getImageLoader().displayImage("file://" + imgList.get(position).getFilePath(), i,appContext.getMemoryOptions());
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
      /*重新设定图片的宽高*/
            i.setScaleType(ImageView.ScaleType.FIT_XY);
      /*重新设定Layout的宽高*/
            i.setLayoutParams(new Gallery.LayoutParams(PictureViewerActivity.ScreenW, PictureViewerActivity.ScreenH));
      /*设定Gallery背景图*/
//            i.setBackgroundResource(mGalleryItemBackground);
            imageView = i;
            return i;
        }
    }

}





