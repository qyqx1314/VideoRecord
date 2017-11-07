package com.banger.videorecord.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.banger.videorecord.R;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
//import org.apache.http.HttpException;

/***
 * 异常信息
 *
 * @ClassName: AppException
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jiumin Zhu
 * @date 2015-5-11 下午1:25:09
 *
 */
@SuppressWarnings("serial")
public class AppException extends Exception implements UncaughtExceptionHandler {
	private static final String TAG = "AppException";
	public final static boolean			Debug			= true;	// 是否保存错误日志

	/** 定义异常类型 */
	public final static byte				TYPE_NETWORK	= 0x01;
	public final static byte				TYPE_SOCKET		= 0x02;
	public final static byte				TYPE_HTTP_CODE	= 0x03;
	public final static byte				TYPE_HTTP_ERROR	= 0x04;
	public final static byte				TYPE_XML		= 0x05;
	public final static byte				TYPE_IO			= 0x06;
	public final static byte				TYPE_RUN		= 0x07;

	private byte							type;
	private int								code;
	//CrashHandler实例
	private static AppException instance;
	//程序的Context对象
	private Context mContext;
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler	mDefaultHandler;

	private AppException() {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
		if (Debug) {
			// this.saveErrorLog(excp);
		}
	}
	/** 获取CrashHandler实例 ,单例模式 */
	public static AppException getInstance() {
		if (instance == null)
			instance = new AppException();
		return instance;
	}
	/**
	 * 初始化
	 */
	public void init(Context context) {
		Log.e(TAG, "初始化异常捕获类 " );
		mContext = context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	/**
	 * 提示友好的错误信息
	 *
	 * @param ctx
	 */
	public void makeToast(Context ctx) {
		switch (this.getType()) {
			case TYPE_HTTP_CODE:
				// String err = ctx.getString(R.string.http_status_code_error,
				// this.getCode());
				// Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
				break;
			case TYPE_HTTP_ERROR:
				Toast.makeText(ctx, R.string.http_exception_error,
						Toast.LENGTH_SHORT).show();
				break;
			case TYPE_SOCKET:
				Toast.makeText(ctx, R.string.socket_exception_error,
						Toast.LENGTH_SHORT).show();
				break;
			case TYPE_NETWORK:
				Toast.makeText(ctx, R.string.network_not_connected,
						Toast.LENGTH_SHORT).show();
				break;
			case TYPE_XML:
				Toast.makeText(ctx, R.string.xml_parser_failed,
						Toast.LENGTH_SHORT).show();
				break;
			case TYPE_IO:
				Toast.makeText(ctx, R.string.io_exception_error,
						Toast.LENGTH_SHORT).show();
				break;
			case TYPE_RUN:
				Toast.makeText(ctx, R.string.app_run_code_error,
						Toast.LENGTH_SHORT).show();
				break;
		}
	}

	/**
	 * 保存异常日志
	 *
	 * @param excp
	 */
	public static void saveErrorLog(String excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			// 判断是否挂载了SD卡

			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/drjing/Log/errorlog/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				} else {
				}
				logFilePath = savePath + errorlog;
			}
			// 没有挂载SD卡，无法写文件
			if (logFilePath == "") {
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile, true);
			pw = new PrintWriter(fw);
			pw.println(excp);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	public static AppException http(int code) {
		return new AppException(TYPE_HTTP_CODE, code, null);
	}

	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0, e);
	}

	public static AppException socket(Exception e) {
		return new AppException(TYPE_SOCKET, 0, e);
	}

	public static AppException io(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof IOException) {
			return new AppException(TYPE_IO, 0, e);
		}
		return run(e);
	}

	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML, 0, e);
	}

	public static AppException network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof Exception) {
			return http(e);
		} else if (e instanceof SocketException) {
			return socket(e);
		}
		return http(e);
	}

	public static AppException run(Exception e) {
		return new AppException(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 *
	 * @return
	 */
	public static AppException getAppExceptionHandler() {
		return new AppException();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
			mDefaultHandler.uncaughtException(thread, ex);
	}

	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 *
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		if (mContext == null) {
			return false;
		}

		final String crashReport = getCrashReport(mContext, ex);
		Log.e(TAG, "handleException: "+crashReport );
		// 显示异常信息&发送报告
//		new Thread() {
//			public void run() {
//				Looper.prepare();
////				SharedPrefsUtil.putBooleanValue(context, "send_error_logs",
////						true);
////				UIHelper.sendAppCrashReport(context, crashReport, 1);
//				Log.e(TAG, "run: "+getCrashReport(context, ex) );
////				Looper.loop();
//			}
//
//		}.start();
		return true;
	}

	/**
	 * 获取APP崩溃异常报告
	 *
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context mContext,Throwable ex) {
		PackageInfo pinfo = null;
		try {
			pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("ponshine_llgj_errorlogs\n");
		exceptionStr.append("Version: " + pinfo.versionName + "("
				+ pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
				+ "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
//		}
		System.err.println("=====ErrorReport=========>"
				+ exceptionStr.toString());
		return exceptionStr.toString();
	}
}
