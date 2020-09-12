package com.deepsea.mua.stub.view;

import android.content.Context;
import android.util.AttributeSet;

import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.R;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by JUN on 2019/4/8
 */
public class WrapRefreshLayout extends SmartRefreshLayout {

    private ClassicsFooter mFooter;

    public WrapRefreshLayout(Context context) {
        this(context, null);
    }

    public WrapRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //设置头部、底部
        ClassicsHeader mHeader = new ClassicsHeader(context).setDrawableArrowSize(20);
        mHeader.setTextSizeTitle(14);
        mHeader.setTextSizeTime(10);
        setRefreshHeader(mHeader);
        mFooter = new ClassicsFooter(context).setDrawableSize(20);
        mFooter.setTextSizeTitle(14);
        setRefreshFooter(mFooter);
        //设置阻尼(显示下拉高度/手指真实下拉高度=阻尼效果)
        setDragRate(0.3F);
        //回弹动画时长（毫秒）
        setReboundDuration(200);
        //Header标准高度（显示下拉高度>=标准高度 触发刷新）
        setHeaderHeight(50);
        //Footer标准高度（显示上拉高度>=标准高度 触发加载）
        setFooterHeight(ResUtils.dp2px(context, 15));
        //最大显示下拉高度/Header标准高度
        setHeaderMaxDragRate(2);
        //触发刷新距离 与 HeaderHeight 的比率
        setHeaderTriggerRate(1);
        //是否在全部加载结束之后Footer跟随内容
        setEnableFooterFollowWhenNoMoreData(false);
        //是否在列表不满一页时候开启上拉加载功能
        setEnableLoadMoreWhenContentNotFull(true);
        //是否在刷新的时候禁止列表的操作
        setDisableContentWhenRefresh(true);
        //是否在加载的时候禁止列表的操作
        setDisableContentWhenLoading(true);
    }

    @Override
    public SmartRefreshLayout finishRefresh() {
        return super.finishRefresh(0);
    }

    public void setFooterAccentColor(int color) {
        mFooter.setAccentColor(color);
    }

    public void setMaterialHeader() {
        MaterialHeader header = new MaterialHeader(getContext());
        header.setColorSchemeColors(ResUtils.getColor(getContext(), R.color.orange));
        setRefreshHeader(header);
    }
}
