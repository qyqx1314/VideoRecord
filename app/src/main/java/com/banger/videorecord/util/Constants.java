package com.banger.videorecord.util;

import android.Manifest;
import android.os.Environment;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.activity.AddVideoActivity_;
import com.banger.videorecord.mouble.record.activity.VideoRecordActivity;
import com.banger.videorecord.mouble.record.activity.VideoRecordActivity_;
import com.banger.videorecord.mouble.record.activity.VideoRecordsActivity_;
import com.banger.videorecord.mouble.setting.activity.UserInfoActivity_;

import java.io.File;

/**
 * $desc$
 * author：jiumin
 * create on 2016/1/12 15:03
 */
public class Constants {
    public static final String VIDEO_PACKAGE  = "/VideoRecord/video/";
    public static final String LOGIN_TIME_OVER  = "user_login_time_over";
    public static final String BASE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    public static final String BANGER="banger/";//业务文件路径
    public static final String DATA="data/";//业务文件路径
    public static final String INFO="info/";//业务文件路径
    public static final String OFFLINE="offline/";//离线文件路径
    public static final String ONLINE="online/";//离线文件路径
    public static final String FILE="file/";//业务文件路径
    public static final String XML="xml/";//业务文件路径
    public static final String CLASS_PATH=BASE_PATH+BANGER+DATA;//业务文件路径
    public static final String INFO_PATH=BASE_PATH+BANGER+INFO;//业务文件路径
    public static final String XML_SAVE_PATH=BASE_PATH+BANGER+DATA+XML;//xml文件文件路径
    public static final String OFFLINE_PATH=BASE_PATH+BANGER+OFFLINE;//业务文件路径
    public static final String ONLINE_XML_PATH=CLASS_PATH+ONLINE+"xml/";//业务文件路径
    public static final String OFFLINE_XML_PATH=CLASS_PATH+OFFLINE+"xml/";//业务文件路径
    public static final String ONLINE_FILE_PATH=CLASS_PATH+ONLINE+FILE;//业务文件路径
    public static final String OFFLINE_FILE_PATH=CLASS_PATH+OFFLINE+FILE;//业务文件路径
    public static final String VIDEO_SUCCESS_PATH="bangerInfo/mediaInfo/video/upsuccess";//上传成功路径
    public static final String VIDEO_UPFAIL_PATH="bangerInfo/mediaInfo/video/upfail";//视频上传失败路径
    public static final String VIDEO_PATH="bangerInfo/mediaInfo/video/all";//本地视频路径
    public static final String IMAGE_PATH="bangerInfo/img";//本地图片路径
    public static final String VIDEO_UP_PATH="bangerInfo/mediaInfo/video/up";//上传路径
    public static final int ADD_SCHEDULE=1001;
    public static final int ADD_PRODUCT=1002;
    public static final int ADD_CUSTOMER=1003;
    public static final int ADD_PHOTO=1004;
    public static final int ADD_VIDEO=1005;
    public static final int DISPLAY_VIDEO=1006;
    public static final int GET_XML=1007;

    public static final String USER_NAME="USER_NAME";//用户名
    public static final String USER_PWD="USER_PWD";//密码
    public static final String LOGIN_IP="LOGIN_IP";//IP
    public static final String LOGIN_PORT="LOGIN_PORT";//端口
    public static final String LOGIN_NODE="LOGIN_NODE";//节点
    public static final String LOGIN_STATE="LOGIN_STATE";//登录状态，0为在线 1为离线

    public static final String STORAGE_PHONE="STORAGE_PHONE";//保存在手机位置
    public static final String STORAGE_OUTPATH="STORAGE_OUTPATH";//保存在手机位置
    public static final String[] HOME_TAB_ITEM_TEXTVIEW_ARRAY = new String [] {"记录","","我的"};
    public static final String[] HOME_TAB_ITEM_TAG_ARRAY = new String [] {"videoRecord","addVideo","userInfo"};
    public static final int[] HOME_TAB_ITEM_IMAGEVIEW_ARRAY = {R.drawable.icon_record_choose,R.drawable.ic_tab_artists,R.drawable.icon_my_choose};
    public static final Class[] HOME_TAB_ITEM_CLASS_ARRAY = {VideoRecordsActivity_.class,AddVideoActivity_.class,UserInfoActivity_.class};
    public static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final int RG_REQUEST =100;
    public static final String TABVIDEOACTIVITYTYPE = "tabvideoactivity_type";
    public static final String TABVIDEORECEIVEBROADCAST = "com.banger.action.tabvideoactivity";
    public static final String FRAGMENT_DIALOG = "dialog";
    public static final String UP_BROADCAST = "com.banger.action.cutclip";//上传广播
    public static final String UP_SUCCESS_BROADCAST = "com.banger.action.upSuccess";//上传成功广播
    public static final String ADD_BROADCAST = "com.banger.action.add";//上传广播
    public static final String OFF_DOWNLOAD_BROADCAST = "com.banger.action.offdownload";//离线下载广播
    public static final String OFF_SEARCH = "com.banger.action.search";//离线搜索
    public static final String RESETURI  = "reset_uri";
    public static final String RESETLOGIN  = "reset_login";
    public static final String Business_Info  = "businessInfo";
    //视频权限
    public static final String[] CAMERA_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    //读写文件权限
    public static final String[] RECORD_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    public static final String[] PHOTO_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    public static final String[] FILE_PERMISSIONS = {
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;
    public static final int REQUEST_CODE_SOME_FILE_PERMISSIONS = 3;
    public static final String [] ID_TYPE={"身份证", "台湾居民来往大陆通行证", "港澳居民来往内地通行证(香港)", "港澳居民来往内地通行证(澳门)"
            , "临时身份证", "护照", "户口簿", "军人身份证"
            , "武装警察身份证", "外交人员身份证", "外国人居留许可证", "边民出入境通行证", "其他"};
}
