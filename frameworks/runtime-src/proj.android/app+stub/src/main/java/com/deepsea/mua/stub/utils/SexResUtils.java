package com.deepsea.mua.stub.utils;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deepsea.mua.stub.R;

/**
 * Created by JUN on 2019/5/6
 */
public class SexResUtils {

    public static void setSexImg(ImageView sexIv, String sex) {
        if (sex == null) {
            ViewBindUtils.setVisible(sexIv, false);
            return;
        }

        switch (sex) {
            case "1":
                ViewBindUtils.setVisible(sexIv, true);
                sexIv.setImageResource(R.drawable.man);
                break;
            case "2":
                ViewBindUtils.setVisible(sexIv, true);
                sexIv.setImageResource(R.drawable.woman);
                break;
            default:
                ViewBindUtils.setVisible(sexIv, false);
                break;
        }
    }

    /**
     * 发现页设置性别
     *
     * @param sexIv
     * @param sex
     */
    public static void setSexImgInFindPage(RelativeLayout sexGroup, ImageView sexIv, String sex) {
        if (sex == null) {
            ViewBindUtils.setVisible(sexIv, false);
            return;
        }

        switch (sex) {
            case "1":
                ViewBindUtils.setVisible(sexIv, true);
                sexIv.setImageResource(R.drawable.icon_find_sex_man);
                ViewBindUtils.setBackgroundRes(sexGroup, R.drawable.bg_sex_man);
                break;
            case "2":
                ViewBindUtils.setVisible(sexIv, true);
                sexIv.setImageResource(R.drawable.icon_find_sex_women);
                ViewBindUtils.setBackgroundRes(sexGroup, R.drawable.bg_sex_women);
                break;
            default:
                ViewBindUtils.setVisible(sexGroup, false);
                break;
        }
    }

    public static void setSexImg(ImageView sexIv, String sex, int manRes, int womenRes) {
        if (sex == null) {
            ViewBindUtils.setVisible(sexIv, false);
            return;
        }

        switch (sex) {
            case "1":
                ViewBindUtils.setVisible(sexIv, true);
                sexIv.setImageResource(manRes);
                break;
            case "2":
                ViewBindUtils.setVisible(sexIv, true);
                sexIv.setImageResource(womenRes);
                break;
            default:
                ViewBindUtils.setVisible(sexIv, false);
                break;
        }
    }

    public static String getSex(String sex) {
        if (sex == null) {
            return "保密";
        }

        switch (sex) {
            case "1":
                return "男";
            case "2":
                return "女";
            default:
                return "保密";
        }
    }
}
