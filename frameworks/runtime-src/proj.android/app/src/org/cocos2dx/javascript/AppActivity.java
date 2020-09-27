package org.cocos2dx.javascript;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import org.cocos2dx.javascript.ui.splash.activity.SplashActivity;
import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxHelper;
import org.json.JSONException;
import org.json.JSONObject;

import com.deepsea.mua.advertisement.AdManage;
import com.deepsea.mua.core.alipay.Alipay;
import com.deepsea.mua.core.alipay.PayResult;
import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.login.LoginApi;
import com.deepsea.mua.core.login.OnLoginListener;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.NetWorkUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.view.floatwindow.permission.PermissionUtil;
import com.deepsea.mua.core.wxpay.WxPay;
import com.deepsea.mua.core.wxpay.WxpayBroadcast;
import com.deepsea.mua.stub.entity.ChessLoginParam;
import com.deepsea.mua.stub.entity.InstallParamVo;
import com.deepsea.mua.stub.entity.QPWxOrder;
import com.deepsea.mua.stub.jpush.JpushUtils;
import com.deepsea.mua.stub.permission.PermissionCallback;
import com.deepsea.mua.stub.utils.BitmapUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;
import com.hh.game.R;
import com.hhgame.httpClient.httpClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jiguang.verifysdk.api.VerifyListener;

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
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);

    }

    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            ccActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPrefrencesUtil.saveData(ccActivity, "inviteCode", "inviteCode", bindData);
                    SharedPrefrencesUtil.saveData(ccActivity, "channelCode", "channelCode", channelCode);
                }
            });

        }
    };

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
     * 第三方支付
     *
     * @param type alipay  支付宝
     * @param sign 签名信息
     *             返回onThirdPayCallBack(String code) 0是成功  -1 失败
     */
    public static void thirdPay(String type, String sign) {
        Log.d("==================", "thirdPay:" + "type:" + type + "sign" + sign);
        if (type.equals("alipay")) {
            ccActivity.alipay(sign);
        } else {
            ccActivity.wxpay(sign);
        }

    }

    /**
     * 微信支付
     */
    private void wxpay(String wxInfo) {
        Log.d("===========wxpay", wxInfo);
        String json = JsonConverter.toJson(wxInfo);
        Log.d("===========json", json);
        QPWxOrder result = JsonConverter.fromJson(wxInfo, QPWxOrder.class);
        Log.d("===========QPWxOrder", result.getAppId());

        if (result != null) {
            WxPay.WXPayBuilder builder = new WxPay.WXPayBuilder();
            builder.setAppId(result.getAppId());
            builder.setNonceStr(result.getNoncestr());
            builder.setPackageValue("Sign=WXPay");
            builder.setPartnerId(result.getMch_id());
            builder.setPrepayId(result.getPrepay_id());
            builder.setSign(result.getSign());
            builder.setTimeStamp(result.getTimestamp());
            mWxPay = builder.build();
            mWxPay.startPay(ccActivity);
            mWxPay.registerWxpayResult(mWxpayReceiver);
        } else {
            Log.d("===========", "wxpay null");

        }


    }

    /**
     * 支付宝支付
     *
     * @param signature
     */
    private void alipay(String signature) {
        onStartAlipay(signature);
    }

    private void onStartAlipay(String signature) {
        Alipay alipay = new Alipay(ccActivity);
        alipay.startPay(signature);
        alipay.setAlipayListener(new Alipay.AlipayListener() {
            @Override
            public void onSuccess(PayResult result) {
                Log.d("==================", "thirdPay-ali-result" + result);
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

    private WxpayBroadcast.WxpayReceiver mWxpayReceiver = new WxpayBroadcast.WxpayReceiver() {
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
    private void onPayResult(int success) {
        Log.d("====== wxpay result", success + "");
        ccActivity.RunJS("ThirdPayCallback", String.valueOf(success));
    }

    private void unregisterWxpayResult() {
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
        String[] permission = new String[]{Manifest.permission.READ_PHONE_STATE};
        boolean hasPermission = PermissionUtil.hasSelfPermission(ccActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission) {
            operateLogin(type);
        } else {
            com.deepsea.mua.stub.permission.PermissionUtil.request(ccActivity, permission, new PermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    operateLogin(type);
                }

                @Override
                public void shouldShowRational(String[] rationalPermissons, boolean before) {

                }

                @Override
                public void onPermissonReject(String[] rejectPermissons) {

                }
            });
        }


    }

    private static void operateLogin(String type) {
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
                    String myContent = content.replace("+", "%2B");
                    Log.d("=========content", content);
                    JSONObject result = new JSONObject();
                    try {
                        result.put("platform", 2);
                        result.put("accounttype", 1);
                        result.put("device", Build.MODEL);
                        result.put("phoneModel", "android");
                        result.put("account", myContent);
                        result.put("imei", NetWorkUtils.getImei(ccActivity));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ccActivity.RunJS_obj("THIRD_LOGIN_RESULT", result.toString());
                } else {
                    ToastUtils.showToast("一键登录失败，请尝试其他登录方式" + code);
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
                Log.d("=============", JsonConverter.toJson(apiUser));
                JSONObject result = new JSONObject();
                try {
                    result.put("platform", 3);
                    result.put("accounttype", 0);
                    result.put("device", Build.MODEL);
                    result.put("phoneModel", "android");
                    result.put("unionId", apiUser.getOpenId());
                    result.put("account", apiUser.getOpenId());
                    result.put("imei", NetWorkUtils.getImei(ccActivity));
                    result.put("nickName", apiUser.getUserName());
                    result.put("photo", apiUser.getUserIcon());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ccActivity.RunJS_obj("THIRD_LOGIN_RESULT", result.toString());

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

        String[] permission = new String[]{Manifest.permission.READ_PHONE_STATE};
        boolean hasPermission = PermissionUtil.hasSelfPermission(ccActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission) {
            operateGetphoneInfo(type);
        } else {
            com.deepsea.mua.stub.permission.PermissionUtil.request(ccActivity, permission, new PermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    operateGetphoneInfo(type);
                }

                @Override
                public void shouldShowRational(String[] rationalPermissons, boolean before) {

                }

                @Override
                public void onPermissonReject(String[] rejectPermissons) {

                }
            });
        }
    }

    private static void operateGetphoneInfo(String type) {
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
     * 分享-图片
     *
     * @param platform 平台WEIXIN, 微信  WEIXIN_CIRCLE 微信朋友圈
     * @param fileName 图片名字
     */
    public static void shareImage(String platform, String fileName) {

        File file = ccActivity.getFilesDir();
        File imgFile = new File(file, fileName);
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), getBitmapOption(2));
//        UMImage th = new UMImage(ccActivity, url);//网络图片
        UMImage image = new UMImage(ccActivity, bitmap);//data/data图片
        SHARE_MEDIA share_media;
        if (platform.equals("WEIXIN")) {
            share_media = SHARE_MEDIA.WEIXIN;
        } else {
            share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        new ShareAction(ccActivity).setPlatform(share_media).withMedia(image).share();
    }

    private static BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    /**
     * 分享-文章
     *
     * @param title       标题
     * @param platform    平台WEIXIN, 微信  WEIXIN_CIRCLE 微信朋友圈
     * @param description 描述
     * @param fileName    缩略图
     */
    public static void shareArticle(String platform, String title, String description, String url, String fileName) {
        UMImage thumb = null;
        if (!TextUtils.isEmpty(fileName)) {
            File file = ccActivity.getFilesDir();
            File imgFile = new File(file, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), getBitmapOption(2));
            thumb = new UMImage(ccActivity, bitmap);//data/data图片
        } else {
            thumb = new UMImage(ccActivity, R.mipmap.logo);//本地图片
        }
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(description);//描述
        SHARE_MEDIA share_media;
        if (platform.equals("WEIXIN")) {
            share_media = SHARE_MEDIA.WEIXIN;
        } else {
            share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        new ShareAction(ccActivity).setPlatform(share_media).withMedia(web).share();
    }

    /**
     * 激励视频
     * 返回 rewardVideoCallback（）0成功 -1 失败
     */
    public static void showRewardVideo(String userId) {
        ccActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdManage adManage = new AdManage();
                adManage.init(ccActivity);
                adManage.loadAd("945477184", userId, new AdManage.OnLoadAdListener() {
                    @Override
                    public void onRewardVideoCached() {
                        adManage.playAd();
                    }

                    @Override
                    public void onVidioPlayComplete(String result) {
                        ccActivity.RunJS("rewardVideoCallback", result);
                    }
                });
            }
        });


    }


    /**
     * 生成二维码
     *
     * @param url return 本地路径JPUSH_PKGNAME
     */
    public static void getInvitationCode(String url, String uid) {
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean hasPermission = PermissionUtil.hasSelfPermission(ccActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission) {
            operateGetInvitationCode(url, uid);
        } else {
            com.deepsea.mua.stub.permission.PermissionUtil.request(ccActivity, permission, new PermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    operateGetInvitationCode(url, uid);
                }

                @Override
                public void shouldShowRational(String[] rationalPermissons, boolean before) {

                }

                @Override
                public void onPermissonReject(String[] rejectPermissons) {

                }
            });
        }
    }

    private static void operateGetInvitationCode(String url, String uid) {
        Bitmap mBitmap = CodeUtils.createImage(url, 400, 400, null);
        String picPath = BitmapUtils.saveBitmap(ccActivity, mBitmap, uid);
        ccActivity.RunJS("inviteCodeCallback", picPath);
    }

    /**
     * 申请动态权限
     */
    public static void requestPermission() {
        List<String> mPermissionList = new ArrayList<>();

        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA};
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(ccActivity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        ToastUtils.showToast(mPermissionList.size() + "");
        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(ccActivity, permissions, 3);
        }

    }

    /**
     * 获取安装参数
     */

    public static void getInstallParam() {
        Log.d("OpenInstall", "OpenInstall");

        //获取OpenInstall安装数
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                Log.d("OpenInstall", "getInstall : inviteCode = " + bindData);
                ccActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String pid = "";
                        if (!TextUtils.isEmpty(bindData)) {
                            InstallParamVo vo = JsonConverter.fromJson(bindData, InstallParamVo.class);
                            pid = vo.getInstallPid();
                        }
                        ccActivity.RunJS("installParam", pid);
                    }
                });
            }

            @Override
            public void onInstallFinish(AppData appData, Error error) {
                super.onInstallFinish(appData, error);

            }
        });

    }

    /**
     * 复制到剪切板
     *
     * @param content
     */
    public static void copy(String content) {
        ClipboardManager cm = (ClipboardManager) ccActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(mClipData);
        ToastUtils.showToast("复制成功");
    }

    /**
     * 跳转到相亲
     */
    public static void jumpToBlindDate(String userInfo) {
        Log.d("=====jumpToBlindDate", userInfo);
        ChessLoginParam param = JsonConverter.fromJson(userInfo, ChessLoginParam.class);
        Intent intent = new Intent(ccActivity, SplashActivity.class);
        intent.putExtra("chessLoginParam", param);
        ccActivity.startActivity(intent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JVerificationInterface.clearPreLoginCache();
        unregisterWxpayResult();
        wakeUpAdapter = null;
    }


}
