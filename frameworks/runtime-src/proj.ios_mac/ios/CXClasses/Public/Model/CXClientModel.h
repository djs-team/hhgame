//
//  CXClientModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/15.
//

#import <Foundation/Foundation.h>
#import "SocketMessage.h"
#import "SocketManager.h"
#import "CXLiveRoomModel.h"
#import "AgoraRtcEngineManager.h"
#import "EasemobManager.h"
#import "CXFriendInviteModel.h"
#import "EasemobManager.h"
#import "CXSytemModel.h"
#import "DDAudioLRCParser.h"

typedef NS_ENUM(NSUInteger, GameUserIdentity) {
    GameUserIdentityNormal = 0,
    GameUserIdentityManager,
    GameUserIdentityOwner,
    GameUserIdentityAdmin,
};

NS_ASSUME_NONNULL_BEGIN

@class CXClientModel;

@protocol CXClientModelEventListener <NSObject>
@optional
// Socket
- (void)modelClient:(CXClientModel *)client reconnectRoomSuccess:(BOOL)success;
- (void)modelClient:(CXClientModel *)client room:(NSString *)roomId error:(NSError *)error;
- (void)modelClient:(CXClientModel *)client didReceiveNotification:(__kindof SocketMessageNotification*)notification;
// Ease
- (void)modelClient:(CXClientModel *)client didReceiveRoomMessage:(NSArray<EasemobRoomMessage*>*)msgs;
@end

/** 加入房间Block*/
typedef void(^CXClientModelJoinRoomCallBack)(NSString *roomId, BOOL success);
typedef void(^CXClientModelLeaveRoomCallBack)(NSString *roomId, BOOL success);

@interface CXClientModel : NSObject

@property (nullable, nonatomic, readonly) NSHashTable<id<CXClientModelEventListener>> * listener;
@property (nullable, nonatomic, readonly) AgoraRtcEngineManager * agoraEngineManager;
@property (nullable, nonatomic, strong) SocketManager * socket;
@property (nullable, nonatomic, readonly) EasemobManager * easemob;

// 用户相关
@property (nonatomic, strong) NSString *applePayType; // 苹果支付方式
@property (nonatomic, strong) NSString *userId;
@property (nonatomic, strong) NSString *token;
@property (nonatomic, strong) NSNumber *sex; // 1:男 2:女
@property (nonatomic, strong) NSString *avatar;
@property (nonatomic, strong) NSString *username;
@property (nonatomic, strong) NSString *nickname;
@property (strong, nonatomic) NSNumber *balance;

// 是否可获赠上麦卡，1:可以 2:不可以
@property (nullable, assign) NSNumber * is_receive;
// 可领取上麦卡数量
@property (nullable, assign) NSNumber * card_num;

@property (nonatomic, strong) NSString *unreadCountStr;// 当前用户未读消息数

// 所有好友的userid数组
@property (nonatomic, strong) NSArray *firendIdArrays;
// 所有好友的数据
@property (nonatomic, strong) NSArray <CXFriendInviteModel *> *firendArrays;

@property (nonatomic, assign) BOOL user_isAnchor; // 自己是不是主播身份
@property (nullable, assign) NSNumber * is_receive; // 是否可获赠上麦卡，1:可以 2:不可以
@property (nullable, assign) NSNumber * card_num; // 可领取上麦卡数量

@property (nonatomic, assign) BOOL isAgreeInviteJoinRoom; // 是否同意邀请加入房间
@property (nonatomic, strong) CXInviteMikeModel *currentAgreeInviteMikeModel; // 当前同意的邀请

@property (nonatomic, assign) BOOL isSocketManagerReconnect; // 是否断线重连了

// music
@property (nonatomic, assign) NSInteger currentMusicPlayingProgress;
@property (nonatomic, strong) DDAudioLRC *currentMusicPlayingLRCModel;
@property (nonatomic, strong) NSURL *currentMusicPlayingSongPath;

@property (nonatomic, copy) NSString *registration_id;

// 房间相关
@property (nonatomic, strong) CXLiveRoomModel *room;
// YES：房间内 NO：房间外
@property (nonatomic, assign) BOOL isJoinedRoom;

+ (instancetype)instance;

- (void)joinRoom:(NSString *)roomId callback:(CXClientModelJoinRoomCallBack)callback;
- (void)reconnectRoom:(NSString *)roomId;
- (void)leaveRoomCallBack:(CXClientModelLeaveRoomCallBack)callback;

// 发送Socket请求
- (nullable __kindof SocketMessageRequest *)sendSocketRequest:(__kindof SocketMessageRequest *)request withCallback:(nullable SocketManagerSendRequestCallback)callback;

// 发送环信消息
- (nullable EasemobRoomMessage *)sendRoomText:(NSString*)text success:(void(^)(EasemobRoomMessage * _Nonnull msg))callback;


@end

NS_ASSUME_NONNULL_END
