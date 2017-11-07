package com.banger.videorecord.mouble.record.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.DataBase;
import com.banger.videorecord.mouble.record.bean.FiledInfo;
import com.banger.videorecord.mouble.record.inf.MyTextListener;
import com.banger.videorecord.mouble.record.util.DataUtil;
import com.banger.videorecord.widget.MyDatePickerDialog;
import com.banger.videorecord.widget.MyItemsDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by zhusiliang on 16/9/29.
 */
public class AddCustomerItem extends LinearLayout {
    private Context context;
    private FiledInfo info;
    private String selectInfo;
    private String timeStr = "";

    public AddCustomerItem(Context context, FiledInfo info) {
        super(context);
        this.context = context;
        this.info = info;
        initView();
    }

    public AddCustomerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer_info, null);
        TextView txt_star = (TextView) view.findViewById(R.id.txt_star);
        TextView txt_left = (TextView) view.findViewById(R.id.txt_left);
        EditText txt_right1 = (EditText) view.findViewById(R.id.txt_right1);
        final TextView txt_right2 = (TextView) view.findViewById(R.id.txt_right2);
        ImageView img_down = (ImageView) view.findViewById(R.id.img_down);
        LinearLayout layout_choose = (LinearLayout) view.findViewById(R.id.layout_choose);
        txt_left.setText(info.getFieldCNName());
        //是否隐藏
        if (info.getIsRequire() == 0) {
            txt_star.setVisibility(View.INVISIBLE);
        }
        txt_right1.setText(info.getValue());
        txt_right2.setText(info.getValue());
        List<String> options = info.getOptions();

        //下拉框图片才显示
        if (info.getFieldType() != null && (info.getFieldType().equals("Select") || info.getFieldType().equals("MultipleSelect"))) {
            img_down.setVisibility(View.VISIBLE);
            txt_right1.setVisibility(View.GONE);
            txt_right2.setVisibility(View.VISIBLE);
            MyTextListener myTextListener = new MyTextListener(info);
            txt_right2.addTextChangedListener(myTextListener);
            //如果有值就弹出dialog
            if (options != null && options.size() > 0) {
                String[] items = (String[]) options.toArray(new String[options.size()]);
                if (info.getFieldType().equals("MultipleSelect")) {
                    MyOnclickListener2 myOnclickListener = new MyOnclickListener2(items, context, txt_right2);
                    layout_choose.setOnClickListener(myOnclickListener);
                } else {
                    MyOnclickListener myOnclickListener = new MyOnclickListener(items, context, txt_right2);
                    layout_choose.setOnClickListener(myOnclickListener);
                }
            }
            //如果是时间的话，需要弹出时间的选择器
        } else if (info.getFieldType() != null && info.getFieldType().equals("Date")) {
            img_down.setVisibility(View.VISIBLE);
            txt_right1.setVisibility(View.GONE);
            txt_right2.setVisibility(View.VISIBLE);
            //文本监听事件，最后面会给info赋值，一套过去
            MyTextListener myTextListener = new MyTextListener(info);
            txt_right2.addTextChangedListener(myTextListener);
            MyOnclickListener3 myOnclickListener = new MyOnclickListener3(context, txt_right2);
            layout_choose.setOnClickListener(myOnclickListener);

        } else {
            //如果服务器给的类型是number 只能输入数字
            if(info.getFieldType() != null && info.getFieldType().equals("Number")){
                txt_right1.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            img_down.setVisibility(View.GONE);
            txt_right1.setVisibility(View.VISIBLE);
            txt_right2.setVisibility(View.GONE);
            MyTextListener myTextListener = new MyTextListener(info);
            txt_right1.addTextChangedListener(myTextListener);
            txt_right1.setHint("请输入" + info.getFieldCNName() + "信息");
        }
        addView(view);
    }


    /**
     * 自定义点击事件 单选的
     */
    class MyOnclickListener implements View.OnClickListener {
        private String[] items;
        private Context context;
        private TextView textView;

        public MyOnclickListener(String[] items, Context context, TextView textView) {
            this.items = items;
            this.context = context;
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            MySelectItemDialog mySelectItemDialog = new MySelectItemDialog(context, new MySelectItemDialog.OnCustomDialogListener() {
                @Override
                public void returnSelector(int index) {
                    textView.setText(items[index]);
                }
            });
            mySelectItemDialog.updataDialog(items);
            mySelectItemDialog.show();
        }
    }

    /**
     * 自定义点击事件 多选的
     */
    class MyOnclickListener2 implements OnClickListener {
        private String[] items;
        private Context context;
        private TextView textView;

        public MyOnclickListener2(String[] items, Context context, TextView textView) {
            this.items = items;
            this.context = context;
            this.textView = textView;

        }

        @Override
        public void onClick(View v) {
            MyItemsDialog myItemsDialog = new MyItemsDialog(context, new MyItemsDialog.OnItemSelectListener() {
                @Override
                public void returnItems(List<DataBase> list) {
                    StringBuffer stringBuffer = new StringBuffer();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < items.length; i++) {
                            for (DataBase dataBase : list) {
                                if (dataBase.getValue().equals(items[i])) {
                                    if (stringBuffer.toString().length() == 0) {
                                        stringBuffer.append(items[i]);
                                    } else {
                                        stringBuffer.append(",").append(items[i]);
                                    }
                                }
                            }
                        }
                    }
                    textView.setText(stringBuffer.toString());
                    selectInfo = stringBuffer.toString();
                }
            });
            myItemsDialog.updateDialog(DataUtil.changeArrryToList(items), DataUtil.changeStringToList(selectInfo));
            myItemsDialog.show();

        }
    }

    /**
     * 事件的选择自定义点击事件
     */
    class MyOnclickListener3 implements OnClickListener {
        private Context context;
        private TextView textView;

        public MyOnclickListener3(Context context, TextView textView) {
            this.context = context;
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            if (TextUtils.isEmpty(timeStr)) {
                date = new Date(System.currentTimeMillis());
            } else {
                try {
                    date = format.parse(timeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            calendar.setTime(date);
            MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    timeStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            myDatePickerDialog.show();
        }
    }

}
