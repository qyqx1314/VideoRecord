package com.banger.videorecord.mouble.record.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.bean.FiledInfo;
import com.banger.videorecord.mouble.record.bean.ProductExtraInfo;
import com.banger.videorecord.mouble.record.inf.MyTextListener;

import java.util.List;

/**
 * Created by zhusiliang on 16/9/29.
 */
public class AddProductItem extends LinearLayout {
    private Context context;
    private ProductExtraInfo info;

    public AddProductItem(Context context,ProductExtraInfo info) {
        super(context);
        this.context=context;
        this.info=info;
        initView();
    }

    public AddProductItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_product_info, null);
        TextView txt_left = (TextView) view.findViewById(R.id.txt_left);
        TextView txt_right = (TextView) view.findViewById(R.id.txt_right);
        txt_left.setText(info.getName());
        txt_right.setText(info.getValue());
        //下拉框图片才显示
        addView(view);
    }

}

