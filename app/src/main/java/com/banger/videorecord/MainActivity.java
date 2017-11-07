package com.banger.videorecord;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.banger.videorecord.mouble.setting.bean.Person;
import com.banger.videorecord.mouble.setting.bean.Phone;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    Button insert,select1,select2;
    private StorageManager mStorageManager;
    private Method mMethodGetPaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert = (Button)findViewById(R.id.insert);
        select1 = (Button)findViewById(R.id.select1);
        select2 = (Button)findViewById(R.id.select2);
        insert.setOnClickListener(this);
        select1.setOnClickListener(this);
        select2.setOnClickListener(this);

        String inPath = getInnerSDCardPath();
        Log.e(TAG, "内置SD卡路径： "+inPath+"机身内存总大小："+getRomTotalSize()+"机身可用总大小："+getRomAvailableSize());

//        insert();
//        select1();
//        select2();
//        getPath2();
        initMstorageManager();
        String [] outPath = getVolumePaths();
        for (String aa:outPath){
            if (!aa.contains(inPath)){
                Log.e(TAG, "outPath: "+aa );
                test(aa);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert:
                insert();
                break;
            case R.id.select1:
                select1();
                break;
            case R.id.select2:
                select2();
                break;
        }
    }

    void insert(){
       Person p = new Person();
        p.setName("person1");
        p.setGender("male");
        p.save();

        Phone phone1 = new Phone();
        phone1.setPerson(p);
        phone1.setPhoneNumber("123456");
        phone1.save();

        Phone phone2 = new Phone();
        phone2.setPerson(p);
        phone2.setPhoneNumber("456789");
        phone2.save();
        Log.e(TAG, "insert: " );
    }
    void select1(){
//        DataSupport.find(Phone.class,2,true);
//        DataSupport.findAll(Phone.class,true);
        List<Phone> phones = DataSupport.where("id>?", "-1").order("id").limit(3).find(Phone.class);
        for(Phone p : phones) {
            Person per = p.getPerson();
            if (null!=per){
                Log.e(TAG, "getName: "+per.getName() );
            }
            Log.e(TAG, "getPerson: "+p.getPerson()+"getId:"+p.getId()+"getPhoneNumber:"+p.getPhoneNumber());
        }

    }
    void select2(){
        Cursor cursor = DataSupport.findBySQL("select * from phone where id=?","1");
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            Log.e(TAG, "select2: "+cursor.getString(cursor.getColumnIndex("phonenumber")));
        }
        cursor.close();
    }
    /**
     * 获取内置SD卡路径
     * @return
     */
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }
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
     * 获得机身内存总大小
     *
     * @return
     */
    private String getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
        }
        long totalBlocks = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBlocks = stat.getBlockCountLong();
        }
        return Formatter.formatFileSize(MainActivity.this, blockSize * totalBlocks);
    }
    /**
     * 获得机身可用内存
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return Formatter.formatFileSize(MainActivity.this, blockSize * availableBlocks);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void test(String path){
        File sdCard = new File(path);
        StatFs sf = new StatFs(sdCard.getPath());
        long blockSize = sf.getBlockSizeLong();
        long totalBlocks = sf.getBlockCountLong();//总大小
        long availableBlocks = sf.getAvailableBlocksLong(); //剩余大小
        Log.e(TAG, "总大小: "+Formatter.formatFileSize(MainActivity.this, blockSize * totalBlocks)+"剩余大小为:"+Formatter.formatFileSize(MainActivity.this, blockSize * availableBlocks) );

    }
}
