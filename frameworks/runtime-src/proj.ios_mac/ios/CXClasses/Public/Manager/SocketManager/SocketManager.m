//
//  SocketManager.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketManager.h"
#import <SocketRocket/SocketRocket.h>
#import "CXSocketMessageSendReconnectRequest.h"
#import "CXLiveRoomViewController.h"
#import "CXBaseNavigationController.h"

typedef enum : NSUInteger {
    JoinRoom,
    LeaveRoom,
} SocketManagerState;

@interface SocketManager () <SRWebSocketDelegate>

@property (nonatomic) NSMutableDictionary<NSNumber*, NSMutableArray<__kindof SocketMessageRequest*>*> * sendingMessages;
@property (nonatomic) NSMutableDictionary<NSNumber*, Class> * messageClasses;

@property (nonatomic, nullable) NSString * token;
@property (nonatomic, nullable) NSString * roomId;
@property (nonatomic, nullable) NSURL * address;
@property (nonatomic, nullable) SRWebSocket *socket;
@property (nonatomic, assign) SocketManagerState smState;

@property (nonatomic, nullable, weak) SocketMessageJoinRoom * joinRoomRequest;
@property (nonatomic, nullable, weak) SocketMessageLogin * loginRequest;
@property (nonatomic, nullable, weak) SocketMessageLeaveRoom * leaveRoomRequest;
@property (nonatomic, nullable, weak) CXSocketMessageSendReconnectRequest *reconnectRequest;

@end


@implementation SocketManager

- (instancetype)init {
    if (self = [super init]) {
        _sendingMessages = [NSMutableDictionary new];
        _messageClasses = [NSMutableDictionary new];
        [_messageClasses addEntriesFromDictionary:@{
                                                    @(SocketMessageIDRoomInit):SocketMessageRoomInit.class
                                                    ,@(SocketMessageIDUserJoinRoom):SocketMessageUserJoinRoomArgs.class
                                                    ,@(SocketMessageIDHeatValue):SocketMessageHeatValue.class
                                                    ,@(SocketMessageIDUserSitdown):SocketMessageUserSitdownArgs.class
                                                    ,@(SocketMessageIDUserStandup):SocketMessageUserStandupArgs.class
                                                    ,@(SocketMessageIDSeatLocked):SocketMessageSeatLockedArgs.class
                                                    ,@(SocketMessageIDSeatUnlocked):SocketMessageSeatLockedArgs.class
                                                    ,@(SocketMessageIDMicModelChanged):SocketMessageMicModelChanged.class
                                                    ,@(SocketMessageIDHeadBeatChanged):SocketMessageHeadBeatChanged.class
                                                    ,@(SocketMessageIDSeatHeartValueUpdate):SocketMessageSeatHeartValueUpdate.class
                                                    ,@(SocketMessageIDSeatRoseValueUpdate):SocketMessageSeatHeartValueUpdateArgs.class
                                                    ,@(SocketMessageIDGiftEvent):SocketMessageGiftEventArgs.class
                                                    ,@(SocketMessageIDMicroOrder):SocketMessageMicroOrder.class
                                                    ,@(SocketMessageIDReceiveInviteUpMirco):SocketMessageInviteResponse.class
                                                    ,@(SocketMessageIDReceiveInviteCost):SocketMessageUpMricoCost.class
                                                    ,@(SocketMessageIDReplyInviteUpMirco):SocketMessageReplyInvite.class
                                                    ,@(SocketMessageIDMicroCancelOrder):SocketMessageMicroCancelOrder.class
                                                    ,@(SocketMessageIDSeatMicDisable):SocketMessageSeatMicDisableArgs.class
                                                    ,@(SocketMessageIDSeatMicEnable):SocketMessageSeatMicDisableArgs.class
                                                    ,@(SocketMessageIDUserDisableChatMessage):SocketMessageUserDisableChatMessage.class
                                                    ,@(SocketMessageIDDeported):SocketMessageDeported.class
                                                    ,@(SocketMessageIDRoomLockUpdate):SocketMessageRoomLockUpdate.class
                                                    ,@(SocketMessageIDRoomManagerUpdate):SocketMessageRoomManagerUpdate.class
                                                    ,@(SocketMessageIDLuckyDrawResult):SocketMessageLuckyDrawResultArgs.class
                                                    ,@(SOcketMessageIDSendEmoticonBegin):SOcketMessageSendEmoticonBegin.class
                                                    ,@(SOcketMessageIDSendEmoticonResult):SOcketMessageSendEmoticonResult.class
                                                    ,@(SocketMessageIDRoomDescUpdate):SocketMessageRoomDescUpdate.class
                                                    ,@(SocketMessageIDRoomNameUpdate):SocketMessageRoomNameUpdate.class
                                                    ,@(SocketMessageIDRoomWelcomeUpdate):SocketMessageRoomWelcomeUpdate.class
                                                    ,@(SocketMessageIDSeatCountdown):SocketMessageSeatCountdown.class
                                                    ,@(SocketMessageIDOnlineMember_apply):CXSocketMessageOnlineMemberNumber.class
                                                    ,@(SocketMessageIDOnlineMember_online):CXSocketMessageOnlineMemberNumber.class
                                                    ,@(SocketMessageIDSendGiftAddFriendSuccess):SocketMessageSendGiftAddFriendResponse.class
                                                    ,@(SocketMessageIDCloseRoomRequest):SocketMessageDeported.class
                                                    ,@(SocketMessageIDSystemToastMessage):SocketMessageDeported.class
                                                    ,@(SocketMessageIDTaskInfo):CXSocketMessageTaskUpdateInfo.class
                                                    ,@(SocketMessageIDTaskUpdateList):CXSocketMessageTaskInfo.class
                                                    ,@(SocketMessageIDTaskStartCounting):CXSocketMessageTaskUpdateInfo.class
                                                    ,@(SocketMessageIDTaskStopCounting):CXSocketMessageTaskUpdateInfo.class
                                                    
                                                    // Music
                                                    ,@(SocketMessageIDMusicReserveList):CXSocketMessageMusicReceiveReverseList.class
                                                    ,@(SocketMessageIDMusicPlayingDetail):SocketMessageMusicReceivePlayingDetail.class
                                                    ,@(SocketMessageIDMusicStartPlaySongSyncGist):SocketMessageMusicReceivePlayingDetail.class
                                                    ,@(SocketMessageIDMusicStartSyncPause):SocketMessageMusicReceivePlayingStatus.class
                                                    ,@(SocketMessageIDMusicStartSyncVolum):SocketMessageMusicReceivePlayingStatus.class
                                                    ,@(SocketMessageIDMusicStartPlayingViewDetail):SocketMessageMusicReceivePlayingStatus.class
                                                    ,@(SocketMessageIDMusicStartSyncLastSongerStop):SocketMessageMusicReceivePlayingStatus.class
                                                    ,@(SocketMessageIDMusicDownloadSongState):SocketMessageMusicReceivePlayingStatus.class
                                                    ,@(SocketMessageIDMusicStartSyncPepeat):SocketMessageMusicReceivePlayingStatus.class
                                                    ,@(SocketMessageIDMusicUpdateSongRank):SocketMessageMusicUpdateSongRank.class
                                                    ,@(SocketMessageIDMusicNotifySongLyric):SocketMessageMusicNotifySongLyric.class
                                                    ,@(SocketMessageIDShareHelpMessage):CXSocketMessageSystemShareHelpNotification.class
                                                    ,@(SocketMessageIDMusicReserveSync):CXSocketMessageMusicReverseSync.class
                                                    
                                                    // System
                                                    ,@(SocketMessageIDMicroSeatNumber):CXSocketMessageSystemNotification.class
                                                    
                                                    ,@(SocketMessageIDUpdateGuardianMessage):SocketMessageUserJoinRoom.class
                                                    ,@(SocketMessageIDShowGuardianAnimationMessage):SocketMessageUserJoinRoom.class
                                                    ,@(SocketMessageIDKeepaLiveNotification):CXSocketMessageSystemNotification.class
                                                    
                                                    // Task
                                                    ,@(SocketMessageIDLiveRoomUpdateFinishTaskMessage):CXSocketMessageTaskUpdateFinishTaskInfo.class
                                                    
                                                    // RedPacket
                                                    ,@(SocketMessageIDNotifyRedPacketResultToClientMessage):CXSocketMessageNotifyRedPacketResultToClient.class
                                                    ,@(SocketMessageIDNotifyStartRobRedPacketMessage):CXSocketMessageNotifyStartRobRedPacket.class
                                                    ,@(SocketMessageIDNotifyRedPacketProgressMessage):CXSocketMessageNotifyRedPacketProgress.class
                                                    ,@(SocketMessageIDNotifyLetterEffectMessage):CXSocketMessageNotifyStartRobRedPacket.class
                                                    
                                                    }];
    }
    return self;
}

- (void)dealloc {
    if (self.socket) {
        switch (self.socket.readyState) {
            case SR_CONNECTING:
            case SR_OPEN:
                self.delegate = nil;
                [self.socket close];
                break;
            case SR_CLOSING:
                break;
            case SR_CLOSED:
                break;
        }
    }
}

#pragma mark - request

- (NSMutableArray<__kindof SocketMessageRequest *>*)messageQueueForID:(NSNumber*)MsgId {
    NSMutableArray<__kindof SocketMessageRequest *> * queue = [_sendingMessages objectForKey:MsgId];
    if (!queue) {
        queue = [NSMutableArray new];
        [_sendingMessages setObject:queue forKey:MsgId];
    }
    return queue;
}

- (__kindof SocketMessageRequest * _Nullable)finishMessage:(NSNumber*)MsgId {
    NSMutableArray * array = [self messageQueueForID:MsgId];
    if (array && array.count) {
        __kindof SocketMessageRequest * request = array.firstObject;
        [array removeFirstObject];
        return request;
    }
    return nil;
}

- (nullable __kindof SocketMessageRequest *)sendRequest:(__kindof SocketMessageRequest *)request withCallback:(nullable SocketManagerSendRequestCallback)callback {
    NSString * requestJson = [request toRequestJson];
    request.callback = callback;
    [self.socket send:requestJson];
    [[self messageQueueForID:@(request.MsgId)] addObject:request];
    
    return request;
}

- (BOOL)joinRoom:(NSString*)roomId withToken:(NSString*)token atAddr:(NSString*)address {
    if (!roomId.length) return NO;
    if (!token.length) return NO;
    if (!address.length) return NO;
    NSURL * url = [NSURL URLWithString:address];
    if (!url) return NO;
    
    _token = token;
    _address = url;
    _roomId = roomId;
    
    self.socket = [[SRWebSocket alloc] initWithURL:self.address];
    self.socket.delegate = self;
    
    [self.socket open];
    
    return YES;
}

#pragma mark - Login
- (nullable SocketMessageLogin *)login {
    self.smState = JoinRoom;
    SocketMessageLogin * login = [SocketMessageLogin new];
    login.UserToken = self.token;
    __weak typeof (self) wself = self;
    [CXClientModel instance].isSocketManagerReconnect = NO;
    self.loginRequest = [self sendRequest:login withCallback:^(SocketMessageLogin * _Nonnull login) {
        wself.loginRequest = nil;
        if ([login.response.Success isEqual: @-2]) {
            // 重连登录
            CXSocketMessageSendReconnectRequest *reconnect = [CXSocketMessageSendReconnectRequest new];
            wself.reconnectRequest = [wself sendRequest:reconnect withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                if (request.response.isSuccess) {
                    
                    if ([CXClientModel instance].isJoinedRoom == YES) {
                        // 重连成功
                        if (wself.delegate && [wself.delegate respondsToSelector:@selector(socketManager:reconnectionSuccess:)]) {
                            [wself.delegate socketManager:wself reconnectionSuccess:YES];
                            
                            [CXClientModel instance].isSocketManagerReconnect = YES;
                        }
                    } else {
                        UIViewController *vc = [CXTools currentViewController];
                        CXLiveRoomViewController *roomVC = [CXLiveRoomViewController new];
                        CXBaseNavigationController *nav = [[CXBaseNavigationController alloc] initWithRootViewController:roomVC];
                        [vc presentViewController:nav modalStyle:UIModalPresentationFullScreen animated:YES completion:nil];
                    }
                    
                } else {
                    if (self.delegate && [self.delegate respondsToSelector:@selector(socketManager:room:error:)]) {
                        NSError *error = [[NSError alloc] init];
                        [self.delegate socketManager:self room:self.roomId error:error];
                    }
                }
            }];
            
        } else if (login.response.isSuccess) {
            // 进入房间
            [wself joinRoom];
        } else {
            // 出错
            [wself.socket close];
        }
    }];
    return self.loginRequest;
}

#pragma mark - join room
- (nullable SocketMessageJoinRoom *)joinRoom {
    SocketMessageJoinRoom * joinRoom = [SocketMessageJoinRoom new];
    joinRoom.RoomId = self.roomId;
    
    // 红娘进房间
    joinRoom.RoomName = [CXClientModel instance].room.RoomData.RoomName;
    joinRoom.RoomMode = [CXClientModel instance].room.RoomData.RoomType.integerValue;
//    joinRoom.IsExclusiveRoom = [CXClientModel instance].room.RoomData.IsExclusiveRoom;
//    joinRoom.IsCloseCamera = [CXClientModel instance].room.RoomData.IsCloseCamera;
//    joinRoom.IsOpenRedPacket = [CXClientModel instance].room.RoomData.IsOpenRedPacket;
//    joinRoom.IsOpenBreakEgg = [CXClientModel instance].room.RoomData.IsOpenBreakEgg;
//    joinRoom.IsOpenPickSong = [CXClientModel instance].room.RoomData.IsOpenPickSong;
//    joinRoom.IsOpenMediaLibrary = [CXClientModel instance].room.RoomData.IsOpenMediaLibrary;
//    joinRoom.IsOpenVideoFrame = [CXClientModel instance].room.RoomData.IsOpenVideoFrame;
    
    __weak typeof (self) wself = self;
    self.joinRoomRequest = [self sendRequest:joinRoom withCallback:^(SocketMessageJoinRoom * _Nonnull joinRoom) {
        wself.joinRoomRequest = nil;
        if (joinRoom.response.isSuccess) {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(socketManager:joinRoomSuccess:)]) {
                [wself.delegate socketManager:wself joinRoomSuccess:wself.roomId];
            }
        } else if ([joinRoom.response.Success isEqual:@-127]) {
            [wself joinRoom];
        } else if([joinRoom.response.Success isEqual:@3]) {
            [[CXTools currentViewController] toast:@"此房间已锁"];
            [wself.socket close];
        } else if([joinRoom.response.Success isEqual:@7]) {
            [[CXTools currentViewController] toast:@"房间已解散，请刷新重试"];
            [wself.socket close];
        } else if ([joinRoom.response.Success isEqual:@9]) {
            [[CXTools currentViewController] toast:@"玫瑰余额不足，你不能进入专属视频房"];
            [wself.socket close];
        } else if([joinRoom.response.Success isEqual:@10]) {
            [[CXTools currentViewController] toast:joinRoom.response.Code];
            [wself.socket close];
        } else if([joinRoom.response.Success isEqual:@11]) {
            // 处于封禁状态，暂时不能开播
            NSString *timeStr = @"";
            if (joinRoom.response.BanTime < 0) {
                timeStr = @"永久";
            } else {
                timeStr = [wself getMMSSFromSS:joinRoom.response.BanTime];
            }
            
            NSString *banStr = [NSString stringWithFormat:@"您由于%@被禁播，还有%@禁播结束",joinRoom.response.Code, timeStr];
            [[CXTools currentViewController] toast:banStr];
            [wself.socket close];
        } else if([joinRoom.response.Success isEqual:@20]) {
            [[CXTools currentViewController] toast:@"服务器在维护"];
            [wself.socket close];
        } else {
            [[CXTools currentViewController] toast:joinRoom.response.Code];
            [wself.socket close];
        }
        
    }];
    return self.joinRoomRequest;
}

//传入 秒  得到 xx:xx:xx
- (NSString *)getMMSSFromSS:(NSInteger)totalTime{
    NSInteger seconds = totalTime;

    NSString *str_hour = [NSString stringWithFormat:@"%02ld",seconds/3600];
    //format of minute
    NSString *str_minute = [NSString stringWithFormat:@"%02ld",(seconds%3600)/60];
    //format of second
    NSString *str_second = [NSString stringWithFormat:@"%02ld",seconds%60];
    //format of time
    NSString *format_time = [NSString stringWithFormat:@"%@:%@:%@",str_hour,str_minute,str_second];
    
    return format_time;
}

#pragma mark - leave room
- (void)leaveRoom {
    if (!self.socket) {
        if (self.delegate && [self.delegate respondsToSelector:@selector(socketManager:didLeaveRoom:)]) {
            [self.delegate socketManager:self didLeaveRoom:self.roomId];
        }
        return;
    }
    self.smState = LeaveRoom;
    SocketMessageLeaveRoom * leaveRoom = [SocketMessageLeaveRoom new];
    __weak typeof (self) wself = self;
    self.leaveRoomRequest = [self sendRequest:leaveRoom withCallback:^(SocketMessageLeaveRoom * _Nonnull leaveRoom) {
        wself.leaveRoomRequest = nil;
        if ([leaveRoom.response.Success isEqual:@1]) {
            [wself.socket close];
        }
        [wself.socket close];
    }];
}

#pragma mark - delegate

- (void)webSocketDidOpen:(SRWebSocket *)webSocket {
    if (self.roomId) {
        [self login];
    } else {
        [self.socket close];
    }
}

- (void)webSocket:(SRWebSocket *)webSocket didFailWithError:(NSError *)error {
    self.socket.delegate = nil;
    self.socket = nil;
    NSMutableDictionary<NSNumber*, NSMutableArray<__kindof SocketMessageRequest*>*> * sendingMessages = [self.sendingMessages copy];
    [self.sendingMessages removeAllObjects];
    [sendingMessages enumerateKeysAndObjectsUsingBlock:^(NSNumber * _Nonnull key, NSMutableArray<__kindof SocketMessageRequest *> * _Nonnull obj, BOOL * _Nonnull stop) {
        [obj enumerateObjectsUsingBlock:^(__kindof SocketMessageRequest * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            obj.error = error;
            if (obj.callback) {
                obj.callback (obj);
            }
        }];
    }];
    
    if (self.smState == LeaveRoom) {
        if (self.delegate && [self.delegate respondsToSelector:@selector(socketManager:didLeaveRoom:)]) {
            [self.delegate socketManager:self didLeaveRoom:self.roomId];
        }
    } else {
        if (self.delegate && [self.delegate respondsToSelector:@selector(socketManager:room:error:)]) {
            [self.delegate socketManager:self room:self.roomId error:error];
        }
    }
}

- (void)webSocket:(SRWebSocket *)webSocket didCloseWithCode:(NSInteger)code reason:(NSString *)reason wasClean:(BOOL)wasClean {
    self.socket.delegate = nil;
    self.socket = nil;
    
    NSError * error = [NSError errorWithDomain:@"com.xinyue.mua.game" code:10001 userInfo:@{NSLocalizedDescriptionKey:@"连接已断开"}];
    NSMutableDictionary<NSNumber*, NSMutableArray<__kindof SocketMessageRequest*>*> * sendingMessages = [self.sendingMessages copy];
    [self.sendingMessages removeAllObjects];
    [sendingMessages enumerateKeysAndObjectsUsingBlock:^(NSNumber * _Nonnull key, NSMutableArray<__kindof SocketMessageRequest *> * _Nonnull obj, BOOL * _Nonnull stop) {
        [obj enumerateObjectsUsingBlock:^(__kindof SocketMessageRequest * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            obj.error = error;
            if (obj.callback) {
                obj.callback (obj);
            }
        }];
    }];
    
    if (self.smState == LeaveRoom) {
        if (self.delegate && [self.delegate respondsToSelector:@selector(socketManager:didLeaveRoom:)]) {
            [self.delegate socketManager:self didLeaveRoom:self.roomId];
        }
    } else {
        if (self.delegate && [self.delegate respondsToSelector:@selector(socketManager:room:error:)]) {
            [self.delegate socketManager:self room:self.roomId error:error];
        }
    }
}

- (void)webSocket:(SRWebSocket *)webSocket didReceiveMessage:(id)message {
    @try {
        if ([message isKindOfClass:NSString.class]) {
            NSData * data = [message dataUsingEncoding:NSUTF8StringEncoding];
            NSError * error;
            NSDictionary * json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
            if (!error) {
                SocketMessageResponse * response = [SocketMessageResponse modelWithJSON:json];
                SocketMessageRequest * request = [self finishMessage:@(response.MsgId)];
                if (request) {
                    request.response = [[request responseClass] modelWithJSON:json];
                    if (request.callback) {
                        request.callback(request);
                    }
                }
                else {
                    Class class = [_messageClasses objectForKey:@(response.MsgId)];
                    if (class != nil) {
                        id msg = [class modelWithJSON:json];
                        if (msg) {
                            if (self.delegate && [self.delegate respondsToSelector:@selector(socketManager:didReceiveMessage:)]) {
                                [self.delegate socketManager:self didReceiveMessage:msg];
                            }
                        }
                    }
                }
            }
        }
    } @catch (NSException *exception) {
    } @finally {
        
    }
}

@end
