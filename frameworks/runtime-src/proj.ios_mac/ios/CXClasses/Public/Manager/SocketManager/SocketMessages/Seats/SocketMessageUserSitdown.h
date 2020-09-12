//
//  SocketMessageUserSitdown.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN


@interface SocketMessageUserSitdownUser : NSObject

@property (nonatomic, strong) NSString * HeadImageUrl;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) NSString * PrettyId;
@property (nonatomic, strong) NSString * PrettyAvatar;
@property (nonatomic, strong) NSString * Age;
@property (nonatomic, strong) NSString * Stature;
@property (nonatomic, strong) NSString * City;
@property (nonatomic, strong) NSNumber * Sex; // 1:男 2:女

- (NSString*)displayId;

@end


@interface SocketMessageUserSitdownMicroInfo : NSObject

@property (nonatomic, strong) NSNumber *DaojishiShichang;
@property (nonatomic, strong) NSString *DaojishiShijiandian;
@property (nonatomic, strong) NSNumber *IsDisabled;
@property (nonatomic, strong) NSNumber *IsLocked;
@property (nonatomic, strong) NSNumber *Number;
@property (nonatomic, strong) NSNumber *Type;
@property (nonatomic, strong) NSNumber *XinDongZhi;
@property (nonatomic, strong) SocketMessageUserSitdownUser * User;
@property (nonatomic, strong) NSArray<NSString *> *RoseRanks;

- (NSIndexPath*)indexPath;

@end


@interface SocketMessageUserSitdownUserGuard : NSObject

@end


@interface SocketMessageUserSitdown : SocketMessageNotification

@property (nonatomic, strong) NSNumber * DukeLevel;
@property (nonatomic, strong) SocketMessageUserSitdownMicroInfo * MicroInfo;
@property (nonatomic, strong) NSNumber * RoomGuardLevel;
@property (nonatomic, strong) NSArray<SocketMessageUserSitdownUserGuard*> * UserGuardList;
@property (nonatomic, strong) NSNumber * UserIdentity;
@property (nonatomic, strong) NSNumber * UserLevel;
@property (nonatomic, strong) NSNumber * VipLevel;
@property (nonatomic, strong) NSString * GuardSign; // 守护榜标签

@end

@interface SocketMessageUserSitdownArgs : SocketMessageNotification

@property (nonatomic, strong) NSArray <SocketMessageUserSitdown*> * Args;

@end

NS_ASSUME_NONNULL_END
