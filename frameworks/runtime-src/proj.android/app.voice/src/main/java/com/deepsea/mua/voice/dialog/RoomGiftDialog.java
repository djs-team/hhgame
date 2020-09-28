package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.MultiSend;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.SingleSend;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogVoiceGiftBinding;
import com.deepsea.mua.voice.view.present.OnPresentListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/3/29
 * 语音直播间送礼弹框
 */
public class RoomGiftDialog extends BaseDialog<DialogVoiceGiftBinding> {

    private OnPresentListener mListener;

    public RoomGiftDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_voice_gift;
    }

    @Override
    protected float getDimAmount() {
        return 0;
    }

    @Override
    protected void initListener() {
        mBinding.presentView.setOnPresentListener(new OnPresentListener() {
            @Override
            public void onMultiSend(MultiSend sendModel) {
//                dismiss();
                if (mListener != null) {
                    mListener.onMultiSend(sendModel);
                }
            }

            @Override
            public void onSingleSend(SingleSend sendModel) {
//                dismiss();
                if (mListener != null) {
                    mListener.onSingleSend(sendModel);
                }
            }

            @Override
            public void onRecharge() {
                dismiss();
                if (mListener != null) {
                    mListener.onRecharge();
                }
            }


        });
    }

    /**
     * 设置礼物数据
     *
     * @param list
     */
    public void setGiftData(List<GiftBean> list) {
        mBinding.presentView.setGiftData(list);
    }

    /**
     * 设置背包数据
     *
     * @param list
     */
    public void setPackData(List<GiftBean> list) {
        mBinding.presentView.setPackData(list);
    }

    /**
     * 设置账户余额
     *
     * @param balance
     */
    public void setBalance(String balance) {
        mBinding.presentView.setBalance(balance);
    }

    /**
     * 设置打赏用户信息
     *
     * @param user
     */
    public void setSingleData(MicroUser user,boolean isAddFriend) {
        mBinding.presentView.setSingleData(user,isAddFriend);
    }

    /**
     * 设置全麦用户数据
     *
     * @param list
     */
    public void setMicroData(List<RoomData.MicroInfosBean> list) {
        List<MicroUser> datas = null;
        if (list != null) {
            datas = new ArrayList<>();
            MicroUser user = null;
            for (RoomData.MicroInfosBean bean : list) {
                if (bean.getUser() != null && !TextUtils.equals(bean.getUser().getUserId(), UserUtils.getUser().getUid())) {
                    user = new MicroUser();
                    user.setType(bean.getType());
                    user.setNumber(bean.getNumber());
                    user.setUser(bean.getUser());
                    datas.add(user);
                }
            }
        }
        mBinding.presentView.setReceiverData(datas);
    }

    public void setOnPresentListener(OnPresentListener listener) {
        this.mListener = listener;
    }

    @Override
    public void show() {
        mBinding.presentView.resetSendNum();
        super.show();
    }
}
