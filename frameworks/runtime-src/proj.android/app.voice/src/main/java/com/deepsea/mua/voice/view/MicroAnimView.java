package com.deepsea.mua.voice.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.utils.GifLoader;

/**
 * Created by JUN on 2019/8/7
 */
public class MicroAnimView extends ImageView {

    private static final int GAME_RESULT_DURATION = 2000;

    private AnimStatus mStatus;

    private GifLoader mLoader;

    public MicroAnimView(Context context) {
        this(context, null);
    }

    public MicroAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MicroAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLoader = new GifLoader();
        mLoader.setOnLoadListener(new GifLoader.OnLoadListener() {
            @Override
            public void onFinished(int duration) {
                if (!isGaming()) {
                    mStatus = AnimStatus.NONE;
                    setVisibility(GONE);
                }
            }

            @Override
            public void onError(String msg) {
                if (!isGaming()) {
                    mStatus = AnimStatus.NONE;
                    setVisibility(GONE);
                }
            }
        });
    }

    private boolean isGaming() {
        return mStatus == AnimStatus.GAME || mStatus == AnimStatus.GAMERESULT;
    }

    /**
     * 加载普通麦位动画
     *
     * @param url
     */
    public void loadNormalAnim(String url) {
        if (!isGaming()) {
            setVisibility(VISIBLE);
            mLoader.removeCallbacksAndMessages();
            mLoader.loadImage(url, this);
            mStatus = AnimStatus.NORMAL;
        }
    }

    /**
     * 加载礼物动画
     *
     * @param url
     */
    public void loadGiftAnim(String url) {
        if (!isGaming()) {
            loadNormalAnim(url);
            mStatus = AnimStatus.GIFT;
        }
    }

    /**
     * 加载游戏动画
     *
     * @param url
     */
    public void loadGameAnim(String url) {
        setVisibility(VISIBLE);
        GlideUtils.loadCircleGif(this, url, R.color.transparent, R.color.transparent);
        mStatus = AnimStatus.GAME;
    }

    /**
     * 加载游戏结果
     *
     * @param url
     */
    public void loadGameResult(String url) {
        if (TextUtils.isEmpty(url)) {
            release();
        } else {
            setVisibility(VISIBLE);
            GlideUtils.circleImage(this, url, R.drawable.ic_place, R.drawable.ic_place);
            mStatus = AnimStatus.GAMERESULT;
        }
    }

    public void release() {
        setVisibility(GONE);
        mStatus = AnimStatus.NONE;
    }

    @Override
    protected void onAttachedToWindow() {
        mStatus = AnimStatus.NONE;
        super.onAttachedToWindow();
    }

    public enum AnimStatus {
        /**
         * 普通麦位动画
         */
        NORMAL,

        /**
         * 礼物动画
         */
        GIFT,

        /**
         * 游戏动画
         */
        GAME,

        /**
         * 游戏结果
         */
        GAMERESULT,

        /**
         * 空状态
         */
        NONE
    }
}
