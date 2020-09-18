package com.deepsea.mua.im.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.im.EaseUI;
import com.deepsea.mua.im.R;
import com.deepsea.mua.im.domain.EaseUser;
import com.hyphenate.chat.EMClient;

public class EaseUserUtils {

    static EaseUI.EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        String url = "";
        if (user != null && user.getAvatar() != null) {
            url = user.getAvatar();
        }

        GlideUtils.circleImage(imageView, url, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
    }

    public static void setCurrentUserAvatar(String myAvatar) {
        EaseUser user = getUserInfo(EMClient.getInstance().getCurrentUser());
        if (user != null) {
            user.setAvatar(myAvatar);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNickname() != null) {
                textView.setText(user.getNickname());
            } else {
                textView.setText(username);
            }
        }
    }
}
