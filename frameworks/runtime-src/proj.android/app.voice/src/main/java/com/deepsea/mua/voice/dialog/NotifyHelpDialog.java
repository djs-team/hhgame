package com.deepsea.mua.voice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.NotifyHelpAdapter;
import com.deepsea.mua.voice.databinding.DialogNotifyHelpBinding;
import com.deepsea.mua.voice.databinding.DialogSongAlertBinding;

import java.util.List;

/**
 * Created by JUN on 2018/9/27
 */
public class NotifyHelpDialog extends BaseDialog<DialogNotifyHelpBinding> {

    public NotifyHelpDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnClickListener {
        /**
         * 点击回调
         *
         */
        void onClick(WsUser bean);
    }
    private  OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_notify_help;
    }

    @Override
    protected float getWidthPercent() {
        return 0.85F;
    }



    public void setMsg(List<RoomData.MicroInfosBean> data) {
        NotifyHelpAdapter mAdapter = new NotifyHelpAdapter(mContext);
        mBinding.rvMicro.setLayoutManager(new GridLayoutManager(mContext, 3));
        mBinding.rvMicro.setAdapter(mAdapter);
        mAdapter.setNewData(data);
        mAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (onClickListener!=null){
                    onClickListener.onClick(data.get(position).getUser());
                }
            }
        });

    }
}
