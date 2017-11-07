package com.banger.videorecord.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
	public static final long INTERVAL_OF_WEEK = 1000 * 60 * 60 * 24 * 7;
	public static final long INTERVAL_OF_DAY = 1000 * 60 * 60 * 24;

	public DateUtil() {
		throw new RuntimeException("private constructor!");
	}

	public static int getDayOfMonth() {
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int day = aCalendar.get(Calendar.DATE);
		return day;
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static int getCurrentMonthLastDay() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		c.roll(Calendar.DATE, -1);
		return c.get(Calendar.DATE);
	}

	public static int getMonthLastDay(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DATE, 1);
		c.roll(Calendar.DATE, -1);
		int maxDate = c.get(Calendar.DATE);
		return maxDate;
	}

	public static String getMonthDayString(int year, int month, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DATE, day);
		Date d = c.getTime();
		return format.format(d);
	}

	public static String getDayString(int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, day);
		Date d = c.getTime();
		return format.format(d);
	}

	public static long getDistance(int day, int hour, int minute) {
		Calendar then = Calendar.getInstance();
		then.set(Calendar.DAY_OF_WEEK, day);
		then.set(Calendar.HOUR_OF_DAY, hour);
		then.set(Calendar.MINUTE, 0);
		then.set(Calendar.SECOND, 0);
		Calendar now = Calendar.getInstance();
		long distance = then.getTimeInMillis() - now.getTimeInMillis();
		if (distance < 0) {
			distance += INTERVAL_OF_WEEK;
		}
		return distance;
	}

	public static long getDistance(int hour) {
		Calendar then = Calendar.getInstance();
		then.set(Calendar.HOUR_OF_DAY, hour);
		then.set(Calendar.MINUTE, 0);
		then.set(Calendar.SECOND, 0);
		Calendar now = Calendar.getInstance();
		long distance = then.getTimeInMillis() - now.getTimeInMillis();
		if (distance < 0) {
			distance += INTERVAL_OF_DAY;
		}
		return distance;
	}

	public static long getDistance(int hour, int minute) {
		Calendar then = Calendar.getInstance();
		then.set(Calendar.HOUR_OF_DAY, hour);
		then.set(Calendar.MINUTE, minute);
		then.set(Calendar.SECOND, 0);
		Calendar now = Calendar.getInstance();
		long distance = then.getTimeInMillis() - now.getTimeInMillis();
		if (distance < 0) {
			distance += INTERVAL_OF_DAY;
		}
		return distance;
	}

	public static String getCurrentDate(String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
		return simpleDateFormat.format(new Date());
	}


	public static int getCurrentYear() {
		Calendar calendar=Calendar.getInstance(Locale.CHINA);
		long time=System.currentTimeMillis();
		calendar.setTimeInMillis(time);
		int year=  calendar.get(Calendar.YEAR);
		return year;
	}

	public static int getCurrentMonthInt() {
		Calendar calendar=Calendar.getInstance(Locale.CHINA);
		long time=System.currentTimeMillis();
		calendar.setTimeInMillis(time);
		int month=  calendar.get(Calendar.MONTH);
		return month;
	}

	public static String getMomDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_YEAR);
		now.set(GregorianCalendar.DAY_OF_YEAR, day + 1);
		Date d = now.getTime();
		return format.format(d);
	}

	public static String getLastSevenDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_YEAR);
		now.set(GregorianCalendar.DAY_OF_YEAR, day - 7);
		Date d = now.getTime();
		return format.format(d);
	}

	public static String getMonthFirstDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Calendar now = Calendar.getInstance();
		now.set(GregorianCalendar.DAY_OF_MONTH, 1);
		Date d = now.getTime();
		return format.format(d);
	}

	public static String getMonthSevenDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Calendar now = Calendar.getInstance();
		now.set(GregorianCalendar.DAY_OF_MONTH, 7);
		Date d = now.getTime();
		return format.format(d);
	}

	public static String getMonthLastDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, 1);
		now.roll(Calendar.DATE, -1);
		Date d = now.getTime();
		return format.format(d);
	}

	public static String getCurrentMonth() {
		SimpleDateFormat format = new SimpleDateFormat("MM", Locale.CHINA);
		return format.format(new Date());
	}
	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
		return format.format(new Date());
	}
	public static String covenDate(String str) {
		String result = str;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		SimpleDateFormat format1 = new SimpleDateFormat("d", Locale.CHINA);
		try {
			Date date = format.parse(str);
			return format1.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @Description: 比较日期大小
	 * @author Jiumin Zhu
	 * @createtime: 2015-1-5 上午10:57:32
	 * @param beginTime 开始时间，第一个时间
	 * @param endTime 结束时间，第二个时间
	 * @param formatDateTime 时间格式
	 * @return 开始时间小于结束时间为ture
	 *
	 */
	public static boolean compareDate(String beginTime, String endTime,String formatDateTime) {
		SimpleDateFormat format = new SimpleDateFormat(formatDateTime, Locale.CHINA);
		try {
			Date beginDateTime = format.parse(beginTime);
			Date endDateTime = format.parse(endTime);
			if (beginDateTime.getTime() < endDateTime.getTime()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

//	public static int diffDate(String startDate) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		SimpleDateFormat format1 = new SimpleDateFormat("d");
//		try {
//			Date sDate = format.parse(startDate);
//			Date eDate = format.parse(DateUtil.getLastSevenDate());
//			String sDay = format1.format(sDate);
//			String eDay = format1.format(eDate);
//			return StringUtil.toInt(sDay) - StringUtil.toInt(eDay);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	// 获取当天时间
	public static String getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat, Locale.CHINA);// 可以方便地修改日期格式
		return dateFormat.format(now);
	}

	public static String datailDate(String str) {
		String result = str;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try {
			Date date = format.parse(str);
			return format1.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 计算当月最后一天,返回字符串
	public static String getDefaultDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取当月第一天
	public static String getFirstDayOfMonth() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		long day = 1;
		try {
			Date date = myFormatter.parse(sj1);
			Date mydate = myFormatter.parse(sj2);
			day = ((date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}
	/**
	 * 将字符串时间，格式化
	 */
	public static String setFormatStr(String dates,String dateFormat) {
		String str = "";
		SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat, Locale.CHINA);
		try {
			Date date = myFormatter.parse(dates);
			str = myFormatter.format(date);
		} catch (Exception e) {
			return "";
		}
		return str ;
	}

	//将毫秒值转化为时间格式
	public static String getFormatedDateTime(String pattern, Long dateTime) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
		return sDateFormat.format(new Date(dateTime));
	}

	//将毫秒值转化为时分秒
	public static String formatLongToTimeStr(Long l) {
		int hour = 0;
		int minute = 0;
		int second = 0;

		second = l.intValue() / 1000;

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
	private static String getTwoLength(final int data) {
		if(data < 10) {
			return "0" + data;
		} else {
			return "" + data;
		}
	}

	/**
	 * @param str1
	 * @param str2
	 * @return比较两个时间相差的秒数
	 */
	public static long getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		Date one;
		Date two;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			sec=diff/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sec;
	}

	public static String timeToString(int second){
		StringBuffer sb=new StringBuffer();
		int hour=second/3600;
		int minute=(second%3600)/60;
		second=second%60;
		if(hour==0){
			sb.append("00:");
		}else if(hour>0&&hour<10){
			sb.append("0").append(hour).append(":");
		}else {
			sb.append(""+hour).append(":");
		}

		if(minute==0){
			sb.append("00:");
		}else if(minute>0&&minute<10){
			sb.append("0").append(minute).append(":");
		}else {
			sb.append(""+minute).append(":");
		}

		if(second==0){
			sb.append("00");
		}else if(second>0&&second<10){
			sb.append("0").append(second);
		}else {
			sb.append(""+second);
		}
		return sb.toString();
	}

}