package com.banger.videorecord.mouble.setting.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import com.banger.videorecord.MainActivity;
import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.widget.MySelectItemDialog;
import com.banger.videorecord.mouble.setting.bean.Person;
import com.banger.videorecord.mouble.setting.bean.Phone;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StorageUtils;
import com.banger.videorecord.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/6/16.
 * 存储状态设置
 */
@EActivity(R.layout.activity_storade_state)
public class StorageStateActivity extends Activity {
    private static final String TAG = "StorageStateActivity";
    private StorageManager mStorageManager;
    private Method mMethodGetPaths;
    @ViewById
    TextView activity_title;
    @ViewById
    TextView note_local;//存储位置
    @ViewById
    TextView have_used;//已用
    @ViewById
    TextView have_lost;//剩余
    String[] items = new String[]{ "手机", "拓展卡"};
    StorageUtils storageUtils;
    SharedPrefsUtil sp;
    @AfterViews
    void initViews(){
        activity_title.setText("存储状态");
        storageUtils=StorageUtils.getInstance();
        sp =SharedPrefsUtil.getInstance();
        initMstorageManager();
        int location = sp.getIntValue(StorageStateActivity.this, Constants.STORAGE_PHONE, 0);
        if(location==0){
            note_local.setText("手机");
            have_used.setText(storageUtils.getRomUsedSize(StorageStateActivity.this));
            have_lost.setText(storageUtils.getRomAvailableSize(StorageStateActivity.this));
        }else if(location==1){
            note_local.setText("拓展卡");
            File file=new File(getVolumePaths()[1]);
            have_used.setText(storageUtils.getMemoryInfo(file,StorageStateActivity.this,1));
            have_lost.setText(storageUtils.getMemoryInfo(file,StorageStateActivity.this,2));
        }

//        for (String aa:outPath){
//            if (!aa.contains(inPath)){//排除内置
//                Log.e(TAG, "outPath: "+aa );
//
//            }
//        }
    }

    /**
     * 初始化内存管理
     */
    public void initMstorageManager() {
        mStorageManager = (StorageManager)
                getSystemService(Activity.STORAGE_SERVICE);
        try {
            mMethodGetPaths = mStorageManager.getClass()
                    .getMethod("getVolumePaths");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    /**
     * 返回所有手机储存路径
     * @return
     */
    public String[] getVolumePaths() {
        String[] paths = null;
        try {
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }


    @Click
    void img_go_back(){

        Intent in = new Intent();
        in.putExtra( "result", "res" );
        setResult( RESULT_OK, in );
        finish();
    }

    @Click
    void layout_pro(){//存储位置

//        File file1=getExternalFilesDir(null);
//        Log.e(TAG, "getExternalFilesDir:=====>>>>>>> " + file1.getAbsolutePath());
//        File file2=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
//        Log.e(TAG, "getExternalStoragePublicDirectory:=====>>>>>>> " + file2.getAbsolutePath());

        //判断手机是否有SD卡
//        int count=storageUtils.getExtSDCardPaths().size();
        int count=getVolumePaths().length;
        if(count>=2){
            showDia(items);
        }else{
            ToastUtil.showShortToast(StorageStateActivity.this, "当前手机没有拓展卡");
        }
    }

    private void showDia(String[] items){
        MySelectItemDialog dialog=new MySelectItemDialog(StorageStateActivity.this,new MyDialogListener());
        dialog.updataDialog(items);
        dialog.show();
    }
    class MyDialogListener implements MySelectItemDialog.OnCustomDialogListener {
        @Override
        public void returnSelector(int index) {
            if(index==0){//手机内存
                note_local.setText("手机");
                have_used.setText(storageUtils.getRomUsedSize(StorageStateActivity.this));
                have_lost.setText(storageUtils.getRomAvailableSize(StorageStateActivity.this));
                sp.putIntValue(StorageStateActivity.this, Constants.STORAGE_PHONE,0);
            }else{
                note_local.setText("拓展卡");
                File file=new File(getVolumePaths()[1]);//+"DCIM/Camera/IMG_20160616_183810.jpg"
                File file1=getExternalFilesDir(null);
                Log.e(TAG, "getExternalFilesDir:=====>>>>>>> " + file1.getName());
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//                FileUtils.openFile(file,StorageStateActivity.this);
                if(file.length()>100){//单位是M
//                Log.e(TAG, "length========: "+file.listFiles().length);
                    Log.e(TAG, "returnSelector: ========"+getVolumePaths()[1]);
                    have_used.setText(storageUtils.getMemoryInfo(file, StorageStateActivity.this, 1));
                    have_lost.setText(storageUtils.getMemoryInfo(file,StorageStateActivity.this,2));
                    sp.putIntValue(StorageStateActivity.this, Constants.STORAGE_PHONE, 1);
                    sp.putStringValue(StorageStateActivity.this,Constants.STORAGE_OUTPATH,getVolumePaths()[1]+ File.separator+"Android/data/");
                }
            }

        }
    }
}
