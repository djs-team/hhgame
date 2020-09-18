package com.deepsea.mua.stub.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by JUN on 2019/3/22
 */
public class FormatUtils {

    /**
     * 按手机号格式输出 3-4-4
     *
     * @param s
     * @param start
     * @param before
     * @return
     */
    public static void formatMobile(EditText editText, CharSequence s, int start, int before) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            editText.setText(sb.toString());
            editText.setSelection(index);
        }
    }

    /**
     * 金额输入
     *
     * @param editText
     * @param s
     */
    public static void formatInputMoney(EditText editText, CharSequence s) {
        formatInputMoney(editText, s, 2);
    }

    /**
     * 金额输入
     *
     * @param editText
     * @param s
     * @param length   小数点后位数
     */
    public static void formatInputMoney(EditText editText, CharSequence s, int length) {
        if (s.toString().contains(".")) {
            if ((s.length() - s.toString().indexOf(".") - 1) > length) {
                s = s.subSequence(0, s.toString().indexOf(".") + length + 1);
                editText.setText(s);
                editText.setSelection(s.length());
            }
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (!TextUtils.isEmpty(s)) {
            if (s.indexOf(".") > 0) {
                //去掉多余的0
                s = s.replaceAll("0+?$", "");
                //如最后一位是.则去掉
                s = s.replaceAll("[.]$", "");
            }
            return s;
        } else {
            return "0";
        }
    }


    /**
     * 移除小数点后数据
     *
     * @param source
     * @return
     */
    public static String removeDot(String source) {
        if (!TextUtils.isEmpty(source) && source.indexOf(".") > 0) {
            source = source.substring(0, source.indexOf("."));
        }
        return source;
    }

    /**
     * @向上取整
     */
    public static String getCeil(double d) {
        // d=2.523214324;
        DecimalFormat df = new DecimalFormat("###,##0.00");
        df.setRoundingMode(RoundingMode.UP);
        return df.format(d);
    }

    /**
     * 向上取整
     */
    public static String getCeil(double d, String format) {
        // d=2.523214324;
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.UP);
        return df.format(d);
    }

    /**
     * 向下取整
     *
     * @param d
     * @param format
     * @return
     */
    public static String getFloor(double d, String format) {
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(d);
    }

    /**
     * 向下取整
     *
     * @param s
     * @param format
     * @return
     */
    public static String getFloor(String s, String format) {
        double d = isNumber(s) ? new BigDecimal(s).doubleValue() : 0;
        return getFloor(d, format);
    }

    /**
     * 四舍五入,并保留小数位
     *
     * @param d
     * @param scale
     * @return
     */
    public static String formatNumberScale(double d, int scale) {
        if (scale < 0) {
            return String.valueOf(d);
        }
        BigDecimal bigDec = new BigDecimal(d);
        return bigDec.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    /**
     * 判断是否为(正数)数字(包含浮点数)
     *
     * @return
     */
    public static boolean isNumber(String number) {
        return !TextUtils.isEmpty(number) && number.matches("^\\d+(\\.\\d+)?$");
    }

    /**
     * 是否为0
     *
     * @param value
     * @return
     */
    public static boolean equalsZero(String value) {
        BigDecimal decimal = new BigDecimal(value);
        BigDecimal zero = new BigDecimal(0.0);
        int result = decimal.compareTo(zero);
        return result == 0;
    }

    /**
     * 是否>0
     *
     * @param value
     * @return
     */
    public static boolean moreThanZero(String value) {
        if (isNumber(value)) {
            BigDecimal decimal = new BigDecimal(value);
            BigDecimal zero = new BigDecimal(0.0);
            int result = decimal.compareTo(zero);
            return result == 1;
        } else {
            return false;
        }
    }

    /**
     * value1 是否大于 value2
     *
     * @param value1
     * @param value2
     * @return
     */
    public static boolean moreThan(String value1, String value2) {
        if (isNumber(value1)) {
            if (isNumber(value2)) {
                BigDecimal decimal = new BigDecimal(value1);
                BigDecimal decimal2 = new BigDecimal(value2);
                int result = decimal.compareTo(decimal2);
                return result == 1;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 减法
     *
     * @param value1
     * @param value2
     * @return
     */
    public static String subtract(String value1, String value2) {
        if (isNumber(value1) && isNumber(value2)) {
            BigDecimal decimal = new BigDecimal(value1);
            BigDecimal decimal2 = new BigDecimal(value2);
            return decimal.subtract(decimal2).toString();
        }
        return "";
    }

    /**
     * 减法
     *
     * @param value1
     * @param value2
     * @return
     */
    private static String subtract(double value1, double value2) {
        BigDecimal decimal1 = new BigDecimal(value1);
        BigDecimal decimal2 = new BigDecimal(value2);
        return decimal1.subtract(decimal2).toString();
    }

    /**
     * 减法
     *
     * @param value1
     * @param value2
     * @return
     */
    private static String subtract(String value1, double value2) {
        BigDecimal decimal1 = new BigDecimal(value1);
        BigDecimal decimal2 = new BigDecimal(value2);
        return decimal1.subtract(decimal2).toString();
    }

    /**
     * 除法
     *
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    public static float divide(String value1, String value2, int scale) {
        if (isNumber(value1) && isNumber(value2)) {
            BigDecimal decimal = new BigDecimal(value1);
            BigDecimal decimal2 = new BigDecimal(value2);
            BigDecimal divide = decimal.divide(decimal2, scale, RoundingMode.HALF_UP);
            return divide.floatValue();
        }
        return 0;
    }

    public static String divideDown(String value1, String value2, int scale) {
        if (isNumber(value1) && isNumber(value2)) {
            BigDecimal decimal = new BigDecimal(value1);
            BigDecimal decimal2 = new BigDecimal(value2);
            BigDecimal divide = decimal.divide(decimal2, scale, RoundingMode.HALF_DOWN);
            return divide.toString();
        }
        return "0";
    }
    public static String multiply(String value1, String value2) {
        if (isNumber(value1) && isNumber(value2)) {
            BigDecimal decimal = new BigDecimal(value1);
            BigDecimal decimal2 = new BigDecimal(value2);
            BigDecimal multiplyNum = decimal.multiply(decimal2);
            return multiplyNum.toString();
        }
        return "0";
    }

    public static String formatTenThousand(long value) {
        if (Math.abs(value) < 10000) {
            return String.valueOf(value);
        }
        String prefix = value >= 0 ? "" : "-";
        String result = subZeroAndDot(divide(Math.abs(value) + "", "10000", 1) + "") + "万";
        return prefix + result;
    }

    /**
     * url编码 allow不编码
     *
     * @param uri
     * @return
     */
    public static String encodeURI(String uri) {
        return Uri.encode(uri, ":/-![].,%?&=");
    }
}
