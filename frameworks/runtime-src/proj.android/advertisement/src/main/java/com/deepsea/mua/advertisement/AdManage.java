package com.deepsea.mua.advertisement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

public class AdManage {
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mIsLoaded = false; //视频是否加载完成
    private Activity mContext;
//    private static AdManage singleton = null;

    public interface OnLoadAdListener {
        void onRewardVideoCached();

        void onVidioPlayComplete(String result);
    }

    private OnLoadAdListener onLoadAdListener;


//    private AdManage() {
//
//    }

//    public static AdManage getInstance() {
//        if (singleton == null) {
//            singleton = new AdManage();
//        }
//        return singleton;
//    }

    public void init(Activity mContext) {
        this.mContext = mContext;
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mContext);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(mContext.getApplicationContext());
    }

    private boolean mHasShowDownloadActive = false;
    boolean mIsExpress = true;

    public void loadAd(final String codeId, String userId, OnLoadAdListener listener) {
        this.onLoadAdListener = listener;
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot;
        if (mIsExpress) {
            //个性化模板广告需要传入期望广告view的宽、高，单位dp，
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(true)
                    .setRewardName("玫瑰") //奖励的名称
                    .setRewardAmount(2)  //奖励的数量
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
                    .setExpressViewAcceptedSize(500, 500)
                    .setUserID(userId)//用户id,必传参数
                    .setMediaExtra("media_extra") //附加参数，可选
                    .setOrientation(TTAdConstant.HORIZONTAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
        } else {
            //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(true)
                    .setRewardName("玫瑰") //奖励的名称
                    .setRewardAmount(2)  //奖励的数量
                    .setUserID(userId)//用户id,必传参数
                    .setMediaExtra("media_extra") //附加参数，可选
                    .setOrientation(TTAdConstant.HORIZONTAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
        }
        if (mTTAdNative == null) {

        }
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                mIsLoaded = true;
                if (onLoadAdListener != null) {
                    onLoadAdListener.onRewardVideoCached();
                }
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {

                mIsLoaded = false;
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {

                    }

                    @Override
                    public void onAdVideoBarClick() {

                    }

                    @Override
                    public void onAdClose() {

                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {

                    }

                    @Override
                    public void onVideoError() {
                        if (onLoadAdListener != null) {
                            onLoadAdListener.onVidioPlayComplete("-1");
                        }
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        if (onLoadAdListener != null) {
                            onLoadAdListener.onVidioPlayComplete("0");
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        if (onLoadAdListener != null) {
                            onLoadAdListener.onVidioPlayComplete("-1");
                        }
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Log.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                    }
                });
            }
        });
    }


    public void playAd() {
        if (mttRewardVideoAd != null && mIsLoaded) {
            //step6:在获取到广告后展示,强烈建议在onRewardVideoCached回调后，展示广告，提升播放体验
            //该方法直接展示广告
//                    mttRewardVideoAd.showRewardVideoAd(RewardVideoActivity.this);

            //展示广告，并传入广告展示的场景
            mttRewardVideoAd.showRewardVideoAd(mContext, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
            mttRewardVideoAd = null;
        }
    }

}
