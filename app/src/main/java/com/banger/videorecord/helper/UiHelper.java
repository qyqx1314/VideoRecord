package com.banger.videorecord.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.banger.videorecord.R;
import com.banger.videorecord.adapter.TimerRunAble;
import com.banger.videorecord.bean.result.Version;
import com.banger.videorecord.mouble.setting.business.imp.MyDialogLisner;
import com.banger.videorecord.widget.WheelView;
import java.util.Arrays;

/**
 * Created by zhujm on 2016/5/17.
 * ui辅助类
 */
public class UiHelper {
    private static final String TAG = "UiHelper";
    private static UiHelper uiHelper;
    private  Handler handler;
    public static synchronized UiHelper getInstance() {
        if(null==uiHelper){
            uiHelper = new UiHelper();
        }
        return uiHelper;
    }
    private Handler initHandler(){
        if (null==handler){
            handler = new Handler();
        }
        return  handler;
    }

    /**
     * 自定义样式刷新
     * @param cont activity
     * @return dialog
     */
    public AlertDialog createProgress(Activity cont){
        AlertDialog dialog = new AlertDialog.Builder(cont,R.style.dialog).create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        win.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        win.setContentView(R.layout.circular_progress);
        TextView msg = (TextView) win
                .findViewById(R.id.progressText);
        this.initHandler().postDelayed(new TimerRunAble(initHandler(),msg,dialog),500);
        return dialog;
    }
    /**
     * 系统样式刷新
     * @param cont activity
     * @return dialog
     */
    public AlertDialog createProgressTwo(Activity cont){
        AlertDialog dialog = new AlertDialog.Builder(cont, R.style.dialog).create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        win.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        win.setContentView(R.layout.progress);
        TextView msg = (TextView) win
                .findViewById(R.id.progressText);
        this.initHandler().postDelayed(new TimerRunAble(initHandler(),msg,dialog),500);
        return dialog;
    }
    public void updateDialog(Context mContext, String dialogContent, String dialogTitle, String buttonConfirm, String buttonCancle, final Version version, OnCustomDialogListener customDialogListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(
                R.layout.version_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        win.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        win.setContentView(view);
        TextView txtTitle=(TextView)view.findViewById(R.id.txtTitle);
        TextView content=(TextView)view.findViewById(R.id.dialog_content);
        Button btnConfirm=(Button)view.findViewById(R.id.btnConfirm);
        Button btnCancel=(Button)view.findViewById(R.id.btnCancel);
//        dialogContent = "1.更新内容\n2.第二个更新内容\n3.第三个更新内容";
        content.setText(dialogContent);
        txtTitle.setText(dialogTitle);
        btnConfirm.setText(buttonConfirm);
        btnCancel.setText(buttonCancle);
        btnConfirm.setOnClickListener(new MyDialogLisner(customDialogListener,dialog,version));
        btnCancel.setOnClickListener(new MyDialogLisner(customDialogListener,dialog,version));
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });
    }

    // 定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        public void confirm(Dialog dialog,Version version);
        public void cancle(Dialog dialog, Version version);
    }
    public interface OnCustomListener {
        public void confirm(WheelView.OnWheelViewListener onWheelViewListener,AlertDialog dialog);
        public void cancle(AlertDialog dialog);
    }
    public void showDialog(String txtCancle,String txtConFirm,Activity activity,String [] planets,int select,WheelView.OnWheelViewListener onWheelViewListener,OnCustomListener onCustomListener) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.wheel_view, null);
        AlertDialog dialog = new AlertDialog.Builder(activity,R.style.shareDialog_style).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        // 设置布局
        window.setContentView(view);
        // 设置宽高
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        // 设置监听
        window.setGravity(Gravity.BOTTOM);
        WheelView  wv = (WheelView) view.findViewById(R.id.wheel_view_wv);
//                wv.setOffset(2);
        wv.setItems(Arrays.asList(planets));
        wv.setSeletion(select);
        wv.setOnWheelViewListener(onWheelViewListener);
//        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//            }
//        });
//      点击事件，退出dialog
        TextView cancle= (TextView) view.findViewById(R.id.txt_cancel);
        TextView confirm= (TextView) view.findViewById(R.id.txt_sure);
        cancle.setText(txtCancle);
        confirm.setText(txtConFirm);
        cancle.setOnClickListener(new ItemOnclick(0,dialog,onCustomListener,onWheelViewListener));
        confirm.setOnClickListener(new ItemOnclick(1,dialog,onCustomListener,onWheelViewListener));
    }
    class ItemOnclick implements View.OnClickListener{
        OnCustomListener onCustomListener;
        int type;
        WheelView.OnWheelViewListener onWheelViewListener;
        AlertDialog dialog;
        public ItemOnclick(int type,AlertDialog dialog, OnCustomListener onCustomListener,WheelView.OnWheelViewListener onWheelViewListener) {
            this.type = type;
            this.dialog = dialog;
            this.onCustomListener = onCustomListener;
            this.onWheelViewListener = onWheelViewListener;
        }

        @Override
        public void onClick(View v) {
            if (type==1){
                onCustomListener.confirm(onWheelViewListener,dialog);
            }else {
                onCustomListener.cancle(dialog);
            }
        }
    }
}
