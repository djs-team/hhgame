package com.deepsea.mua.stub.utils.notch;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by JUN on 2019/7/5
 */
public class HwNotchUtils {

    public static boolean hasNotchInScreen(Context context) {

        boolean ret = false;

        try {

            ClassLoader cl = context.getClassLoader();

            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");

            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");

            ret = (boolean) get.invoke(HwNotchSizeUtil);

        } catch (ClassNotFoundException e) {

            Log.e("test", "hasNotchInScreen ClassNotFoundException");

        } catch (NoSuchMethodException e) {

            Log.e("test", "hasNotchInScreen NoSuchMethodException");

        } catch (Exception e) {

            Log.e("test", "hasNotchInScreen Exception");

        } finally {

            return ret;

        }
    }
}
