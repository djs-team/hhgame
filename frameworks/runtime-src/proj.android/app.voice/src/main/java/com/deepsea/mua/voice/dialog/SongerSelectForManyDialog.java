package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MicroSelectAdapter;
import com.deepsea.mua.voice.databinding.DialogSongerForManySelectBinding;
import com.deepsea.mua.voice.databinding.DialogSongerSelectBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2018/9/27
 */
public class SongerSelectForManyDialog extends BaseDialog<DialogSongerForManySelectBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onSelectClick(int Level, int Number);

    }

    private OnClickListener listener;

    public SongerSelectForManyDialog(@NonNull Context context) {
        super(context);
        setContent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_songer_for_many_select;
    }

    @Override
    protected float getWidthPercent() {
        return 0.8F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }


    public SongerSelectForManyDialog setListener(OnClickListener cli) {
        this.listener = cli;
        return this;
    }

    public void setContent() {
        RoomModel roomModel = RoomController.getInstance().getRoomModel();
        List<RoomData.MicroInfosBean> microInfosBeans = roomModel.getMicros();
//        if (microInfosBeans.size() < 3) {
        microInfosBeans.add(roomModel.getHostMicro());
        if (roomModel.getmSofaMicro()!=null){
            microInfosBeans.add(roomModel.getmSofaMicro());
        }
//        }

        RoomData.MicroInfosBean hostInfo = null;
        List<RoomData.MicroInfosBean> guestMicros = new ArrayList<>();


        for (RoomData.MicroInfosBean bean : microInfosBeans) {
            if (bean.getType() == 0) {
                hostInfo = bean;
            } else {
                if (bean.getUser() != null) {
                    guestMicros.add(bean);
                }
            }
        }
        setHostInfo(hostInfo);
        MicroSelectAdapter microSelectAdapter = new MicroSelectAdapter(mContext);
        mBinding.rvMicro.setLayoutManager(new GridLayoutManager(mContext, 2));
        mBinding.rvMicro.setAdapter(microSelectAdapter);
        microSelectAdapter.setOnSelectMicUserListener(new MicroSelectAdapter.OnSelectMicUserListener() {
            @Override
            public void onSelectClick(int Level, int Number) {
                if (listener != null) {
                    listener.onSelectClick(Level, Number);
                }
            }
        });
        microSelectAdapter.setNewData(guestMicros);

    }

    private void setHostInfo(RoomData.MicroInfosBean bean) {
        if (bean.getUser() != null) {
            WsUser hongUser = bean.getUser();
            //红娘
            String hongAvatar = hongUser.getHeadImageUrl();
            if (!TextUtils.isEmpty(hongAvatar)) {
                GlideUtils.loadImage(mBinding.ivMatchmakerBigAvatar, hongAvatar, R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg);
                GlideUtils.circleImage(mBinding.ivMatchmakerSmallAvatar, hongAvatar, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            }
            ViewBindUtils.setText(mBinding.tvMatchmakerName, hongUser.getName());

            ViewBindUtils.RxClicks(mBinding.rlUserMatchmaker, o -> {
                ViewBindUtils.setVisible(mBinding.rlMatchmakerSelect, mBinding.rlMatchmakerSelect.getVisibility() == View.GONE);
            });
            ViewBindUtils.RxClicks(mBinding.ivMatchmakerSelectConfirm, o -> {
                if (listener != null) {
                    listener.onSelectClick(bean.getType(), bean.getNumber());
                }
            });
        }
    }

}
