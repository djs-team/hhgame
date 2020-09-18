package com.deepsea.mua.stub.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

/**
 * Created by JUN on 2019/4/17
 */
public class FileUtils {


    /**
     * check if sdcard exist
     *
     * @return
     */
    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 读取assets文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssetsFile(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int fileLength = is.available();
            byte[] buffer = new byte[fileLength];
            int readLength = is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isFileExist(String filePath) {
        //判断文件是否存在

        {
            try {
                File f = new File(filePath);
                if (!f.exists()) {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }

            return true;

        }
        }

        /**
         * 读取assets文件字符串
         *
         * @param context
         * @param fileName
         * @return
         */
        public static String readFromAssets (Context context, String fileName){
            StringBuilder sb = new StringBuilder();
            AssetManager am = context.getAssets();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(am.open(fileName)));
                String next;
                while (null != (next = br.readLine())) {
                    sb.append(next);
                }
            } catch (IOException e) {
                e.printStackTrace();
                sb.delete(0, sb.length());
            } finally {
                try {
                    if (null != br) {
                        br.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return sb.toString().trim();
        }

        /**
         * 获取指定文件大小
         */
        public static long getFileSize (File file){
            long size = 0;
            if (file.exists()) {
                try {
                    FileInputStream fis = null;
                    fis = new FileInputStream(file);
                    size = fis.available();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return size;
        }

        /**
         * 获取指定文件夹
         */
        public static long getFileSizes (File file){
            long size = 0;
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
            return size;
        }

        /**
         * 转换文件大小
         *
         * @param fileS
         * @return
         */
        public static String toFileSize ( long fileS){
            DecimalFormat df = new DecimalFormat("#.00");
            String fileSizeString = "";
            String wrongSize = "0B";
            if (fileS == 0) {
                return wrongSize;
            }
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "MB";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "GB";
            }
            return fileSizeString;
        }
    }
