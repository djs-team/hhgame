package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.faceliveness.helper.AuditHelper;
import com.deepsea.mua.faceliveness.listener.RPAuditListener;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityWebBinding;
import com.deepsea.mua.mine.utils.JS2AndroidUtil;
import com.deepsea.mua.mine.utils.WebViewUtils;
import com.deepsea.mua.mine.viewmodel.H5ViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AuthenticationAlertDialog;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.AuthenticationBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by JUN on 2019/6/27
 */
@Route(path = ArouterConst.PAGE_WEB)
public class WebActivity extends BaseActivity<ActivityWebBinding> {

    private static final String URL = "url";
    private static final String TITLE = "title";

    @Autowired(name = "url")
    String mUrl;
    @Autowired(name = "title")
    String mTitle;

    private WebView mWebView;
    @Inject
    RoomJoinController mRoomJump;
    @Inject
    ViewModelFactory mModelFactory;
    private H5ViewModel mViewModel;

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        mUrl = savedInstanceState.getString(URL);
        mTitle = savedInstanceState.getString(TITLE);
        Log.d("WebActivity",mUrl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL, mUrl);
        outState.putString(TITLE, mTitle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(H5ViewModel.class);

        makeWebView();
        clearCookies();
        addSettings();
        addCallback();
        mBinding.titleBar.setTitle(mTitle);
//        mUrl = WebViewUtils.addTokenToUrl(mUrl);
        mWebView.loadUrl(mUrl);
    }

    private void makeWebView() {
        mWebView = new WebView(mContext);
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.addJavascriptInterface(new JS2AndroidUtil(this), "android");
        mBinding.container.addView(mWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void clearCookies() {
        // Edge case: an illegal state exception is thrown if an instance of
        // CookieSyncManager has not be created.  CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the
        // app, restore saved state, and click logout before running a UI
        // dialog in a WebView -- in which case the app crashes
        try {
            @SuppressWarnings("unused")
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            if (Build.VERSION.SDK_INT < 21) {
                cookieManager.removeAllCookie();
                CookieSyncManager.createInstance(mContext).sync();
            } else {
                cookieManager.removeAllCookies(null);
                cookieManager.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void addSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        //settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setTextZoom(100);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    private void addCallback() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(mTitle)) {
                    return;
                }

                if (!TextUtils.isEmpty(title)) {
                    if (title.length() > 10) {
                        title = title.substring(0, 10);
                        title = title + "...";
                    }
                    mBinding.titleBar.setTitle(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                try {
                    showProgress();
                    mLoadingDialog.setCancelable(true);
                    if (newProgress == 100) {
                        hideProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("PayResult.aspx")){
                    WebActivity.this.finish();
                    return false;
                }
                if (url.startsWith("weixin:")) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    } catch (ActivityNotFoundException localActivityNotFoundException) {
                        Toast.makeText(mContext, "请检查是否安装客户端", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.clearFormData();
            mWebView.removeAllViews();
            mWebView.destroy();
            mRoomJump.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jumpRoom(String roomId) {
        mRoomJump.startJump(roomId, mContext);
    }

    public void gotoAccreditation() {

        mWebView.post(new Runnable() {
            @Override
            public void run() {
                RxPermissions permissions = new RxPermissions(WebActivity.this);
                Disposable subscribe = permissions.request(Manifest.permission.CAMERA)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                getVerifyToken();
                            }
                        });
            }
        });
    }

    /**
     * 实人认证
     */
    private void getVerifyToken() {
        mViewModel.getVerifyToken().observe(this, new BaseObserver<AuditBean>() {
            @Override
            public void onSuccess(AuditBean result) {
                if (result == null)
                    return;
                AuditHelper.start(result.getToken(), mContext, new RPAuditListener() {
                    @Override
                    public void onAuditPass() {
                        createApprove();
                    }

                    @Override
                    public void onAuditNot(String code) {
                        super.onAuditNot(code);
                        switch (code) {
                            case "-1":
                                ToastUtils.showToast("退出认证");
                                break;
                            case "3001":
                                ToastUtils.showToast("认证token无效或已过期");
                                break;
                            case "3101":
                                ToastUtils.showToast("用户姓名身份证实名校验不匹配");
                                break;
                            case "3102":
                                ToastUtils.showToast("实名校验身份证号不存在");
                                break;
                            case "3103":
                                ToastUtils.showToast("实名校验身份证号不合法");
                                break;
                            case "3104":
                                ToastUtils.showToast("认证已通过，重复提交");
                                break;
                            case "3204":
                            case "3206":
                                ToastUtils.showToast("非本人操作");
                                break;
                            case "3208":
                                ToastUtils.showToast("公安网无底照");
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                toastShort(msg);
            }
        });
    }

    private void createApprove() {
        mViewModel.createapprove().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result.getCode()==200) {
                    toastShort(result.getDesc());
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                if (code == 2006) {
                    AuthenticationAlertDialog dialog = new AuthenticationAlertDialog(mContext);
                    dialog.setContent(msg);
                    dialog.show();
                }else {
                    toastShort(msg);
                }
            }
        });

    }

}
