package com.banger.videorecord.mouble.record.business;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zhusiliang on 16/6/15.
 */
public class SaveImgRunnable implements Runnable {
    private File file;
    private Bitmap bitmap;
    private Handler handler;

    public SaveImgRunnable(File file,Bitmap bitmap,Handler handler){
        this.file=file;
        this.bitmap=bitmap;
        this.handler=handler;
    }
    @Override
    public void run() {
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);//压缩,100表示不压缩
            byte[] byteArray = stream.toByteArray();
            fos.write(byteArray);
            fos.close();
            if(file.exists()){
                Message msg=Message.obtain();
                msg.obj=file.getPath();
                msg.what=200;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
