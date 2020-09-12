package com.deepsea.mua.core.view.banner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.deepsea.mua.core.R;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.core.view.NoScrollViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 用viewpager实现的广告轮播
 */
public class SlidingPlayView extends LinearLayout {

    private Context mContext;
    private LinearLayout navLinearLayout;
    public LayoutParams navLayoutParams = null;
    private Bitmap displayImage, hideImage;
    private int navHorizontalGravity = Gravity.CENTER_HORIZONTAL;
    private LinearLayout mNavLayoutParent = null;
    private NoScrollViewPager mViewPager;
    private MyAdapter mAdapter = null;
    private List<View> mListViews = null;
    private int count, position;
    private boolean play = false;
    private boolean touch = false;
    private boolean up = false;
    private boolean indicator_center_horizontal;
    private boolean indicator_visible = true;
    private ViewPagerScroller mScroller;

    private int mDuration;
    private int mIndicatorNormalId;
    private int mIndicatorSelectId;

    private int mIndicatorPaddingLeft;
    private int mIndicatorPaddingTop;
    private int mIndicatorPaddingRight;
    private int mIndicatorPaddingBottom;
    private int coefficient = 1;

    private boolean needPlay = false;

    public SlidingPlayView(Context context) {
        super(context);
        initView(context, null);
    }

    public SlidingPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public SlidingPlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public void initCoefficient(int dataSize) {
        if (dataSize >= 4) {
            coefficient = 1;
        } else if (dataSize >= 2) {
            coefficient = 2;
        } else {
            coefficient = 4;
        }
    }

    public int getCoefficient() {
        return coefficient;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 添加viewpager和indication
     *
     * @param context
     */
    private void initView(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SlidingPlayView);
        if (arr != null) {
            mDuration = arr.getInt(R.styleable.SlidingPlayView_duration, 5000);
            mIndicatorNormalId = arr.getResourceId(R.styleable.SlidingPlayView_indicator_normal, R.drawable.ic_indicator);
            mIndicatorSelectId = arr.getResourceId(R.styleable.SlidingPlayView_indicator_select, R.drawable.ic_indicator_s);
            indicator_center_horizontal = arr.getBoolean(R.styleable.SlidingPlayView_indicator_center_horizontal, true);
            mIndicatorPaddingLeft = arr.getDimensionPixelOffset(R.styleable.SlidingPlayView_indicator_padding_left, ResUtils.dp2px(context, 3));
            mIndicatorPaddingTop = arr.getDimensionPixelOffset(R.styleable.SlidingPlayView_indicator_padding_top, ResUtils.dp2px(context, 3));
            mIndicatorPaddingRight = arr.getDimensionPixelOffset(R.styleable.SlidingPlayView_indicator_padding_right, ResUtils.dp2px(context, 3));
            mIndicatorPaddingBottom = arr.getDimensionPixelOffset(R.styleable.SlidingPlayView_indicator_padding_bottom, ResUtils.dp2px(context, 5));
            indicator_visible = arr.getBoolean(R.styleable.SlidingPlayView_indicator_visible, true);
            arr.recycle();
        }
        this.mContext = context;
        this.navLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.argb(0, 0, 0, 0));

        RelativeLayout mRelativeLayout = new RelativeLayout(context);

        mViewPager = new NoScrollViewPager(context);
        mViewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        mScroller = new ViewPagerScroller(context);
        mScroller.setScrollDuration(1500);
        mScroller.initViewPagerScroll(mViewPager);

        mNavLayoutParent = new LinearLayout(context);
        mNavLayoutParent.setPadding(mIndicatorPaddingLeft,
                mIndicatorPaddingTop,
                mIndicatorPaddingRight,
                mIndicatorPaddingBottom);
        navLinearLayout = new LinearLayout(context);
        navLinearLayout.setVisibility(View.INVISIBLE);
        mNavLayoutParent.addView(navLinearLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mRelativeLayout.addView(mViewPager, lp1);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        if (indicator_center_horizontal) {
            lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        } else {
            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        }
        mRelativeLayout.addView(mNavLayoutParent, lp2);
        addView(mRelativeLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        initTip();
        initAdapter();

    }

    public void setNavLayoutVisible(boolean isVisible) {
        if (null != mNavLayoutParent) {
            mNavLayoutParent.setVisibility(isVisible ? VISIBLE : GONE);
        }
    }

    private void initAdapter() {
        mListViews = new ArrayList<View>();
        mAdapter = new MyAdapter(SlidingPlayView.this);
        mViewPager.setAdapter(mAdapter);
        controlPlayWhileTouch();
        //setTransformer();
        setPageChangeListener();
    }

    /**
     * 触摸时关闭轮播
     */
    private void controlPlayWhileTouch() {
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        touch = false;
                        startPlay();
                        break;
                    default:
                        touch = true;
                        up = true;
                        stopPlay();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * ViewPager滑动效果
     */
    private void setTransformer() {
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @SuppressLint("NewApi")
            @Override
            public void transformPage(View view, float position) {
                float sx = Math.max(((float) view.getWidth() - 20) / view.getWidth(), 1 - Math.abs(position));
                float sy = Math.max(((float) view.getHeight() - 35) / view.getHeight(), 1 - Math.abs(position));
                if (touch) {
                    view.setScaleX(sx);
                    view.setScaleY(sy);
                } else {
                    if (Math.abs(position) < 1 && up) {
                        view.setScaleX(sx);
                        view.setScaleY(sy);
                    } else {
                        view.setScaleX(1);
                        view.setScaleY(1);
                    }

                    if (Math.abs(position) == 1) {
                        up = false;
                    }

                }
            }
        });
    }

    /**
     * 添加页面切换回调
     */
    private void setPageChangeListener() {
        //实现无限轮播当position=0时，设置到第mAdapter.getCount() - 2页，当position=mAdapter.getCount()-1时，设置到第0页。
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                makesurePosition();
                if (position == 0) {
                    mViewPager.removeAllViews();
                    mViewPager.setCurrentItem(mListViews.size(), false);

                    View view = mListViews.get(mListViews.size() - 1);
                    if (view != null) {
                        ViewParent vp = view.getParent();
                        if (vp != null) {
                            ViewGroup parent = (ViewGroup) vp;
                            parent.removeView(view);
                        }
                        mViewPager.addView(view);
                    }
                }

                if (position == mAdapter.getCount() - 1) {
                    mViewPager.removeAllViews();
                    mViewPager.setCurrentItem(position % mListViews.size(), false);

                    View view = mListViews.get(0);
                    if (view != null) {
                        ViewParent vp = view.getParent();
                        if (vp != null) {
                            ViewGroup parent = (ViewGroup) vp;
                            parent.removeView(view);
                        }
                        mViewPager.addView(view);
                    }
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    //初始化点点
    private void initTip() {
        displayImage = BitmapFactory.decodeResource(getResources(), mIndicatorSelectId);
        hideImage = BitmapFactory.decodeResource(getResources(), mIndicatorNormalId);
        mNavLayoutParent.setHorizontalGravity(navHorizontalGravity);
        navLinearLayout.setGravity(Gravity.CENTER);
        navLinearLayout.setVisibility(indicator_visible ? VISIBLE : INVISIBLE);
    }

    private void creatIndex() {
        navLinearLayout.removeAllViews();
        count = mListViews.size() / getCoefficient();
        navLayoutParams.setMargins(5, 5, 5, 5);
        for (int j = 0; j < count; j++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(navLayoutParams);
            if (j == 0) {
                imageView.setImageBitmap(displayImage);
            } else {
                imageView.setImageBitmap(hideImage);
            }
            navLinearLayout.addView(imageView, j);
        }
    }

    private void makesurePosition() {
        position = mViewPager.getCurrentItem() - 1;
        for (int j = 0; j < count; j++) {
            if (position % count == j) {
                ((ImageView) navLinearLayout.getChildAt(position % count)).setImageBitmap(displayImage);
            } else {
                ((ImageView) navLinearLayout.getChildAt(j)).setImageBitmap(hideImage);
            }
        }
    }

    private Handler handler = new InnerHandler(SlidingPlayView.this);

    private static class InnerHandler extends Handler {
        private WeakReference<SlidingPlayView> weakReference;

        public InnerHandler(SlidingPlayView playView) {
            weakReference = new WeakReference<SlidingPlayView>(playView);
        }

        @Override
        public void handleMessage(Message msg) {
            SlidingPlayView playView = weakReference.get();
            if (playView == null) {
                return;
            }
            if (msg.what == 0) {
                int count = playView.mAdapter.getCount();
                int i = playView.mViewPager.getCurrentItem();

                if (i == count - 2) {
                    i = 1;
                    playView.mViewPager.setCurrentItem(i, false);
                } else {
                    i++;
                    playView.mViewPager.setCurrentItem(i, true);
                }

                if (playView.play) {
                    postDelayed(playView.runnable, playView.mDuration);
                }
            }
        }
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            if (mViewPager != null) {
                handler.sendEmptyMessage(0);
            }
        }
    };

    public void removeAllViews() {
        if (null != mViewPager && mViewPager.getChildCount() != 0) {
            mViewPager.removeAllViews();
        }
    }

    /**
     * 是否开始轮播
     *
     * @return
     */
    public boolean isPlaying() {
        return play;
    }

    /**
     * 开始轮播
     */
    public void startPlay() {
        startPlay(needPlay);
    }

    public void startPlay(boolean start) {
        needPlay = start;
        if (start) {
            if (mListViews.size() / getCoefficient() == 1) {
                mViewPager.setNoScroll(true);
                return;
            } else {
                mViewPager.setNoScroll(false);
            }
            if (handler != null) {
                play = true;
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(runnable, mDuration);
            }
        }
    }

    /**
     * 停止轮播
     */
    public void stopPlay() {
        if (handler != null) {
            play = false;
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopPlay();
        super.onDetachedFromWindow();
    }

    public void setNavHorizontalGravity(int horizontalGravity) {
        navHorizontalGravity = horizontalGravity;
    }

    /**
     * 设置每页显示时间
     *
     * @param duration
     */
    public void setShowDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * 设置viewpager页面切换间隔时间
     *
     * @param duration
     */
    public void setScrollDuration(int duration) {
        mScroller.setScrollDuration(duration);
    }

    /**
     * 添加需显示的view列表
     *
     * @param view
     */
    public void addView(List<View> view) {
        handler.removeCallbacks(runnable);
        mViewPager.removeAllViews();
        mListViews = view;
        mAdapter.notifyDataSetChanged();
        //mViewPager.setCurrentItem(mListViews.size() * 500 + 1);
        mViewPager.setCurrentItem(1, false);
        creatIndex();

        if (mListViews.size() / getCoefficient() == 1) {
            mViewPager.setNoScroll(true);
        } else {
            mViewPager.setNoScroll(false);
        }
    }

    /**
     * 设置页面指示图标
     *
     * @param normalId
     * @param selectId
     */
    public void setIndicatorDrawableId(int normalId, int selectId) {
        Bitmap normal = BitmapFactory.decodeResource(mContext.getResources(), normalId);
        Bitmap select = BitmapFactory.decodeResource(mContext.getResources(), selectId);
        if (normal != null) {
            Bitmap temp = hideImage;
            hideImage = normal;
            if (temp != null && temp.isRecycled()) {
                temp.recycle();
                temp = null;
            }
        }
        if (select != null) {
            Bitmap temp = displayImage;
            displayImage = select;
            if (temp != null && temp.isRecycled()) {
                temp.recycle();
                temp = null;
            }
        }
    }

    private static class MyAdapter extends PagerAdapter {

        private WeakReference<SlidingPlayView> weakReference;

        public MyAdapter(SlidingPlayView playView) {
            weakReference = new WeakReference<SlidingPlayView>(playView);
        }

        @Override
        public int getCount() {
            SlidingPlayView playView = weakReference.get();
            if (playView == null) {
                return 0;
            }
            return playView.mListViews.size() > 0 ? playView.mListViews.size() * 1000 + 2 : 0;//实现无限轮播的方法之一
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                SlidingPlayView playView = weakReference.get();
                if (playView == null) {
                    return null;
                }
                View view = playView.mListViews.size() > 0 ? playView.mListViews.get((position + playView.mListViews.size() - 1)
                        % playView.mListViews.size()) : null;
                if (view != null) {
                    ViewParent vp = view.getParent();
                    if (vp != null) {
                        ViewGroup parent = (ViewGroup) vp;
                        parent.removeView(view);
                    }
                    container.addView(view, 0);
                }
                return view;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //
//            SlidingPlayView playView = weakReference.get();
//            if (playView == null) {
//                return;
//            }
//            if (playView.mListViews.size() > 0) {
//                ((ViewPager) container).removeView(playView.mListViews.get((position +playView. mListViews.size() - 1) % playView.mListViews.size()));
//            }
        }

        @Override
        public int getItemPosition(Object object) {
            //POSITION_NONE，表示child都没有绘制过
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}