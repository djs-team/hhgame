package org.cocos2dx.javascript.ui.login.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;

import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.StringUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.fm.openinstall.OpenInstall;
import com.hh.game.R;
import com.hh.game.databinding.ActivityBindPhoneBinding;

import org.cocos2dx.javascript.ui.login.viewmodel.LoginViewModel;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/15
 */
public class BindPhoneActivity extends BaseActivity<ActivityBindPhoneBinding> {
    @Inject
    ViewModelFactory mViewModelFactory;
    private LoginViewModel mViewModel;
    private String openRes;
    private ApiUser mOpenUser;

    public static Intent newIntent(Context context, String openRes) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra("openRes", openRes);
        return intent;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        openRes = intent.getStringExtra("openRes");
    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        openRes = savedInstanceState.getString("openRes");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mOpenUser != null) {
            outState.putString("openRes", JsonConverter.toJson(mOpenUser));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }


    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel.class);
        if (!TextUtils.isEmpty(openRes)) {
            mOpenUser = JsonConverter.fromJson(openRes, ApiUser.class);
        }
        mBinding.setIsPasswordLogin(false);
        int matchNum = (int) (7000 * Math.random() + 2000);
        ViewBindUtils.setText(mBinding.tvMatchNum, String.valueOf(matchNum));
        initRegisterProtocol();
        initVideView();
    }
    /**
     * 播放视频
     */
    private void initVideView() {
        //播放路径
        mBinding.loginVv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vidio));
        //播放
        mBinding.loginVv.start();
        //循环播放
        mBinding.loginVv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mBinding.loginVv.start();
            }
        });
    }
    //返回重启加载
    @Override
    protected void onRestart() {
        initVideView();
        super.onRestart();
    }
    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        mBinding.loginVv.stopPlayback();
        super.onStop();
    }
    private void initRegisterProtocol() {
        String protocol = "登录注册即同意 《用户协议》 、《合合隐私协议》";
        SpannableString spannableString = new SpannableString(protocol);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public void onClick(@NonNull View widget) {
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getRegisterProtocol());
            }
        }, 7, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);

                ds.setColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public void onClick(@NonNull View widget) {
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getPrivacyPolicy());
            }
        }, 16, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.protocolTv.setHighlightColor(Color.TRANSPARENT);
        mBinding.protocolTv.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.protocolTv.setText(spannableString);
    }

    private void showLoginLoading() {
        GlideUtils.loadGif(mBinding.loginBtn, R.drawable.icon_login_gif_loading);
    }

    private void hideLoginLoading() {
        ViewBindUtils.setImageRes(mBinding.loginBtn, R.drawable.icon_login_arrow);
    }

    public void onClick(View view) {
        //切换登录方式
        if (view == mBinding.vertifyLoginTv) {
            mBinding.setIsPasswordLogin(!mBinding.getIsPasswordLogin());
            mBinding.passwordEdit.setText("");
            mBinding.passwordEdit.setHint(mBinding.getIsPasswordLogin() ? "请输入密码" : "请输入验证码");
            mBinding.vertifyLoginTv.setText(mBinding.getIsPasswordLogin() ? "验证码登录" : "密码登录");
        }

        String account = mBinding.accountEdit.getText().toString();
        String password = mBinding.passwordEdit.getText().toString();
        //登录
        if (view == mBinding.loginBtn) {
            if (!StringUtils.isMobileNO(account)) {
                toastShort("请输入正确的手机号");
                return;
            }
            if (mBinding.passwordEdit.getText().toString().length() != 6) {
                toastShort("请输入正确的验证码");
                return;
            }
            mViewModel.isWxPhoneRegister(account, password).observe(this, new BaseObserver<BaseApiResult>() {
                @Override
                public void onSuccess(BaseApiResult result) {
                    if (result != null) {
                        if (result.getCode() == 200) {
                            //未注册过
                            OpenInstall.reportEffectPoint("mobile_verified", 1);
                            mBinding.passwordEdit.getEditableText().clear();
                            hideLoginLoading();
                            Intent intent = new Intent(mContext, RegisterActivity.class);
                            intent.putExtra("mobile", account);
                            intent.putExtra("mOpenUser", mOpenUser);
                            startActivity(intent);

                        } else {
                            toastShort(result);
                        }
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    super.onError(msg, code);
                }
            });
        }
        //获取验证码
        else if (view == mBinding.getCodeTv) {
            if (!StringUtils.isMobileNO(account)) {
                toastShort("请输入正确的手机号");
                return;
            }
            mViewModel.sendSMS(account, "1").observe(this, new BaseObserver<BaseApiResult>() {
                @Override
                public void onSuccess(BaseApiResult result) {
                    if (result != null) {
                        toastShort(result.getDesc());
                    }
                }
            });
            mViewModel.initSendCaptchaTv(mBinding.getCodeTv);

            mBinding.passwordEdit.setFocusable(true);
            mBinding.passwordEdit.setFocusableInTouchMode(true);
            mBinding.passwordEdit.requestFocus();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            MobEventUtils.onExitEvent(mContext);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.cancelTimer();
    }
}
