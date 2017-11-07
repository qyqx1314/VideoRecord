package com.banger.videorecord.util;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtil {
	private static ToastUtil  toastUtil = null;
	public static synchronized ToastUtil getInstance(){
		if (toastUtil==null){
			toastUtil = new ToastUtil();
		}
		return toastUtil;
	}
	public static void showToast(Context context, String str, int time) {
		// TODO Auto-generated constructor stub
		if (context != null) {
			Toast toast = Toast.makeText(context, str, time);
			// 可以控制toast显示的位置
			toast.setGravity(Gravity.CENTER, 0, 30);
			toast.show();
		}
	}


//	public static void clickCommendToast(Activity activity,String string) {
//		View toastRoot = activity.getLayoutInflater().inflate(R.layout.click_commend_toast, null);
//		TextView message = (TextView) toastRoot.findViewById(R.id.comment_succeed_tv);
//		message.setText(string);
//		Toast toastStart = new Toast(activity);
//		toastStart.setGravity(Gravity.CENTER, 0, 10);
//		toastStart.setDuration(Toast.LENGTH_LONG);
//		toastStart.setView(toastRoot);
//		toastStart.show();
//	}
//	public static void showMessage(Activity activity,String msgId) {
//		View view = activity.getLayoutInflater().inflate(R.layout.toast_message,null);
//		TextView txt = (TextView) view.findViewById(R.id.toast_message);
//		Toast toast = new Toast(activity);
//		toast.setView(view);
//		txt.setText(msgId);
//		toast.setGravity(Gravity.CENTER, 0, 200);
//		toast.setDuration(Toast.LENGTH_LONG);
//		toast.show();
//	}
	//短时间吐司
	public static void showShortToast(Context context, String str){
		Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
	}
	//长时间时间吐司
	public static void showLongToast(Context context, String str){
		Toast.makeText(context,str,Toast.LENGTH_LONG).show();
	}

}
