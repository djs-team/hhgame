//
//  SocketMessageMicroOrder.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageMicroOrderData : NSObject

@property (nonatomic, strong) NSNumber * Sex;
@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;

@property (nonatomic, strong) LiveRoomUser * User;

- (NSIndexPath*)indexPath;

@end

@interface SocketMessageMicroOrder : SocketMessageNotification

@property (nonatomic, strong) SocketMessageMicroOrderData * MicroOrderData;

@property (nonatomic, strong) NSNumber *GuardState; // 0不是主持守护，1是普通守护，2是榜一守护
@property (nonatomic, strong) NSString * GuardHeadImage; // 被守护头像（榜一用到的）

@end

@interface SocketMessageMicroCancelOrder : SocketMessageNotification

@property (nonatomic, strong) NSNumber * Id;
@property (nonatomic, strong) NSNumber * State;

@end

NS_ASSUME_NONNULL_END
