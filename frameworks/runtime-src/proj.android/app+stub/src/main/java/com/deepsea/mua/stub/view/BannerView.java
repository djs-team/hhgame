package com.deepsea.mua.stub.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by david on 2016/9/23.
 */
public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {

    /**
     * 广告url集合，这里不加修饰文案，因此直接放一个url
     * 如果需要加入文字信息，自行封装为javaBean
     */
    private List<String> urls;
    private Context context;
    private ViewPager viewPager;

    /**
     * 存放圆点指示器
     */
    private LinearLayout bannerPage;

    private PagerAdapter adapter;

    /**
     * 用户手动滑动广告中
     */
    private boolean isScrollingByUser;

    /**
     * 轮播切换小圆点宽度默认宽度
     */

    private int IndicatorDotWidth;

    /**
     * 当前页pos
     */
    private int currentPosition = 0;

    /**
     * 历史页pos
     */
    private int prePosition = 0;

    /**
     * 轮播图总数
     */
    private int count;

    private TimerTask task;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    if (viewPager != null && !isScrollingByUser) {
                        currentPosition++;
                        viewPager.setCurrentItem(currentPosition);
                    }
                    break;
            }
        }
    };
    private BannerOnClickListener onClickListener;

    public BannerView(Context context) {
        super(context);
        this.context = context;
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        if (adapter == null) {
            return;
        }
        viewPager.setAdapter(adapter);
        /**
         * viewpager 切换动画
         */
        viewPager.setPageTransformer(true, new MyPageTransformer());
        bannerPage.removeAllViews();

        /**
         * 向线性布局中添加小圆点指示器
         * */
        View dot;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < count; i++) {
            dot = new View(context);
            params = new LinearLayout.LayoutParams(IndicatorDotWidth, IndicatorDotWidth);
            params.setMargins(IndicatorDotWidth, 0, 0, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.dot_bg_selector);
            dot.setEnabled(false);//默认设为非选中
            bannerPage.addView(dot);
        }
        int midPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % count;
        currentPosition = midPosition;
        bannerPage.getChildAt(0).setEnabled(true);
        viewPager.setCurrentItem(midPosition);// 设置item 一定要放在圆点初始化之后，否则 onPageSelected（） 会报空指针异常
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        IndicatorDotWidth = dip2px(context, 5);
        View view = LayoutInflater.from(context).inflate(R.layout.bannerview_layout, null);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        bannerPage = (LinearLayout) view.findViewById(R.id.bannerPage);
        viewPager.addOnPageChangeListener(this);
        addView(view);
    }

    public void setData(List<String> urls) {
        this.urls = urls;
        count = urls.size();
        if (adapter == null) {
            adapter = new MyAdapter(urls);
        }
        init();
    }


    public void startBannerScrollTask(long timeSpace) {
        //true 说明这个timer以daemon方式运行（优先级低，程序结束timer也自动结束）
        /*
      定时滚动相关
     */
        Timer timer = new Timer(true);
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer.schedule(task, 1000, timeSpace);//1000ms后按指定时间间隔轮播
    }

    public void stopBannerTask() {
        if (task != null) {
            task.cancel();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        bannerPage.getChildAt(prePosition % urls.size()).setEnabled(false);
        bannerPage.getChildAt(currentPosition % urls.size()).setEnabled(true);// 设置true放后面  防止初始化时 两个pos都为0时  没有默认选中
        prePosition = currentPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        /**
         * 用户手动滑动广告时，取消自动翻页相应
         */
        if (state == 0) {
            isScrollingByUser = false;
        } else//用户手动滑动中
            isScrollingByUser = true;

    }


    class MyAdapter extends PagerAdapter {
        List<String> adUrls;

        public MyAdapter(List<String> adUrls) {
            this.adUrls = adUrls;
        }

        @Override
        public int getCount() {

            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(context);
            String currentUrl = adUrls.get(position % adUrls.size());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideUtils.loadImage(imageView, currentUrl);

            if (onClickListener != null) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onClickListener(position % adUrls.size());
                    }
                });
            }
            container.addView(imageView);

            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            /**
             * 尺寸变化
             */
//            float normalizedposition = Math.abs(Math.abs(position) - 1);
//            page.setScaleX(normalizedposition / 2 + 0.5f);
//            page.setScaleY(normalizedposition / 2 + 0.5f);

            /**
             * 淡入淡出
             */
            final float normalizedposition = Math.abs(Math.abs(position) - 1);
            page.setAlpha(normalizedposition);
        }
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnClickListener(BannerOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface BannerOnClickListener {
        void onClickListener(int pos);
    }
}
