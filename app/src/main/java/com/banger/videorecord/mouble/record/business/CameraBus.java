package com.banger.videorecord.mouble.record.business;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.banger.videorecord.mouble.record.widget.ConfirmationDialog;
import com.banger.videorecord.util.Constants;

/**
 * 摄像头操作
 * Created by zhujm on 2016/4/25.
 */
public class CameraBus {
    private static final String TAG = "CameraBus";
    protected static CameraBus cameraBus;
    public static CameraBus newInstance() {
        if (null==cameraBus){
            cameraBus = new CameraBus();
        }
        return cameraBus;
    }
    //提示获取开启摄像头权限
    @TargetApi(23)
    public void requestCameraPermission(Activity activity) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)&&activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//            new ConfirmationDialog().show(fragmentManager, Constants.FRAGMENT_DIALOG);
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    Constants.REQUEST_CAMERA_PERMISSION);
        }
    }

    //提示获取开启写入文件
    @TargetApi(23)
        public void requestWritePermission(Activity activity) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            new ConfirmationDialog().show(fragmentManager, Constants.FRAGMENT_DIALOG);
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_CAMERA_PERMISSION);
        }
    }

    @TargetApi(23)
    public void requestWriteSettingPermission(Activity activity) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_SETTINGS)&&activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_SETTINGS)) {
//            new ConfirmationDialog().show(fragmentManager, Constants.FRAGMENT_DIALOG);
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_SETTINGS},
                    Constants.REQUEST_CAMERA_PERMISSION);
        }
    }

    //提示获取录音文件权限
    @TargetApi(23)
    public void requestRecordPermission(Activity activity) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)&&activity.shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
//            new ConfirmationDialog().show(fragmentManager, Constants.FRAGMENT_DIALOG);
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                    Constants.REQUEST_CAMERA_PERMISSION);
        }
    }
    /**
     * 判断是否授予权限
     * @param permissions 权限
     * @param activity 当前操作吃activity
     * @return false true
     */
    public boolean hasPermissionsGranted(String[] permissions,Activity activity) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
