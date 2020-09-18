//
//  SocketMessage.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, SocketMessageID) {
    SocketMessageIDLogin = 1,   //
    SocketMessageIDJoinRoom = 2,    //加入房间
    SocketMessageIDLeaveRoom = 3,   //离开房间
    SocketMessageIDKickOut = 5,     //剔出房间
    SocketMessageIDJoinSeat = 6,    //坐下
    SocketMessageIDLeaveSeat = 8,   //站起
    SocketMessageIDDisableMic = 10, //禁麦
    SocketMessageIDEnableMic = 11,  //取消禁麦
    SocketMessageIDLockSeat = 14,   //锁座位
    SocketMessageIDUnlockSeat = 15, //解锁座位
    SocketMessageID22 = 22,         //麦序置顶
    SocketMessageID23 = 23,         //移出麦序
    SocketMessageIDIsFreeMicro = 24,
    SocketMessageIDIsHeadBeat = 26,             //设置心动模式
    
    //============ 送礼 ==============/
    SocketMessageIDGroupGift = 29,              //送礼物（送给麦上的人，多个）
    SocketMessageIDSendGift = 58,               //送礼物（送给不在麦上的人，多个）
    SocketMessageIDSendBlueRoseMessage = 120,   //赠送蓝玫瑰
    
    SocketMessageIDSetUserIsManager = 31,       //设置管理员
    SocketMessageIDSetRoomLock = 33,            //修改房间锁状态
    SocketMessageIDWelcomMsg = 34,              //设置欢迎语
    SocketMessageIDClearHeart = 36,             //清空心动值
    SocketMessageIDSeatDuration = 38,           //给麦位上显示个倒计时
    SocketMessageIDSetTagId = 43,               //
    SocketMessageIDSetName = 49,                //
    SocketMessageIDSetDesc = 51,                //
    SocketMessageID53 = 53,                     //
    SocketMessageIDGetIsDisableMsgUsers = 56,   //
    SocketMessageIDGetOnlineUserList = 57,      //
    SocketMessageIDGetRankList = 59,            //
    SocketMessageIDBuyHammers = 65,             //购买锤子
    SocketMessageIDLuckyDraw = 66,              //砸蛋
    SocketMessageIDLuckyDrawRecord = 116,       //砸蛋前十条记录
    SocketMessageIDLuckyDrawAllRecord = 117,    //砸蛋所有记录
    SOcketMessageIDSendSendEmoticon = 61,
    
    SocketMessageIDLuckyDrawResult = 67,        //砸蛋返回结果
    SOcketMessageIDSendEmoticonBegin = 62,
    SOcketMessageIDSendEmoticonResult = 63,
    SocketMessageIDUserSetTop = 64,             //置顶
    
    SocketMessageIDRoomInit = 200,
    SocketMessageIDHeatValue = 54,
    SocketMessageIDUserJoinRoom = 4,            //加入房间
    SocketMessageIDInviteUpMirco = 70,          //邀请上麦
    SocketMessageIDReceiveInviteUpMirco = 71,   //收到上麦邀请
    SocketMessageIDReplyInviteUpMirco = 72,     //回复是否接受邀请
    SocketMessageIDReceiveInviteCost = 74,      //查询成本多少
    SocketMessageIDOnlineMember_apply = 75,       //申请上麦人数
    SocketMessageIDOnlineMember_online = 76,     //在线人数
    SocketMessageIDSendGiftAddFriendSuccess = 77,//送礼加好友成功
    SocketMessageIDSendReconnectRequest = 78,//发送断线重连请求
    SocketMessageIDCloseRoomRequest = 79,//自己不在房间
    
    
    
    SocketMessageID18 = 18,                     /* 原来是其他人进入房间的消息，已废弃，改用4 */
    SocketMessageIDMicroOrder = 20,             /*  进入到排序对列中  */
    SocketMessageIDMicroCancelOrder = 21,       /*  退出排序对列  */
    SocketMessageIDMicModelChanged = 25,
    SocketMessageIDHeadBeatChanged = 27,
    
    SocketMessageIDSeatHeartValueUpdate = 37,   //更新麦位心动值
    
    SocketMessageIDDeported = 45,   //{"MsgId":45}
    SocketMessageIDRoomLockUpdate = 48,    //{"MsgId":48,"IsLock":true}
    SocketMessageIDRoomManagerUpdate = 55,    //{"MsgId":55,"IsManager":true,"UserId":1001537,"Name":"小提莫","UserLevel":1,"VipLevel":0,"RoomGuardLevel":0,"UserGuardList":[],"DukeLevel":0}
    SocketMessageIDSeatCountdown = 39,   //{"MsgId":39,"Level":2,"Number":3,"SpeechTime":"2019-07-27T19:24:34.3481132+08:00","Duration":30}
    SocketMessageIDRoomWelcomeUpdate = 35,//{"MsgId":35,"WelcomMsg":"官方公告："}
    SocketMessageIDRoomDescUpdate = 52,
    SocketMessageIDRoomNameUpdate = 50, //{"MsgId":50,"Name":"iOS name"}
    
    
    // 任务
    SocketMessageIDTaskInfo = 80, // 同步任务
    SocketMessageIDTaskCheck = 81, // 任务检测请求
    SocketMessageIDTaskUpdateList = 82, // 同步任务列表
    SocketMessageIDTaskStartCounting = 83, //  任务开始计时
    SocketMessageIDTaskStopCounting = 84, // 任务停止计时
    SocketMessageIDTaskGetAward = 85, // 领取任务奖励
    SocketMessageIDTaskGetOneAward = 86, // 领取某个任务奖励
    
    // 系统
    SocketMessageIDUpdate = 300, // 心跳
    SocketMessageIDSystemToastMessage = 125, // 通知客户端提示语
    SocketMessageIDMicroSeatNumber = 127, // 同步用户上麦卡数量
    
    // 分享参数
    SocketMessageIDSystemShareParam = 87,
    
    // 点歌
    SocketMessageIDMusicList = 89, // 获取歌曲列表
    SocketMessageIDMusicDianbo = 90, // 点播
    SocketMessageIDMusicReserveList = 91, // 同步预约列表（服务器主动发送)
    SocketMessageIDMusicReserveSync = 124, // 预约歌曲同步（有人点歌，红娘收到此消息）
    SocketMessageIDMusicSticky = 92, // 置顶
    SocketMessageIDMusicNext = 93, // 播放下一首歌曲
    SocketMessageIDMusicPlayingDetail = 94, // 同步播放歌曲的信息（服务器主动发送）
    SocketMessageIDMusicDeleteReserve = 95, // 删除预约队列
    SocketMessageIDMusicGetReserveList = 96, // 获取预约队列
    SocketMessageIDMusicGetPlaySongParam = 97, // 获取播放歌曲信息
    SocketMessageIDMusicSearchList = 98, // 搜索歌曲
    SocketMessageIDMusicStartPlaySong = 99, // 播放歌曲
    SocketMessageIDMusicStartPlaySongSyncGist = 101, // 播放歌曲同步给嘉宾
    SocketMessageIDMusicStartPause = 102, // 暂停
    SocketMessageIDMusicStartSyncPause = 105, // 同步暂停
    SocketMessageIDMusicStartChangeVolum = 103, // 变音量
    SocketMessageIDMusicStartSyncVolum = 106, // 同步音量
    SocketMessageIDMusicStartRepeat = 104, // 重唱
    SocketMessageIDMusicStartSyncPepeat = 107, // 同步重唱
    SocketMessageIDMusicStartSyncLastSongerStop = 108, // 红娘切歌，切歌前演唱嘉宾收到消息
    SocketMessageIDMusicStartPlayingViewDetail = 109, // 播放页面，按钮状态值
    SocketMessageIDMusicStartDownloadSong = 110, // 开始下载歌曲
    SocketMessageIDMusicDownloadSongSuccess = 111, // 下载歌曲完成
    SocketMessageIDMusicDownloadSongState = 112, // 歌曲下载状态
    
    SocketMessageIDMusicUpdateSongLyric = 123, // 同步歌词
    SocketMessageIDMusicNotifySongLyric = 122, // 广播歌词
    
    SocketMessageIDMusicUpdateSongRank = 113, // 唱歌排行更新玫瑰数
    SocketMessageIDMusicRank = 118, // 房间内歌神排行榜
    
    SocketMessageIDShareHelpMessage = 119, //  广播助力消息
    
    SocketMessageIDUpdateGuardianMessage = 128, // 同步用户守护牌
    SocketMessageIDShowGuardianAnimationMessage = 129, // 显示守护动画
    
    SocketMessageIDLiveRoomSendMessage = 130, // 打字发言

    SocketMessageIDLiveRoomUpdateFinishTaskMessage = 131, // 打字发言
    
    //============ 麦位管理 ==============/
    SocketMessageIDUserSitdown = 7,             //广播上麦消息
    SocketMessageIDUserStandup = 9,             //广播下麦消息
    SocketMessageIDSeatMicDisable = 12, //广播禁麦消息
    SocketMessageIDSeatMicEnable = 13,  //广播取消禁麦消息
    SocketMessageIDSeatLocked = 16, // 广播锁麦消息
    SocketMessageIDSeatUnlocked = 17, // 广播取消锁麦消息
    SocketMessageIDSeatRoseValueUpdate = 121,   //更新麦位玫瑰数
    SocketMessageIDSeatFansList = 126, // 获取麦位粉丝列表
    
    //============ 礼物 ==============/
    SocketMessageIDGiftEvent = 30, // 广播赠送礼物消息
    
    //============ 红包 ==============/
    SocketMessageIDRobRedPacketMessage = 133,                   // 抢红包
    SocketMessageIDNotifyRedPacketResultToClientMessage = 134,  // 广播红包列表
    SocketMessageIDNotifyLetterEffectMessage = 135,             // 广播文字
    SocketMessageIDNotifyStartRobRedPacketMessage = 136,        // 广播开始抢红包
    SocketMessageIDNotifyRedPacketProgressMessage = 137,        // 广播红包进度
    SocketMessageIDGetRedPacketPlayDescMessage = 138,           // 红包玩法介绍

    //============ 用户信息 ==============/
    SocketMessageIDGetUserPersonalData = 46,            // 获取用户个人资料
    SocketMessageIDGetDisableMsgTemplates = 132,        // 禁言表数据获取
    SocketMessageIDSetUserIsDisableMsg = 28,            // 设置是否禁言
    SocketMessageIDUserDisableChatMessage = 47,         // 收到有没有被主持禁言
    
};


#define SocketMessageStatusCodeSuccess 1


@class SocketMessageResponse;


@interface SocketMessage : NSObject

@property SocketMessageID MsgId;

- (NSString*)toString;

@end


@interface SocketMessageRequest : SocketMessage;

- (Class)responseClass;
@property (nonatomic, strong) SocketMessageResponse * response;
@property (nonatomic, strong) NSError * error;
@property (nonatomic, copy) void(^callback)(__kindof SocketMessageRequest * request);
- (NSString*)toRequestJson;
- (BOOL)noError;

@end


@interface SocketMessageResponse : SocketMessage

@property (nonatomic, strong) NSNumber * Success;
- (BOOL)isSuccess;
- (NSString*)desc;

@end


@interface SocketMessageNotification : SocketMessage

@end


#define SocketMessageInitMethod(ID) \
- (instancetype)init {  \
    if (self = [super init]) {  \
        self.MsgId = ID;    \
    }   \
    return self;    \
}

NS_ASSUME_NONNULL_END



#import "SocketMessageGetRankList.h"
#import "SocketMessageLuckyDraw.h"
#import "SocketMessageJoinRoom.h"
#import "SocketMessageGetOnlineUserList.h"
#import "SocketMessageGetUserInfo.h"
#import "SocketMessageLogin.h"
#import "SocketMessageLeaveRoom.h"
#import "SocketMessageLuckyDrawResult.h"
#import "SocketMessageGroupGift.h"
#import "SocketMessageIsHeadBeat.h"


#import "SocketMessageRoomInit.h"
#import "SocketMessageUserJoinRoom.h"
#import "SocketMessageHeatValue.h"
#import "SocketMessageUserSitdown.h"
#import "SocketMessageUserStandup.h"
#import "SocketMessageSeatLocked.h"
#import "SocketMessageSeatUnlocked.h"
#import "SocketMessageMicModelChanged.h"
#import "SocketMessageHeadBeatChanged.h"
#import "SocketMessageSeatHeartValueUpdate.h"
#import "SocketMessageGiftEvent.h"
#import "SocketMessageMicroOrder.h"
#import "SocketMessageSeatMicDisable.h"
#import "SocketMessageSeatMicEnable.h"
#import "SocketMessageUserDisableChatMessage.h"
#import "SocketMessageDeported.h"
#import "SocketMessageRoomLockUpdate.h"
#import "SocketMessageRoomManagerUpdate.h"
#import "SOcketMessageSendEmoticonBegin.h"
#import "SOcketMessageSendEmoticonResult.h"
#import "SocketMessageRoomDescUpdate.h"
#import "SocketMessageRoomNameUpdate.h"
#import "SocketMessageRoomWelcomeUpdate.h"
#import "SocketMessageSeatCountdown.h"
#import "SocketMessageRoomManagerUpdate.h"
#import "SocketMessageInvite.h"
#import "CXSocketMessageOnlineMemberNumber.h"

#import "SocketMessageTextMessage.h"

#import "CXSocketMessageTask.h"
#import "CXSocketMessageSystemRequest.h"

#import "SocketMessageJoinSeat.h"
#import "SocketMessageUnlockSeat.h"
#import "SocketMessageLockSeat.h"
#import "SocketMessageLeaveSeat.h"
#import "SocketMessageSetRoomLock.h"

#import "CXSocketMessageMusic.h"
#import "CXLiveRoomSendGiftRequest.h"

#import "CXSocketMessageSeatsRequest.h"

#import "SocketMessageKickOut.h"

#import "SocketMessageSendGift.h"

// 红包
#import "CXSocketMessageRedPacket.h"
