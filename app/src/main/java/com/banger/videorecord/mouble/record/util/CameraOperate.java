package com.banger.videorecord.mouble.record.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import com.banger.videorecord.mouble.record.business.ReturnMediaCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.hardware.Camera.Size;

/**
 * Created by Xuchaowen on 2016/5/27.
 * 录像及照相操作
 */
public class CameraOperate {
    private static final String TAG = "CameraOperate";
    private Camera camera;
    private boolean isRecording = true; // true表示没有录像，点击开始；false表示正在录像，点击暂停
    private SurfaceHolder surfaceHolder;
    private SurfaceView my_surfaceview;
    private MediaRecorder mediaRecorder;//录音
    private Bitmap bitmap;
    public Bitmap resizeBmp;
    public ImageView pre_view;
    private ReturnMediaCallback callback;
    private File videoFile;//自定义设置的视频文件目录
    private File myVideoFile;//自定义设置的视频文件目录
    private File imgFile;
    private Camera.CameraInfo cameraInfo = null;
    float previewRate = -1f;
    private int width = 1280;//视频录制宽默认
    private int high = 720;//视频录制高默认

    public CameraOperate(int type ,float previewRate, SurfaceView my_surfaceview, File videoFile, File imgFile, ReturnMediaCallback returnMediaCallback) {
        this.my_surfaceview = my_surfaceview;
        surfaceHolder = my_surfaceview.getHolder();
        this.callback = returnMediaCallback;
        this.myVideoFile = videoFile;
        this.imgFile = imgFile;
        this.previewRate = previewRate;
        switch (type){
            case 0:
                this.width = 320;
                this.high = 240;
                break;
            case 1:
                this.width = 640;
                this.high = 480;
                break;
            case 2:
                this.width = 1280;
                this.high = 720;
                break;
        }
        Log.e(TAG, "CameraOperate:全屏比例 " + previewRate);
        initCamera();
    }


    /**
     * 初始化摄像头
     */
    public void initCamera() {
        my_surfaceview.getHolder().setFixedSize(my_surfaceview.getWidth(),
                my_surfaceview.getHeight());//预览区域
        // 设置SurfaceHolder对象的类型，拍照
        my_surfaceview.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceCallBackImp());
        //开发时建议设置
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void aa() {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            //设置Camera的角度/方向
            camera.setDisplayOrientation(90);
            camera.setParameters(getParameters(90));
            camera.setParameters(sizeChanged());
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.cancelAutoFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 控件回调
     */
    private class SurfaceCallBackImp implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                // 开启相机
                if (camera == null) {
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    //设置Camera的角度/方向
                    camera.setDisplayOrientation(90);
                    camera.setParameters(getParameters(90));
                    camera.setParameters(sizeChanged());
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    camera.cancelAutoFocus();//自动对焦，这一句必须加上
                    camera.setPreviewDisplay(holder);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            surfaceHolder = holder;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.e(TAG, "surfaceChanged: ");
            surfaceHolder = holder;
            // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览
            camera.setParameters(sizeChanged());
            camera.startPreview(); // 开启预览
            //实现自动对焦
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        Log.e(TAG, "onAutoFocus: ");
                        camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                    }
                }

            });
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG, "surfaceDestroyed: ");
            stopCamera();
            surfaceHolder = null;
            mediaRecorder = null;
        }

    }


    public void releaseCamera() {
        if (cameraInfo == null) {
            preChangeView(0);
        } else {
            preChangeView(cameraInfo.facing);
        }
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
        // 取能适用的最大的SIZE
        Camera.Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Camera.Size s : sizes) {
            if (s.width == width && s.height == high) {
                Log.e(TAG, "getBestSupportedSize: 设置的哦--" + s.width + "-------" + s.height);
                return s;
            } else {
                int area = s.width * s.height;
                Log.e(TAG, "getBestSupportedSize: " + s.width + "-------" + s.height);
                if (area > largestArea) {
                    largestArea = area;
                    largestSize = s;
                }
            }
        }
        return largestSize;
    }

    private Camera.Parameters getParameters(int rotation) {
        Camera.Parameters parameters = camera.getParameters();// 获取mCamera的参数对象
//              parameters.setPreviewFrameRate(10); // 每秒5帧.默认30帧
        parameters.setPictureFormat(ImageFormat.JPEG);// 设置照片的输出格式
        parameters.set("jpeg-quality", 100);// 照片质量
//                    // 设置拍摄照片的实际分辨率
//                    parameters.setPictureSize(display.getWidth() / 2,
//                            display.getHeight() / 2);
        parameters.setRotation(rotation);
        return parameters;
    }

    Camera.Parameters sizeChanged() {
        Camera.Parameters mParams = camera.getParameters();// 获取mCamera的参数对象
        Camera.Size largestSize = getBestSupportedSize(mParams
                .getSupportedPreviewSizes());
        Log.e(TAG, "sizeChanged: --" + largestSize.width + "------" + largestSize.height);
        mParams.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
//        largestSize = getBestSupportedSize(mParams
//                .getSupportedPictureSizes());// 设置捕捉图片尺寸
        mParams.setPictureSize(largestSize.width, largestSize.height);
        mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        List<String> list = mParams.getSupportedFocusModes();
        for (String a : list) {
            Log.e(TAG, "getSupportedFocusModes: " + a);
        }
        if (list.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            List<String> focusModes = mParams.getSupportedFocusModes();
            for (String a : focusModes) {
                Log.e(TAG, "focusModes --getSupportedFocusModes: " + a);
            }
        }
        return mParams;
    }

    /**
     * 照相
     */
    public void takePhoto(String filePath) {
        imgFile = new File(filePath);
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.autoFocus(null);
            camera.setParameters(parameters);
            //已经自动对焦
            camera.takePicture(new ShutCallBackImpl(), null, new PicCallBacKImpl()); // 拍照
        }
    }

    /**
     * 拍照后的最主要的返回
     */
    private class PicCallBacKImpl implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //拍摄时
            bitmap = BitmapFactory.decodeByteArray(data, 0,
                    data.length);
            resizeBmp = ThumbnailUtils.extractThumbnail(bitmap, 400, 400, MediaStore.Images.Thumbnails.MICRO_KIND);//预览设置宽高
            try {
                FileOutputStream fos = new FileOutputStream(imgFile.getPath());
                fos.write(data);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.returnImg(imgFile.getAbsolutePath());
            pre_view.setImageBitmap(resizeBmp);
            camera.startPreview();
        }
    }

    /**
     * 拍照时的动作
     * 默认会有咔嚓一声
     */
    private class ShutCallBackImpl implements Camera.ShutterCallback {
        @Override
        public void onShutter() {
        }
    }


    /**
     * 切换前后摄像头
     */
    public void changeCamera() {
        //切换前后摄像头
        if (null == cameraInfo) {
            cameraInfo = new Camera.CameraInfo();
        }
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        if (cameraCount == 2) {
            switch (cameraInfo.facing) {
                case Camera.CameraInfo.CAMERA_FACING_FRONT:
                    cameraInfo.facing = 0;
                    preChangeView(Camera.CameraInfo.CAMERA_FACING_BACK);
                    break;
                case Camera.CameraInfo.CAMERA_FACING_BACK:
                    cameraInfo.facing = 1;
                    preChangeView(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    break;
            }
        }
    }

    /**
     * 切换时摄像头准备
     */
    public void preChangeView(int cameraId) {
        stopCamera();
        camera = Camera.open(cameraId);//打开当前选中的摄像头
        try {
            camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (cameraId) {
            case Camera.CameraInfo.CAMERA_FACING_FRONT:
                camera.setParameters(getParameters(270));
                break;
            case Camera.CameraInfo.CAMERA_FACING_BACK:
                camera.setParameters(getParameters(90));
                break;
        }
        camera.startPreview();//开始预览
        camera.setDisplayOrientation(90);
    }

    /**
     * 停止预览，释放Camera
     */
    public void stopCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null; // 记得释放Camera
        }
    }

    /**
     * 开始录像
     */
    public void startVideo(int width, int high, Context context) {
        this.width = width;
        this.high = high;
        videoFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/VideoRecord/video/temp/");//缓存
        if (!videoFile.exists()) {
            videoFile.mkdirs();
        }
        if (isRecording) {
            stopCamera();
            if (null == mediaRecorder) {
                mediaRecorder = new MediaRecorder();
            } else {
                mediaRecorder.reset();
            }

            if (null == cameraInfo) {
                cameraInfo = new Camera.CameraInfo();
                cameraInfo.facing = 0;
            }
            if (cameraInfo.facing == 0) {//后置
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                camera.setParameters(sizeChanged());
                camera.setDisplayOrientation(90);//视频旋转90度
                camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
                camera.startPreview(); // 开启预览
                mediaRecorder.setOrientationHint(90);
            } else {//前置
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//打开摄像头
                camera.setParameters(sizeChanged());
                camera.setDisplayOrientation(90);
                camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                mediaRecorder.setOrientationHint(270);
                camera.startPreview(); // 开启预览
            }
            mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    Log.e(TAG, "onInfo: " + mr.toString() + "what is -->" + what + "extra is -->" + extra);
                }
            });
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    Log.e(TAG, "onError: " + mr.toString() + "what is -->" + what + "extra is -->" + extra);
                }
            });
            try {
                camera.unlock();
//                camera.cancelAutoFocus();
                mediaRecorder.setCamera(camera);
                mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());// 预览
                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 录音源为麦克风
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 输出格式为3gp
                mediaRecorder.setVideoSize(width, high);// 视频尺寸
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 视频编码
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 音频编码
                mediaRecorder.setMaxDuration(3000000);
                switch (high){
                    case 240:
                        mediaRecorder.setVideoEncodingBitRate(384000);
                        Log.e(TAG, "startVideo: "+(384000));
                        break;
                    case 480:
                        mediaRecorder.setVideoEncodingBitRate(384000*2);
                        Log.e(TAG, "startVideo: "+(384000*2));
                        break;

                    case 720:
                        mediaRecorder.setVideoEncodingBitRate(384000*3);
                        Log.e(TAG, "startVideo: "+(384000*3));
                        break;
                }
                mediaRecorder.setOutputFile(myVideoFile.getAbsolutePath());
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (Exception e) {
                Log.e(TAG, "startVideo: " + e.getMessage());
                e.printStackTrace();
            }
            isRecording = !isRecording;
            Toast.makeText(context, "开始录制", Toast.LENGTH_SHORT).show();
        } else {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            callback.returnVideo(myVideoFile.getAbsolutePath());
            Toast.makeText(context, "录制完成，已保存", Toast.LENGTH_SHORT).show();
            isRecording = !isRecording;
            try {
                camera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
