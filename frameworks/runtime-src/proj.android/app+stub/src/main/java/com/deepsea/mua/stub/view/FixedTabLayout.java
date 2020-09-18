package com.deepsea.mua.stub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepsea.mua.stub.R;

/**
 * Created by JUN on 2019/8/29
 */
public class FixedTabLayout extends LinearLayout {

    private int mTextSize;
    private int mTextNormalColor;
    private int mTextSelectColor;
    private int mLineHeight;
    private int mLineBackground;
    private int mHorizontalMargin;
    private int mLineWidth;
    private int mTabPadding;
    private int mTabWidth;
    private int mTabBackground;

    private ViewPager mViewPager;

    public FixedTabLayout(Context context) {
        this(context, null);
    }

    public FixedTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);

        if (isInEditMode()) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FixedTabLayout);
        if (array != null) {
            mTextSize = array.getDimensionPixelSize(R.styleable.FixedTabLayout_textSize, 30);
            mTextNormalColor = array.getColor(R.styleable.FixedTabLayout_textNormalColor, 0xFF666666);
            mTextSelectColor = array.getColor(R.styleable.FixedTabLayout_textSelectColor, 0xFF222222);
            mLineHeight = array.getDimensionPixelSize(R.styleable.FixedTabLayout_lineHeight, 2);
            mLineBackground = array.getResourceId(R.styleable.FixedTabLayout_lineBackground, R.color.black);
            mHorizontalMargin = array.getDimensionPixelSize(R.styleable.FixedTabLayout_horizontalMargin, 0);
            mLineWidth = array.getDimensionPixelOffset(R.styleable.FixedTabLayout_lineWidth, 0);
            mTabPadding = array.getDimensionPixelSize(R.styleable.FixedTabLayout_tabPadding, 0);
            mTabWidth = array.getDimensionPixelSize(R.styleable.FixedTabLayout_tabWidth, 0);
            mTabBackground = array.getResourceId(R.styleable.FixedTabLayout_tabBackground, 0);
            array.recycle();
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null) {
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    initTabItem();
                }
            });
        }
        initViewPagerListener();
        initTabItem();
    }

    public void setSelectTextColor(int color) {
        this.mTextSelectColor = color;
        if (mViewPager != null) {
            setCurrentItem(mViewPager.getCurrentItem());
        }
    }

    public void setCurrentItem(int index) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof TabLayout) {
                TabLayout tabLayout = (TabLayout) child;
                final boolean isSelected = (i == index);
                if (isSelected) {
                    tabLayout.getTitleTv().setTextColor(mTextSelectColor);
                    tabLayout.getLineView().setBackgroundResource(mLineBackground);
                } else {
                    tabLayout.getTitleTv().setTextColor(mTextNormalColor);
                    tabLayout.getLineView().setBackgroundResource(R.color.transparent);
                }
                tabLayout.setSelected(isSelected);
            }
        }
    }

    private void initViewPagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setCurrentItem(position);
            }
        });
    }

    public void initTabItem() {
        if (this.getChildCount() != 0) {
            this.removeAllViews();
        }
        PagerAdapter adapter = mViewPager.getAdapter();
        if (null != adapter) {
            int count = adapter.getCount();

            for (int i = 0; i < count; i++) {
                CharSequence title = adapter.getPageTitle(i);
                if (TextUtils.isEmpty(title)) {
                    throw new RuntimeException("the title of indicator cannot be null");
                }
                addTabView(title.toString());
            }
        }
        setItemClickEvent();
        setCurrentItem(mViewPager.getCurrentItem());
    }

    private void addTabView(String title) {
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.leftMargin = lp.rightMargin = mHorizontalMargin;
        if (mTabWidth == 0 && mTabPadding == 0) {
            lp.weight = 1;
        }

        TabLayout tabLayout = new TabLayout(getContext());

        View lineView = tabLayout.getLineView();
        TextView titleView = tabLayout.getTitleTv();
        titleView.setText(title);

        titleView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        int measuredWidth = titleView.getMeasuredWidth();
        LinearLayout.LayoutParams lineLp = (LayoutParams) lineView.getLayoutParams();
        lineLp.width = mLineWidth > 0 ? mLineWidth : measuredWidth;
        lineView.setLayoutParams(lineLp);
        addView(tabLayout, lp);
    }

    private void setItemClickEvent() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int finalI = i;
            view.setOnClickListener(v -> mViewPager.setCurrentItem(finalI));
        }
    }

    private class TabLayout extends LinearLayout {

        private TextView mTitleTv;
        private View mLineView;

        public TabLayout(Context context) {
            super(context);
            this.init(context);
        }

        private void init(Context context) {
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER_HORIZONTAL);

            mTitleTv = new TextView(context);
            mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mTitleTv.setGravity(Gravity.CENTER);
            if (mTabBackground != 0) {
                setBackgroundResource(mTabBackground);
            }
            if (mTabWidth <= 0) {
                mTitleTv.setPadding(mTabPadding, 0, mTabPadding, 0);
            } else {
                mTitleTv.setWidth(mTabWidth);
            }

            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 0);
            lp.weight = 1;
            addView(mTitleTv, lp);

            mLineView = new View(context);
            addView(mLineView, new LinearLayout.LayoutParams(0, mLineHeight));
        }

        public View getLineView() {
            return mLineView;
        }

        public TextView getTitleTv() {
            return mTitleTv;
        }
    }
}
