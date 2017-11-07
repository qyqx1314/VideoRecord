package com.banger.videorecord.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchaowen on 2016/6/15.
 * 读取手机内存工具类
 */
public class StorageUtils {

    private StorageManager mStorageManager;
    private Method mMethodGetPaths;

    private static com.banger.videorecord.util.StorageUtils StorageUtils = null;

    protected StorageUtils() {

    }

    public static synchronized com.banger.videorecord.util.StorageUtils getInstance() {
        if (StorageUtils == null) {
            StorageUtils = new com.banger.videorecord.util.StorageUtils();
        }
        return StorageUtils;
    }


    private static final String ERROR = "0";


    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
    /**
     * 获取内置SD卡路径
     * @return
     */
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取手机内存地址，包括外带拓展卡
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
    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }
    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }
    /**
     * 获取机身已占用内存
     */
    public static String getRomUsedSize(Context context){
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();//总
        long availableBlocks = stat.getAvailableBlocksLong();//剩
        return Formatter.formatFileSize(context, blockSize * (totalBlocks-availableBlocks));
    }

    /**
     * 获取机身已占用内存的百分比
     */

    public static int getRomUsedPrecent(){
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();//总
        long availableBlocks = stat.getAvailableBlocksLong();//剩
        return (int) ((totalBlocks-availableBlocks)*100/totalBlocks);

    }



    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     *
     * @return
     */
    public static String getTotalExternalMemorySize(Context context) {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return Formatter.formatFileSize(context,totalBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    /**
     * 获取外置TF卡的路径
     * @return
     */
    public static List<String> getExtSDCardPaths() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                paths.add(mountPath);
                Log.e("SD", "getExtSDCardPaths:=============> "+paths.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }


    /**
     * 根据路径获取内存状态
     * @param path
     * @return
     */
    public static  String getMemoryInfo(File path,Context context,int state) {
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSizeLong();   // 获得一个扇区的大小

        long totalBlocks = stat.getBlockCountLong();    // 获得扇区的总数

        long availableBlocks = stat.getAvailableBlocksLong();   // 获得可用的扇区数量

        // 总空间
        String totalMemory = Formatter.formatFileSize(context, totalBlocks * blockSize);
        String havenMemory = Formatter.formatFileSize(context, (totalBlocks - availableBlocks) * blockSize);
        // 可用空间
        String availableMemory = Formatter.formatFileSize(context, availableBlocks * blockSize);
        String teturn="";
        if (state == 1) {
            teturn=havenMemory;
        } else if (state == 2) {
            teturn= availableMemory;
        }else if(state == 3){
            teturn=""+(totalBlocks - availableBlocks)*100/totalBlocks;
        }
        return  teturn;
    }


}
