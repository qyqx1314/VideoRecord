package com.banger.videorecord.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhusiliang on 16/10/12.
 */
public class MyItemsDialog extends Dialog {

    private List<DataBase> items;//整个的item
    private List<DataBase> selectItems;//传入进来之前选中的
    private Context ctx;
    private LinearLayout selectLayout;
    private List<DataBase> indexList;//监听选中的list
    private Button btn_sure;
    private Button btn_cancel;

    // 定义回调事件，用于dialog的点击事件 最后将选中的list扔回到activity
    public interface OnItemSelectListener {
        void returnItems(List<DataBase> list);
    }

    private OnItemSelectListener onItemSelectListener;

    public MyItemsDialog(Context context, OnItemSelectListener onItemSelectListener) {
        super(context, R.style.MySelectDialog);
        this.onItemSelectListener = onItemSelectListener;
        this.ctx = context;
        indexList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_item_more);
        selectLayout = (LinearLayout) findViewById(R.id.dialog_select);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.7);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = screenWidth;
        initItems();
        initListener();
    }

    /**
     * 初始化所有的item
     */
    private void initItems() {
        selectLayout.removeAllViews();
//        if (selectLayout.getChildCount()<items.length){
        int j = items.size() - selectLayout.getChildCount();
        //增加选项
        for (int i = 0; i < j; i++) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.select_item_checked, null);
            ImageView imgCheck = (ImageView) view.findViewById(R.id.img_checked);
            TextView textView = (TextView) view.findViewById(R.id.txtView);
            textView.setText("" + items.get(i).getValue());
            //如果之前选中了就直接select为true 并且把选中的放到indexList
            if(isSelect(items.get(i).getValue())){
                imgCheck.setSelected(true);
                indexList.add(items.get(i));
            }
            selectLayout.addView(view);
            MyOnclickListener myOnclickListener = new MyOnclickListener(i, imgCheck);
            view.setOnClickListener(myOnclickListener);
        }
    }
    //初始化确定和取消的按钮的点击事件
    private void initListener() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectListener.returnItems(indexList);
                dismiss();
            }
        });

    }
    //传入数据，整个的item 以及之前选好的
    public void updateDialog(List<DataBase> items, List<DataBase> selectItems) {
        this.items = items;
        this.selectItems = selectItems;
        if (selectLayout != null) {
            initItems();
        }

    }
    //自定义点击事件，如果选中了则add没有则remove   value标识
    class MyOnclickListener implements View.OnClickListener {
        private int index;
        private ImageView imgCheck;

        MyOnclickListener(int index, ImageView imgCheck) {
            this.index = index;
            this.imgCheck = imgCheck;
        }

        @Override
        public void onClick(View v) {
            imgCheck.setSelected(!imgCheck.isSelected());
            if (imgCheck.isSelected()) {
                indexList.add(items.get(index));
            } else {
                DataBase info=new DataBase();
                for(DataBase dataBase:indexList){
                    if(items.get(index).getValue().equals(dataBase.getValue())){
                        info=dataBase;
                    }
                }
                indexList.remove(info);
            }
        }
    }
    //判断这个是否被选中
    private boolean isSelect(String value) {
        boolean result = false;
        if (selectItems != null && selectItems.size() > 0) {
            for (DataBase dataBase : selectItems) {
                if (dataBase.getValue().equals(value)) {
                    result = true;
                }
            }
        }
        return result;
    }
}