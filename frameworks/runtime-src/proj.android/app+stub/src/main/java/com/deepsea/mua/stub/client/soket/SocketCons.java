package com.deepsea.mua.stub.client.soket;

/**
 * Created by JUN on 2019/8/20
 */
public interface SocketCons {

    //成功
    int SUCCESS = 1;
    //连接房间
    int SEND_TOKEN = 1;
    //进入房间
    int JOIN_ROOM = 2;
    //退出房间
    int EXIT_ROOM = 3;
    //用户进入房间
    int USER_JOIN_ROOM = 4;
    //踢出房间
    int KICK_ROOM = 5;
    //上麦
    int UP_MICRO = 6;
    //上麦消息
    int USER_UP_MICRO = 7;
    //下麦
    int DOWN_MICRO = 8;
    //下麦消息
    int USER_DOWN_MICRO = 9;
    //房间信息
    int ROOM_INFO = 200;

    interface Error {
        //用户不存在
        int USER_NO_EXIST = -5;
        int RE_CONNECT = -2;
        //房间被锁
        int ROOM_LOCK = 3;
        //被踢出房间
        int ROOM_KICK = 4;
        //青少年模式、家长模式
        int PARENT_LOCK = 5;
        //服务器繁忙
        int SERVER_BUSY = 6;
        //被踢下麦
        int KICK_MICRO = 3;
        int Maintenance = 20;
    }
}
