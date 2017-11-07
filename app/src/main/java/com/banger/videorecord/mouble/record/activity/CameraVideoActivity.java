package com.banger.videorecord.mouble.record.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.DataBase;
import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.helper.AppException;
import com.banger.videorecord.helper.UiHelper;
import com.banger.videorecord.mouble.record.bean.DisplayImgEntity;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.ImageListInfo;
import com.banger.videorecord.mouble.record.bean.ProcessInfo;
import com.banger.videorecord.mouble.record.bean.VideoEntity;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.business.CameraBus;
import com.banger.videorecord.mouble.record.business.ReturnMediaCallback;
import com.banger.videorecord.mouble.record.util.CameraOperate;
import com.banger.videorecord.mouble.record.util.DisplayUtil;
import com.banger.videorecord.mouble.record.widget.MyCircleImageView;
import com.banger.videorecord.util.DateUtil;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.ToastUtil;
import com.banger.videorecord.util.VideoUtils;
import com.banger.videorecord.widget.TestWheelDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/5/30.
 * 录像照片
 */
public class CameraVideoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "CameraVideoActivity";
    private SurfaceView my_surfaceview;//显示区域
    private ImageButton takeBtn;//拍摄
    private ImageButton takeBtnVideo;//录像
    private TextView changeFlashModeIV;//转换视频录制分辨率
    private ImageView swichCameraIV;//翻转摄像头
    private MyCircleImageView pre_view;//照片预览
    private ImageView img_play;//表示视频的图标
    private ImageView img_display;//照片预览
    private FrameLayout layout_photo;
    private int cameraState;//当前相机状态，1是录像 2拍照
    private int round;//拍照环节，1代表环节1 2代表环节2
    private int width = 320;//视频录制宽默认
    private int high = 240;//视频录制高默认
    private CameraOperate cameraOperate;//摄像头对象
    private String imgName;//传递过来的图片名称
    private String videoName;//传递过来的视频名称
    private String bizNo;      //传递过来的业务名称
    //图片存储文件
    private File imgFile;
    //视频存储文件
    private File videoFile;
    //保存按钮
    private TextView txt_done;
    //取消按钮
    private TextView txt_delete;
    //之前的title
    private TextView activity_title;
    //之前的title
    private TextView txt_record_time;
    //返回按钮
    private ImageView img_go_back;
    //预览界面
    private RelativeLayout layout_display;
    //全局变量
    private AppContext appContext;
    //  录像开始时间和结束时间
    private String startTime, endTime;
    //传递过来的实体类，用于xml生成，暂时不用
    private VideoInfoBean videoInfoBean;
    private List<DataBase> list;
    private boolean isRecording = false;
    private boolean isDisplay = false;
    private boolean isSave = false;
    float previewRate = -1f;
    private int time = 0;
    private String playerPath = "";
    private int type = 1;
    private String[] planets = new String[]{"省流", "标清", "高清"};
    private boolean isPhoto = false;
    private ArrayList<ImageInfoBean> imgLists = new ArrayList<>();
    private ProcessInfo processInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_video);
        Window window = CameraVideoActivity.this.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }
        appContext = (AppContext) getApplication();
//        Toast.makeText(CameraVideoActivity.this, "手机型号:" + android.os.Build.MODEL +
//                        ",SDK版本:" + android.os.Build.VERSION.SDK +
//                        ",系统版本:" + android.os.Build.VERSION.RELEASE, Toast.LENGTH_LONG
//        ).show();
        //sdk版本21为Android5.0
//         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                    // your code using Camera API here - is between 1-20
//                 } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                     // your code using Camera2 API here - is api 21 or higher
//                 };
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initData();
        initViews();

    }

    private void initViews() {
        //如果是大于23的系统这边需要手动开启权限
        cameraState = getIntent().getIntExtra("state", 2);
        Log.e(TAG, "initViews: cameraState is--> " + cameraState);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] pre = null;
            switch (cameraState) {
                case 1:
                    pre = Constants.RECORD_PERMISSIONS;
                    break;

                case 2:
                    pre = Constants.PHOTO_PERMISSIONS;
                    break;
            }
//                     // your code using Camera2 API here - is api 21 or higher
            boolean state = CameraBus.newInstance().hasPermissionsGranted(pre, CameraVideoActivity.this);
            if (!state) {
                requestPermissions(pre, Constants.REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
                return;
            }
        }

        videoInfoBean = (VideoInfoBean) getIntent().getSerializableExtra("videoBean");
        processInfo= (ProcessInfo) getIntent().getSerializableExtra("process");
        round = getIntent().getIntExtra("round", 1);
        imgName = "img"+System.currentTimeMillis();
        videoName = "video"+System.currentTimeMillis();
        bizNo = getIntent().getStringExtra("bizNo");
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        initFilePath();
        my_surfaceview = (SurfaceView) findViewById(R.id.my_surfaceview);
        takeBtn = (ImageButton) findViewById(R.id.take_btn);
        txt_record_time = (TextView) findViewById(R.id.txt_record_time);
        takeBtnVideo = (ImageButton) findViewById(R.id.take_video_btn);
        swichCameraIV = (ImageView) findViewById(R.id.swich_camera_iv);
        changeFlashModeIV = (TextView) findViewById(R.id.flash_iv);
        layout_photo = (FrameLayout) findViewById(R.id.layout_photo);
        img_display = (ImageView) findViewById(R.id.img_display);
        pre_view = (MyCircleImageView) findViewById(R.id.pre_view);
        txt_done = (TextView) findViewById(R.id.txt_done);
        txt_delete = (TextView) findViewById(R.id.txt_delete);
        img_play = (ImageView) findViewById(R.id.img_play);
        img_go_back = (ImageView) findViewById(R.id.img_go_back);
        img_go_back.setVisibility(View.VISIBLE);
        activity_title = (TextView) findViewById(R.id.activity_title);
        layout_display = (RelativeLayout) findViewById(R.id.layout_display);
//        timeTextView= (TextView) findViewById(R.id.camera_time);
        takeBtn.setOnClickListener(this);
        takeBtnVideo.setOnClickListener(this);
        swichCameraIV.setOnClickListener(this);
        changeFlashModeIV.setOnClickListener(this);
        pre_view.setOnClickListener(this);
        txt_done.setOnClickListener(this);
        txt_delete.setOnClickListener(this);
        img_go_back.setOnClickListener(this);
        img_display.setOnClickListener(this);

        type = SharedPrefsUtil.getInstance().getIntValue(appContext, "videoType", 1);
        initRadio(type);
//       初始化照相机
        initCamera(type);
//        cameraOperate.initCamera(CameraVideoActivity.this);
//        cameraOperate.initCamera();
        initCameraView();

    }

    private void initCamera(int type) {
        cameraOperate = new CameraOperate(type,previewRate, my_surfaceview, videoFile, imgFile, new ReturnMediaCallback() {
            //           照片生成的文件进行处理
            @Override
            public void returnImg(String path) {
                saveImageToDb();
                imgName = "img" + System.currentTimeMillis();
                initFilePath();
                takeBtn.setEnabled(true);
                pre_view.setVisibility(View.VISIBLE);
                try {
                    appContext.getImageLoader().displayImage("file://" + path, pre_view, appContext.getMemoryOptions());
                } catch (AppException e) {
                    e.printStackTrace();
                }
//                changeState();
//                try {
//                    appContext.getImageLoader().displayImage("file://" + path, img_display);
//                } catch (AppException e) {
//                    e.printStackTrace();
//                }
            }

            //          生成视频文件回调处理
            @Override
            public void returnVideo(String path) {
                isSave = true;
                isRecording = false;
                playerPath = path;
                endTime = DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss");
                time = 0;
                handler.removeCallbacks(runnable);
                txt_record_time.setText("");
                changeState();
                Bitmap bitmap = VideoUtils.getVideoThumb(path);
                playerPath = path;
//                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
//                img_display.setImageBitmap(bitmap);
                img_display.setImageBitmap(bitmap);
            }
        });
    }

    //初始化文件路径
    private void initFilePath() {
        if (cameraState == 2) {
            if (FileUtils.isSdState()) {
                if (appContext.getLoginState() == 0) {
                    File file = new File(Constants.ONLINE_FILE_PATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    imgFile = new File(file, imgName + ".jpg");
                } else {
                    File file = new File(Constants.OFFLINE_FILE_PATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    imgFile = new File(file, imgName + ".jpg");
                }

            }
        } else {

            if (appContext.getLoginState() == 0) {
                File vFile = new File(Constants.ONLINE_FILE_PATH);
                if (!vFile.exists()) {
                    vFile.mkdirs();
                }
                videoFile = new File(vFile, videoName + ".avi");
            } else {
                File vFile = new File(Constants.OFFLINE_FILE_PATH);
                if (!vFile.exists()) {
                    vFile.mkdirs();
                }
                videoFile = new File(vFile, videoName + ".avi");
            }
        }
    }


    private void saveImageToDb() {
        //保存图片到数据库
        ImageInfoBean imgBean = new ImageInfoBean();
        imgBean.setState(1);
        imgBean.setProcess(round);
        imgBean.setBizNo(bizNo);
        imgBean.setFileName(imgName + ".jpg");
        imgBean.setFilePath(imgFile.getAbsolutePath());
        if(processInfo!=null){
            System.out.println("zzzz go on");
            imgBean.setProcessId(processInfo.getProcessId());
            imgBean.setProcessName(processInfo.getProcessName());
        }
        imgBean.saveThrows();
        imgLists.add(imgBean);

    }

    /**
     * 提示确认退出
     */
    private void showDialogPermissions(String content, final DialogCallBack dialogCallBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraVideoActivity.this);
        builder.setMessage(content);
//        builder.setTitle("提示");
        builder.setPositiveButton("重新获取", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogCallBack.confirmClick(dialog, which);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogCallBack.cancelClick(dialog, which);
            }
        });
        builder.create().show();
    }

    interface DialogCallBack {
        void confirmClick(DialogInterface dialog, int which);

        void cancelClick(DialogInterface dialog, int which);
    }

    //初始化是拍照还是录像
    private void initCameraView() {
        if (cameraState == 2) {
            changeFlashModeIV.setVisibility(View.GONE);
            takeBtn.setVisibility(View.VISIBLE);
            takeBtnVideo.setVisibility(View.GONE);
            activity_title.setText("环节");
        } else {
            changeFlashModeIV.setVisibility(View.VISIBLE);
            activity_title.setText("环节");
            takeBtn.setVisibility(View.GONE);
            takeBtnVideo.setVisibility(View.VISIBLE);
        }
    }

    private void initRadio(int type) {
        if (type == 0) {
            width = 320;
            high = 240;
            changeFlashModeIV.setText("省流");
        }
        if (type == 1) {
            width = 640;
            high = 480;
            changeFlashModeIV.setText("标清");
        }
        if (type == 2) {
            width = 1280;
            high = 720;
            changeFlashModeIV.setText("高清");
        }
    }

    //拍照完成到预览，部分控件隐藏，部分控件显示
    private void changeState() {
        layout_display.setVisibility(View.VISIBLE);
        img_display.setVisibility(View.VISIBLE);
        img_display.setVisibility(View.VISIBLE);
        txt_done.setVisibility(View.VISIBLE);
        txt_delete.setVisibility(View.VISIBLE);

        layout_photo.setVisibility(View.GONE);
        takeBtn.setVisibility(View.GONE);
        takeBtnVideo.setVisibility(View.GONE);
        swichCameraIV.setVisibility(View.GONE);
        changeFlashModeIV.setVisibility(View.GONE);
        if (cameraState == 1) {
            img_play.setVisibility(View.VISIBLE);
        }
        img_go_back.setVisibility(View.VISIBLE);
        activity_title.setText("预览");
        takeBtn.setEnabled(true);
    }

    //预览点击取消到重新拍照，部分控件隐藏，部分控件显示
    private void changeStateToPhoto() {
        layout_display.setVisibility(View.GONE);
        img_display.setVisibility(View.GONE);
        txt_done.setVisibility(View.GONE);
        txt_delete.setVisibility(View.GONE);

        layout_photo.setVisibility(View.VISIBLE);

        swichCameraIV.setVisibility(View.VISIBLE);
        changeFlashModeIV.setVisibility(View.VISIBLE);
        if (cameraState == 1) {
            img_play.setVisibility(View.GONE);
            takeBtnVideo.setVisibility(View.VISIBLE);
            takeBtnVideo.setSelected(false);
            isSave = false;
        } else {
            takeBtn.setVisibility(View.VISIBLE);
        }

        cameraOperate.releaseCamera();
        initCameraView();

    }

    void initData() {
        list = new ArrayList<>();
        DataBase dataBase = new DataBase();
        dataBase.setValue("省流");
        list.add(dataBase);
        dataBase = new DataBase();
        dataBase.setValue("标清");
        list.add(dataBase);
        dataBase = new DataBase();
        dataBase.setValue("高清");
        list.add(dataBase);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_btn://开始拍照
                cameraOperate.pre_view = pre_view;
                cameraOperate.takePhoto(imgFile.getAbsolutePath());
                takeBtn.setEnabled(false);
                break;
            case R.id.take_video_btn://开始录像
                if (!takeBtnVideo.isSelected()) {
                    startTime = DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss");
                    isRecording = true;
                    txt_record_time.setVisibility(View.VISIBLE);
                    handler.postDelayed(runnable, 1000);
                    changeFlashModeIV.setVisibility(View.GONE);
                } else {
                    int recTime = (int) DateUtil.getDistanceTimes(startTime, DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
                    if (recTime < 5) {
                        ToastUtil.showShortToast(CameraVideoActivity.this, "录像时间不能小于五秒");
                        return;
                    }
                }
                swichCameraIV.setVisibility(View.GONE);
                cameraOperate.startVideo(width, high, CameraVideoActivity.this);
                takeBtnVideo.setSelected(true);
                break;
            case R.id.swich_camera_iv://转换摄像头
                cameraOperate.changeCamera();
                break;
            case R.id.flash_iv://转换录制分辨率
                UiHelper.getInstance().showDialog("取消", "确定", this, planets, type, new com.banger.videorecord.widget.WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        super.onSelected(selectedIndex, item);
//                Log.e(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                type = selectedIndex;
                    }
                }, new UiHelper.OnCustomListener() {
                    @Override
                    public void confirm(com.banger.videorecord.widget.WheelView.OnWheelViewListener onWheelViewListener, AlertDialog dialog) {
                        dialog.dismiss();
                        //不为0 也就是滑动了。没滑动无需设置
                        if (onWheelViewListener.selectedIndex != 0) {
                            type = onWheelViewListener.selectedIndex - 1;
                            changeFlashModeIV.setText(planets[type]);
                            changeVideoSize(type);
                        }
                    }

                    @Override
                    public void cancle(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.pre_view://点击看大图Ca
                Intent intent = new Intent(CameraVideoActivity.this, PictureViewerActivity_.class);
                DisplayImgEntity displayImgEntity = new DisplayImgEntity();
                displayImgEntity.setImgList(imgLists);
                intent.putExtra("entity", displayImgEntity);
                intent.putExtra("Position", imgLists.size() - 1);
                startActivityForResult(intent, Constants.DISPLAY_VIDEO);
                break;
            case R.id.txt_done://保存
                intent = new Intent();
                if (cameraState == 2) {
                    isRecording = false;
                    //保存图片到数据库
                    ImageInfoBean imgBean = new ImageInfoBean();
                    imgBean.setState(1);
                    imgBean.setBizNo(bizNo);
                    imgBean.setFileName(imgName+".jpg");
                    imgBean.setFileId(imgName);
                    imgBean.setFilePath(imgFile.getAbsolutePath());
                    imgBean.saveThrows();
                    intent.putExtra("imgBean", imgBean);

                } else {
                    //保存视频到数据库
                    videoInfoBean.setRecordNo(videoName);
                    videoInfoBean.setFileId(videoName);
                    videoInfoBean.setFilePath(videoFile.getAbsolutePath());
                    videoInfoBean.setBeginTime(startTime);
                    videoInfoBean.setEndTime(endTime);
                    videoInfoBean.setFileName(videoName+".avi");
                    videoInfoBean.setFileId(videoName);
                    videoInfoBean.setFileSize(FileUtils.getFileSize(videoFile.getAbsolutePath()));
                    videoInfoBean.setRecTime((int) (DateUtil.getDistanceTimes(startTime, endTime)));
                    videoInfoBean.setLocation(0);
                    videoInfoBean.setCreateTime(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
                    videoInfoBean.save();
                    intent.putExtra("videoBean", videoInfoBean);
                }
                setResult(Constants.ADD_PHOTO, intent);
                finish();
                break;

            case R.id.txt_delete://保存
                changeStateToPhoto();
                break;
            case R.id.img_go_back://取消a
                if (isSave || isRecording) {
                    showLog();
//                ToastUtil.showShortToast(CameraVideoActivity.this,"正在录制中，请先完成录制");
                } else {
                    intent = new Intent();
                    intent.putExtra("imgList", imgLists);
                    setResult(Constants.ADD_PHOTO, intent);
                    finish();
                }
                break;
            //生成录像预览之后可以点击播放
            case R.id.img_display:
                if (cameraState == 1) {
//                    intent = new Intent(Intent.ACTION_VIEW);
                    isDisplay = true;
//                    intent.setDataAndType(Uri.fromFile(videoFile), "video/3gp");
                    intent = new Intent(CameraVideoActivity.this, VideoDisplayActivity.class);
                    intent.putExtra("filePath", playerPath);
                    intent.putExtra("process", 1);
                    intent.putExtra("isDisplay", true);
//                    intent = new Intent(CameraVideoActivity.this, VideoSurfacePlayer.class);
////                    intent.setClass(CameraVideoActivity.this,VideoSurfacePlayer.class);
//                    intent.putExtra("videoFile", playerPath);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 更改录制尺寸
     */
    private void changeVideoSize(int type) {

        if (type == 0) {
            changeFlashModeIV.setText("省流");
            width = 320;
            high = 240;
            SharedPrefsUtil.getInstance().putIntValue(this, "videoType", 0);
        } else if (type == 1) {
            changeFlashModeIV.setText("标清");
            width = 640;
            high = 480;
            SharedPrefsUtil.getInstance().putIntValue(this, "videoType", 1);
        } else {
            changeFlashModeIV.setText("高清");
            width = 1280;
            high = 720;
            SharedPrefsUtil.getInstance().putIntValue(this, "videoType", 2);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.DISPLAY_VIDEO && data != null) {
            ImageListInfo bean = (ImageListInfo) data.getSerializableExtra("img");
            Log.e("qqq", "onActivityResult: " + bean.getImgList().size() + "==" + bean.getRingNum());
            if (null != bean) {
                if (bean.getImgList() == null || bean.getImgList().size() == 0) {
                    pre_view.setVisibility(View.GONE);
                    imgLists.clear();
                } else {
                    imgLists.clear();
                    imgLists.addAll(bean.getImgList());
                    pre_view.setVisibility(View.VISIBLE);
                    try {
                        appContext.getImageLoader().displayImage("file://" + imgLists.get(imgLists.size() - 1).getFilePath(), pre_view, appContext.getMemoryOptions());
                    } catch (AppException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isDisplay) {
            isDisplay = false;
            return;
        }
        if (cameraState == 1 && isRecording) {
            cameraOperate.startVideo(width, high, CameraVideoActivity.this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                time++;
                String timeStr = DateUtil.timeToString(time);
                txt_record_time.setText("" + timeStr);
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean state = false;
        String content = "请检查";
        //REQUEST_CODE_SOME_FEATURES_PERMISSIONS

        if (requestCode == Constants.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 1 && permissions.length > 1) {
                for (int i = 0; i < permissions.length; i++) {
                    String a = permissions[i];
                    int b = grantResults[i];
                    //没有获取到权限
                    if (b == PackageManager.PERMISSION_DENIED) {
//                        if(cameraState==1){
                        state = true;
//                        }else if (cameraState==2){
//                            if (!a.equals("android.permission.RECORD_AUDIO")){
//                                state = true;
//                            }
//                        }
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.RECORD_AUDIO,
                        if (a.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (content.equals("请检查")) {
                                content = content + "写入";
                            } else {
                                content = content + ",写入";
                            }
                        } else if (a.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            if (content.equals("请检查")) {
                                content = content + "读取";
                            } else {
                                content = content + ",读取";
                            }
                        } else if (a.equals(Manifest.permission.CAMERA)) {
                            if (content.equals("请检查")) {
                                content = content + "拍照";
                            } else {
                                content = content + ",拍照";
                            }
                        } else if (a.equals(Manifest.permission.RECORD_AUDIO)) {
                            if (content.equals("请检查")) {
                                content = content + "录音";
                            } else {
                                content = content + ",录音";
                            }
                        }
                    }
                    Log.e(TAG, "onRequestPermissionsResult: --grantResults[" + i + "]--- " + b + "--permissions[" + i + "]--- " + a);
                }
                content = content + "的权限";
                if (state) {
                    prompt(content);
                } else {
                    initViews();
                    cameraOperate.aa();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(23)
    void prompt(String content) {
        showDialogPermissions(content, new DialogCallBack() {
            @Override
            public void confirmClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestPermissions(Constants.RECORD_PERMISSIONS, Constants.REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }

            @Override
            public void cancelClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }

    /**
     * 提示确认退出
     */
    private void showLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraVideoActivity.this);
        builder.setMessage("当前业务有拍摄的视频，返回将丢失这些视频，是否确认？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            if (isSave || isRecording) {
                showLog();
//                ToastUtil.showShortToast(CameraVideoActivity.this,"正在录制中，请先完成录制");
            } else {
                if (imgLists != null && imgLists.size() > 0) {
                    intent.putExtra("imgList", imgLists);
                    setResult(Constants.ADD_PHOTO, intent);
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //监听home键
    @Override
    protected void onUserLeaveHint() {//监听home键
        super.onUserLeaveHint();
//        showLog();

    }
}
