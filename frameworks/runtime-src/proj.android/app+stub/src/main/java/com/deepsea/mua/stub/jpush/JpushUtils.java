package com.deepsea.mua.stub.jpush;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.apiaddress.H5Address;
import com.uuzuche.lib_zxing.DisplayUtil;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;

public class JpushUtils {
    public static JVerifyUIConfig getDialogPortraitConfig(Context mContext, int winWith) {
        int widthDp = DisplayUtil.dip2px(mContext, winWith);
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder().setDialogTheme(widthDp - 60, 300, 0, 0, false);
//        uiConfigBuilder.setLogoHeight(30);
//        uiConfigBuilder.setLogoWidth(30);
//        uiConfigBuilder.setLogoOffsetY(-15);
//        uiConfigBuilder.setLogoOffsetX((widthDp-40)/2-15-20);
//        uiConfigBuilder.setLogoImgPath("logo_login_land");
        uiConfigBuilder.setLogoHidden(true);

        uiConfigBuilder.setNumFieldOffsetY(104).setNumberColor(Color.BLACK);
        uiConfigBuilder.setSloganOffsetY(135);
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setLogBtnOffsetY(161);

        uiConfigBuilder.setPrivacyOffsetY(15);
        uiConfigBuilder.setCheckedImgPath("cb_chosen");
        uiConfigBuilder.setUncheckedImgPath("cb_unchosen");
        uiConfigBuilder.setNumberColor(0xFF222328);
        uiConfigBuilder.setLogBtnImgPath("selector_btn_normal");
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnHeight(44);
        uiConfigBuilder.setLogBtnWidth(250);
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5, 0xFF8998FF);
        uiConfigBuilder.setPrivacyText("登录即同意《", "", "", "》并授权极光认证Demo获取本机号码");
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
//        uiConfigBuilder.setPrivacyOffsetX(52-15);
        uiConfigBuilder.setPrivacyTextSize(10);


        // 图标和标题
        LinearLayout layoutTitle = new LinearLayout(mContext);
        RelativeLayout.LayoutParams layoutTitleParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitleParam.setMargins(0, DisplayUtil.dip2px(mContext, 50), 0, 0);
        layoutTitleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutTitleParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutTitleParam.setLayoutDirection(LinearLayout.HORIZONTAL);
        layoutTitle.setLayoutParams(layoutTitleParam);

        ImageView img = new ImageView(mContext);
        img.setImageResource(R.mipmap.logo);


        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParam.setMargins(0, 0, DisplayUtil.dip2px(mContext, 6), 0);
        layoutTitle.addView(img, imgParam);
        uiConfigBuilder.addCustomView(layoutTitle, false, null);

        // 关闭按钮
        ImageView closeButton = new ImageView(mContext);

        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, DisplayUtil.dip2px(mContext, 10.0f), DisplayUtil.dip2px(mContext, 10), 0);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        closeButton.setLayoutParams(mLayoutParams1);
        closeButton.setImageResource(R.drawable.close);
        uiConfigBuilder.addCustomView(closeButton, true, null);

        return uiConfigBuilder.build();
    }

    public static JVerifyUIConfig getDialogLandscapeConfig(Context mContext) {
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder().setDialogTheme(240, 150, 0, 0, false);
//        uiConfigBuilder.setLogoHeight(40);
//        uiConfigBuilder.setLogoWidth(40);
//        uiConfigBuilder.setLogoOffsetY(-15);
//        uiConfigBuilder.setLogoImgPath("logo_login_land");
        uiConfigBuilder.setLogoHidden(true);

        uiConfigBuilder.setNumFieldOffsetY(37).setNumberColor(Color.BLACK);
        uiConfigBuilder.setNumberSize(12);
        uiConfigBuilder.setSloganOffsetY(60);
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setLogBtnOffsetY(90);

        uiConfigBuilder.setPrivacyOffsetY(10);
        uiConfigBuilder.setCheckedImgPath("cb_chosen");
        uiConfigBuilder.setUncheckedImgPath("cb_unchosen");
        uiConfigBuilder.setNumberColor(0xFF222328);
        uiConfigBuilder.setLogBtnImgPath("selector_btn_normal");
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnTextSize(13);
        uiConfigBuilder.setLogBtnHeight(22);
        uiConfigBuilder.setLogBtnWidth(130);
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5, 0xFF8998FF);
        uiConfigBuilder.setPrivacyText("登录即同意《", "", "", "》并授合合互娱获取本机号码");
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
//        uiConfigBuilder.setPrivacyOffsetX(52-15);
        uiConfigBuilder.setPrivacyTextSize(5);

        // 图标和标题
        LinearLayout layoutTitle = new LinearLayout(mContext);
        RelativeLayout.LayoutParams layoutTitleParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitleParam.setMargins(DisplayUtil.dip2px(mContext, 20), DisplayUtil.dip2px(mContext, 15), 0, 0);
        layoutTitleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutTitleParam.setLayoutDirection(LinearLayout.HORIZONTAL);
        layoutTitle.setLayoutParams(layoutTitleParam);

        ImageView img = new ImageView(mContext);
        img.setImageResource(R.drawable.icon_logo_qipai);


        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(321, 123);
        imgParam.setMargins(0, 0, DisplayUtil.dip2px(mContext, 6), 0);
        layoutTitle.addView(img, imgParam);
        uiConfigBuilder.addCustomView(layoutTitle, false, null);


        return uiConfigBuilder.build();
    }



}
