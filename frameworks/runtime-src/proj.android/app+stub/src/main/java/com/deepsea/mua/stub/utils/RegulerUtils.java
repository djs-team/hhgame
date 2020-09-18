package com.deepsea.mua.stub.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegulerUtils {
    public static String matchNumComma(String num) {
        String regEx = "[^0-9,]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(num);
        String result = m.replaceAll("").trim();
        return result;
    }
    public static String matchNum(String num) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(num);
        String result = m.replaceAll("").trim();
        return result;
    }
}
