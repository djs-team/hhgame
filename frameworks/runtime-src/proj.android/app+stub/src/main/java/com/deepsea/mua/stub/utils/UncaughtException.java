package com.deepsea.mua.stub.utils;

import android.os.Environment;
import android.util.Log;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.stub.app.ActivityCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 捕获全局异常,因为有的异常我们捕获不到
 *
 * @author river
 */
public class UncaughtException implements UncaughtExceptionHandler {

    private final static String TAG = "UncaughtException";

    private static UncaughtException sMUncaughtException;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private boolean isShowLog = false;

    private UncaughtException() {
    }

    public synchronized static UncaughtException getInstance() {
        if (sMUncaughtException == null) {
            sMUncaughtException = new UncaughtException();
        }
        return sMUncaughtException;
    }

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void openLog() {
        isShowLog = true;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //退出程序
            ActivityCache.getInstance().finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
//        Thread {
//            Looper.prepare()
//            toast("很抱歉,程序出现异常,即将退出")
//            Looper.loop()
//        }.start()

        if (isShowLog) {
            saveCrashInfo2File(ex);
        }
        return true;
    }

    /**
     * 保存错误信息到文件中 *
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
        String time = formatter.format(new Date());
        sb.append("\n" + time + "----");

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);

        Log.e(TAG, result);

        try {

            String fileName = "exception.log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "crash" + File.separator + AppUtils.getApp().getPackageName() + File.separator;
                File dir = new File(path);
                if (!dir.exists()) {
                    boolean ret = dir.mkdirs();
                    if (!ret)
                        return "";
                }
                File file = new File(path + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(sb.toString().getBytes());
                fos.close();
            }

            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }

        return null;
    }
}
