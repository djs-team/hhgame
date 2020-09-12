/****************************************************************************
 Copyright (c) 2015-2016 Chukong Technologies Inc.
 Copyright (c) 2017-2018 Xiamen Yaji Software Co., Ltd.

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
package org.cocos2dx.javascript;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Display;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxHelper;
import org.json.JSONException;
import org.json.JSONObject;


import com.deepsea.mua.core.alipay.Alipay;
import com.deepsea.mua.core.alipay.PayResult;
import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.login.LoginApi;
import com.deepsea.mua.core.login.OnLoginListener;
import com.deepsea.mua.core.utils.NetWorkUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.view.floatwindow.permission.PermissionUtil;
import com.deepsea.mua.core.wxpay.WxPay;
import com.deepsea.mua.core.wxpay.WxpayBroadcast;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.jpush.JpushUtils;
import com.deepsea.mua.stub.jpush.OnJpushClickListener;
import com.deepsea.mua.stub.mvp.NewSubscriberCallBack;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.UserUtils;
import com.hh.game.R;
import com.hhgame.httpClient.httpClient;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jiguang.verifysdk.api.VerifyListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppActivity extends Cocos2dxActivity {

    public static AppActivity ccActivity;

    private static WxPay mWxPay;
    private final String TAG = "AppActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setEnableVirtualButton(false);
        super.onCreate(savedInstanceState);
        // Workaround in https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
        if (!isTaskRoot()) {
            // Android launched another instance of the root activity into an existing task
            //  so just quietly finish and go away, dropping the user back into the activity
            //  at the top of the stack (ie: the last state of this task)
            // Don't need to finish it again since it's finished in super.onCreate .
            return;
        }
        JVerificationInterface.preLogin(this, 1000, new PreLoginListener() {
            @Override
            public void onResult(final int code, final String content) {
                Log.d(TAG, "[" + code + "]message=" + content);
            }
        });

        ccActivity = this;


    }

    //param 是{} 对象的
    public void RunJS_obj(String name, String param) {
        Cocos2dxHelper.runOnGLThread((new Runnable() {
            String js;
            String para;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String command = "cc.eventManager.dispatchCustomEvent('" + js + "'," + para + ")";
                org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge.evalString(command);
            }

            public Runnable setjs(String js, String pa) {
                this.js = js;
                this.para = pa;
                return this;
            }
        }).setjs(name, param));
    }

    public void RunJS(String name, String param) {

        Cocos2dxHelper.runOnGLThread((new Runnable() {

            String js;
            String para;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Log.i("weixin", "js:" + js + "para:" + para);
                String command = "cc.eventManager.dispatchCustomEvent('" + js + "','" + para + "' )";
                Log.i("command", command);
                org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge.evalString(command);
            }

            public Runnable setjs(String js, String pa) {
                this.js = js;
                this.para = pa;
                return this;
            }

        }).setjs(name, param));
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            //失败处理
            return;
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            // 获取照片返回结果
            Log.e("HelloOC:", "getPictureFromPhone -- 获取照片返回结果 data.getData() = " + data.getData());
            String photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Log.e("HelloOC:", "getPictureFromPhone -- photoPath::: " + photoPath);
            JSONObject result = new JSONObject();
            try {
                result.put("photoPath", photoPath);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RunJS_obj("GET_PHOTO_FROM_ALBUM", result.toString());
        }

    }

    /**
     * @param fileName  {String}
     * @param url       {String}
     * @param eventName {String}
     */
    public static void uploadFile(final String fileName, final String url, final String eventName) {
        new Thread() {
            public void run() {
                httpClient http = new httpClient(fileName, url);
                http.uploadFile();
                Log.i("send:", "send successful");
                if (http.ok == 1) {
                    ccActivity.RunJS(eventName, http.filePath);
                }
            }
        }.start();
    }

    public static void uploadPic(final String filePath, final String actionUrlPar, final String eventName, final String token) {
        new Thread() {
            public void run() {
                httpClient http = new httpClient(filePath, actionUrlPar, eventName, token);
                http.uploadFilePic();
                Log.i("send:", "send successful");
                Log.i("send:", "send successful http.ok = " + http.ok);
                if (http.ok == 1) {
                    ccActivity.RunJS(eventName, http.uploadPicReturnData);
                    Log.i("result 333", "getPictureFromPhone -- result hhhhh = " + http.uploadPicReturnData);
                } else if (http.ok == 0) {
                    Log.i("result 444", "getPictureFromPhone -- result faild ");
                    JSONObject result = new JSONObject();
                    try {
                        result.put("errno", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ccActivity.RunJS(eventName, result.toString());
                }
            }
        }.start();
    }

    // 调用相册获取对应的图片
    public static void getPictureFromPhoneAlbum() {
        Log.i("HelloOC", "getPictureFromPhoneAlbum");
        if (ccActivity != null) {
            ccActivity.goPhotoAlbum();
        }
    }

    public void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    /**
     * 微信支付
     *
     * @param rmb
     * @param chargeid
     */
    private static void wxpay(String rmb, String chargeid) {
        String uid = UserUtils.getUser().getUid();
        HttpHelper.instance().getApi(RetrofitApi.class).js_wxpay(rmb, Constant.CHARGE_WX, Constant.CHARGE_NORMAL, Constant.CHARGE_ACT_N, uid, chargeid, "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewSubscriberCallBack<WxOrder>() {
                    @Override
                    protected void onSuccess(WxOrder result) {
                        if (result != null) {
                            WxPay.WXPayBuilder builder = new WxPay.WXPayBuilder();
                            builder.setAppId(result.getAppid());
                            builder.setNonceStr(result.getNoncestr());
                            builder.setPackageValue(result.getPackageX());
                            builder.setPartnerId(result.getPartnerid());
                            builder.setPrepayId(result.getPrepayid());
                            builder.setSign(result.getSign());
                            builder.setTimeStamp(result.getTimestamp());
                            mWxPay = builder.build();
                            mWxPay.startPay(ccActivity);
                            mWxPay.registerWxpayResult(mWxpayReceiver);
                        }

                    }

                    @Override
                    protected void onError(int errorCode, String errorMsg) {

                    }
                });

    }

    private static void alipay(String rmb, String chargeid) {
        String uid = UserUtils.getUser().getUid();
        HttpHelper.instance().getApi(RetrofitApi.class).js_alipay(rmb, Constant.CHARGE_ALI, Constant.CHARGE_NORMAL, Constant.CHARGE_ACT_N, uid, chargeid, "", "", "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewSubscriberCallBack<String>() {
                    @Override
                    protected void onSuccess(String response) {
                        onStartAlipay(response);

                    }

                    @Override
                    protected void onError(int errorCode, String errorMsg) {

                    }
                });
    }

    private static void onStartAlipay(String signature) {
        Alipay alipay = new Alipay(ccActivity);
        alipay.startPay(signature);
        alipay.setAlipayListener(new Alipay.AlipayListener() {
            @Override
            public void onSuccess(PayResult result) {
                onPayResult(0);
                ToastUtils.showToast(result.getResult());
            }

            @Override
            public void onError(String msg) {
                onPayResult(-1);
                ToastUtils.showToast(msg);
            }
        });
    }

    private static WxpayBroadcast.WxpayReceiver mWxpayReceiver = new WxpayBroadcast.WxpayReceiver() {
        @Override
        public void onSuccess() {
            ToastUtils.showToast("成功");
            unregisterWxpayResult();
            onPayResult(0);

        }

        @Override
        public void onError() {
            ToastUtils.showToast("error");
            unregisterWxpayResult();
            onPayResult(-1);
        }
    };

    /**
     * 支付结果 0成功-1失败
     *
     * @param success
     */
    private static void onPayResult(int success) {
        ccActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Cocos2dxJavascriptJavaBridge.evalString("window.myLayer.testAndroid()"); // java to js
            }
        });
    }

    private static void unregisterWxpayResult() {
        if (mWxPay != null) {
            mWxPay.unregisterWxpayResult(mWxpayReceiver);
        }
    }

    /**
     * 登录
     * wx  微信登录
     * jpush 极光一键登录
     */
    public static void login(String type) {
        if (type.equals("wx")) {
            loginWx();
        } else if (type.equals("jpush")) {
            loginOne();
        } else {
            ToastUtils.showToast("不支持的登录方式");
        }
    }

    /**
     * 一键登录
     */
    private static void loginOne() {
        JVerificationInterface.setCustomUIWithConfig(JpushUtils.getDialogLandscapeConfig(ccActivity), JpushUtils.getDialogLandscapeConfig(ccActivity));
        JVerificationInterface.loginAuth(ccActivity, false, new VerifyListener() {
            @Override
            public void onResult(int code, String content, String operator) {
                if (code == 6000) {
                    ToastUtils.showToast(content);
                } else if (code == 6002) {
                    return;
                } else {
//                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                JVerificationInterface.dismissLoginAuthActivity(false, new RequestCallback<String>() {
                    @Override
                    public void onResult(int code, String desc) {
                    }
                });
            }
        }, new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
            }
        });
    }

    /**
     * 微信登录
     */
    private static void loginWx() {
        LoginApi loginApi = new LoginApi();
        loginApi.setPlatform(SHARE_MEDIA.WEIXIN.getName(), ccActivity);
        loginApi.setOnLoginListener(new OnLoginListener() {
            @Override
            public void onLogin(ApiUser apiUser) {
                ToastUtils.showToast(apiUser.getOpenId());
            }

            @Override
            public void onCancel() {
                ToastUtils.showToast("取消登录");
            }

            @Override
            public void onError(String msg) {
                ToastUtils.showToast(msg);
            }
        });
        loginApi.login();

    }

    /**
     * 获取手机信息
     * type:imei
     * model 手机型号
     * rssi wifi强度
     */
    public static void getPhoneInfo(String type) {
        boolean hasPermission = PermissionUtil.hasSelfPermission(ccActivity, "android.permission.READ_PHONE_STATE");
        if (!hasPermission) {
            ActivityCompat.requestPermissions(ccActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            return;
        }
        String info = "";
        switch (type) {
            case "imei":
                info = NetWorkUtils.getImei(ccActivity);
                break;
            case "model":
                info = android.os.Build.MODEL;
                break;
            case "rssi":
                info = NetWorkUtils.getWifiRssi(ccActivity);

                break;
        }

        ccActivity.RunJS("deviceInfo", info);
    }

    public static void toast(String msg) {
        ToastUtils.showToast(msg);
    }

    /**
     * 分享
     * WEIXIN, 微信
     * WEIXIN_CIRCLE 微信朋友圈
     *
     * @param type
     */
    public static void share(String type) {
        String img = "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1812993978,4158651947&fm=26&gp=0.jpg";
        UMImage th = new UMImage(ccActivity, img);//网络图片
        UMImage image = new UMImage(ccActivity, img);//网络图片
        image.setThumb(th);
        if (type.equals("WEIXIN")) {
            ToastUtils.showToast("wexin");
            new ShareAction(ccActivity).setPlatform(SHARE_MEDIA.WEIXIN).withText("hello").withMedia(image).share();
        } else if (type.equals("WEIXIN_CIRCLE")) {
            ToastUtils.showToast("wexin_cicle");

            UMWeb web = new UMWeb("http://www.baidu.com");
            web.setTitle("This is music title");//标题
            web.setThumb(image);  //缩略图
            web.setDescription("my description");//描述
            new ShareAction(ccActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(web).share();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JVerificationInterface.clearPreLoginCache();
        unregisterWxpayResult();
    }


}
