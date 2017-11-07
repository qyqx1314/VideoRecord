package com.banger.videorecord.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	public static String fillzero(long num, int len) {
		String formater = "";
		for (int i = 0; i < len; i++) {
			formater += "0";
		}
		DecimalFormat df = new DecimalFormat(formater);
		return df.format(num);
	}

	public static int getWindowWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getWindowHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 *
	 *
	 * @Title: flowDoubleToString
	 * @Description: 剩余两位浮点小数。
	 * @param @param used
	 * @param @param total
	 * @param @return 
	 * @return
	 * @throws
	 */
	public static String flowDoubleToString(double used, double total) {
		double f = (total - used) / 1024.0;
		DecimalFormat valueDF = new DecimalFormat("##0.00");
		return valueDF.format(f);
	}

	// TODO 无小数点字符串
	public static String doubleToString(double v) {
		DecimalFormat valueDF = new DecimalFormat("##0");
		return valueDF.format(v);
	}

	// TODO 1位小数点字符串
	public static String doubleToStringOne(double v) {
		DecimalFormat valueDF = new DecimalFormat("##0.0");
		return valueDF.format(v);
	}

	// TODO 2位小数点字符串
	public static String doubleToStringTwo(double v) {
		DecimalFormat valueDF = new DecimalFormat("##0.00");
		return valueDF.format(v);
	}

	public static String getUrlFileName(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		return fileName;
	}


	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 *
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否邮箱
	 *
	 * @Title: isEmail
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param email
	 * @param @return 设定文件
	 * @return 返回类型
	 * @throws
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}


	/**
	 *
	 * @Title: toInt
	 * @Description: 将字符串转型为int
	 * @param @param str 转型的数据
	 * @param @param defValue 默认数据
	 * @param @return 
	 * @return
	 * @throws
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 *
	 * @Title: toInt
	 * @Description: 将object 类型数据转型为int
	 * @param @param obj
	 * @param @return 默认为0
	 * @return
	 * @throws
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 *
	 * @Title: toLong
	 * @Description: 将String类型转型为 long
	 * @param @param obj
	 * @param @return 默认为0
	 * @return
	 * @throws
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 *
	 * @Title: toBool
	 * @Description: 将string 类型转型为 boolen 类型
	 * @param @param b
	 * @param @return 默认为false
	 * @return 返回类型
	 * @throws
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/***
	 *
	 *
	 * @Title: toDouble
	 * @Description: 字符串转 double
	 * @param @param b
	 * @param @return 默认为0.0
	 * @return
	 * @throws
	 */
	public static double toDouble(String b) {
		try {
			return Double.parseDouble(b);
		} catch (Exception e) {

		}
		return 0.0;
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}



	/**
	 *
	 * @Description: 四舍五入保留一位小数
	 * @author Jiumin Zhu
	 * @createtime: 2015-7-8 上午10:15:51
	 * @param flow
	 * @return
	 *
	 */
	public static double formatDouble(double flow) {
		BigDecimal bdm = new BigDecimal(flow);
		double flows = bdm.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		return flows;
	}

	/**
	 *
	 * @Description:  四舍五入取整数
	 * @author Jiumin Zhu
	 * @createtime: 2015-7-8 上午10:16:35
	 * @param flow
	 * @return
	 *
	 */
	public static int formatInt(double flow) {
		int flows = 0;
		try {
			BigDecimal bdm = new BigDecimal(flow);
			flows = (int) bdm.setScale(0, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		} catch (Exception e) {
		}
		return flows;
	}


//	public static String getImage(String image_url){
//		if (TextUtils.isEmpty(image_url)){
//			return "";
//		}else{
//			if (image_url.contains("http://")){
//				return image_url;
//			}else{
//				return AppContext.OOS_IMAGE + image_url;
//			}
//		}
//	}


	/**
	 * md加密
	 *
	 * @param string
	 * @return 2015-4-24 Administrator
	 */
	public static String md5(String string) {

		byte[] hash;

		try {

			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {

			throw new RuntimeException("Huh, MD5 should be supported?", e);

		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException("Huh, UTF-8 should be supported?", e);

		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {

			if ((b & 0xFF) < 0x10)
				hex.append("0");

			hex.append(Integer.toHexString(b & 0xFF));

		}

		return hex.toString();

	}

//	public static boolean isEmail(String strEmail) {
//		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
//		Pattern p = Pattern.compile(strPattern);
//		Matcher m = p.matcher(strEmail);
//		return m.matches();
//	}

	public static boolean isegal(String strEmail) {
		String strPattern = "^[a-zA-Z0-9]+$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	public static boolean isLegel(String strEmail) {
		String strPattern = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
}
