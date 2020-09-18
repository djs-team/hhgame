package com.deepsea.mua.stub.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 作者：liyaxing  2019/8/21 11:56
 * 类别 ：
 */
public class TimeUtils {
    /**
     * 格式化时间展示为05’10”
     */
    public static String formatRecordTime(long recTime, long maxRecordTime) {
        int time = (int) ((maxRecordTime - recTime) / 1000);
        float minute = time / 60;
        float second = time % 60;
        //return String.format("%2d’%2d”", minute, second);
        return String.format("%2d:%2d", minute, second);
    }

    /**
     * 格式化时间展示为05’06”
     */
    public static String formatTime(int recTime) {
        int minute = recTime / 60;
        int second = recTime % 60;
        return String.format("%02d’%02d”", minute, second);
    }

    /**
     * 格式化时间展示为05:06
     */
    public static String formatSongTime(int recTime) {
        recTime = recTime / 1000;
        int minute = recTime / 60;
        int second = recTime % 60;
        return String.format("%02d:%02d", minute, second);
    }

    public static int getSysYear() {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        return year;
    }

    public static int getSysMonth() {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.MONTH) + 1;
        return year;
    }

    public static int getSysDay() {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.DAY_OF_MONTH);
        return year;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String time2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy年MM月dd日 HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static long date2Time(String timeString) {
        long time = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        try {
            date = format.parse(timeString);
            time = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getTodayStr() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        Date d = cal.getTime();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String today = sp.format(d);
        return today;
    }

    public static String getYesTodayStr() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String yesToday = sp.format(d);
        return yesToday;
    }

    public static String formatTime(String time) {
        String ftime = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(time);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy年MM月dd日");
            ftime = formatter2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ftime;
    }

    public static int formatTime2(String time) {
        String ftime = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date = formatter.parse(time);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
            ftime = formatter2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(ftime)) {

            return Integer.valueOf(ftime);
        } else {
            return 0;
        }
    }

    /**
     * 一小时的秒数
     */
    private static final int HOUR_SECOND = 60 * 60;

    /**
     * 一分钟的秒数
     */
    private static final int MINUTE_SECOND = 60;

    /**
     * 根据秒数获取时间串
     *
     * @param second (eg: 100s)
     * @return (eg : 00 : 01 : 40)
     */
    public static String getTimeStrBySecond(int second) {
        if (second <= 0) {

            return "00:00:00";
        }

        StringBuilder sb = new StringBuilder();
        int hours = second / HOUR_SECOND;
        if (hours > 0) {

            second -= hours * HOUR_SECOND;
        }

        int minutes = second / MINUTE_SECOND;
        if (minutes > 0) {

            second -= minutes * MINUTE_SECOND;
        }

        return (hours >= 10 ? (hours + "")
                : ("0" + hours) + ":" + (minutes >= 10 ? (minutes + "") : ("0" + minutes)) + ":"
                + (second >= 10 ? (second + "") : ("0" + second)));
    }

    public static long getSysTime() {
        return System.currentTimeMillis() / 1000;

    }

    public static String minute2hm(int time) {
        int hours = (int) Math.floor(time / 60);
        int minute = time % 60;
        return hours + "小时" + minute + "分钟";
    }

    public static String minute2hm2(double time) {
        int hours = (int) Math.floor(time / 60);
        int minute = (int) (time % 60);
        if (hours > 0) {
            return hours + "小时" + minute + "分钟";
        } else {
            return minute + "分钟";
        }
    }

    /**
     * 返回日时分秒
     *
     * @param second
     * @return
     */
    public static String secondToTime(long second) {

        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days) {
            return days + "天，" + hours + "小时，" + minutes + "分，" + second + "秒";
        } else {
            return hours + "小时，" + minutes + "分，" + second + "秒";
        }
    }

    public static String addTime(int hour) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String maxDateStr = sdf.format(new Date());
        String minDateStr = "";
        Calendar calc = Calendar.getInstance();
        try {
            calc.setTime(sdf.parse(maxDateStr));
            calc.add(calc.DATE, hour / 24);
            Date minDate = calc.getTime();
            minDateStr = sdf.format(minDate);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return minDateStr;

    }
}
