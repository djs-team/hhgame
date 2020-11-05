package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MicroSortAdapter;
import com.deepsea.mua.voice.databinding.DialogSortManageBinding;
import com.deepsea.mua.voice.databinding.DialogSortManageForTwoBinding;

import java.util.List;

/**
 * 麦序管理
 * Created by JUN on 2019/4/23
 */
public class SortManageForTwoDialog extends BaseDialog<DialogSortManageForTwoBinding> {

    private MicroSortAdapter.OnMicroListener mListener;

    private MicroSortAdapter mAdapter;
    private Context mContext;

    public SortManageForTwoDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_sort_manage_for_two;
    }

    public void setOnMicroListener(MicroSortAdapter.OnMicroListener listener) {
        this.mListener = listener;
    }

    private void init() {
        mBinding.rvMicro.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MicroSortAdapter(mContext);
        mAdapter.setManager(true);
        mAdapter.setOnMicroListener(new MicroSortAdapter.OnMicroListener() {
            @Override
            public void onTopMicro(String uid) {
                dismiss();
                if (mListener != null) {
                    mListener.onTopMicro(uid);
                }
            }

            @Override
            public void onOnWheat(String uid) {
                dismiss();
                if (mListener != null) {
                    mListener.onOnWheat(uid);
                }
            }

            @Override
            public void onRemove(String uid) {
                dismiss();
                if (mListener != null) {
                    mListener.onRemove(uid);
                }
            }
        });
        mBinding.rvMicro.setAdapter(mAdapter);
    }


    public void setNewData(List<MicroOrder> lists) {
        mAdapter.setNewData(lists);
    }


}
