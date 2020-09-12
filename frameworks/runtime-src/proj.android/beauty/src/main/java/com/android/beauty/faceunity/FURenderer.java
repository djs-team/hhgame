package com.android.beauty.faceunity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.android.beauty.faceunity.entity.Effect;
import com.android.beauty.faceunity.entity.FaceBean;
import com.android.beauty.faceunity.entity.Filter;
import com.android.beauty.faceunity.utils.Constant;
import com.faceunity.wrapper.faceunity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.faceunity.wrapper.faceunity.FU_ADM_FLAG_FLIP_X;

/**
 * 一个基于 Faceunity Nama SDK 的简单封装，方便简单集成，理论上简单需求的步骤：
 * <p>
 * 1.通过 OnEffectSelectedListener 在 UI 上进行交互
 * 2.合理调用 FURenderer 构造函数
 * 3.对应的时机调用 onSurfaceCreated 和 onSurfaceDestroyed
 * 4.处理图像时调用 onDrawFrame
 */
public class FURenderer {
    private static final String TAG = FURenderer.class.getSimpleName();

    public static final int FU_ADM_FLAG_EXTERNAL_OES_TEXTURE = faceunity.FU_ADM_FLAG_EXTERNAL_OES_TEXTURE;

    private Context mContext;

    /**
     * 目录 assets 下的 *.bundle 为程序的数据文件。
     * 其中 v3.bundle：人脸识别数据文件，缺少该文件会导致系统初始化失败；
     * face_beautification.bundle：美颜和美型相关的数据文件；
     * anim_model.bundle：优化表情跟踪功能所需要加载的动画数据文件；适用于使用 Animoji 和 Avatar 功能的用户，如果不是，可不加载
     * ardata_ex.bundle：高精度模式的三维张量数据文件。适用于换脸功能，如果没用该功能可不加载
     * fxaa.bundle：3D 绘制抗锯齿数据文件。加载后，会使得 3D 绘制效果更加平滑。
     * 目录effects下是我们打包签名好的道具
     */
    public static final String BUNDLE_V3 = "v3.bundle";
    // 美颜 bundle
    public static final String BUNDLE_FACE_BEAUTIFICATION = "face_beautification.bundle";


    private volatile static double mFilterLevel = 1.0f; // 滤镜强度
    private volatile static double mSkinDetect = 1.0f; // 肤色检测开关
    private volatile static double mHeavyBlur = 0.0f; // 重度磨皮开关
    private volatile static double mBlurLevel = 0.7f; // 磨皮程度
    private volatile static double mColorLevel = 0.3f; // 美白
    private volatile static double mRedLevel = 0.3f; // 红润
    private volatile static double mEyeBright = 0.0f; // 亮眼
    private volatile static double mToothWhiten = 0.0f;//美牙
    private volatile static double mFaceShape = BeautificationParams.FACE_SHAPE_CUSTOM; // 脸型
    private volatile static double mFaceShapeLevel = 1.0f; // 程度
    private volatile static double mCheekThinning = 0f; // 瘦脸
    private volatile static double mCheekV = 0.5f; // V 脸
    private volatile static double mCheekNarrow = 0f; // 窄脸
    private volatile static double mCheekSmall = 0f; // 小脸
    private volatile static double mEyeEnlarging = 0.4f; // 大眼
    private volatile static double mIntensityChin = 0.3f; // 下巴
    private volatile static double mIntensityForehead = 0.3f; // 额头
    private volatile static double mIntensityMouth = 0.4f; // 嘴形
    private volatile static double mIntensityNose = 0.5f; // 瘦鼻
    private volatile static double mChangeFrames = 0f; // 渐变帧数
    // 默认滤镜，粉嫩效果
    private volatile static String sFilterName = new Filter(Filter.Key.FENNEN_1).filterName();

    private int mFrameId = 0;

    // 句柄索引
    private static final int ITEM_ARRAYS_FACE_BEAUTY_INDEX = 0;
    public static final int ITEM_ARRAYS_EFFECT_INDEX = 1;

    private static final int ITEM_ARRAYS_CHANGE_FACE_INDEX = 6;
    private static final int ITEM_ARRAYS_LIVE_PHOTO_INDEX = 8;
    private static final int ITEM_ARRAYS_FACE_MAKEUP_INDEX = 9;
    public static final int ITEM_ARRAYS_AVATAR_BACKGROUND = 10;
    public static final int ITEM_ARRAYS_AVATAR_HAIR = 11;
    public static final int ITEM_ARRAYS_NEW_FACE_TRACKER = 12;
    // 句柄数量
    private static final int ITEM_ARRAYS_COUNT = 13;


    // 美颜和其他道具的 handle 数组
    private volatile int[] mItemsArray = new int[ITEM_ARRAYS_COUNT];
    // 用于和异步加载道具的线程交互
    private HandlerThread mFuItemHandlerThread;
    private Handler mFuItemHandler;

    private boolean isNeedFaceBeauty = true;
    private volatile Effect mDefaultEffect; // 默认道具（同步加载）
    private boolean mIsCreateEGLContext; //是否需要手动创建 EGLContext
    private int mInputTextureType = 0; // 输入的图像 texture 类型，Camera 提供的默认为 EXTERNAL OES
    private int mInputImageFormat = 0;
    // 美颜和滤镜的默认参数
    private volatile boolean isNeedUpdateFaceBeauty = true;

    private volatile int mInputImageOrientation = 270;
    private volatile int mInputPropOrientation = 270; // 道具方向（针对全屏道具）
    private volatile int mIsInputImage = 0; // 输入的是否是图片
    private volatile int mCurrentCameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private volatile int mMaxFaces = 4; // 同时识别的最大人脸数

    private List<Runnable> mEventQueue;
    private OnBundleLoadCompleteListener mOnBundleLoadCompleteListener;
    private static boolean mIsInited;
    private volatile int mDefaultOrientation = 90;
    private volatile int mRotMode = 1;
    private boolean mNeedBackground;

    /**
     * 创建及初始化 Faceunity 相应的资源
     */
    public void onSurfaceCreated() {
        Log.e(TAG, "onSurfaceCreated");
        onSurfaceDestroyed();

        mEventQueue = Collections.synchronizedList(new ArrayList<Runnable>());

        mFuItemHandlerThread = new HandlerThread("FUItemHandlerThread");
        mFuItemHandlerThread.start();
        mFuItemHandler = new FUItemHandler(mFuItemHandlerThread.getLooper());

        /**
         * fuCreateEGLContext 创建 OpenGL 环境
         * 适用于没 OpenGL 环境时调用
         * 如果调用了 fuCreateEGLContext，在销毁时需要调用 fuReleaseEGLContext
         */
//        if (mIsCreateEGLContext)
//            faceunity.fuCreateEGLContext();

        mFrameId = 0;
        /**
         *fuSetExpressionCalibration 控制表情校准功能的开关及不同模式，参数为 0 时关闭表情校准，2 为被动校准。
         * 被动校准：该种模式下会在整个用户使用过程中逐渐进行表情校准，用户对该过程没有明显感觉。
         *
         * 优化后的 SDK 只支持被动校准功能，即 fuSetExpressionCalibration 接口只支持 0（关闭）或 2（被动校准）这两个数字，设置为 1 时将不再有效果。
//         */
//        faceunity.fuSetExpressionCalibration(2);
//        faceunity.fuSetMaxFaces(mMaxFaces); // 设置多脸，目前最多支持 8 人。

        if (isNeedFaceBeauty) {
            mFuItemHandler.sendEmptyMessage(ITEM_ARRAYS_FACE_BEAUTY_INDEX);
        }
//        if (isNeedBeautyHair) {
//            if (mHairColorType == HAIR_NORMAL) {
//                mFuItemHandler.sendEmptyMessage(ITEM_ARRAYS_EFFECT_HAIR_NORMAL_INDEX);
//            } else {
//                mFuItemHandler.sendEmptyMessage(ITEM_ARRAYS_EFFECT_HAIR_GRADIENT_INDEX);
//            }
//        }
//        if (isNeedAnimoji3D) {
//            mFuItemHandler.sendEmptyMessage(ITEM_ARRAYS_EFFECT_ABIMOJI_3D_INDEX);
//        }
//        if (isNeedPosterFace) {
//            mItemsArray[ITEM_ARRAYS_CHANGE_FACE_INDEX] = loadItem(BUNDLE_CHANGE_FACE);
//        }
//
//        // 设置动漫滤镜
//        int style = mComicFilterStyle;
//        mComicFilterStyle = CartoonFilter.NO_FILTER;
//        onCartoonFilterSelected(style);
//
//        if (mNeedBackground) {
//            loadAvatarBackground();
//        }
//
//        // 异步加载默认道具，放在加载 animoji 3D 和动漫滤镜之后
//        if (mDefaultEffect != null) {
//            mFuItemHandler.sendMessage(Message.obtain(mFuItemHandler, ITEM_ARRAYS_EFFECT_INDEX, mDefaultEffect));
//        }
//
//        // 恢复美妆的参数值
//        if (mMakeupParams.size() > 0) {
//            selectMakeupItem(new HashMap<>(mMakeupParams), false);
//        }
//
//        // 恢复质感美颜的参数值
//        if (mLightMakeupItemMap.size() > 0) {
//            Set<Map.Entry<Integer, MakeupItem>> entries = mLightMakeupItemMap.entrySet();
//            for (Map.Entry<Integer, MakeupItem> entry : entries) {
//                MakeupItem makeupItem = entry.getValue();
//                onLightMakeupSelected(makeupItem, makeupItem.getLevel());
//            }
//        }
        prepareDrawFrame();

        // 设置同步
        setAsyncTrackFace(true);
    }



    /**
     * 获取 Faceunity sdk 版本库
     */
    public static String getVersion() {
        return faceunity.fuGetVersion();
    }

    /**
     * 获取证书相关的权限码
     */
    public static int getModuleCode(int index) {
        return faceunity.fuGetModuleCode(index);
    }

    /**
     * FURenderer 构造函数
     */
    private FURenderer(Context context, boolean isCreateEGLContext) {
        this.mContext = context;
        this.mIsCreateEGLContext = isCreateEGLContext;
    }

    /**
     * 销毁 Faceunity 相关的资源
     */
    public void onSurfaceDestroyed() {
        Log.e(TAG, "onSurfaceDestroyed");
        if (mFuItemHandlerThread != null) {
            mFuItemHandlerThread.quitSafely();
            mFuItemHandlerThread = null;
            mFuItemHandler = null;
        }
        if (mEventQueue != null) {
            mEventQueue.clear();
        }

        int posterIndex = mItemsArray[ITEM_ARRAYS_CHANGE_FACE_INDEX];
        if (posterIndex > 0) {
            faceunity.fuDeleteTexForItem(posterIndex, "tex_input");
            faceunity.fuDeleteTexForItem(posterIndex, "tex_template");
        }

        mFrameId = 0;
        isNeedUpdateFaceBeauty = true;
        Arrays.fill(mItemsArray, 0);
        faceunity.fuDestroyAllItems();
        faceunity.fuOnDeviceLost();
        faceunity.fuDone();
        if (mIsCreateEGLContext)
            faceunity.fuReleaseEGLContext();
    }

    /**
     * 单输入接口(fuRenderToNV21Image)
     *
     * @param img NV21 数据
     * @param w
     * @param h
     * @return
     */
    public int onDrawFrame(byte[] img, int w, int h) {
        if (img == null || w <= 0 || h <= 0) {
            Log.e(TAG, "onDrawFrame data null");
            return 0;
        }
        prepareDrawFrame();

        int flags = mInputImageFormat;
        if (mCurrentCameraType != Camera.CameraInfo.CAMERA_FACING_FRONT)
            flags |= FU_ADM_FLAG_FLIP_X;

        if (mNeedBenchmark)
            mFuCallStartTime = System.nanoTime();
        int fuTex = faceunity.fuRenderToNV21Image(img, w, h, mFrameId++, mItemsArray, flags);
        if (mNeedBenchmark)
            mOneHundredFrameFUTime += System.nanoTime() - mFuCallStartTime;
        return fuTex;
    }

    /**
     * 单输入接口(fuRenderToNV21Image)，自定义画面数据需要回写到的 byte[]
     *
     * @param img         NV21数据
     * @param w
     * @param h
     * @param readBackImg 画面数据需要回写到的byte[]
     * @param readBackW
     * @param readBackH
     * @return
     */
    public int onDrawFrame(byte[] img, int w, int h, byte[] readBackImg, int readBackW, int readBackH) {
        if (img == null || w <= 0 || h <= 0 || readBackImg == null || readBackW <= 0 || readBackH <= 0) {
            Log.e(TAG, "onDrawFrame data null");
            return 0;
        }
        prepareDrawFrame();

        int flags = mInputImageFormat;
        if (mCurrentCameraType != Camera.CameraInfo.CAMERA_FACING_FRONT)
            flags |= FU_ADM_FLAG_FLIP_X;

        if (mNeedBenchmark)
            mFuCallStartTime = System.nanoTime();
        int fuTex = faceunity.fuRenderToNV21Image(img, w, h, mFrameId++, mItemsArray, flags,
                readBackW, readBackH, readBackImg);
        if (mNeedBenchmark)
            mOneHundredFrameFUTime += System.nanoTime() - mFuCallStartTime;
        return fuTex;
    }

    /**
     * 双输入接口(fuDualInputToTexture)，处理后的画面数据并不会回写到数组，由于省去相应的数据拷贝性能相对最优，推荐使用。
     *
     * @param img NV21数据
     * @param tex 纹理ID
     * @param w
     * @param h
     * @return
     */
    public int onDrawFrame(boolean needBeauty,byte[] img, int tex, int w, int h) {
        if (tex <= 0 || img == null || w <= 0 || h <= 0) {
            Log.e(TAG, "onDrawFrame data null");
            return 0;
        }
        if (needBeauty)
        prepareDrawFrame();

        int flags = mInputTextureType | mInputImageFormat;
        if (mCurrentCameraType != Camera.CameraInfo.CAMERA_FACING_FRONT)
            flags |= FU_ADM_FLAG_FLIP_X;

        if (mNeedBenchmark)
            mFuCallStartTime = System.nanoTime();
        int fuTex = faceunity.fuDualInputToTexture(img, tex, flags, w, h, mFrameId++, mItemsArray);
        if (mNeedBenchmark)
            mOneHundredFrameFUTime += System.nanoTime() - mFuCallStartTime;
        return fuTex;
    }
    /**
     * 双输入接口(fuDualInputToTexture)，处理后的画面数据并不会回写到数组，由于省去相应的数据拷贝性能相对最优，推荐使用。
     *
     * @param img NV21数据
     * @param tex 纹理ID
     * @param w
     * @param h
     * @return
     */
    public int onDrawFrameNew(byte[] img, int tex, int w, int h) {
        if (tex <= 0 || img == null || w <= 0 || h <= 0) {
            Log.e(TAG, "onDrawFrame data null");
            return 0;
        }

        int flags = mInputTextureType | mInputImageFormat;
        if (mCurrentCameraType != Camera.CameraInfo.CAMERA_FACING_FRONT)
            flags |= FU_ADM_FLAG_FLIP_X;

        if (mNeedBenchmark)
            mFuCallStartTime = System.nanoTime();
        int fuTex = faceunity.fuDualInputToTexture(img, tex, flags, w, h, mFrameId++, mItemsArray);
        if (mNeedBenchmark)
            mOneHundredFrameFUTime += System.nanoTime() - mFuCallStartTime;
        return fuTex;
    }

    /**
     * 双输入接口(fuDualInputToTexture)，自定义画面数据需要回写到的 byte[]
     *
     * @param img         NV21数据
     * @param tex         纹理ID
     * @param w
     * @param h
     * @param readBackImg 画面数据需要回写到的byte[]
     * @param readBackW
     * @param readBackH
     * @return
     */
    public int onDrawFrame(byte[] img, int tex, int w, int h, byte[] readBackImg, int readBackW, int readBackH) {
        if (tex <= 0 || img == null || w <= 0 || h <= 0 || readBackImg == null || readBackW <= 0 || readBackH <= 0) {
            Log.e(TAG, "onDrawFrame data null");
            return 0;
        }
        prepareDrawFrame();

        int flags = mInputTextureType | mInputImageFormat;
        if (mCurrentCameraType != Camera.CameraInfo.CAMERA_FACING_FRONT)
            flags |= FU_ADM_FLAG_FLIP_X;

        if (mNeedBenchmark)
            mFuCallStartTime = System.nanoTime();
        int fuTex = faceunity.fuDualInputToTexture(img, tex, flags, w, h, mFrameId++, mItemsArray,
                readBackW, readBackH, readBackImg);
        if (mNeedBenchmark)
            mOneHundredFrameFUTime += System.nanoTime() - mFuCallStartTime;
        return fuTex;
    }

    /**
     * 单输入接口(fuRenderToTexture)
     *
     * @param tex 纹理ID
     * @param w
     * @param h
     * @return
     */
    public int onDrawFrame(int tex, int w, int h) {
        if (tex <= 0 || w <= 0 || h <= 0) {
            Log.e(TAG, "onDrawFrame data null");
            return 0;
        }
        prepareDrawFrame();

        int flags = mInputTextureType;
        if (mCurrentCameraType != Camera.CameraInfo.CAMERA_FACING_FRONT)
            flags |= FU_ADM_FLAG_FLIP_X;

        if (mNeedBenchmark)
            mFuCallStartTime = System.nanoTime();
        int fuTex = faceunity.fuRenderToTexture(tex, w, h, mFrameId++, mItemsArray, flags);
        if (mNeedBenchmark)
            mOneHundredFrameFUTime += System.nanoTime() - mFuCallStartTime;
        return fuTex;
    }

    /**
     * 单美颜接口(fuBeautifyImage)，将输入的图像数据，送入SDK流水线进行全图美化，并输出处理之后的图像数据。
     * 该接口仅执行图像层面的美化处 理（包括滤镜、美肤），不执行人脸跟踪及所有人脸相关的操作（如美型）。
     * 由于功能集中，相比 fuDualInputToTexture 接口执行美颜道具，该接口所需计算更少，执行效率更高。
     *
     * @param tex 纹理ID
     * @param w
     * @param h
     * @return
     */
    public int onDrawFrameBeautify(int tex, int w, int h) {
        if (tex <= 0 || w <= 0 || h <= 0) {
            Log.e(TAG, "onDrawFrame data null");
            return 0;
        }
        prepareDrawFrame();

        int flags = mInputTextureType;

        if (mNeedBenchmark)
            mFuCallStartTime = System.nanoTime();
        int fuTex = faceunity.fuBeautifyImage(tex, flags, w, h, mFrameId++, mItemsArray);
        if (mNeedBenchmark)
            mOneHundredFrameFUTime += System.nanoTime() - mFuCallStartTime;
        return fuTex;
    }


    /**
     * 全局加载相应的底层数据包，应用使用期间只需要初始化一次
     * 初始化系统环境，加载系统数据，并进行网络鉴权。必须在调用 SDK 其他接口前执行，否则会引发崩溃。
     */
    public static void initFURenderer(Context context) {
        if (mIsInited) {
            return;
        }
        try {
            // 获取 faceunity SDK 版本信息
            Log.e(TAG, "fu sdk version " + faceunity.fuGetVersion());
            long startTime = System.currentTimeMillis();
            /**
             * fuSetup faceunity 初始化
             * 其中 v3.bundle：人脸识别数据文件，缺少该文件会导致系统初始化失败；
             *      authpack：用于鉴权证书内存数组。
             * 首先调用完成后再调用其他 FU API
             */
            InputStream v3 = context.getAssets().open(BUNDLE_V3);
            byte[] v3Data = new byte[v3.available()];
            v3.read(v3Data);
            v3.close();
            faceunity.fuSetup(v3Data, authpack.A());


            long duration = System.currentTimeMillis() - startTime;
            Log.i(TAG, "setup fu sdk finish: " + duration + "ms");
        } catch (Exception e) {
            Log.e(TAG, "initFURenderer error", e);
        }
        mIsInited = true;
    }





    //--------------------------------------对外可使用的接口----------------------------------------




    /**
     * 保存和退出，二选一即可
     * 直接退出捏脸状态，不保存当前捏脸状态，进入跟踪状态。使用上一次捏脸，进行人脸表情跟踪。
     */
    public void quitFaceup() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], "quit_facepup", 1);
                }
            }
        });
    }

    /**
     * 触发保存捏脸，并退出捏脸状态，进入跟踪状态。耗时操作，必要时设置。
     */
    public void recomputeFaceup() {
//        Log.d(TAG, "recomputeFaceup() called");
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], "need_recompute_facepup", 1);
                }
            }
        });
    }

    /**
     * 设置捏脸属性的权值，范围 [0-1]。这里 param 对应的就是第几个捏脸属性，从 1 开始。
     *
     * @param key
     * @param value
     */
    public void fuItemSetParamFaceup(final String key, final double value) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "fuItemSetParamFaceup() called , key:" + key + ", value:" + value + ", handle " + mItemsArray[ITEM_ARRAYS_EFFECT_INDEX]);
                if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], "{\"name\":\"facepup\",\"param\":\"" + key + "\"}", value);
                }
            }
        });
    }

    /**
     * 获取捏脸属性
     *
     * @param key
     * @return
     */
    public float fuItemGetParamFaceup(final String key) {
        return (float) faceunity.fuItemGetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], "{\"name\":\"facepup\",\"param\":\"" + key + "\"}");
    }

    /**
     * 设置 avatar 颜色参数
     *
     * @param key
     * @param value [r,g,b] 或 [r,g,b,intensity]
     */
    public void fuItemSetParamFaceColor(final String key, final double[] value) {
//        Log.d(TAG, "fuItemSetParamFaceColor() called with: key = [" + key + "], value = [" + Arrays.toString(value) + "]");
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (value.length > 3) {
                    if (mItemsArray[ITEM_ARRAYS_AVATAR_HAIR] > 0) {
                        faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_AVATAR_HAIR], key, value);
                    }
                } else {
                    if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0) {
                        faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], key, value);
                    }
                }
            }
        });
    }

    /**
     * 表情动图的插值开关，开启后会给点位加个包围圈，五官距离近时容易相互影响；关闭后不加包围圈，单五官时影响会比较大。
     *
     * @param useInterpolate2
     */
    public void setUseInterpolate2(final boolean useInterpolate2) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_LIVE_PHOTO_INDEX] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_LIVE_PHOTO_INDEX], "use_interpolate2", useInterpolate2 ? 1 : 0);
                }
            }
        });
    }


    /**
     * whether avatar bundle is loaded
     *
     * @return
     */
    public boolean isAvatarLoaded() {
        return mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0;
    }

    /**
     * whether avatar hair and background bundle is loaded
     *
     * @return
     */
    public boolean isAvatarMakeupItemLoaded() {
        return mItemsArray[ITEM_ARRAYS_AVATAR_BACKGROUND] > 0 && mItemsArray[ITEM_ARRAYS_AVATAR_HAIR] > 0;
    }

    /**
     * （参数为浮点数）,直接设置绝对缩放
     *
     * @param scale
     */
    public void setAvatarScale(final float scale) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], "absoluteScale", scale);
                }
            }
        });
    }

    /**
     * （参数为浮点数）,直接设置绝对缩放
     *
     * @param scale
     */
    public void setAvatarHairScale(final float scale) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_AVATAR_HAIR] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_AVATAR_HAIR], "absoluteScale", scale);
                }
            }
        });
    }

    /**
     * （参数为[x,y,z]数组）,直接设置绝对位移
     *
     * @param xyz
     */
    public void setAvatarTranslate(final double[] xyz) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], "absoluteTranslate", xyz);
                }
            }
        });
    }

    /**
     * （参数为[x,y,z]数组）,直接设置绝对位移
     *
     * @param xyz
     */
    public void setAvatarHairTranslate(final double[] xyz) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_AVATAR_HAIR] > 0) {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_AVATAR_HAIR], "absoluteTranslate", xyz);
                }
            }
        });
    }

    /**
     * 类似 GLSurfaceView 的 queueEvent 机制
     */
    public void queueEvent(Runnable r) {
        if (mEventQueue == null)
            return;
        mEventQueue.add(r);
    }

    /**
     * 类似 GLSurfaceView 的 queueEvent 机制,保护在快速切换界面时进行的操作是当前界面的加载操作
     */
    private void queueEventItemHandle(Runnable r) {
        if (mFuItemHandlerThread == null || Thread.currentThread().getId() != mFuItemHandlerThread.getId())
            return;
        queueEvent(r);
    }

    /**
     * 设置人脸跟踪异步
     *
     * @param isAsync 是否异步
     */
    public void setAsyncTrackFace(final boolean isAsync) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "setAsyncTrackFace " + isAsync);
                faceunity.fuSetAsyncTrackFace(isAsync ? 1 : 0);
            }
        });
    }

    /**
     * 设置需要识别的人脸个数
     *
     * @param maxFaces
     */
    public void setMaxFaces(final int maxFaces) {
        if (mMaxFaces != maxFaces && maxFaces > 0) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mMaxFaces = maxFaces;
                    faceunity.fuSetMaxFaces(mMaxFaces);
                }
            });
        }
    }

    /**
     * 表情动图切换相机时，设置方向
     *
     * @param isFront
     */
    public void setIsFrontCamera(final boolean isFront) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (isFront && mInputImageOrientation == 90) {
                    // 解决 Nexus 手机前置相机发生X镜像的问题
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_LIVE_PHOTO_INDEX], "is_swap_x", 1);
                } else {
                    faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_LIVE_PHOTO_INDEX], "is_swap_x", 0);
                }
                faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_LIVE_PHOTO_INDEX], "is_front", isFront ? 1 : 0);
            }
        });
    }

    /**
     * 每帧处理画面时被调用
     */
    private void prepareDrawFrame() {
        // 计算 FPS 等数据
//        benchmarkFPS();

//        // 获取人脸是否识别，并调用回调接口
//        int isTracking = faceunity.fuIsTracking();
//        if (mOnTrackingStatusChangedListener != null && mTrackingStatus != isTracking) {
//            mOnTrackingStatusChangedListener.onTrackingStatusChanged(mTrackingStatus = isTracking);
//        }

//        // 获取 faceunity 错误信息，并调用回调接口
//        int error = faceunity.fuGetSystemError();
//        if (error != 0) {
//            Log.e(TAG, "fuGetSystemErrorString " + faceunity.fuGetSystemErrorString(error));
//            if (mOnSystemErrorListener != null) {
//                mOnSystemErrorListener.onSystemError(faceunity.fuGetSystemErrorString(error));
//            }
//        }
        // 修改美颜参数
        if (isNeedUpdateFaceBeauty && mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX] > 0) {
            // filter_name 滤镜名称
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FILTER_NAME, sFilterName);
            // filter_level 滤镜强度 范围 0~1 SDK 默认为 1
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FILTER_LEVEL, mFilterLevel);

            // skin_detect 精准美肤（肤色检测开关） 0:关闭 1:开启 SDK默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.SKIN_DETECT, mSkinDetect);
            // heavy_blur 磨皮类型 0:清晰磨皮 1:重度磨皮 SDK默认为 1
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.HEAVY_BLUR, mHeavyBlur);
            // blur_level 磨皮 范围 0~6 SDK 默认为 6
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.BLUR_LEVEL, 6 * mBlurLevel);
            // nonskin_blur_scale 肤色检测之后，非肤色区域的融合程度，范围 0-1，SDK 默认为0.45
//            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.NONSKIN_BLUR_SCALE, 0);
            // color_level 美白 范围 0~1 SDK 默认为 0.2
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.COLOR_LEVEL, mColorLevel);
            // red_level 红润 范围 0~1 SDK默认为 0.5
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.RED_LEVEL, mRedLevel);
            // eye_bright 亮眼 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.EYE_BRIGHT, mEyeBright);
            // tooth_whiten 美牙 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.TOOTH_WHITEN, mToothWhiten);

            // face_shape_level 美型程度 范围 0~1 SDK 默认为 1
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FACE_SHAPE_LEVEL, mFaceShapeLevel);
            // face_shape 脸型 0：女神 1：网红，2：自然，3：默认，4：精细变形，5 用户自定义，SDK 默认为 3
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FACE_SHAPE, mFaceShape);
            // eye_enlarging 大眼 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.EYE_ENLARGING, mEyeEnlarging);
            // cheek_thinning 瘦脸 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_THINNING, mCheekThinning);
            // cheek_narrow 窄脸 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_NARROW, mCheekNarrow);
            // cheek_small 小脸 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_SMALL, mCheekSmall);
            // cheek_v V 脸 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_V, mCheekV);
            // intensity_nose 鼻子 范围 0~1 SDK 默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_NOSE, mIntensityNose);
            // intensity_chin 下巴 范围 0~1 SDK 默认为 0.5    大于 0.5 变大，小于 0.5 变小
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_CHIN, mIntensityChin);
            // intensity_forehead 额头 范围 0~1 SDK默认为 0.5    大于 0.5 变大，小于 0.5 变小
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_FOREHEAD, mIntensityForehead);
            // intensity_mouth 嘴型 范围 0~1 SDK 默认为 0.5   大于 0.5 变大，小于 0.5 变小
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_MOUTH, mIntensityMouth);
            // change_frame 变形渐变调整参数，0 渐变关闭，大于 0 渐变开启，值为渐变需要的帧数
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHANGE_FRAMES, mChangeFrames);
            isNeedUpdateFaceBeauty = false;
        }

        if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0 && mDefaultEffect != null &&
                mDefaultEffect.effectType() == Effect.EFFECT_TYPE_GESTURE) {
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX], "rotMode", mRotMode);
        }
        //queueEvent的Runnable在此处被调用
        while (!mEventQueue.isEmpty()) {
            mEventQueue.remove(0).run();
        }
    }
public void  setNeedFaceBeauty(){
        isNeedFaceBeauty=true;
}
    public void setFaceParam(FaceBean bean) {
        if (mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX] > 0) {
            // filter_name 滤镜名称
            sFilterName=bean.getSelectedFilter();
            mFilterLevel=bean.getSelectedFilterLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FILTER_NAME, sFilterName);
            // filter_level 滤镜强度 范围 0~1 SDK 默认为 1
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FILTER_LEVEL,mFilterLevel);

            // skin_detect 精准美肤（肤色检测开关） 0:关闭 1:开启 SDK默认为 0
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.SKIN_DETECT, bean.isSkinDetectEnable()?1:0);
            // heavy_blur 磨皮类型 0:清晰磨皮 1:重度磨皮 SDK默认为 1
            mHeavyBlur=bean.getBlurType();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.HEAVY_BLUR, mHeavyBlur);
            // blur_level 磨皮 范围 0~6 SDK 默认为 6
            double mBlurLevel=1;
            if (mHeavyBlur==0){
                mBlurLevel=bean.getBlurLevel_0();
            }else if (mHeavyBlur==1){
                mBlurLevel=bean.getBlurLevel_1();
            }else {
                mBlurLevel=bean.getBlurLevel_2();
            }
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.BLUR_LEVEL, 6 * mBlurLevel);
            // nonskin_blur_scale 肤色检测之后，非肤色区域的融合程度，范围 0-1，SDK 默认为0.45
//            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.NONSKIN_BLUR_SCALE, 0);
            // color_level 美白 范围 0~1 SDK 默认为 0.2
            mColorLevel=bean.getWhiteLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.COLOR_LEVEL, mColorLevel);
            // red_level 红润 范围 0~1 SDK默认为 0.5
            mRedLevel=bean.getRedLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.RED_LEVEL, mRedLevel);
            // eye_bright 亮眼 范围 0~1 SDK 默认为 0
            mEyeBright=bean.getEyelightingLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.EYE_BRIGHT,mEyeBright);
            // tooth_whiten 美牙 范围 0~1 SDK 默认为 0
            mToothWhiten= bean.getBeautyToothLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.TOOTH_WHITEN, mToothWhiten);

            // face_shape_level 美型程度 范围 0~1 SDK 默认为 1
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FACE_SHAPE_LEVEL, 1);
            // face_shape 脸型 0：女神 1：网红，2：自然，3：默认，4：精细变形，5 用户自定义，SDK 默认为 3
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.FACE_SHAPE, 3);
            // eye_enlarging 大眼 范围 0~1 SDK 默认为 0
            mEyeEnlarging=bean.getEnlargingLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.EYE_ENLARGING, mEyeEnlarging);
            // cheek_thinning 瘦脸 范围 0~1 SDK 默认为 0
            mCheekThinning= bean.getThinningLevel();

            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_THINNING, mCheekThinning);
            // cheek_narrow 窄脸 范围 0~1 SDK 默认为 0
            mCheekNarrow=bean.getNarrowLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_NARROW, mCheekNarrow);
            // cheek_small 小脸 范围 0~1 SDK 默认为 0
            mCheekSmall= bean.getSmallLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_SMALL,mCheekSmall);
            // cheek_v V 脸 范围 0~1 SDK 默认为 0
           mCheekV= bean.getVLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHEEK_V, mCheekV);
            // intensity_nose 鼻子 范围 0~1 SDK 默认为 0
            mIntensityNose=bean.getNoseLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_NOSE, mIntensityNose);
            // intensity_chin 下巴 范围 0~1 SDK 默认为 0.5    大于 0.5 变大，小于 0.5 变小
            mIntensityChin=bean.getJewLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_CHIN,mIntensityChin) ;
            // intensity_forehead 额头 范围 0~1 SDK默认为 0.5    大于 0.5 变大，小于 0.5 变小
            mIntensityForehead= bean.getForeheadLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_FOREHEAD,mIntensityForehead);
            // intensity_mouth 嘴型 范围 0~1 SDK 默认为 0.5   大于 0.5 变大，小于 0.5 变小
            mIntensityMouth=bean.getMouthLevel();
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.INTENSITY_MOUTH,mIntensityMouth );
            // change_frame 变形渐变调整参数，0 渐变关闭，大于 0 渐变开启，值为渐变需要的帧数
            faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX], BeautificationParams.CHANGE_FRAMES, 0);
        }

    }




    //--------------------------------------美颜参数与道具回调----------------------------------------

    public void loadAvatarBackground() {
        mNeedBackground = true;
        if (mFuItemHandler == null) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mFuItemHandler.sendEmptyMessage(ITEM_ARRAYS_AVATAR_BACKGROUND);
                }
            });
        } else {
            mFuItemHandler.sendEmptyMessage(ITEM_ARRAYS_AVATAR_BACKGROUND);
        }
    }

    public void unloadAvatarBackground() {
        mNeedBackground = false;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mItemsArray[ITEM_ARRAYS_AVATAR_BACKGROUND] > 0) {
                    faceunity.fuDestroyItem(mItemsArray[ITEM_ARRAYS_AVATAR_BACKGROUND]);
                    mItemsArray[ITEM_ARRAYS_AVATAR_BACKGROUND] = 0;
                }
            }
        });
    }



    public void fixPosterFaceParam(float value) {
        faceunity.fuItemSetParam(mItemsArray[ITEM_ARRAYS_CHANGE_FACE_INDEX], "warp_intensity", value);
    }






    //--------------------------------------IsTracking（人脸识别回调相关定义）----------------------------------------

    private int mTrackingStatus = 0;

    public interface OnTrackingStatusChangedListener {
        void onTrackingStatusChanged(int status);
    }

    private OnTrackingStatusChangedListener mOnTrackingStatusChangedListener;

    //--------------------------------------FaceUnitySystemError（faceunity错误信息回调相关定义）----------------------------------------

    public interface OnSystemErrorListener {
        void onSystemError(String error);
    }

    private OnSystemErrorListener mOnSystemErrorListener;


    //--------------------------------------OnBundleLoadCompleteListener（faceunity道具加载完成）----------------------------------------

    public void setOnBundleLoadCompleteListener(OnBundleLoadCompleteListener onBundleLoadCompleteListener) {
        mOnBundleLoadCompleteListener = onBundleLoadCompleteListener;
    }

    /**
     * fuCreateItemFromPackage 加载道具
     *
     * @param bundlePath 道具 bundle 的路径
     * @return 大于 0 时加载成功
     */
    private int loadItem(String bundlePath) {
        int item = 0;
        try {
            if (!TextUtils.isEmpty(bundlePath)) {
                InputStream is = bundlePath.startsWith(Constant.filePath) ? new FileInputStream(new File(bundlePath)) : mContext.getAssets().open(bundlePath);
                byte[] itemData = new byte[is.available()];
                int len = is.read(itemData);
                is.close();
                item = faceunity.fuCreateItemFromPackage(itemData);
                Log.e(TAG, "bundle path: " + bundlePath + ", length: " + len + "Byte, handle:" + item);
            }
        } catch (IOException e) {
            Log.e(TAG, "loadItem error ", e);
        }
        return item;
    }


    //--------------------------------------FPS（FPS相关定义）----------------------------------------

    private static final float NANO_IN_ONE_MILLI_SECOND = 1000000.0f;
    private static final float TIME = 1f;
    private int mCurrentFrameCnt = 0;
    private long mLastOneHundredFrameTimeStamp = 0;
    private long mOneHundredFrameFUTime = 0;
    private boolean mNeedBenchmark = true;
    private long mFuCallStartTime = 0;

    private OnFUDebugListener mOnFUDebugListener;

    public interface OnFUDebugListener {
        void onFpsChange(double fps, double renderTime);
    }

    private void benchmarkFPS() {
        if (!mNeedBenchmark)
            return;
        if (++mCurrentFrameCnt == TIME) {
            mCurrentFrameCnt = 0;
            long tmp = System.nanoTime();
            double fps = (1000.0f * NANO_IN_ONE_MILLI_SECOND / ((tmp - mLastOneHundredFrameTimeStamp) / TIME));
            mLastOneHundredFrameTimeStamp = tmp;
            double renderTime = mOneHundredFrameFUTime / TIME / NANO_IN_ONE_MILLI_SECOND;
            mOneHundredFrameFUTime = 0;

            if (mOnFUDebugListener != null) {
                mOnFUDebugListener.onFpsChange(fps, renderTime);
            }
        }
    }

    //--------------------------------------道具（异步加载道具）----------------------------------------

    /**
     * 加载美妆资源数据
     *
     * @param path
     * @return bytes, width and height
     * @throws IOException
     */
    private Pair<byte[], Pair<Integer, Integer>> loadMakeupResource(String path) throws IOException {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        try {
            is = mContext.getAssets().open(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            int bmpByteCount = bitmap.getByteCount();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            byte[] bitmapBytes = new byte[bmpByteCount];
            ByteBuffer byteBuffer = ByteBuffer.wrap(bitmapBytes);
            bitmap.copyPixelsToBuffer(byteBuffer);
            return Pair.create(bitmapBytes, Pair.create(width, height));
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public interface OnBundleLoadCompleteListener {
        /**
         * bundle 加载完成
         *
         * @param what
         */
        void onBundleLoadComplete(int what);
    }


    /**
     * 设置对道具设置相应的参数
     *
     * @param itemHandle
     */
    private void updateEffectItemParams(Effect effect, final int itemHandle) {
        if (effect == null || itemHandle == 0)
            return;
        if (mIsInputImage == 1) {
            faceunity.fuItemSetParam(itemHandle, "isAndroid", 0.0);
        } else {
            faceunity.fuItemSetParam(itemHandle, "isAndroid", 1.0);
        }

        int effectType = effect.effectType();
        if (effectType == Effect.EFFECT_TYPE_NORMAL) {
            // rotationAngle 参数是用于旋转普通道具
            faceunity.fuItemSetParam(itemHandle, "rotationAngle", 360 - mInputPropOrientation);
        }
        if (effectType == Effect.EFFECT_TYPE_BACKGROUND) {
            // 计算角度（全屏背景分割，第一次未识别人脸）
            faceunity.fuSetDefaultRotationMode((360 - mInputImageOrientation) / 90);
        }
        int back = mCurrentCameraType == Camera.CameraInfo.CAMERA_FACING_BACK ? 1 : 0;
        if (effectType == Effect.EFFECT_TYPE_AVATAR) {
            // Avatar 头型和头发镜像
            faceunity.fuItemSetParam(itemHandle, "isFlipExpr", back);
            setAvatarHairParams(mItemsArray[ITEM_ARRAYS_AVATAR_HAIR]);
        }

        if (effectType == Effect.EFFECT_TYPE_ANIMOJI || effectType == Effect.EFFECT_TYPE_PORTRAIT_DRIVE) {
            // 镜像顶点
            faceunity.fuItemSetParam(itemHandle, "is3DFlipH", back);
            // 镜像表情
            faceunity.fuItemSetParam(itemHandle, "isFlipExpr", back);
            // 这两句代码用于识别人脸默认方向的修改，主要针对 animoji 道具的切换摄像头倒置问题
            faceunity.fuItemSetParam(itemHandle, "camera_change", 1.0);
            faceunity.fuSetDefaultRotationMode((360 - mInputImageOrientation) / 90);
        }

        if (effectType == Effect.EFFECT_TYPE_GESTURE) {
            // loc_y_flip 与 loc_x_flip 参数是用于对手势识别道具的镜像
            faceunity.fuItemSetParam(itemHandle, "is3DFlipH", back);
            faceunity.fuItemSetParam(itemHandle, "loc_y_flip", back);
            faceunity.fuItemSetParam(itemHandle, "loc_x_flip", back);
            faceunity.fuItemSetParam(itemHandle, "rotMode", mRotMode);
        }

        if (effectType == Effect.EFFECT_TYPE_ANIMOJI) {
            // 镜像跟踪（位移和旋转）
            faceunity.fuItemSetParam(itemHandle, "isFlipTrack", back);
            // 镜像灯光
            faceunity.fuItemSetParam(itemHandle, "isFlipLight ", back);
            // 设置 Animoji 跟随人脸
            faceunity.fuItemSetParam(itemHandle, "{\"thing\":\"<global>\",\"param\":\"follow\"}", 1);
        }
        setMaxFaces(effect.maxFace());
    }

    private void setAvatarHairParams(int itemAvatarHair) {
        int back = mCurrentCameraType == Camera.CameraInfo.CAMERA_FACING_BACK ? 1 : 0;
        if (itemAvatarHair > 0) {
            faceunity.fuItemSetParam(itemAvatarHair, "is3DFlipH", back);
            faceunity.fuItemSetParam(itemAvatarHair, "isFlipTrack", back);
        }
    }



    // --------------------------------------------------------------------------------------------

    /**
     * 美颜道具参数，包含红润、美白、清晰磨皮、重度磨皮、滤镜、变形、亮眼、美牙功能。
     */
    static class BeautificationParams {
        // 滤镜名称，默认 origin
        public static final String FILTER_NAME = "filter_name";
        // 滤镜程度，0-1，默认 1
        public static final String FILTER_LEVEL = "filter_level";
        // 美白程度，0-1，默认 0.2
        public static final String COLOR_LEVEL = "color_level";
        // 红润程度，0-1，默认 0.5
        public static final String RED_LEVEL = "red_level";
        // 磨皮程度，0-6，默认 6
        public static final String BLUR_LEVEL = "blur_level";
        // 肤色检测开关，0 代表关，1 代表开，默认 0
        public static final String SKIN_DETECT = "skin_detect";
        // 肤色检测开启后，非肤色区域的融合程度，0-1，默认 0.45
        public static final String NONSKIN_BLUR_SCALE = "nonskin_blur_scale";
        // 磨皮类型，0 代表清晰磨皮，1 代表重度磨皮，默认 1
        public static final String HEAVY_BLUR = "heavy_blur";
        // 变形选择，0 代表女神，1 网红，2 自然，3 预设，4，精细变形，5 用户自定义，默认 3
        public static final String FACE_SHAPE = "face_shape";
        // 变形程度，0-1，默认 1
        public static final String FACE_SHAPE_LEVEL = "face_shape_level";
        // 大眼程度，0-1，默认 0.5
        public static final String EYE_ENLARGING = "eye_enlarging";
        // 瘦脸程度，0-1，默认 0
        public static final String CHEEK_THINNING = "cheek_thinning";
        // 窄脸程度，0-1，默认 0
        public static final String CHEEK_NARROW = "cheek_narrow";
        // 小脸程度，0-1，默认 0
        public static final String CHEEK_SMALL = "cheek_small";
        // V脸程度，0-1，默认 0
        public static final String CHEEK_V = "cheek_v";
        // 瘦鼻程度，0-1，默认 0
        public static final String INTENSITY_NOSE = "intensity_nose";
        // 嘴巴调整程度，0-1，默认 0.5
        public static final String INTENSITY_MOUTH = "intensity_mouth";
        // 额头调整程度，0-1，默认 0.5
        public static final String INTENSITY_FOREHEAD = "intensity_forehead";
        // 下巴调整程度，0-1，默认 0.5
        public static final String INTENSITY_CHIN = "intensity_chin";
        // 变形渐变调整参数，0 渐变关闭，大于 0 渐变开启，值为渐变需要的帧数
        public static final String CHANGE_FRAMES = "change_frames";
        // 亮眼程度，0-1，默认 1
        public static final String EYE_BRIGHT = "eye_bright";
        // 美牙程度，0-1，默认 1
        public static final String TOOTH_WHITEN = "tooth_whiten";
        // 美颜参数全局开关，0 代表关，1 代表开
        public static final String IS_BEAUTY_ON = "is_beauty_on";

        // 女神
        public static final int FACE_SHAPE_GODDESS = 0;
        // 网红
        public static final int FACE_SHAPE_NET_RED = 1;
        // 自然
        public static final int FACE_SHAPE_NATURE = 2;
        // 默认
        public static final int FACE_SHAPE_DEFAULT = 3;
        // 精细变形
        public static final int FACE_SHAPE_CUSTOM = 4;
    }

    /*----------------------------------Builder---------------------------------------*/

    /**
     * FURenderer Builder
     */
    public static class Builder {

        private boolean createEGLContext = false;
        private Effect defaultEffect;
        private int maxFaces = 1;
        private Context context;
        private int inputTextureType = 0;
        private int inputImageFormat = 0;
        private int inputImageRotation = 270;
        private int inputPropRotation = 270;
        private int isIputImage = 0;
        private boolean isNeedFaceBeauty = true;
        private int currentCameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
        private OnBundleLoadCompleteListener onBundleLoadCompleteListener;
        private OnFUDebugListener onFUDebugListener;
        private OnTrackingStatusChangedListener onTrackingStatusChangedListener;
        private OnSystemErrorListener onSystemErrorListener;

        public Builder( Context context) {
            this.context = context;
        }

        /**
         * 是否需要自己创建EGLContext
         *
         * @param createEGLContext
         * @return
         */
        public Builder createEGLContext(boolean createEGLContext) {
            this.createEGLContext = createEGLContext;
            return this;
        }

        /**
         * 是否需要立即加载道具
         *
         * @param defaultEffect
         * @return
         */
        public Builder defaultEffect(Effect defaultEffect) {
            this.defaultEffect = defaultEffect;
            return this;
        }


        /**
         * 输入的是否是图片
         *
         * @param isIputImage
         * @return
         */
        public Builder inputIsImage(int isIputImage) {
            this.isIputImage = isIputImage;
            return this;
        }

        /**
         * 识别最大人脸数
         *
         * @param maxFaces
         * @return
         */
        public Builder maxFaces(int maxFaces) {
            this.maxFaces = maxFaces;
            return this;
        }

        /**
         * 传入纹理的类型（传入数据没有纹理则无需调用）
         * camera OES 纹理：1
         * 普通 2D 纹理：2
         *
         * @param textureType
         * @return
         */
        public Builder inputTextureType(int textureType) {
            this.inputTextureType = textureType;
            return this;
        }

        /**
         * 输入的byte[]数据类型
         *
         * @param inputImageFormat
         * @return
         */
        public Builder inputImageFormat(int inputImageFormat) {
            this.inputImageFormat = inputImageFormat;
            return this;
        }

        /**
         * 输入的画面数据方向
         *
         * @param inputImageRotation
         * @return
         */
        public Builder inputImageOrientation(int inputImageRotation) {
            this.inputImageRotation = inputImageRotation;
            return this;
        }

        /**
         * 道具方向
         *
         * @param inputPropRotation
         * @return
         */
        public Builder inputPropOrientation(int inputPropRotation) {
            this.inputPropRotation = inputPropRotation;
            return this;
        }



        /**
         * 是否需要美颜效果
         *
         * @param needFaceBeauty
         * @return
         */
        public Builder setNeedFaceBeauty(boolean needFaceBeauty) {
            isNeedFaceBeauty = needFaceBeauty;
            return this;
        }



        /**
         * 当前的摄像头（前后置摄像头）
         *
         * @param cameraType
         * @return
         */
        public Builder setCurrentCameraType(int cameraType) {
            currentCameraType = cameraType;
            return this;
        }

        /**
         * 设置 debug 数据回调
         *
         * @param onFUDebugListener
         * @return
         */
        public Builder setOnFUDebugListener(OnFUDebugListener onFUDebugListener) {
            this.onFUDebugListener = onFUDebugListener;
            return this;
        }

        /**
         * 设置是否检查到人脸的回调
         *
         * @param onTrackingStatusChangedListener
         * @return
         */
        public Builder setOnTrackingStatusChangedListener(OnTrackingStatusChangedListener onTrackingStatusChangedListener) {
            this.onTrackingStatusChangedListener = onTrackingStatusChangedListener;
            return this;
        }

        /**
         * 设置bundle加载完成回调
         *
         * @param onBundleLoadCompleteListener
         * @return
         */
        public Builder setOnBundleLoadCompleteListener(OnBundleLoadCompleteListener onBundleLoadCompleteListener) {
            this.onBundleLoadCompleteListener = onBundleLoadCompleteListener;
            return this;
        }


        /**
         * 设置 SDK 使用错误回调
         *
         * @param onSystemErrorListener
         * @return
         */
        public Builder setOnSystemErrorListener(OnSystemErrorListener onSystemErrorListener) {
            this.onSystemErrorListener = onSystemErrorListener;
            return this;
        }

        public FURenderer build() {
            FURenderer fuRenderer = new FURenderer(context, createEGLContext);
            fuRenderer.mMaxFaces = maxFaces;
            fuRenderer.mInputTextureType = inputTextureType;
            fuRenderer.mInputImageFormat = inputImageFormat;
            fuRenderer.mInputImageOrientation = inputImageRotation;
            fuRenderer.mInputPropOrientation = inputPropRotation;
            fuRenderer.mIsInputImage = isIputImage;
            fuRenderer.mDefaultEffect = defaultEffect;
            fuRenderer.isNeedFaceBeauty = isNeedFaceBeauty;
            fuRenderer.mCurrentCameraType = currentCameraType;
            fuRenderer.mOnFUDebugListener = onFUDebugListener;
            fuRenderer.mOnTrackingStatusChangedListener = onTrackingStatusChangedListener;
            fuRenderer.mOnSystemErrorListener = onSystemErrorListener;
            fuRenderer.mOnBundleLoadCompleteListener = onBundleLoadCompleteListener;
            return fuRenderer;
        }
    }

    static class AvatarConstant {
        public static final int EXPRESSION_LENGTH = 46;
        public static final float[] ROTATION_DATA = new float[]{0f, 0f, 0f, 1f};
        public static final float[] PUP_POS_DATA = new float[]{0f, 0f};
        public static final int VALID_DATA = 1;
        public static final float[] EXPRESSIONS = new float[EXPRESSION_LENGTH];

        static {
            Arrays.fill(EXPRESSIONS, 0f);
        }
    }

//--------------------------------------Builder----------------------------------------

    class FUItemHandler extends Handler {

        FUItemHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 加载普通道具
                case ITEM_ARRAYS_EFFECT_INDEX: {
                    final Effect effect = (Effect) msg.obj;
                    if (effect == null) {
                        return;
                    }
                    boolean isNone = effect.effectType() == Effect.EFFECT_TYPE_NONE;
                    final int itemEffect = isNone ? 0 : loadItem(effect.path());
                    if (!isNone && itemEffect <= 0) {
                        Log.w(TAG, "create effect item failed: " + itemEffect);
                        return;
                    }
                    queueEventItemHandle(new Runnable() {
                        @Override
                        public void run() {
                            if (mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] > 0) {
                                faceunity.fuDestroyItem(mItemsArray[ITEM_ARRAYS_EFFECT_INDEX]);
                                mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] = 0;
                            }
                            if (!mNeedBackground && mItemsArray[ITEM_ARRAYS_AVATAR_BACKGROUND] > 0) {
                                faceunity.fuDestroyItem(mItemsArray[ITEM_ARRAYS_AVATAR_BACKGROUND]);
                                mItemsArray[ITEM_ARRAYS_AVATAR_BACKGROUND] = 0;
                            }
                            if (mItemsArray[ITEM_ARRAYS_AVATAR_HAIR] > 0) {
                                faceunity.fuDestroyItem(mItemsArray[ITEM_ARRAYS_AVATAR_HAIR]);
                                mItemsArray[ITEM_ARRAYS_AVATAR_HAIR] = 0;
                            }
                            if (itemEffect > 0) {
                                updateEffectItemParams(effect, itemEffect);
                            }
                            mItemsArray[ITEM_ARRAYS_EFFECT_INDEX] = itemEffect;
                        }
                    });
                }
                break;
                // 加载美颜 bundle
                case ITEM_ARRAYS_FACE_BEAUTY_INDEX: {
                    final int itemBeauty = loadItem(BUNDLE_FACE_BEAUTIFICATION);
                    if (itemBeauty <= 0) {
                        Log.w(TAG, "load face beauty item failed: " + itemBeauty);
                        return;
                    }
                    queueEventItemHandle(new Runnable() {
                        @Override
                        public void run() {
                            mItemsArray[ITEM_ARRAYS_FACE_BEAUTY_INDEX] = itemBeauty;
                            isNeedUpdateFaceBeauty = true;
                        }
                    });
                }
                default:
            }
            if (mOnBundleLoadCompleteListener != null) {
                mOnBundleLoadCompleteListener.onBundleLoadComplete(msg.what);
            }
        }
    }
}
