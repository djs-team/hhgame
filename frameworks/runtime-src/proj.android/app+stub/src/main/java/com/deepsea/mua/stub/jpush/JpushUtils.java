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
        img.setImageResource(R.drawable.icon_login_logo);


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

    public static JVerifyUIConfig getFullScreenLandscapeConfig(Context mContext, String bg, OnJpushClickListener onJpushClickListener) {
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder();

//                .setLogoWidth(70)
//                .setLogoHeight(70)
//
//                .setLogoOffsetY(30)
//                .setNumFieldOffsetY(150)
//                .setSloganOffsetY(185)
//                .setLogBtnOffsetY(210)
//                .setPrivacyOffsetY(30)
        uiConfigBuilder.setStatusBarHidden(false);
        uiConfigBuilder.setSloganTextColor(0xFFFFFFFF);
        uiConfigBuilder.setSloganOffsetY(185);
        uiConfigBuilder.setSloganTextSize(12);
        uiConfigBuilder.setNumFieldOffsetY(70);
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogoImgPath("ic_icon");
        uiConfigBuilder.setNavTransparent(true);
        uiConfigBuilder.setNavReturnImgPath("ic_back");
        uiConfigBuilder.setNavText("登录&注册");
        uiConfigBuilder.setNavTextSize(12);
        uiConfigBuilder.setNavColor(0xFFFFFFFF);

        uiConfigBuilder.setLogoHidden(true);

        uiConfigBuilder.setCheckedImgPath("cb_chosen");
        uiConfigBuilder.setUncheckedImgPath("cb_unchosen");
        uiConfigBuilder.setNumberColor(0x99FFFFFF);
        uiConfigBuilder.setNumberSize(16);
        uiConfigBuilder.setNumberTextBold(true);

        uiConfigBuilder.setLogBtnImgPath("selector_btn_normal");
        uiConfigBuilder.setLogBtnTextColor(0xFFFFFFFF);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnOffsetY(120);
        uiConfigBuilder.setLogBtnWidth(297);
        uiConfigBuilder.setLogBtnHeight(39);
        uiConfigBuilder.setPrivacyCheckboxHidden(false);

//        uiConfigBuilder.setAppPrivacyColor(0xFF999999, 0xFFFFFFFF);
//        uiConfigBuilder.setPrivacyText("登录注册即同意", "和", "以及", "");
//        uiConfigBuilder.setAppPrivacyOne("用户协议", AddressCenter.getAddress().getRegisterProtocol());
//        uiConfigBuilder.setAppPrivacyTwo("合合隐私协议", AddressCenter.getAddress().getPrivacyPolicy());
//        uiConfigBuilder.setPrivacyTextCenterGravity(true);
//        uiConfigBuilder.setPrivacyTextSize(12);


//        uiConfigBuilder.setPrivacyOffsetX(52-15);
        uiConfigBuilder.setPrivacyOffsetY(30);
        uiConfigBuilder.setAuthBGVideoPath(bg, null);


        return uiConfigBuilder.build();
    }

    public static JVerifyUIConfig getFullScreenLandscapeConfig2(Context mContext, String bg, OnJpushClickListener onJpushClickListener) {
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder();
        uiConfigBuilder.setStatusBarHidden(false);
        uiConfigBuilder.setSloganTextColor(0xFFFFFFFF);
        uiConfigBuilder.setSloganOffsetY(286);
        uiConfigBuilder.setSloganTextSize(12);
        uiConfigBuilder.setNumFieldOffsetY(110);
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogoImgPath("ic_icon");
        uiConfigBuilder.setNavTransparent(true);
        uiConfigBuilder.setNavReturnImgPath("ic_back");
        uiConfigBuilder.setNavText("登录&注册");
        uiConfigBuilder.setNavTextSize(18);
        uiConfigBuilder.setNavColor(0xFFFFFFFF);

        uiConfigBuilder.setLogoHidden(true);

        uiConfigBuilder.setCheckedImgPath("cb_chosen");
        uiConfigBuilder.setUncheckedImgPath("cb_unchosen");
        uiConfigBuilder.setNumberColor(0x99FFFFFF);
        uiConfigBuilder.setNumberSize(28);
        uiConfigBuilder.setNumberTextBold(true);
        uiConfigBuilder.setNumFieldOffsetY(240);
        uiConfigBuilder.setLogBtnImgPath("selector_btn_normal");
        uiConfigBuilder.setLogBtnTextColor(0xFFFFFFFF);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnOffsetY(340);
        uiConfigBuilder.setLogBtnWidth(297);
        uiConfigBuilder.setLogBtnHeight(39);
        uiConfigBuilder.setPrivacyCheckboxHidden(false);

        uiConfigBuilder.setAppPrivacyColor(0xFF999999, 0xFFFFFFFF);
        uiConfigBuilder.setPrivacyText("登录注册即同意", "和", "以及", "");
        uiConfigBuilder.setAppPrivacyOne("用户协议", AddressCenter.getAddress().getRegisterProtocol());
        uiConfigBuilder.setAppPrivacyTwo("合合隐私协议", AddressCenter.getAddress().getPrivacyPolicy());
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
        uiConfigBuilder.setPrivacyTextSize(12);


//        uiConfigBuilder.setPrivacyOffsetX(52-15);
        uiConfigBuilder.setPrivacyOffsetY(19);
        uiConfigBuilder.setAuthBGVideoPath(bg, null);


        // 手机登录按钮
        RelativeLayout.LayoutParams layoutParamPhoneLogin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamPhoneLogin.setMargins(0, DisplayUtil.dip2px(mContext, 15.0f), DisplayUtil.dip2px(mContext, 15.0f), 0);
        layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        TextView tvPhoneLogin = new TextView(mContext);
        tvPhoneLogin.setText("手机号码登录");
        tvPhoneLogin.setLayoutParams(layoutParamPhoneLogin);
        uiConfigBuilder.addNavControlView(tvPhoneLogin, new JVerifyUIClickCallback() {
            @Override
            public void onClicked(Context context, View view) {
//                toNativeVerifyActivity();
            }
        });

//        // 微信qq新浪登录
//
//        LinearLayout layoutLoginGroup = new LinearLayout(mContext);
//        RelativeLayout.LayoutParams layoutLoginGroupParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutLoginGroupParam.setMargins(0, DisplayUtil.dip2px(mContext, 235), 0, 0);
//        layoutLoginGroupParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        layoutLoginGroupParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        layoutLoginGroupParam.setLayoutDirection(LinearLayout.HORIZONTAL);
//        layoutLoginGroup.setLayoutParams(layoutLoginGroupParam);
//
//        ImageView btnWechat = new ImageView(mContext);
//
//
//        btnWechat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JShareInterface.authorize(Wechat.Name, mAuthListener);
//
//            }
//        });
//
//
//        btnWechat.setImageResource(R.drawable.ic_wx);

//
//        LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        btnParam.setMargins(25, 0, 25, 0);
//        layoutLoginGroup.addView(btnWechat, btnParam);
//        uiConfigBuilder.addCustomView(layoutLoginGroup, false, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
////                ToastUtil.showToast(MainActivity.this, "功能未实现", 1000);
//            }
//        });

        View matching = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.item_fastlogin_matching, null, false);
        TextView matchMum = matching.findViewById(R.id.tv_match_num);
        int matchNum = (int) (7000 * Math.random() + 2000);
        matchMum.setText(String.valueOf(matchNum));
        uiConfigBuilder.addCustomView(matching, false, null);
        View loginType = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.item_fastlogin_other_login, null, false);
        ImageView phoneLogin = loginType.findViewById(R.id.iv_login_phone);
        ImageView wxLogin = loginType.findViewById(R.id.iv_wx_login);
        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJpushClickListener != null) {
                    onJpushClickListener.phoneLogin();
                }
            }
        });
        wxLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJpushClickListener != null) {
                    onJpushClickListener.wxLogin();
                }
            }
        });

        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, DisplayUtil.dip2px(mContext.getApplicationContext(), 397.0f), 0, 0);
        loginType.setLayoutParams(mLayoutParams1);

        uiConfigBuilder.addCustomView(loginType, false, null);

//        final View dialogViewTitle = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.dialog_login_title,null, false);
//
//        uiConfigBuilder.addNavControlView(dialogViewTitle, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
//
//            }
//        });

//        final View dialogView = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.dialog_login_agreement_land,null, false);
//
//        dialogView.findViewById(R.id.dialog_login_no).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JVerificationInterface.dismissLoginAuthActivity();
//            }
//        });

//        dialogView.findViewById(R.id.dialog_login_yes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogView.setVisibility(View.GONE);
//                dialogViewTitle.setVisibility(View.GONE);
//            }
//        });

//        uiConfigBuilder.addCustomView(dialogView, false, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
////                ToastUtil.showToast(MainActivity.this, "功能未实现", 1000);
//            }
//        });

        return uiConfigBuilder.build();
    }

}
