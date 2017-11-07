package com.banger.videorecord.mouble.setting.business.imp;

import android.app.AlertDialog;
import android.view.View;

import com.banger.videorecord.R;
import com.banger.videorecord.bean.result.Version;
import com.banger.videorecord.helper.UiHelper;

/**
 * Created by zhujm on 2016/7/14.
 */
public class MyDialogLisner implements View.OnClickListener {
    private UiHelper.OnCustomDialogListener customDialogListener;
    private AlertDialog dialog;
    private Version version;
    public MyDialogLisner(UiHelper.OnCustomDialogListener onCustomDialogListener,AlertDialog dialog,Version version) {
        this.customDialogListener = onCustomDialogListener;
        this.dialog = dialog;
        this.version = version;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirm:
                customDialogListener.confirm(dialog,version);
                break;
            case R.id.btnCancel:
                customDialogListener.cancle(dialog,version);
                break;
        }
    }
}
