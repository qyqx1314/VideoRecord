package com.banger.videorecord.util;

import java.io.*;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class ToolUtils {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 300) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1|9]\\d{10}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 验证手机格式
     */
    public static boolean isEmail(String emails) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (TextUtils.isEmpty(emails))
            return false;
        else
            return emails.matches(check);
    }

    /**
     * 验证身份证格式
     */
    public static boolean isIDCard(String idCard) {
        String check = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
        if (TextUtils.isEmpty(idCard))
            return false;
        else
            return idCard.matches(check);
    }


    public static Bitmap decodeUriAsBitmap(Uri uri, Context ctx) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 图片存文件
     *
     * @param bitmap
     * @param filePath
     * @return
     */
    public static String savePicToSdcard(Bitmap bitmap, String filePath) {
        // TODO Auto-generated method stub
        if (bitmap == null) {
            return filePath;
        } else {
            File destFile = new File(filePath);
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                bitmap.compress(CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                filePath = "";
            }
        }
        return filePath;
    }

    /**
     * 字符串替换拼接（百度搜索结果拼接）
     * 文二路大华华 ＋ 大华华邻国际
     */
    public static String addAndReplaceString(String starString, String endString) {
        String returnString = endString;
        char cStar[] = starString.toCharArray();
        char cEnd[] = endString.toCharArray();
        for (int i = cStar.length - 1; i > 0; i--) {
            //当前字符已经不包含
            if (!endString.contains(String.valueOf(cStar[i]))) {
                starString = starString.substring(0, i);
                returnString = starString + endString;
                break;
            } else {
                if (cStar[i] == cEnd[0]) {
                    starString = starString.substring(0, i);
                    returnString = starString + endString;
                    break;
                }
            }
        }

        return returnString;
    }

    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 读取bitmap
     *
     * @param path
     * @param size
     * @return
     * @throws IOException
     */
    public static Bitmap revitionImageSize(String path, int size) {

        Bitmap bitmap = null;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= size)) {
                    in = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 旋转角度
     *
     * @param bp
     * @param rotate
     * @return
     */
    public static Bitmap rotateBitMap(Bitmap bp, int rotate) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(rotate);
        Bitmap nowBp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
        if (!bp.isRecycled()) {
            bp = null;
        }
        return nowBp;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * Convert double to String without trailing 0.
     * NOTE: only work with double that doesn't longer than int
     *
     * @param d
     * @return
     */
    public static String format(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    public static String formatTwo(double d) {
        return String.format("%.2f", d);
    }

    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }
}
