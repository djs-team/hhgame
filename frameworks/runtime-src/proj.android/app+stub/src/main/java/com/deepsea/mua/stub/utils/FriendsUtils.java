package com.deepsea.mua.stub.utils;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.entity.FriendInfoBean;
import com.deepsea.mua.stub.entity.socket.MicroOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FriendsUtils {
    private static FriendsUtils instance;
    private List<FriendInfoBean> friendInfoBeans = new ArrayList<>();

    private FriendsUtils() {
    }

    public static FriendsUtils getInstance() {
        if (instance == null) {
            instance = new FriendsUtils();
        }
        return instance;
    }

    public void saveFriendUtils(List<FriendInfoBean> infoBeans) {
        if (infoBeans != null && infoBeans.size() > 0) {
            friendInfoBeans.clear();
            friendInfoBeans.addAll(infoBeans);
        }
    }

    public void addFriend(FriendInfoBean infoBeans) {
        friendInfoBeans.add(infoBeans);
    }

    public boolean isMyFriend(String uid) {
        boolean isMyFriend = false;
        if (friendInfoBeans != null) {
            for (int i = 0; i < friendInfoBeans.size(); i++) {
                if (uid.equals(friendInfoBeans.get(i).getUser_id())) {
                    isMyFriend = true;

                }
            }
        }
        return isMyFriend;

    }

//    public void clearFriendData() {
//        friendInfoBeans.clear();
//    }


}
