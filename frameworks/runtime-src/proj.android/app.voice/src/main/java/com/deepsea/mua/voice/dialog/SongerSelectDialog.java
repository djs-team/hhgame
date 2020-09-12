package com.deepsea.mua.voice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogSongerSelectBinding;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2018/9/27
 */
public class SongerSelectDialog extends BaseDialog<DialogSongerSelectBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onSelectClick(int Level, int Number);

    }

    private OnClickListener listener;

    public SongerSelectDialog(@NonNull Context context) {
        super(context);
        setContent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_songer_select;
    }

    @Override
    protected float getWidthPercent() {
        return 0.8F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }


    public SongerSelectDialog setListener(OnClickListener cli) {
        this.listener = cli;
        return this;
    }

    public void setContent() {
        RoomModel roomModel = RoomController.getInstance().getRoomModel();
        List<RoomData.MicroInfosBean> microInfosBeans = roomModel.getMicros();
//        if (microInfosBeans.size() < 3) {
        microInfosBeans.add(roomModel.getHostMicro());
//        }
        int microNum = 0;
        for (RoomData.MicroInfosBean bean : microInfosBeans) {
            if (bean.getType() == 0) {
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
            } else if (bean.getType() == 1) {
                if (bean.getUser() != null) {
                    microNum++;
                    WsUser manUser = bean.getUser();
                    String manAvatar = manUser.getHeadImageUrl();
                    if (!TextUtils.isEmpty(manAvatar)) {
                        GlideUtils.loadImage(mBinding.ivManBigAvatar, manAvatar, R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg);
                        GlideUtils.circleImage(mBinding.ivManSmallAvatar, manAvatar, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                    }
                    ViewBindUtils.setText(mBinding.tvManName, manUser.getName());
                    StringBuilder info = new StringBuilder();
                    info.append(manUser.getAge());
                    info.append("岁");
                    if (manUser.getStature() != 0) {
                        info.append("|");
                        info.append(manUser.getStature() + "cm");
                    }
                    info.append("|");
                    info.append("男");
                    ViewBindUtils.setText(mBinding.tvManIntroduction, info.toString());
                    ViewBindUtils.RxClicks(mBinding.rlUserMan, o -> {
                        ViewBindUtils.setVisible(mBinding.rlManSelect, mBinding.rlManSelect.getVisibility() == View.GONE);
                    });
                    ViewBindUtils.RxClicks(mBinding.ivManSelectConfirm, o -> {
                        if (listener != null) {
                            listener.onSelectClick(bean.getType(), bean.getNumber());
                        }
                    });
                } else {
                    ViewBindUtils.setVisible(mBinding.rlUserMan, false);
                }
            } else if (bean.getType() == 2) {
                if (bean.getUser() != null) {
                    microNum++;
                    WsUser womenUser = bean.getUser();
                    String womenAvatar = womenUser.getHeadImageUrl();
                    if (!TextUtils.isEmpty(womenAvatar)) {
                        GlideUtils.loadImage(mBinding.ivWomenBigAvatar, womenAvatar, R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg);
                        GlideUtils.circleImage(mBinding.ivWomenSmallAvatar, womenAvatar, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                    }
                    ViewBindUtils.setText(mBinding.tvWomenName, womenUser.getName());
                    StringBuilder info = new StringBuilder();
                    info.append(womenUser.getAge());
                    info.append("岁");

                    if (womenUser.getStature() != 0) {
                        info.append("|");
                        info.append(womenUser.getStature() + "cm");
                    }
                    info.append("|");
                    info.append("女");
                    ViewBindUtils.setText(mBinding.tvWomenIntroduction, info.toString());
                    ViewBindUtils.RxClicks(mBinding.rlUserWomen, o -> {
                        ViewBindUtils.setVisible(mBinding.rlWomenSelect, mBinding.rlWomenSelect.getVisibility() == View.GONE);
                    });
                    ViewBindUtils.RxClicks(mBinding.ivWomenSelectConfirm, o -> {
                        if (listener != null) {
                            listener.onSelectClick(bean.getType(), bean.getNumber());
                        }
                    });
                } else {
                    ViewBindUtils.setVisible(mBinding.rlUserWomen, false);
                }
            }
        }
        ViewBindUtils.setVisible(mBinding.viewPadding, microNum == 2);

    }

}
