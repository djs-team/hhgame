package com.deepsea.mua.stub.utils;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JUN on 2018/12/13
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount;
    private int mSpanSpace;

    public GridItemDecoration(int spanCount, int spanSpace) {
        this.mSpanCount = spanCount;
        this.mSpanSpace = spanSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        int column = (pos) % mSpanCount + 1;
        //左侧为(当前条目数-1)/总条目数*divider宽度
        outRect.left = (column - 1) * mSpanSpace / mSpanCount;
        //右侧为(总条目数-当前条目数)/总条目数*divider宽度
        outRect.right = (mSpanCount - column) * mSpanSpace / mSpanCount;
    }
}
