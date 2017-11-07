package com.banger.videorecord.mouble.record.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banger.videorecord.R;


public class MySelectItemDialog extends Dialog {

    private  String[] items;
    private Context ctx;
    private LinearLayout selectLayout;
    private int selectIndex;
    // 定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener {
        void returnSelector(int index);
    }

    private OnCustomDialogListener customDialogListener;

    public MySelectItemDialog(Context context, OnCustomDialogListener customDialogListener) {
        super(context, R.style.MySelectDialog);
        this.customDialogListener = customDialogListener;
        this.ctx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_item);
        selectLayout=(LinearLayout)findViewById(R.id.dialog_select);
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.7);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width=screenWidth;
        initItems();
    }

    private void initItems(){
        selectLayout.removeAllViews();
//        if (selectLayout.getChildCount()<items.length){
            int j=items.length-selectLayout.getChildCount();
            //增加选项
            for (int i=0;i<j;i++){
                View view= LayoutInflater.from(ctx).inflate(R.layout.selector_item, null);
                View line=view.findViewById(R.id.line);
                TextView textView= (TextView) view.findViewById(R.id.txtView);
                textView.setText("" + items[i]);
                if(i==j-1){
                    line.setVisibility(View.GONE);
                }
                selectLayout.addView(view);
                MyOnclickListener myOnclickListener=new MyOnclickListener(i);
                view.setOnClickListener(myOnclickListener);
            }
//        }
//        if (selectLayout.getChildCount()>items.length){
//            int j=selectLayout.getChildCount();
//            //删除选项
//            for (int i=j;i>items.length;i--){
//                selectLayout.removeViewAt(i-1);
//            }
//        }
        //循环赋值
//        for (int i=0;i<selectLayout.getChildCount();i++){
//            View view=selectLayout.getChildAt(i);
//            TextView txtView=(TextView)view.findViewById(R.id.txtView);
//            View line=view.findViewById(R.id.line);
//            view.setOnClickListener(new MyOnclickListener(i));
//            txtView.setText(items[i]+"");
//            if (i==selectLayout.getChildCount()-1){
//                line.setVisibility(View.GONE);
//            }else line.setVisibility(View.VISIBLE);
//        }
    }

    public void updataDialog(String[] items){
        this.items=items;
        if (selectLayout!=null){
            initItems();
        }

    }


    class MyOnclickListener implements View.OnClickListener {
        private int index;
        MyOnclickListener(int index){
            this.index=index;
        }
        @Override
        public void onClick(View v) {
            customDialogListener.returnSelector(index);
            dismiss();
        }
    }
}