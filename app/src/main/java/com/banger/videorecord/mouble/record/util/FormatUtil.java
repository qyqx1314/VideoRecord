package com.banger.videorecord.mouble.record.util;

import android.os.Environment;

import com.banger.videorecord.util.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {

    /**
     * 将缓存文件夹的数据转存到vedio文件下
     *
     * @param recAudioFile
     */
    public static void videoRename(File recAudioFile, String fileName) {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + Constants.VIDEO_PACKAGE;
        fileName = fileName + ".3gp";
        File out = new File(path);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(path, fileName);
        if (recAudioFile.exists())
            recAudioFile.renameTo(out);

    }

    /**
     * 用以计时操作的相关方法
     *
     * @param num
     * @return
     */
    public static String format(int num) {

        String s = num + "";
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * 将秒值转化为00:00:00格式
     */

    public static String formatIntToTimeStr(int l) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = l;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
    }

    public static String getTwoLength(final int data) {
        if(data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    public static String formatTime(String time){

        String a=time.substring(5,time.length()-3);
        String b=a.replace("-","/");

        return b;
    }
}
