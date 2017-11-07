package com.banger.videorecord.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.Version;

/**
 * Created by zhusiliang on 16/4/20.
 */
public class MyDialog extends Dialog {
    private String dialogContent ;
    private String dialogTitle;
    private String buttonConfirm;
    private String buttonCancle;
    private Version version;


    private Button btnConfirm;
    private Button btnCancel;
    private TextView txtTitle;
    private TextView content;
    private Context ctx;
    // 定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        public void confirm(Dialog dialog,Version version);
        public void cancle(Dialog dialog, Version version);
    }
    private OnCustomDialogListener customDialogListener;
    public MyDialog(Context context,String dialogContent, String dialogTitle, String buttonConfirm, String buttonCancle,Version version, OnCustomDialogListener customDialogListener) {
        super(context, R.style.MySelectPhotoDialog);
        this.dialogContent = dialogContent;
        this.customDialogListener = customDialogListener;
        this.dialogTitle=dialogTitle;
        this.buttonConfirm=buttonConfirm;
        this.buttonCancle=buttonCancle;
        this.version = version;
        this.ctx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.version_dialog);

//        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
//        int screenWidth = (int) (metrics.widthPixels * 0.6);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.width=screenWidth;

        txtTitle=(TextView)findViewById(R.id.txtTitle);
        content=(TextView)findViewById(R.id.dialog_content);
        btnConfirm=(Button)findViewById(R.id.btnConfirm);
        btnCancel=(Button)findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
        dialogContent = "1.更新内容\n2.第二个更新内容\n3.第三个更新内容";
        content.setText(dialogContent);
        txtTitle.setText(dialogTitle);
        btnConfirm.setText(buttonConfirm);
        btnCancel.setText(buttonCancle);

        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);

    }
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnConfirm:
                    customDialogListener.confirm(MyDialog.this,version);
                    break;
                case R.id.btnCancel:
                    customDialogListener.cancle(MyDialog.this,version);
                    break;
                default:
                    break;
            }
        }
    };
}
