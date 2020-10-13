package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogBlockOperateBinding;

/**
 * Created by JUN on 2019/10/18
 */
public class BlockOperateDialog extends BaseDialog<DialogBlockOperateBinding> {

    public interface OnClickListener {
        public void onLookDetailsClick();

        public void onBlockCancelClick();

        public void onReportClick();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public BlockOperateDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_block_operate;
    }

    @Override
    protected float getWidthPercent() {
        return 0.51F;
    }

    @Override
    protected void initListener() {
        mBinding.tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onLookDetailsClick();
                }
                dismiss();
            }
        });
        mBinding.tvBlockCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onBlockCancelClick();
                }
                dismiss();
            }
        });
        mBinding.tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onReportClick();
                }
                dismiss();
            }
        });
    }


}
