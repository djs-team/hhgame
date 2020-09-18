package com.deepsea.mua.voice.utils;

import android.text.TextUtils;

import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.utils.UserUtils;

/**
 * Created by JUN on 2019/10/22
 */
public class MatchMakerUtils {

    /**
     * 是否是红娘
     *
     * @return
     */
    public static boolean isMatchMaker() {
        if (UserUtils.getUser() == null)
            return false;
        return TextUtils.equals(UserUtils.getUser().getIs_matchmaker(), "1");
    }

    /**
     * 是否是房主
     *
     * @return
     */
    public static boolean isRoomOwner() {
        RoomModel roomModel = RoomController.getInstance().getRoomModel();
        if (roomModel != null && roomModel.getRoomData() != null) {
            return roomModel.getRoomData().getUserIdentity() == 2;
        }
        return false;
    }

    /**
     * 房间是否能给红娘送礼
     *
     * @return
     */
    public static boolean isCanSendGift() {
        RoomModel roomModel = RoomController.getInstance().getRoomModel();
        if (roomModel != null && roomModel.getRoomData() != null) {
            return roomModel.getRoomData().isCanSendGiftToEmcee();
        }
        return false;
    }
}
