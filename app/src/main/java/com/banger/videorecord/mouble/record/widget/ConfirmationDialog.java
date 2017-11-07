package com.banger.videorecord.mouble.record.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.banger.videorecord.R;


/**
 * Created by zhujm on 2016/4/25
 * Shows OK/Cancel confirmation dialog about camera permission.
 */
public class ConfirmationDialog extends DialogFragment {
    private static final String ARG_MESSAGE = "message";
    private static final String REQUEST_CAMERA = "request_camera";
    private static Activity activity;
    /**
     * Manifest.permission.CAMERA 相机
     *
     * @param message 权限
     * @return 权限请求弹窗
     */
    public static ConfirmationDialog newInstance(String[] message,int requestCamera,Activity activity) {
        if (null == message || message.length <= 0) {
            return null;
        } else {
            ConfirmationDialog.activity = activity;
            ConfirmationDialog dialog = new ConfirmationDialog();
            Bundle args = new Bundle();
            args.putStringArray(ARG_MESSAGE, message);
            args.putInt(REQUEST_CAMERA,requestCamera);
            dialog.setArguments(args);
            return dialog;
        }
    }

    //new String[]{Manifest.permission.CAMERA}
    //ArgumentUtil.REQUEST_CAMERA_PERMISSION
    @TargetApi(23)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= 23) {
//                     // your code using Camera2 API here - is api 21 or higher
            final Bundle bundle = getArguments();
            return new AlertDialog.Builder(activity)
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.requestPermissions(
                                    bundle.getStringArray(ARG_MESSAGE),bundle.getInt(REQUEST_CAMERA));
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
        return null;
        };

}
