package com.deepsea.mua.im.utils;

public class ChatUserInfoController {
    private  static ChatUserInfoController instance;
    private ChatUserInfoController(){}
    private String tochatUserAvatar;

    public String getTochatUserAvatar() {
        return tochatUserAvatar;
    }

    public void setTochatUserAvatar(String tochatUserAvatar) {
        this.tochatUserAvatar = tochatUserAvatar;
    }

    public static ChatUserInfoController getInstance(){
        if (instance==null){
            instance=new ChatUserInfoController();
        }
        return instance;
    }
}
