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
    private static AdManage singleton = null;
    private String result = "-1";

    private boolean reload = false;//关闭按钮时候再次加载

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public interface OnLoadAdListener {
        void onRewardVideoCached();

        void onVidioPlayComplete(String result);
    }

    private OnLoadAdListener onLoadAdListener;


    private AdManage() {

    }

    public static AdManage getInstance() {
        if (singleton == null) {
            singleton = new AdManage();
        }
        return singleton;
    }

    AdSlot adSlot;

    public void init(Activity mContext) {
        this.mContext = mContext;
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mContext);
        //step3:创建TTAdNative对象,用于调用广告请求接口
        if (mTTAdNative == null) {
            mTTAdNative = ttAdManager.createAdNative(mContext.getApplicationContext());
        }

    }

    private void initSlot(String codeId, String userId) {
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
    }

    private boolean mHasShowDownloadActive = false;


    public void setOnLoadAdListener(OnLoadAdListener onLoadAdListener) {
        this.onLoadAdListener = onLoadAdListener;
    }

    public void loadAd(final String codeId, String userId, boolean isReload) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        //个性化模板广告需要传入期望广告view的宽、高，单位dp，
        this.reload = isReload;
        if (adSlot == null) {
            initSlot(codeId, userId);
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
                if (onLoadAdListener != null && !reload) {
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
                        if (onLoadAdListener != null) {
                            onLoadAdListener.onVidioPlayComplete(result);
                        }
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        result = "0";

                    }

                    @Override
                    public void onVideoError() {
                        result = "-1";
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        result = "0";
                    }

                    @Override
                    public void onSkippedVideo() {
                        result = "-1";
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
