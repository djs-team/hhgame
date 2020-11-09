//
//  SocketMessageGiftEvent.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageGiftEventGiveGiftDataMicro : NSObject

@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;

- (NSIndexPath*)indexPath;

@end


@interface SocketMessageGiftEventGiveGiftData : NSObject
@property (nonatomic, strong) NSNumber * Sex; // 1:男 2:女
@property (nonatomic, strong) NSString * TargetName; // 被送礼物的昵称
@property (nonatomic, strong) NSString * TargetId; // 被送礼物的用户Id
@property (nonatomic, strong) SocketMessageGiftEventGiveGiftDataMicro * Micro;

@end


@interface SocketMessageGiftEventGiftGiver : NSObject

@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * HeadImageUrl;
@property (nonatomic, strong) NSNumber * UserId;
@property (nonatomic, strong) NSNumber * Sex; // 1:男 2:女
@property (nonatomic, strong) NSString * City;
@property (nonatomic, strong) NSString * Age;

@end

@interface SocketMessageGiftData : NSObject

@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * Image;
@property (nonatomic, strong) NSString * GiftAnimation;
@property (nonatomic, strong) NSString * Animation;
@property (nonatomic, strong) NSNumber * ClassType;

@end



@interface SocketMessageGiftEvent : SocketMessageNotification

@property (nonatomic, strong) NSNumber * RoomId;
@property (nonatomic, strong) NSString * RoomName;
@property (nonatomic, strong) NSArray<SocketMessageGiftEventGiveGiftData*> * GiveGiftDatas;
@property (nonatomic, strong) SocketMessageGiftEventGiftGiver * GiftGiver;
@property (nonatomic, strong) SocketMessageGiftData * GiftData;
@property (nonatomic, strong) NSNumber * UserLevel;
@property (nonatomic, strong) NSNumber * VipLevel;
@property (nonatomic, strong) NSNumber * RoomGuardLevel;
@property (nonatomic, strong) NSArray * UserGuardList;
@property (nonatomic, strong) NSNumber * DukeLevel;
@property (nonatomic, strong) NSNumber * UserIdentity;
@property (nonatomic, strong) NSNumber * GiftId;
@property (nonatomic, strong) NSNumber * Count;
@property (nonatomic, strong) NSNumber * VisitorNum;
@property (nonatomic, strong) NSString * GuardSign;
@property (nonatomic, strong) NSNumber *GuardState; // 0不是主持守护，1是普通守护，2是榜一守护
@property (nonatomic, strong) NSString * GuardHeadImage; // 被守护头像（榜一用到的）

@end

@interface SocketMessageGiftEventArgs : SocketMessageNotification
@property (nonatomic, strong) NSArray<SocketMessageGiftEvent*> * Args;
@end

@interface SocketMessageSendGiftAddFriendResponse : SocketMessageNotification

@property (nonatomic, strong) NSNumber * UserId;
@property (nonatomic, strong) NSString * NickName;

@end

NS_ASSUME_NONNULL_END
