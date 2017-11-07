package com.banger.videorecord.widget;

import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.DataBase;

import java.util.ArrayList;
import java.util.List;

import common.wheel.widget.OnWheelChangedListener;
import common.wheel.widget.OnWheelScrollListener;
import common.wheel.widget.WheelView;
import common.wheel.widget.adapters.WheelViewAdapter;

/**
 * Created by zhusiliang on 16/4/22.
 */
public class TestWheelDialog extends Dialog{
    private Context context;
    private List<DataBase> dataList;
    private OnSelectChangeListener onSelectChangeListener;
    private TextView txt_sure;
    private TextView txt_cancel;
    private WheelView wheelView;
    private List<TextView> textViewList=new ArrayList<>();
    public interface OnSelectChangeListener{
        public  void button1(Dialog dialog, int index);
        public  void button2(Dialog dialog, int index);
    }

    public TestWheelDialog(Context context, List<DataBase> data, OnSelectChangeListener customDialogListener){
        super(context, R.style.MySelectPhotoDialog);
        this.dataList=data;
        this.context=context;
        this.onSelectChangeListener=customDialogListener;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_wheel_test);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width=screenWidth;

        txt_sure= (TextView) findViewById(R.id.txt_sure);
        txt_cancel= (TextView) findViewById(R.id.txt_cancel);
        wheelView= (WheelView) findViewById(R.id.wheelView);

        MyOnclickListener myOnclickListener=new MyOnclickListener();
        txt_cancel.setOnClickListener(myOnclickListener);
        txt_sure.setOnClickListener(myOnclickListener);
        MyWheelAdapter adapter=new MyWheelAdapter();
        wheelView.setViewAdapter(adapter);

        wheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                clearTextView(textViewList);
                textViewList.get(newValue).setTextColor(context.getResources().getColor(R.color.common_blue));
            }
        });
        wheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

                clearTextView(textViewList);
                textViewList.get(wheel.getCurrentItem()).setTextColor(context.getResources().getColor(R.color.common_blue));
            }
        });

    }

    class MyOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_cancel:
                    onSelectChangeListener.button1(TestWheelDialog.this,wheelView.getCurrentItem());
                    break;
                case R.id.txt_sure:
                    onSelectChangeListener.button2(TestWheelDialog.this, wheelView.getCurrentItem());
                    break;
            }
        }
    }

    class MyWheelAdapter implements WheelViewAdapter {
        @Override
        public int getItemsCount() {
            return dataList.size();
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_wheel_text,parent,false);
            TextView textView= (TextView) convertView.findViewById(R.id.txt_wheel_content);
            textView.setText(dataList.get(index).getValue());
            textViewList.add(textView);
            return convertView;
        }

        @Override
        public View getEmptyItem(View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }
    }
    private void clearTextView(List<TextView> list){
        for(TextView textView:list){
            textView.setTextColor(context.getResources().getColor(R.color.textColor_black));
        }
    }
}
