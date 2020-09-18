package org.cocos2dx.javascript.utils;

import java.io.File;

/**
 * 作者：liyaxing  2019/8/27 09:58
 */
public class FileUtils {

    public static boolean getFileIsExists(String strFile) {// 判断文件是否存在
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
