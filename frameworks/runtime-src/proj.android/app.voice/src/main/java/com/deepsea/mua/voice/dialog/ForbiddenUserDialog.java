package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.DisableMsgData;
import com.deepsea.mua.stub.entity.socket.receive.DisableMsgParam;
import com.deepsea.mua.stub.entity.socket.receive.GetMicroRanksParam;
import com.deepsea.mua.stub.entity.socket.receive.MicroRankData;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.ForbiddenTimeAdapter;
import com.deepsea.mua.voice.databinding.DialogForbiddenUsersBinding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.umeng.commonsdk.statistics.proto.Response;

import java.util.List;

/**
 * 禁言用户
 * Created by JUN on 2019/10/18
 */
public class ForbiddenUserDialog extends BaseDialog<DialogForbiddenUsersBinding> {

    public interface OnClickListener {
        public void onClickOk(int disableMsgId);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ForbiddenUserDialog(@NonNull Context context) {
        super(context);
        addSocketListener();
    }

    ForbiddenTimeAdapter adapter;

    public void setData(WsUser wsUser) {
        WsUser user =wsUser;
        adapter = new ForbiddenTimeAdapter(mContext);
        mBinding.rvForbiddenTime.setLayoutManager(new GridLayoutManager(mContext, 2));
        mBinding.rvForbiddenTime.setAdapter(adapter);
        GlideUtils.circleImage(mBinding.ivPhoto, user.getHeadImageUrl(), R.drawable.ic_place, R.drawable.ic_place);
        ViewBindUtils.setText(mBinding.tvName, user.getName());
        SexResUtils.setSexImgInFindPage(mBinding.rlSex, mBinding.sexIv, String.valueOf(user.getSex()));
        ViewBindUtils.setText(mBinding.tvAge, TextUtils.isEmpty(user.getAge()) ? "" : user.getAge());
        ViewBindUtils.setText(mBinding.tvLocation, user.getCity());
        ViewBindUtils.setVisible(mBinding.ivLocation, !TextUtils.isEmpty(user.getCity()));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_forbidden_users;
    }

    @Override
    protected float getWidthPercent() {
        return 0.85F;
    }

    @Override
    protected void initListener() {
        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickOk(adapter.getDisableMsgId());
                }
                dismiss();
            }
        });

    }

    private void addSocketListener() {
        WsocketManager.create().addWsocketListener(mWsocketListener);
    }

    private void removeSocketListener() {
        WsocketManager.create().removeWsocketListener(mWsocketListener);
    }

    private WsocketListener mWsocketListener = new WsocketListener() {
        public void onMessage(String message) {
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(message).getAsJsonObject();
            int msgId = object.get("MsgId").getAsInt();
            switch (msgId) {
                case 132:
                    Log.d("AG_EX_AV", "126 返回:" + message);
                    DisableMsgParam getMicroRanksParam = JsonConverter.fromJson(message, DisableMsgParam.class);
                    List<DisableMsgData> disableMsgData = getMicroRanksParam.getDisableMsgs();
                    if (disableMsgData != null && disableMsgData.size() > 0) {
                        adapter.setNewData(disableMsgData);
                    }

                    break;

            }
        }

        public void onFailure(Throwable t, Response response) {

        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        removeSocketListener();
    }

}
