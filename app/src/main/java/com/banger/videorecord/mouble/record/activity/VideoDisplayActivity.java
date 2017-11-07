package com.banger.videorecord.mouble.record.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.banger.videorecord.R;
import com.banger.videorecord.mouble.record.bean.VideoInfoBean;
import com.banger.videorecord.mouble.record.business.DBVideoUtils;
import com.banger.videorecord.mouble.record.widget.MyVideoView;
import com.banger.videorecord.util.Constants;
import com.banger.videorecord.widget.MySelectDialog;

import org.androidannotations.annotations.Extra;

import java.io.File;

public class VideoDisplayActivity extends Activity implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    public static final String TAG = "VideoPlayer";
    private MyVideoView mVideoView;
    private Uri mUri;
    private int mPositionWhenPaused = -1;
    private MediaController mMediaController;
    private String filePath;
    private ImageView img_delete;
    private int process;
    private String  processId;
    private ImageView img_go_back;
    private boolean isDisplay;
    private VideoInfoBean videoBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_videodisplay);

        //Set the screen to landscape.
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mVideoView = (MyVideoView) findViewById(R.id.video_view);
        filePath = getIntent().getStringExtra("filePath");
        process = getIntent().getIntExtra("process", 1);
        videoBean = (VideoInfoBean) getIntent().getSerializableExtra("videoBean");
        isDisplay = getIntent().getBooleanExtra("isDisplay", false);
        File file = new File(filePath);
        //Video file
        mUri = Uri.fromFile(file);
        //Create media controller，组件可以控制视频的播放，暂停，回复，seek等操作，不需要你实现
        mMediaController = new MediaController(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setPlayPauseListener(new MyVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
               img_delete.setVisibility(View.GONE);
            }

            @Override
            public void onCompletion() {

            }

            @Override
            public void onPause() {
                if(!isDisplay){
                    img_delete.setVisibility(View.VISIBLE);
                }
            }

        });
        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySelectDialog mySelectDialog = new MySelectDialog(VideoDisplayActivity.this, "确定删除该视频么？", "确定", "取消", new MySelectDialog.OnCustomDialogListener() {
                    @Override
                    public void button1(Dialog dialog) {
                        File file = new File(filePath);
                        if (file.exists()) {
                            file.delete();
                        }
                        if(videoBean!=null){
                            DBVideoUtils.deleteVideo(videoBean.getId());
                        }

                        Intent intent = new Intent();
                        intent.putExtra("process", process);
                        intent.putExtra("processId", process);
                        setResult(Constants.ADD_VIDEO, intent);
                        finish();
                        dialog.dismiss();
                    }

                    @Override
                    public void button2(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
                mySelectDialog.show();

            }
        });
        img_go_back = (ImageView) findViewById(R.id.img_go_back);
        img_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (isDisplay) {
            img_delete.setVisibility(View.GONE);
        }
    }

    public void onStart() {
        img_delete.setVisibility(View.GONE);
        // Play Video
        mVideoView.setVideoURI(mUri);
        mVideoView.start();

        super.onStart();
    }

    public void onPause() {
        System.out.println("zzzz onPause");
        // Stop video when the activity is pause.
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();

        super.onPause();
    }

    public void onResume() {
        System.out.println("zzzz onResume");
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }

        super.onResume();
    }


    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        if(!isDisplay){
            img_delete.setVisibility(View.VISIBLE);
        }

    }
}
