//
//  CXClientModel.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/15.
//

#import "CXClientModel.h"

@interface CXClientModel() <SocketManagerDelegate, EasemobManagerDelegate>

@property (nonatomic, copy) CXClientModelJoinRoomCallBack joinRoomCallBack;
@property (nonatomic, copy) CXClientModelLeaveRoomCallBack leaveRoomCallBack;

@property (nullable, nonatomic) SocketManager * socket;

@property (nonatomic, strong) SocketMessageUserJoinRoom *currentJoinRoom_user;

@end

@implementation CXClientModel

+ (instancetype)instance {
    static CXClientModel * _instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [[CXClientModel alloc] _init];
    });
    return _instance;
}

- (instancetype)_init {
    if (self = [super init]) {
        
        _agoraEngineManager = [AgoraRtcEngineManager instanceWithAppId:@"30870262f27a4642a99e67cc1851f90a"];
        _easemob = [EasemobManager instanceWithAppKey:@"1101191012041033#hehequanji"];
//
//        _agoraEngineManager.delegate = self;
//        _easemob.delegate = self;
        _listener = [NSHashTable weakObjectsHashTable];
        _room = [[CXLiveRoomModel alloc] init];
        _room.RoomData = [[CXLiveRoomDataModel alloc] init];
        self.socket = [[SocketManager alloc] init];
        self.socket.delegate = self;
//        self.dateFormatter = [NSDateFormatter new];
//        self.dateFormatter.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSSSSSzzz";
    }
    return self;
}

- (void)joinRoom:(NSString*)roomId callback:(CXClientModelJoinRoomCallBack)callback {
    if (!roomId) {
        callback ? callback(roomId, NO) : nil;
        return;
    }
    _joinRoomCallBack = callback;
    kWeakSelf
    [CXHTTPRequest csharp_httpWithMethod:HJRequestMethodGET url:@"/Master/GetGate" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (responseObject) {
            NSString *addr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
            if ([weakSelf.socket joinRoom:roomId withToken:weakSelf.token atAddr:addr] == NO) {
                callback ? callback(roomId, NO) : nil;
                return;
            }
        }
    }];
}

- (void)leaveRoomCallBack:(CXClientModelLeaveRoomCallBack)callback {
    _leaveRoomCallBack = callback;
    [self.socket leaveRoom];
    [self.agoraEngineManager leaveRoom:nil];
    [self.easemob leaveRoom];
}

#pragma mark - ==================== Socket ===========================

- (nullable __kindof SocketMessageRequest *)sendSocketRequest:(__kindof SocketMessageRequest *)request withCallback:(nullable SocketManagerSendRequestCallback)callback {
    return [self.socket sendRequest:request withCallback:callback];
}

#pragma mark - SocketManagerDelegate
/// 加入成功
- (void)socketManager:(SocketManager*)mgr joinRoomSuccess:(NSString*)roomId {
    if (self.joinRoomCallBack) {
        self.joinRoomCallBack(roomId, YES);
    }
}
/// 重连成功
- (void)socketManager:(SocketManager*)mgr reconnectionSuccess:(BOOL)success {
    [[self.listener allObjects] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj respondsToSelector:@selector(modelClient:reconnectRoomSuccess:)]) {
            [obj modelClient:self reconnectRoomSuccess:success];
        }
    }];
}
/// 房间内出错
- (void)socketManager:(SocketManager*)mgr room:(NSString*)roomId error:(NSError*)error {
    [[self.listener allObjects] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj respondsToSelector:@selector(modelClient:room:error:)]) {
            [obj modelClient:self room:roomId error:error];
        }
    }];
}
/// 离开房间
- (void)socketManager:(SocketManager*)mgr didLeaveRoom:(NSString*)roomId {
    if (self.leaveRoomCallBack) {
        self.leaveRoomCallBack(roomId, YES);
    }
}
/// 收到消息
- (void)socketManager:(SocketManager *)mgr didReceiveMessage:(__kindof SocketMessageNotification*)message {
    switch (message.MsgId) {
        case SocketMessageIDRoomInit:
            {
                //转换消息类型
                SocketMessageRoomInit * roomInit = message;
                self.balance = roomInit.Balance;
                //创建聊天室数据模型
                self.room = [[CXLiveRoomModel alloc] initWithMessage:roomInit];               
                //计算热度值
                self.room.HeatValue = @(self.room.RoomData.VisitorNumbers.integerValue + self.room.RoomData.ExternVisitorNumbers.integerValue);
                self.room.CanSendGiftToEmcee = roomInit.CanSendGiftToEmcee;
                if (!self.room.mineInfoInRoom && _currentJoinRoom_user) {
                    self.room.mineInfoInRoom = _currentJoinRoom_user;
                }
            }
            break;
        case SocketMessageIDUserJoinRoom: {
            SocketMessageUserJoinRoomArgs *args = message;
            
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageUserJoinRoom * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                SocketMessageUserJoinRoom * user = obj;
                self.room.HeatValue = user.VisitorNum;
                if ([user.UserId isEqualToString:self.userId]) {
                    self.room.mineInfoInRoom = user;
                    self.currentJoinRoom_user = user;
                }
            }];
        }
            break;
        case SocketMessageIDUserSitdown:
        {
            SocketMessageUserSitdownArgs *args = message;
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageUserSitdown * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                //转换消息类型
                SocketMessageUserSitdown * userSitdown = obj;
                //获取麦位索引
                NSIndexPath * indexPath = [userSitdown.MicroInfo indexPath];
                //创建麦位数据
                LiveRoomMicroInfo * seat = [LiveRoomMicroInfo modelWithJSON:[userSitdown.MicroInfo modelToJSONObject]];
                //创建用户数据
                LiveRoomUser * user = [LiveRoomUser modelWithJSON:[userSitdown.MicroInfo.User modelToJSONObject]];
                //更新麦位
                [self.room.seats setObject:seat forKey:indexPath];
                //更新用户
                [self.room.users setObject:user forKey:user.UserId];
                //模型引用关联
                seat.modelUser = user;
                user.modelSeat = seat;
                //表引用关联
                [self.room.seatUsers setObject:user.UserId forKey:indexPath];
                [self.room.userSeats setObject:indexPath forKey:user.UserId];
            }];
            
        }
            break;
        case SocketMessageIDUserStandup: {//有人下麦了
            SocketMessageUserStandupArgs *args = message;
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageUserStandup * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                //转换消息类型
                SocketMessageUserStandup * userStandup = obj;
                //获取麦位索引
                NSIndexPath * indexPath = [userStandup indexPath];
                //获取麦位
                LiveRoomMicroInfo * seat = [self.room.seats objectForKey:indexPath];
                //获取麦位上的用户
                LiveRoomUser * user = seat.modelUser;
                
                if ([userStandup.IsKick isEqual: @1] && userStandup.Level == [CXClientModel instance].sex && [user.UserId isEqualToString:self.userId]) {
                    [[CXTools currentViewController] toast:@"你已被主持移除麦"];
                }
                
                //解除模型引用
                seat.modelUser = nil;
                user.modelSeat = nil;
                //解除表关联
                [self.room.userSeats removeObjectForKey:user.UserId];
                [self.room.seatUsers removeObjectForKey:indexPath];
                //移除用户
                [self.room.users removeObjectForKey:user.UserId];
            }];
            
        }
            break;
        case SocketMessageIDMicroOrder:{//收到这个消息，不干其他的，只加入队列
            SocketMessageMicroOrder * order = message;
            
            LiveRoomMicroOrder * ord = [LiveRoomMicroOrder modelWithJSON:[order.MicroOrderData modelToJSONObject]];
            
            if (order.MicroOrderData.User) {
                LiveRoomUser * userInfo = [LiveRoomUser modelWithJSON:[order.MicroOrderData.User modelToJSONObject]];
                
                ord.modelUser = userInfo;
                
                if (self.room.RoomData.RoomType.integerValue == 5) {
                    if (ord.Sex.integerValue == 1) {
                        [self.room.leftOrders setObject:ord forKey:userInfo.UserId];
                    } else{
                        [self.room.rightOrders setObject:ord forKey:ord.modelUser.UserId];
                    }
                } else if (self.room.RoomData.RoomType.integerValue == 8) {
                    if (ord.Level.integerValue == 4) {
                        [self.room.leftOrders setObject:ord forKey:userInfo.UserId];
                    } else{
                        [self.room.rightOrders setObject:ord forKey:ord.modelUser.UserId];
                    }
                } else {
                    [self.room.rightOrders setObject:ord forKey:ord.modelUser.UserId];
                }
                
            }
        }
            break;
        case SocketMessageIDMicroCancelOrder:{
            SocketMessageMicroCancelOrder * cancel = message;
            
//            [self.room.users removeObjectForKey:[NSString stringWithFormat:@"%@" , cancel.Id]];
            
            [self.room.rightOrders removeObjectForKey:[NSString stringWithFormat:@"%@" , cancel.Id]];
            [self.room.leftOrders removeObjectForKey:[NSString stringWithFormat:@"%@" , cancel.Id]];
            
        }
            break;
        case SocketMessageIDSeatLocked: {
            SocketMessageSeatLockedArgs *args = message;
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageSeatLocked * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                //转换消息类型
                SocketMessageSeatLocked * lock = obj;
                //获取麦位索引
                NSIndexPath * index = [lock indexPath];
                //获取麦位对象
                LiveRoomMicroInfo * seat = [self.room.seats objectForKey:index];
                //设置锁状态
                seat.IsLocked = @(YES);
            }];
            
        }
            break;
        case SocketMessageIDSeatUnlocked: {
            SocketMessageSeatLockedArgs *args = message;
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageSeatLocked * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                //转换消息类型
                SocketMessageSeatLocked * lock = obj;
                //获取麦位索引
                NSIndexPath * index = [lock indexPath];
                //获取麦位对象
                LiveRoomMicroInfo * seat = [self.room.seats objectForKey:index];
                //设置锁状态
                seat.IsLocked = @(NO);
            }];
        }
            break;
        case SocketMessageIDMicModelChanged : {
            //转换消息类型
            SocketMessageMicModelChanged * change = message;
            //设置属性
            self.room.RoomData.IsFreeMicro = change.IsFreeMicro;
        }
            break;
        case SocketMessageIDHeadBeatChanged: {
            //转换消息类型
            SocketMessageHeadBeatChanged * change = message;
            //设置属性
            self.room.RoomData.IsOpenHeartValue = change.IsHeadBeat;
        }
            break;
        case SocketMessageIDSeatHeartValueUpdate: {
            //转换消息类型
            SocketMessageSeatHeartValueUpdate * update = message;
            //获取座位索引
            NSIndexPath * index = [update indexPath];
            //获取座位实例
            LiveRoomMicroInfo * seat = [self.room.seats objectForKey:index];
            //更新值
            seat.XinDongZhi = update.HeartValue;
        }
            break;
        case SocketMessageIDGiftEvent: {
            SocketMessageGiftEventArgs *args = message;
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageGiftEvent * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                SocketMessageGiftEvent * giftEvent = obj;
                //什么都不做
                self.room.HeatValue = giftEvent.VisitorNum;
            }];
            
        }
            break;
        case SocketMessageIDSeatMicDisable: {
            SocketMessageSeatMicDisableArgs *args = message;
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageSeatMicDisable * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                SocketMessageSeatMicDisable * disable = obj;
                NSIndexPath * disableIndex = [disable indexPath];
                LiveRoomMicroInfo * seat = [self.room.seats objectForKey:disableIndex];
                seat.IsDisabled = @(YES);
            }];
        }
            break;
        case SocketMessageIDSeatMicEnable: {
            SocketMessageSeatMicDisableArgs *args = message;
            [args.Args enumerateObjectsUsingBlock:^(SocketMessageSeatMicDisable * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                SocketMessageSeatMicDisable * enable = obj;
                NSIndexPath * enableIndex = [enable indexPath];
                LiveRoomMicroInfo * seat = [self.room.seats objectForKey:enableIndex];
                seat.IsDisabled = @(NO);
            }];
            
            //判断是不是自己的麦,如果是自己的麦修改麦克风状态
//            NSIndexPath * selfIndex = [self.room.userSeats objectForKey:self.uid];
//            if ([selfIndex isEqual:enableIndex]) {
//                self.agoraEngineManager.disableBroadcast = NO;
//            }
        }
            break;
        case SocketMessageIDUserDisableChatMessage: {
            SocketMessageUserDisableChatMessage * disable = message;
            self.room.IsDisableMsg = disable.IsDisableMsg;
        }
            break;
        case SocketMessageIDRoomLockUpdate: {
            SocketMessageRoomLockUpdate * lock = message;
            self.room.RoomData.RoomLock = lock.IsLock;
        }
            break;
        case SocketMessageIDRoomManagerUpdate: {
            SocketMessageRoomManagerUpdate * update = message;
            if (update.UserId.integerValue == self.userId.integerValue) {
                self.room.UserIdentity = update.IsManager;
                self.room.mineInfoInRoom.UserIdentity = update.IsManager.boolValue ? @(GameUserIdentityManager) : @(GameUserIdentityNormal);
            }
        }
            break;
        case SOcketMessageIDSendEmoticonBegin: {
            SOcketMessageSendEmoticonBegin * begin = message;
            NSIndexPath * index = [begin index];
            LiveRoomMicroInfo * model = [self.room.seats objectForKey:index];
            if (model) {
                model.EmoticonAnimationUrl = begin.EmoticonAnimationUrl;
                model.ConnectionId = begin.ConnectionId;
            }
        }
            break;
        case SOcketMessageIDSendEmoticonResult: {
            SOcketMessageSendEmoticonResult * end = message;
            NSIndexPath * index = [end index];
            LiveRoomMicroInfo * model = [self.room.seats objectForKey:index];
            if (model) {
                model.EmoticonAnimationUrl = nil;
                model.ConnectionId = nil;
            }
        }
            break;
//        case SocketMessageIDSeatCountdown: {
//            SocketMessageSeatCountdown * push = message;
//            LiveRoomMicroInfo * seat = [self.room.seats objectForKey:push.indexPath];
//            seat.DaojishiShijiandian = push.SpeechTime;
//            seat.DaojishiShichang = push.Duration;
//            NSDate * daojishi = [self.dateFormatter dateFromString:seat.DaojishiShijiandian];
//            if (daojishi) {
//                seat.DaojishiEndDate = [NSDate dateWithTimeInterval:seat.DaojishiShichang.doubleValue sinceDate:daojishi];
//            }
//        }
//            break;
        case SocketMessageIDRoomWelcomeUpdate: {
            SocketMessageRoomWelcomeUpdate * push = message;
            self.room.RoomData.RoomWelcomes = push.WelcomMsg;
        }
            break;
        case SocketMessageIDRoomDescUpdate: {
            SocketMessageRoomDescUpdate * push = message;
            self.room.RoomData.RoomDesc = push.Desc;
        }
            break;
        case SocketMessageIDRoomNameUpdate: {
            SocketMessageRoomNameUpdate * push = message;
            self.room.RoomData.RoomName = push.Name;
        }
            break;
            
        // Music
        case SocketMessageIDMusicReserveList: {
            CXSocketMessageMusicReceiveReverseList *list = message;
            self.room.music_reverse_allPage = list.AllPage;
            if (self.room.music_reverse_page == 0) {
                [self.room.music_reverseArrays removeAllObjects];
                self.room.music_reverseArrays = [NSMutableArray arrayWithArray:list.SongInfos];
            } else {
                [self.room.music_reverseArrays addObjectsFromArray:list.SongInfos];
            }
        }
             break;
            
        case SocketMessageIDMusicPlayingDetail: {
            SocketMessageMusicReceivePlayingDetail *detail = message;
            self.room.playing_SongInfo = detail.SongInfo;
            self.room.playing_NextSongInfo = detail.NextSongInfo;
            self.room.music_songerId = [NSString stringWithFormat:@"%ld",(long)detail.SongInfo.ConsertUserId];
        }
             break;
        case SocketMessageIDMusicStartPlaySongSyncGist: {
            SocketMessageMusicReceivePlayingDetail *detail = message;
            self.room.playing_SongInfo = detail.SongInfo;
            self.room.music_songerId = [CXClientModel instance].userId;
        }
            break;
        case SocketMessageIDMusicStartSyncVolum: {
            SocketMessageMusicReceivePlayingStatus *detail = message;
            [[CXClientModel instance].agoraEngineManager.engine adjustAudioMixingVolume:detail.Volume];
            [CXClientModel instance].room.music_Volume = detail.Volume;
        }
            break;
        default:
            break;
    }
    
    if (_room.isJoinedRoom == YES) {
        
        [[self.listener allObjects] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            if ([obj respondsToSelector:@selector(modelClient:didReceiveNotification:)]) {
                [obj modelClient:self didReceiveNotification:message];
            }
        }];
    } else {
        [_room.roomMessages addObject:message];
    }
}

#pragma mark - ==================== Ease ===========================
- (nullable EasemobRoomMessage *)sendRoomText:(NSString*)text success:(void(^)(EasemobRoomMessage * _Nonnull msg))callback {
    //不能发送空内容
    if (!text.length) return nil;
    //禁言不能发消息
    if (self.room.IsDisableMsg.boolValue) return nil;
    //进入房间后，收到自己进入房间前消息不知道自己的个人信息，不许发消息。 因为消息展示的名字等信息是由客户端打包发送的。
    if (!self.room.mineInfoInRoom) {
        return nil;
    }
    EasemobRoomMessage * roomMsg = [EasemobRoomMessage new];
    roomMsg.ext = [EasemobRoomMessageExt new];
    roomMsg.roomId = self.room.HuanxinRoomId;
    roomMsg.text = text;
    roomMsg.ext.XYType = EasemobRoomMessageExtTypeText;
    roomMsg.ext.XYUser = self.room.mineInfoInRoom;
    
    [self.easemob sendMessage:roomMsg callback:^(EasemobRoomMessage * _Nonnull msg) {
        if (callback) callback (msg);
    }];
    
    return roomMsg;
}

#pragma mark - EasemobManagerDelegate
- (void)easemob:(EasemobManager *)manager didReceiveRoom:(NSString*)roomId messages:(NSArray<EasemobRoomMessage*>*)messages {
    [[self.listener allObjects] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj respondsToSelector:@selector(modelClient:didReceiveRoomMessage:)]) {
            [obj modelClient:self didReceiveRoomMessage:messages];
        }
    }];
}
@end
