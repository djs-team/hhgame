package com.deepsea.mua.voice.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.deepsea.mua.stub.entity.socket.receive.ReceivePresent;
import com.deepsea.mua.lib.svga.SVGACallback;
import com.deepsea.mua.lib.svga.SVGAImageView;
import com.deepsea.mua.lib.svga.SVGAVideoEntity;
import com.deepsea.mua.lib.svga.proto.MovieEntity;
import com.deepsea.mua.stub.utils.CacheUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/8/1
 */
public class SvgaUtils {

    private Context mContext;
    private List<ReceivePresent> mSources;
    private SVGAImageView mSvgaImage;
    private ISVGAParser mSVGAParser;
    private SvgaParserCallback mParserCallback;

    public SvgaUtils(Context context, SVGAImageView svgaImage) {
        this.mContext = context;
        this.mSvgaImage = svgaImage;
        initAnimator();
    }


    public void setSvgaParserCallback(SvgaParserCallback callback) {
        this.mParserCallback = callback;
    }

    private void initAnimator() {
        mSVGAParser = new ISVGAParser(mContext);
        mSources = new ArrayList<>();
        mSvgaImage.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                System.gc();
                if (mSources != null && mSources.size() > 0) {
                    mSources.remove(0);
                    if (mSources != null && mSources.size() > 0) {
                        parseSVGA();
                    } else {
                        stopSVGA();
                    }
                } else {
                    stopSVGA();
                }
            }

            @Override
            public void onRepeat() {
                stopSVGA();
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    public void startAnimator(ReceivePresent present) {
        mSources.add(mSources.size(), present);
        if (mSources.size() == 1) {
            parseSVGA();
        }
    }


    private void stopSVGA() {
        if (mSvgaImage.isAnimating() && mSources.size() == 0) {
            mSvgaImage.stopAnimation();
        }
    }

    private void parseSVGA() {
        if (mSources.size() > 0) {
            ReceivePresent source = mSources.get(0);
            try {
                mSVGAParser.decodeFromURL(new URL(source.getGiftData().getGiftAnimation()), callback);
                if (source.getGiftData().getClassType() == 1) {
                    mSvgaImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    mSvgaImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                if (mParserCallback != null) {
                    mParserCallback.onSvgaStart(source);
                }
            } catch (Exception e) {
                callback.onError();
                e.printStackTrace();
            }
        } else {
            stopSVGA();
        }
    }

    private final ISVGAParser.ParseCompletion callback = new ISVGAParser.ParseCompletion() {
        @Override
        public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
            mSvgaImage.setVideoItem(svgaVideoEntity);
            mSvgaImage.startAnimation();
//            System.gc();
        }

        @Override
        public void onError() {
            if (mSources.size() > 0) {
                mSources.remove(0);
                parseSVGA();
            } else {
                stopSVGA();
            }
        }
    };

    public void clear() {
        mSources.clear();
        if (mSvgaImage != null) {
            mSvgaImage.stopAnimation();
            mSvgaImage = null;
        }
        mSVGAParser = null;

    }

    public interface SvgaParserCallback {
        void onSvgaStart(ReceivePresent present);
    }

    public static boolean isCached(String key) {
        return new File(buildCacheFile(), key + ".txt").exists();
    }

    private static File buildCacheFile() {
        return CacheUtils.getSvgaDir();
    }

    public static MovieEntity getMovieEntity(String key) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(buildCacheFile(), key + ".txt")));
            MovieEntity entity = (MovieEntity) ois.readObject();
            ois.close();

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveMovieEntity(String key, MovieEntity entity) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(buildCacheFile(), key + ".txt"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(entity);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
