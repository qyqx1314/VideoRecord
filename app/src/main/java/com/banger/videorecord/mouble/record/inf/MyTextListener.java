package com.banger.videorecord.mouble.record.inf;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.banger.videorecord.mouble.record.bean.FiledInfo;

/**
 * Created by zhusiliang on 16/9/27.
 */
public class MyTextListener implements TextWatcher {
    private FiledInfo info;

    public MyTextListener(FiledInfo info) {
        this.info = info;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        info.setValue(s.toString());
    }
}
