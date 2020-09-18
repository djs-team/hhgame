package com.deepsea.mua.core.log;

import android.text.TextUtils;

import org.apache.log4j.Logger;

/**
 * Created by JUN on 2019/8/16
 */
public class Logg {

    private static boolean SHOW_LOG = false;
    private static boolean isConfigured = false;

    public static synchronized void openLog() {
        SHOW_LOG = true;
    }

    public static void d(String tag, String message) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.debug(message);
        }
    }

    public static void d(String tag, String message, Throwable exception) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.debug(message, exception);
        }
    }

    public static void i(String tag, String message) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.info(message);
        }
    }

    public static void i(String tag, String message, Throwable exception) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.info(message, exception);
        }
    }

    public static void w(String tag, String message) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.warn(message);
        }
    }

    public static void w(String tag, String message, Throwable exception) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.warn(message, exception);
        }
    }

    public static void e(String tag, String message) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.error(message);
        }
    }

    public static void e(String tag, String message, Throwable exception) {
        if (SHOW_LOG) {
            Logger logger = getLogger(tag);
            logger.error(message, exception);
        }
    }

    private static Logger getLogger(String tag) {
        if (!isConfigured) {
            Log4jConfigure.configure();
            isConfigured = true;
        }
        Logger logger;
        if (TextUtils.isEmpty(tag)) {
            logger = Logger.getRootLogger();
        } else {
            logger = Logger.getLogger(tag);
        }
        return logger;
    }
}
