//
//  SocketMessageUserJoinRoom.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageUserJoinRoom : SocketMessageNotification

@property (nonatomic, strong) NSNumber * DukeLevel;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSNumber * RoomGuardLevel;
@property (nonatomic, strong) NSArray * UserGuardList;
@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) NSNumber * UserIdentity;
@property (nonatomic, strong) NSNumber * UserLevel;
@property (nonatomic, strong) NSNumber * VipLevel;
@property (nonatomic, strong) NSNumber * VisitorNum;
@property (nonatomic, strong) NSString * Avatar;
@property (nonatomic, strong) NSNumber * Sex; // 1:男 2:女
@property (nonatomic, strong) NSString * City;
@property (nonatomic, strong) NSString * Age;

// 守护榜
@property (nonatomic, strong) NSString * UserImage; // 守护人的头像
@property (nonatomic, strong) NSString * UserName; // 守护人的昵称
@property (nonatomic, strong) NSString * TargetId; // 被守护人的Id
@property (nonatomic, strong) NSString * TargetImage; // 被守护人的头像
@property (nonatomic, strong) NSString * TargetName; // 被守护人的昵称
@property (nonatomic, strong) NSString * GuardSign; // 守护榜标签
@property BOOL IsRoomGuard; // 是否是该主播守护
@property (nonatomic, strong) NSNumber *GuardState; // 0不是主持守护，1是普通守护，2是榜一守护
@property (nonatomic, strong) NSString * GuardHeadImage; // 被守护头像（榜一用到的）

@end

@interface SocketMessageUserJoinRoomArgs : SocketMessageNotification

@property (nonatomic, strong) NSArray <SocketMessageUserJoinRoom*>*Args;

@end

NS_ASSUME_NONNULL_END
