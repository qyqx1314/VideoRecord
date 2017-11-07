package com.banger.videorecord.helper;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.banger.videorecord.bean.result.LoginResult;
import com.banger.videorecord.bean.result.Version;
import com.banger.videorecord.http.inf.HttpTool;
import com.banger.videorecord.http.util.HttpConfig;
import com.banger.videorecord.http.util.RetrofitUtils;
import com.banger.videorecord.mouble.setting.activity.UserInfoActivity;
import com.banger.videorecord.mouble.setting.business.imp.HttpParams;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.util.FileUtils;
import com.banger.videorecord.util.SharedPrefsUtil;
import com.banger.videorecord.util.StringUtil;
import com.banger.videorecord.widget.MyDialog;
import com.banger.zeromq.zmq.FileUploadServer;
import com.banger.zeromq.zmq.IFileUploadServer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.androidannotations.annotations.EApplication;
import org.litepal.LitePalApplication;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhujm on 2016/5/25.
 */
@EApplication
public class AppContext extends Application {
    private static final String TAG = "AppContext";
    private static AppContext appContext = null;
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    private IFileUploadServer uploadServer;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private DisplayImageOptions previewOptions;
    private DisplayImageOptions memoryOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Log.e(TAG, "onCreate: ");
        initImageLoader();
        LitePalApplication.initialize(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        //注册crashHandler
        crashHandler.init(getApplicationContext());
        initNativePath();
//        AppException.getInstance().init(getApplicationContext());
    }

    public static AppContext getInstance() {
        return appContext;
    }

    /**
     * @return CurrentVersionName
     * @Description: 获取当前应用版本名称
     * @author Jiumin Zhu
     * @createtime: 2014年7月7日 下午2:31:16
     */
    public String getCurrentVersionName() {
        String result = "1.0.0";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            result = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return result;
    }

    /**
     * 打开浏览器
     *
     * @param activity 意图
     * @param url      路径
     */
    public void openBrowser(Activity activity, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传tcp服务初始化
     *
     * @return IFileUploadServer
     */
    public IFileUploadServer getUploadServer() {
        if (this.uploadServer == null) {
            this.uploadServer = FileUploadServer.newInstance(HttpConfig.getInstance().getTcpUrlRoot(appContext), 2);
        }
        return this.uploadServer;
    }

    /**
     * 视频文件上传tcp服务初始化
     *
     * @return IFileUploadServer
     */
    public IFileUploadServer getUploadVideoServer() {
        if (this.uploadServer == null) {
            this.uploadServer = FileUploadServer.newInstance(HttpConfig.getInstance().getTcpUpVideo(appContext), 2);
        }
        return this.uploadServer;
    }

    /**
     * 通用imageLoader初始化
     */
    protected void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(720, 1280) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())// default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for releaseapp
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }

    public DisplayImageOptions getPreviewOptions() {
        if (null == previewOptions) {
            previewOptions = new DisplayImageOptions.Builder() //
                    .considerExifParams(true) // 调整图片方向
                    .resetViewBeforeLoading(true) // 载入之前重置ImageView
//                    .showImageOnLoading(R.drawable.ic_picture_loading) // 载入时图片设置为黑色
//                    .showImageOnFail(R.drawable.ic_picture_loadfailed) // 加载失败时显示的图片
                    .delayBeforeLoading(0) // 载入之前的延迟时间
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build(); //
        }
        return previewOptions;
    }

    public DisplayImageOptions getMemoryOptions() {
        if (null == memoryOptions) {
            memoryOptions = new DisplayImageOptions.Builder()

                    .cacheInMemory(false)   //设置图片不缓存于内存中
                    .cacheOnDisc(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
                    .build();
        }
        return memoryOptions;
    }

    /**
     * imageLoader 设置
     *
     * @return DisplayImageOptions
     */
    public DisplayImageOptions getOptions() {
        if (null == options) {
            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.banner_default_d) // 设置图片下载期间显示的图片
//                    .showImageForEmptyUri(R.drawable.banner_default_d) // 设置图片Uri为空或是错误的时候显示的图片
//                    .showImageOnFail(R.drawable.banner_default_d) // 设置图片加载或解码过程中发生错误显示的图片
                    .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                    .considerExifParams(true)
                    .delayBeforeLoading(0)  // 下载前的延迟时间
                    .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
                    .cacheOnDisk(false) // default  设置下载的图片是否缓存在SD卡中
                            //.preProcessor(new BitmapProcessor(R.drawable.banner_default_d))
                            //.postProcessor(...)
                            //.extraForDownloader(...)
                    .considerExifParams(false) // default
                            //.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default 设置图片的解码类型
                            //.decodingOptions(...)  // 图片的解码设置
                    .displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                    .handler(new Handler()) // default
                    .build();
        }
        return options;
    }

    public ImageLoader getImageLoader() throws AppException {
        if (null == imageLoader) {

            imageLoader = ImageLoader.getInstance();
        }
        return imageLoader;
    }

    /**
     * @param
     * @return
     * @Description: 获取当前应用版本
     * @author Jiumin Zhu
     * @createtime: 2014年7月7日 下午2:31:16
     */

    public int getCurrentVersion() {
        int curVersionCode = 0;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            curVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return curVersionCode;
    }

    /**
     * @return
     * @Description: 获取imie号码
     * @author Jiumin Zhu
     * @createtime: 2014年7月7日 下午2:26:12
     */
    public String getIMEI() {
        String imei = "0";
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtil.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }


    /**
     * @param enabled
     * @Description: 是否打开网络连接
     * @author Jiumin Zhu
     * @createtime: 2014年7月7日 下午2:29:58
     */
    private void setMobileNetEnabled(boolean enabled) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> conMgrClass = null;
        java.lang.reflect.Field iConMgrField = null;
        Object iConMgr = null;
        Class<?> iConMgrClass = null;
        Method setMobileDataEnabledMethod = null;
        try {
            conMgrClass = Class.forName(cm.getClass().getName());
            iConMgrField = conMgrClass.getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            iConMgr = iConMgrField.get(cm);
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public boolean isMethodsCompat(int VersionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt ip
     * @return ip
     */
    public String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前ip地址
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            Log.e(TAG, " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage());
            return "00";
        }
        // return null;
    }

    /**
     * 获取机型
     *
     * @return Build.MODE
     */
    public String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取版本号
     *
     * @return Build.RELEASE
     */
    public String getRelese() {
        return Build.VERSION.RELEASE;
    }

    /**
     * device factory name, e.g: Samsung
     *
     * @return the vENDOR
     */
    public String getVendor() {
        return Build.BRAND;
    }

    /**
     * @return the SDK version
     */
    public String getSDKVersion() {
        return Build.VERSION.SDK_INT + "";
    }

    /**
     * 获取ip
     *
     * @return ip
     */
    public String getIp() {
        return SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.LOGIN_IP, "");
    }

    /**
     * 获取端口
     *
     * @return port
     */
    public static String getPort() {
        return SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.LOGIN_PORT, "");
    }

    public int getLoginState() {

        return SharedPrefsUtil.getInstance().getIntValue(this, Constants.LOGIN_STATE, 0);
    }

    public void setLoginState(int state) {

        SharedPrefsUtil.getInstance().putIntValue(this, Constants.LOGIN_STATE, state);
    }

    /**
     * 获取节点
     *
     * @return node
     */
    public static String getNode() {
        return SharedPrefsUtil.getInstance().getStringValue(appContext, Constants.LOGIN_NODE, "");
    }

    /**
     * 获取手机存储位置状态
     */
    public static String getSaveState(Context mContext, String Path) {

        SharedPrefsUtil sp = SharedPrefsUtil.getInstance();
        int location = sp.getIntValue(mContext, Constants.STORAGE_PHONE, 0);
        if (location == 0) {
            return Constants.BASE_PATH + Path;
        } else if (location == 1) {
            String outPath = sp.getStringValue(mContext, Constants.STORAGE_OUTPATH, null);
            if (outPath.length() != 0) {
                return outPath + Path;
            }
        }
        return null;
    }

    public boolean loginTimeOver(LoginResult result, boolean aotu) {
        String firstLogin = SharedPrefsUtil.getInstance().getStringValue(this, Constants.LOGIN_TIME_OVER, "");
        Log.e(TAG, "aotuis-->" + aotu + "--loginTimeOver: firstLogin is-->" + firstLogin + "--cureentLogin is --->" + result.getLoginDate());
        if (aotu) {
            if (result.getLoginDate().equals(firstLogin)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void initNativePath() {
        File file = new File(Constants.OFFLINE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
