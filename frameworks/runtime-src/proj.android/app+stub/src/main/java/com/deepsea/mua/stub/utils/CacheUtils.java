package com.deepsea.mua.stub.utils;

import android.os.Environment;

import com.deepsea.mua.core.utils.AppUtils;

import java.io.File;

/**
 * Created by JUN on 2019/4/2
 */
public class CacheUtils {

    public static String getAppBaseDir() {
        return Environment.getExternalStorageDirectory() + "/hehe";
    }

    public static String getAvatarDir() {
        return getAppBaseDir() + File.separator + "avatar";
    }

    public static String getCameraDir() {
        return getAppBaseDir() + File.separator + "camera";
    }

    public static String getAppDownLoadDir() {
        return getAppBaseDir() + File.separator + "app_download";
    }
    public static String getSongLrcDir(String songName){
        return getAppBaseDir()+ File.separator+"lrc"+File.separator+songName+".lrc";
    }
    public static String getSongMusicDir(String songName){
        return getAppBaseDir()+ File.separator+"music"+File.separator+ songName+".mp3";
    }
    public static String getTestRecord(){
        return getAppBaseDir()+File.separator+"arecord";
    }
    public static String getSongMusicDir(){
        return getAppBaseDir()+ File.separator+"music"+File.separator+"photo.jpj";
    }

    public static String getWebCacheDir() {
        return getAppBaseDir() + File.separator + "web_cache";
    }

    public static String getAuthPhotoDir() {
        return getAppBaseDir() + File.separator + "auth/photo";
    }

    public static String getRecordDir() {
        return getAppBaseDir() + File.separator + "record";
    }

    public static File getSvgaDir() {
        return AppUtils.getApp().getExternalCacheDir();
    }
}
