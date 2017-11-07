package com.banger.videorecord.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.banger.videorecord.R;

/**
 * @author zhusiliang
 *         通用时间选择框
 */

public class MyDatePickerDialog extends DatePickerDialog {
    public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, R.style.MyDateDialog, callBack, year, monthOfYear, dayOfMonth);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    protected void onStop() {
    }
}
