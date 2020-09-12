package com.deepsea.mua.core.log;

import android.os.Environment;
import android.util.Log;

import org.apache.log4j.Level;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by JUN on 2019/8/16
 */
public class Log4jConfigure {

    private static final String TAG = "Log4jConfigure";

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;
    private static final String DEFAULT_LOG_DIR = "//mua//log//";
    private static final String DEFAULT_LOG_FILE_NAME = "mua.log";

    private static final String PACKAGE_NAME = "com.deepsea.mua";

    public static void configure(String fileName) {
        final LogConfigurator logConfigurator = new LogConfigurator();
        try {
            if (isSdcardMounted()) {
                logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                        + DEFAULT_LOG_DIR + fileName);
            } else {
                logConfigurator.setFileName("//data//data//" + PACKAGE_NAME + "//files"
                        + File.separator + fileName);
            }
            //设置root日志输出级别 默认为DEBUG
            logConfigurator.setRootLevel(Level.DEBUG);
            //设置日志输出级别
            logConfigurator.setLevel("org.apache", Level.DEBUG);
            //设置 输出到日志文件的文字格式 默认 %d %-5p [%c{2}]-[%L] %m%n
            logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
            //设置输出到控制台的文字格式 默认%m%n
            logConfigurator.setLogCatPattern("%m%n");
            //设置总文件大小 (10M)
            logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
            //设置最大产生的文件个数
//            logConfigurator.setMaxBackupSize(2);
            //设置所有消息是否被立刻输出 默认为true,false 不输出
            logConfigurator.setImmediateFlush(true);
            //是否本地控制台打印输出 默认为true ，false不输出
            logConfigurator.setUseLogCatAppender(true);
            //设置是否启用文件附加,默认为true。false为覆盖文件
            logConfigurator.setUseFileAppender(true);
            //设置是否重置配置文件，默认为true
            logConfigurator.setResetConfiguration(true);
            //是否显示内部初始化日志,默认为false
            logConfigurator.setInternalDebugging(false);
            logConfigurator.configure();
            Log.d(TAG, "Log4j config finish");
        } catch (Throwable throwable) {
            logConfigurator.setResetConfiguration(true);
            Log.d(TAG, "Log4j config error, use default config. Error:" + throwable);
        }
    }

    public static void configure() {
        configure(DEFAULT_LOG_FILE_NAME);
    }

    private static boolean isSdcardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
