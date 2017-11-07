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

/**
 * Created by zhusiliang on 16/4/20.
 */
public class MySelectDialog extends Dialog {

    private Button btnConfirm;
    private Button btnCancel;
    private String buttonName1;
    private String buttonName2;
    private TextView txtTitle;
    private Context ctx;
    // 定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        public void button1(Dialog dialog);
        public void button2(Dialog dialog);
    }

    private OnCustomDialogListener customDialogListener;
    private String title;

    public MySelectDialog(Context context, String title, String buttonName1, String buttonName2, OnCustomDialogListener customDialogListener) {
        super(context, R.style.MySelectPhotoDialog);
        this.customDialogListener = customDialogListener;
        this.title=title;
        this.buttonName1=buttonName1;
        this.buttonName2=buttonName2;
        this.ctx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select);

        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.6);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width=screenWidth;

        txtTitle=(TextView)findViewById(R.id.txtTitle);
        btnConfirm=(Button)findViewById(R.id.btnConfirm);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        btnConfirm.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);

        txtTitle.setText(title+"");
        btnConfirm.setText(buttonName1+"");
        btnCancel.setText(buttonName2+"");


    }
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnConfirm:
                    customDialogListener.button1(MySelectDialog.this);
                    break;
                case R.id.btnCancel:
                    customDialogListener.button2(MySelectDialog.this);
                    break;
                default:
                    break;
            }
        }
    };
}
