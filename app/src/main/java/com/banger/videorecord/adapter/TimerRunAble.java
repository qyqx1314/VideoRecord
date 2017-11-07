package com.banger.videorecord.adapter;
import android.app.AlertDialog;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by zhujm on 2016/5/18.
 * 刷新提示框的文字变动
 */
public class TimerRunAble implements Runnable {
    private Handler handler;
    private TextView textView;
    private AlertDialog dialog;
    public TimerRunAble(Handler handler, TextView textView, AlertDialog dialog) {
        this.handler = handler;
        this.textView = textView;
        this.dialog = dialog;
    }

    @Override
    public void run() {

        if (this.dialog.isShowing()){
            this.handler.postDelayed(this,500);
            this.textView.setText(setTextValue(this.textView.getText().toString()));
        }else {
            this.handler.removeCallbacks(this);
        }

    }
    private String setTextValue(String value){
        int end = value.length();
        StringBuilder sb = new StringBuilder();
        if (end<3){
            return "";
        }
        switch (value.length()){
            case 3:
                sb.append("请稍候.");
                break;
            case 4:
                sb.append("请稍候..");
                break;
            case 5:
                sb.append("请稍候...");
                break;
            case 6:
                sb.append("请稍候");
                break;
        }
        return sb.toString();
    }
}
