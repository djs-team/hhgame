package com.deepsea.mua.stub.view;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

public class PlayAudioManger {
    private MediaPlayer mMediaPlayer = null;
    private boolean isPlaying = false;
    private static PlayAudioManger instance = new PlayAudioManger();

    private OnPlayAudioListener mListener;

    public static PlayAudioManger getInstance() {
        return instance;
    }


    public void startPlay(AssetFileDescriptor filePath, OnPlayAudioListener aListener) {
        aFilePath = filePath;
        mListener = aListener;
        onPlay(isPlaying);
        isPlaying = !isPlaying;
    }


    // Play start/stop
    private void onPlay(boolean isPlaying) {
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if (mMediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            pausePlaying();
        }
    }

    AssetFileDescriptor aFilePath;

    public void startPlaying() {
        mMediaPlayer = new MediaPlayer();

        try {

            mMediaPlayer.setDataSource(aFilePath.getFileDescriptor(), aFilePath.getStartOffset(), aFilePath.getLength());
            mMediaPlayer.prepare();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    if (mListener != null)
                        mListener.onPlayAudioStart();
                }
            });
        } catch (IOException e) {
            Log.e("", "prepare() failed");
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                stopPlaying();
            }
        });


        //keep screen on while playing audio
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void pausePlaying() {
//        mPlayButton.setImageResource(R.drawable.ic_media_play);
//        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.pause();
        if (mListener != null)
            mListener.onPlayAudioPause();
    }

    public void resumePlaying() {
//        mPlayButton.setImageResource(R.drawable.ic_media_pause);
//        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.start();
        if (mListener != null)

            mListener.onPlayAudioStart();
    }

    public void stopPlaying() {
//        mPlayButton.setImageResource(R.drawable.ic_media_play);
//        mHandler.removeCallbacks(mRunnable);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mListener != null) {
            mListener.onPlayAudioComplete();
        }
        isPlaying = false;


        //allow the screen to turn off again once audio is finished playing
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public boolean getisPlaying() {
        return isPlaying;
    }

    public interface OnPlayAudioListener {
        void onPlayAudioStart();//开始


        void onPlayAudioPause();//暂停

        void onPlayAudioComplete();//播放完毕
    }


}
