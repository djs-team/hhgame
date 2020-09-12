package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.AreaSelectAdapter;
import com.deepsea.mua.mine.databinding.DialogScreenItemBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.entity.AreaVo;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.List;

/**
 * Created by JUN on 2018/9/27
 */
public class ScreenCityDialog extends BaseDialog<DialogScreenItemBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick(AreaVo vo);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ScreenCityDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_screen_item;
    }

    @Override
    protected float getWidthPercent() {
        return 0.5F;
    }


    @Override
    protected float getDimAmount() {
        return 0.5F;
    }


    public void setData(List<AreaVo> data) {
        AreaSelectAdapter adapter = new AreaSelectAdapter(mContext);
        adapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (onClickListener != null) {
                    onClickListener.onClick(data.get(position));
                }
                dismiss();
            }
        });
        mBinding.rvCity.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvCity.setAdapter(adapter);
        adapter.setNewData(data);

    }

}
