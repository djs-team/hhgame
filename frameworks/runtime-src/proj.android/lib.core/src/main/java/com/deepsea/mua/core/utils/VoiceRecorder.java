package com.deepsea.mua.core.utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Time;

import java.io.File;
import java.util.Date;

/**
 * desc:   声音录制类
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/30 18:36
 */
public class VoiceRecorder {
    private static final String EXTENSION = ".m4a";

    private MediaRecorder recorder;

    private boolean isRecording = false;
    private long mStartTime;

    private String mVoiceFilePath;

    private File file;

    public VoiceRecorder() {
    }

    /**
     * start recording to the file
     *
     * @return boolean
     */
    public boolean startRecording() {
        if (!isStorageExists() || TextUtils.isEmpty(mVoiceFilePath))
            return false;

        file = null;
        try {
            // need to create recorder every time, otherwise, will got exception
            // from setOutputFile when try to reuse
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            //配置采集方式，这里用的是麦克风的采集方式
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //配置输出方式，这里用的是MP4，
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //MONO
            recorder.setAudioChannels(1);
            //配置采样频率，频率越高月接近原始声音，Android所有设备都支持的采样频率为44100
            recorder.setAudioSamplingRate(44100);
            //配置文件的编码格式,AAC是比较通用的编码格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //配置码率，这里一般通用的是96000
            recorder.setAudioEncodingBitRate(96000);

            //录音保存目录
            mVoiceFilePath = mVoiceFilePath + File.separator + getVoiceFileName();
            file = new File(mVoiceFilePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            isRecording = true;
            mStartTime = new Date().getTime();
            //配置录音文件的位置
            recorder.setOutputFile(file.getAbsolutePath());
            //开始录制音频
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isStorageExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * stop the recoding
     * seconds of the voice recorded
     */
    public void discardRecording() {
        if (recorder != null) {
            isRecording = false;
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRecord() {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public int stopRecoding() {
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;

            if (file == null || !file.exists() || !file.isFile()) {
                return 401;
            }
            if (file.length() == 0) {
                file.delete();
                return 0;
            }
            int seconds = (int) (new Date().getTime() - mStartTime) / 1000;
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName() {
        Time now = new Time();
        now.setToNow();
        return System.currentTimeMillis() + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getVoiceFilePath() {
        return mVoiceFilePath;
    }

    public void setVoiceFilePath(String voiceFilePath) {
        this.mVoiceFilePath = voiceFilePath;
    }
}