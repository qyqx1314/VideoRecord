package com.banger.videorecord.mouble.record.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;
/**
 * Created by zhujm on 2016/7/14.
 */
public class MyVideoView extends VideoView {
    public static int WIDTH;
    public static int HEIGHT;
    private PlayPauseListener mListener;

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(WIDTH, widthMeasureSpec);
        int height = getDefaultSize(HEIGHT, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }
    @Override
    public void start() {
        super.start();
        if (mListener != null) {
            mListener.onPlay();
        }
    }

    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        super.setOnCompletionListener(l);
        if (mListener != null) {
            mListener.onCompletion();
        }
    }
    public interface PlayPauseListener {
        void onPlay();
        void onCompletion();
        void onPause();
    }

    @Override
    public void pause() {
        super.pause();
        if(mListener!=null){
            mListener.onPause();
        }
    }
}
