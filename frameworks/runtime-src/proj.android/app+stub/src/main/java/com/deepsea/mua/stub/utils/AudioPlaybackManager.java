package com.deepsea.mua.stub.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.LogUtils;

import java.io.IOException;

public class AudioPlaybackManager implements OnCompletionListener {
    private static AudioPlaybackManager instance = new AudioPlaybackManager();
    private PowerManager mPowerManager;
    private WakeLock mWakeLockRead;
    private MediaPlayer mPlayer;
    private String mCurrentMediaPath;
    private OnPlayingListener mListener;

    public static AudioPlaybackManager getInstance() {
        return instance;
    }

    public static int getDuration(String path) {
        if (TextUtils.isEmpty(path))
            return -1;

        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(path);
            player.prepare();
            int dur = player.getDuration();
            player.release();
            return dur;
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtils.i("AAAAAAAAA--->" + e.getMessage());
        }

        return -1;
    }

    public void initManager() {
        mPowerManager = (PowerManager) AppUtils.getApp().getSystemService(Context.POWER_SERVICE);
        mWakeLockRead = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "SCREEN_ON");
        mWakeLockRead.setReferenceCounted(false);
    }

    public void acquiredWakeLock() {
        if (mWakeLockRead != null) {
            mWakeLockRead.acquire();
        }
    }

    public void releaseWakeLock() {
        if (mWakeLockRead != null && mWakeLockRead.isHeld()) {
            mWakeLockRead.release();
        }
    }

    private void startPlaying(String path) {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
            }
        });
        try {
            mPlayer.reset();
            mPlayer.setDataSource(path);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void stopPlayer() {
        this.releasePlayer();
        if (mListener != null) {
            mListener.onStopPay();
            mListener = null;
        }
    }

    public void playAudio(String path, OnPlayingListener listener) {
        this.stopPlayer();
        mListener = listener;
        if (TextUtils.equals(mCurrentMediaPath, path)) {
            mCurrentMediaPath = null;
        } else {
            mCurrentMediaPath = path;
            this.startPlaying(mCurrentMediaPath);
            listener.onStartPay();
        }
    }

    public void stopAudio() {
        this.stopPlayer();
        this.mCurrentMediaPath = null;
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        LogUtils.d("[PP][Manager][Audio] onCompletion");
        this.releasePlayer();
        mCurrentMediaPath = null;
        if (mListener != null) {
            mListener.onComplete();
        }
    }

    public interface OnPlayingListener {
        void onStartPay();

        void onStopPay();

        void onComplete();
    }
}
