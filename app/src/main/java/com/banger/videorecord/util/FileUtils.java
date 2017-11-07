package com.banger.videorecord.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.banger.videorecord.helper.AppContext;
import com.banger.videorecord.mouble.record.bean.ImageInfoBean;
import com.banger.videorecord.mouble.record.bean.LocalRecordBean;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.util.XmlUtils;

import org.androidannotations.annotations.App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qyqx on 2016/4/17.
 * 文件工具类
 */
public class FileUtils {
    private static final String TAG = "FileUtils";
    /**
     * 判断手机中是否挂载SD卡
     *
     * @return
     */
    public static boolean isSdState() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建路径
     * "/sdcard/bangerInfo/creditInfo/photos"
     *
     * @param dir 目标路径
     */
    public static void createDir(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * 得到根目录
     *
     * @return File.separator=="/",考虑跨平台
     */
    public static String getDirPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "bangerInfo/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 得到照片的目录
     *
     * @return
     */
    public static String getPhotosDirPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "bangerInfo/mediaInfo/photos";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * 得到照片的目录
     *
     * @return
     */
    public static String getPhotosNewPath(String biz, int round) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "bangerInfo/" + biz + "/photos/one";
        if (round == 2) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "bangerInfo/" + biz + "/photos/two";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 得到上传视频的目录
     *
     * @return
     */
    public static void getUpLoadDirPath(String filePath) {
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                +filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
            Log.e("aa", "getUpLoadDirPath: ===================" + "创建成功");
        }
    }


    public static int findCount(String path) {
        File file = new File(path);
        if (file.exists() && file.length() != 0) {
            File[] files = file.listFiles();
            return files.length;
        }
        return 0;
//        return FileUtils.imageList(file).size();
    }

    /**
     * 获取图片地址列表
     *
     * @param file
     * @return
     */
    public static ArrayList<String> imageList(File file) {
        ArrayList<String> list = new ArrayList<String>();

        File[] files = file.listFiles();
        for (File f : files) {
            list.add(f.getAbsolutePath());
        }
        Collections.sort(list);
        return list;
    }

    /**
     * 获取环节图片地址
     *
     * @param
     * @return
     */
    public static List<String> getImageList(String path) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (file.exists()) {
            for (File f : files) {
                if (f.getAbsolutePath().contains(".jpg")) {
                    list.add(f.getAbsolutePath());
                }
            }

        }
        return list;
    }

    /**
     * 获取环节图片地址
     *
     * @param
     * @return
     */
    public static List<String> getXmlList(String path) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (file.exists()) {
            for (File f : files) {
                if (f.getAbsolutePath().contains(".xml")) {
                    list.add(f.getAbsolutePath());
                }
            }

        }
        return list;
    }

    /**
     * 获取指定文件夹的图片URI
     */
    public static Uri queryUriforImages(File file, Context mContext) {
//        file=new File("/sdcard/bangerInfo/creditInfo/photos/no");
        final String where = MediaStore.Images.Media.DATA + "='" + "file:/" + file.getAbsolutePath() + "'";
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();
        Cursor cursor = mContentResolver.query(mImageUri, null, where, null, null);
        if (cursor == null) {
            return null;
        }
        int id = -1;
        if (cursor != null) {

            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                id = cursor.getInt(0);
            }
            cursor.close();
        }
        if (id == -1) {
            return null;
        }
        return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
    }


    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 得到录音文件的目录
     *
     * @return
     */
    public static String getRecordDirPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "bangerInfo/creditInfo/record";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 得到视频文件的目录
     *
     * @return
     */
    public static String getVideoDirPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "bangerInfo/mediaInfo/video/all";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getVideoNewPath(String biz, int round) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "bangerInfo/" + biz + "/video/one";

        if (round == 2) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "bangerInfo/" + biz + "/video/two";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static Map<String,String> getMediaVideo(String filePath) {
        File f = new File(filePath);
        File[] files = f.listFiles();// 列出所有文件
        Map<String ,String> map = new HashMap<>();
        // 将所有文件存入list中
        if (files != null) {
            int count = files.length;// 文件个数
            for (int i = 0; i < count; i++) {
                File file = files[i];
                try {
                    Log.e(TAG, "getMediaVideo: "+file.getName() );
                    map.put(file.getName(),file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
    /**
     * 判断list中路径文件是否存在
     */

    public static boolean isExitFile(List<VideoInfoBean> list){
        for(VideoInfoBean videoInfoBean:list){
            File file=new File(videoInfoBean.getFilePath());
            if(!file.exists()){
                return false;
            }
        }
        return true;
    }
    /**
     * 判断list中路径文件是否存在
     */

    public static boolean isExitFileImage(List<ImageInfoBean> list){
        for(ImageInfoBean videoInfoBean:list){
            File file=new File(videoInfoBean.getFilePath());
            if(!file.exists()){
                return false;
            }
        }
        return true;
    }
    /**
     * 判断list中保存的路径文件是否存在
     */

    public static boolean isExit(List<VideoInfoBean> list){
        int i=0;
        for(VideoInfoBean videoInfoBean:list){
            File file=new File(videoInfoBean.getFilePath());
            if(file.exists()){
                ++i;
            }
        }

        if(list.size()==i){
            return true;
        }
        return false;
    }

    public static boolean isImageExit(List<ImageInfoBean> list){
        int i=0;
        for(ImageInfoBean imageInfoBean:list){
            File file=new File(imageInfoBean.getFilePath());
            if(file.exists()){
                ++i;
            }
        }

        if(list.size()==i){
            return true;
        }
        return false;
    }

    public static void saveVideoPath(List<String> pathList,List<VideoInfoBean> list){
        for(VideoInfoBean videoInfoBean:list){
            pathList.add(videoInfoBean.getFilePath());
        }
    }

    public static void saveImagePath(List<String> pathList,List<ImageInfoBean> list){
        for(ImageInfoBean imageInfoBean:list){
            pathList.add(imageInfoBean.getFilePath());
        }
    }


    /**
     * 获取指定文件夹下所有的视频文件
     */
    public static ArrayList<LocalRecordBean> getMediaVideo(String filePath, Context mContext) {
        File f = new File(filePath);
        File[] files = f.listFiles();// 列出所有文件
        ArrayList<LocalRecordBean> datas = new ArrayList<>();
        // 将所有文件存入list中
        if (files != null) {
            int count = files.length;// 文件个数
            for (int i = 0; i < count; i++) {
                File file = files[i];
                if (file.getName().contains("txt")) {
                    LocalRecordBean localRecordBean = null;
//                    File xmlFile = new File(Constants.VIDEO_PATH + "/yim125.xml");
                    FileInputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(file);//循环解析
                        XmlUtils xmlUtils = new XmlUtils();
                        localRecordBean = xmlUtils.parse(mContext, inputStream);
                        localRecordBean.setXmlPath(file.getPath());
                        if (filePath.contains(Constants.VIDEO_UPFAIL_PATH)) {
                            localRecordBean.setUpId(4);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                videoBean.setUri("file:/" + file.getAbsolutePath());
//                    localRecordBean.setPath(file.getPath());
//                videoBean.setUserName(file.getName().substring(0, file.getName().indexOf(".")));//除去后缀名
//                    localRecordBean.setUserName(file.getName());
//                    Log.v("image", "filename = " + file.getAbsolutePath());
                    datas.add(localRecordBean);
                }
            }
        }
        return datas;
    }


    /**
     * 复制选中的视频文件
     */
    public static void copySelect(ArrayList<LocalRecordBean> isSlect, String toPath) {

        for (LocalRecordBean mVideoBean : isSlect) {
            String path = mVideoBean.getXmlPath();
//            String xmlPath=mVideoBean.getXmlPath();
            if (path != null) {
                File file = new File(path);
//                File xmlFile=new File(xmlPath);
//                Log.e("image", "filename ============ " + path);
                File file1 = new File(toPath + "/" + file.getName());
//                File xmlFile1=new File(toPath + "/" + xmlFile.getName());
//                Log.e("image", "tofilename ==========================" + toPath + "/" + file.getName());
                try {
                    if (file.exists() && file.length() != 0) {
                        copyFile(file, file1);
                    }
//                    copyFile(xmlFile, xmlFile1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 删除选中的视频文件
     */
    public static void deleteSelect(ArrayList<LocalRecordBean> isSlect, Context mContext) {

        for (LocalRecordBean mVideoBean : isSlect) {
//            String path = mVideoBean.getPath();
            String path = mVideoBean.getXmlPath();
            if (path != null) {
                File file = new File(path);
//                File xmlFile=new File(xmlPath);
                file.delete();
//                xmlFile.delete();
            }
        }
        isSlect.clear();
    }

    /**
     * 剪切视频文件到指定路径
     *
     * @param path
     */
    public static void CutClip(Context mContext, String path, ArrayList<LocalRecordBean> mIsSelect) {
        FileUtils.copySelect(mIsSelect, path);
        FileUtils.deleteSelect(mIsSelect, mContext);
//        Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 剪切单独文件到指定路径
     *
     * @param
     */
    public static void CutClipAlone(String toPath, String xmlPath) {
        File file = new File(xmlPath);
        File file1 = new File(toPath + "/" + file.getName());
        try {
            if (file.exists() && file.length() != 0) {
                FileUtils.copyFile(file, file1);
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 得到文件的绝对路径
     *
     * @param path
     * @return String
     */
    public static String getFileFullPath(String path) {
        File file = new File(path);
        return (file.isAbsolute()) ? path : file.getAbsolutePath();
    }

    /**
     * 判断文件是否存在
     *
     * @param
     * @return
     */
    public static boolean isExistFile(String file) {
        String fullPath = getFileFullPath(file);
        File f = new File(fullPath);
        return f.isFile() && f.exists();
    }

    /**
     * 本地缩略图目录
     *
     * @return
     */
    public static String getDirThumb() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "bangerInfo/icon/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * 把文件复制到另外一个地方
     *
     * @param src 源文件 包括路径
     * @param dst 目标文件 包括路径
     * @param TRANSLATE_SIZE 文件大小
     * @throws Exception
     */
    private static final int TRANSLATE_SIZE = 16 * 1024;

    public static void copyFile(File src, File dst) throws Exception {

        if (src == null || dst == null) {
            throw new IllegalArgumentException(" the file is empty");
        }

        InputStream in = null;
        OutputStream out = null;
        try {
            dst.getParentFile().mkdirs();
            if (!dst.isFile()) {
                dst.delete();
                dst.createNewFile();
            }
            in = new BufferedInputStream(new FileInputStream(src), TRANSLATE_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst), TRANSLATE_SIZE);
            byte[] buffer = new byte[TRANSLATE_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deteleDatas(File file)
            throws IOException {
        if (file.isDirectory()) {// 处理目录
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                file.delete();
            } else {
                for (File f : files) {
                    deteleDatas(f);
                }
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    /**
     * 根据文件的类型调用手机的应用
     */
    public static void openFile(File f, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }

    private static String getMIMEType(File f) {
        String end = f
                .getName()
                .substring(f.getName().lastIndexOf(".") + 1,
                        f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
                || end.equals("amr") || end.equals("mpeg")
                || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif")
                || end.equals("png") || end.equals("jpeg")) {
            type = "image";
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    /**
     * @param srcPath
     * @param type
     * @return
     * @Description: 从本地读取图片
     * @author Jiumin Zhu
     * @createtime: 2015-6-11 下午1:02:55
     */
    public static Bitmap imageFromFile(String srcPath, int type) {
        System.out.println("图片路径 is " + srcPath);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 200f;//
        float ww = 200f;//
        switch (type) {
            case 1:
                hh = 100f;//
                ww = 100f;//
                break;
            case 2:
                hh = 200f;//
                ww = 200f;//
                break;
            case 3:
                hh = 800f;//
                ww = 480f;//
        }
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    public static Bitmap matrix(Bitmap bitmap, float sx, float sy) {
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap2;
    }

    public static String getFileId(String filename) {

        int n = filename.indexOf(".");
        if (n > 0) {
            return filename.substring(0, n);
        }
        return filename;
    }

    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return file.length();
        }
        return -1;
    }

    public static void writeFile(String fullFileName, String content, boolean coverFlag) {
        writeFile(fullFileName, content, "UTF-8", coverFlag);
    }

    public static void writeFile(String fullFileName, String content, String encoding, boolean coverFlag) {
        if (!isExistFile(fullFileName)) {
            File f = new File(fullFileName);
            try {
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(fullFileName);
                OutputStreamWriter writer = new OutputStreamWriter(fos, encoding);
                writer.write(new String(content.getBytes("UTF-8"), "UTF-8"));
                writer.close();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                File f = new File(fullFileName);
                FileOutputStream fos = new FileOutputStream(f, !coverFlag);
                OutputStreamWriter writer = new OutputStreamWriter(fos, encoding);
                writer.write(content);
                writer.close();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void createDirectory(String path) {
        String fullPath = getFileFullPath(path);
        if (!isExistFilePath(fullPath)) {
            File f = new File(fullPath);
            f.mkdirs();
        }
    }

    /**
     * 写入文件内容
     *
     * @param fullFileName 文件全名称
     * @param content      文件内容
     * @param encoding     文件编码
     */
    public static void writeFile(String fullFileName, String content, String encoding) {
        writeFile(fullFileName, content, encoding, true);
    }

    public static boolean isExistFilePath(String path) {
        String filePath = getFileFullPath(path);
        File f = new File(filePath);
        return f.isDirectory() && f.exists();
    }

    public static String ReadTxtFile(String strFilePath) {
//        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(strFilePath);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (!file.exists()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                FileInputStream instream = new FileInputStream(file);
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line;
                }
                instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }
    public static String changeLongToSize(long size){
        if(size>0&&size<1024){
            return size+"B";
        }else if(size>=1024&&size<1024*1024){
            return String.format("%.2f",  ((double)size / 1024)) + "K";
        }else if(size>=1024*1024){
            return String.format("%.2f", (double) (size / 1024) / 1024) + "M";
        }
        return "";
    }
    public static boolean isExists(String path){
        File file=new File(path);
        if(!file.exists()){
            return false;
        }
        return true;
    }
}