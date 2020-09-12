//
//  SocketMessageMicroOrder.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageMicroOrderDataUser : NSObject

@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * HeadImageUrl;
@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) NSNumber * VipLevel;

@end


@interface SocketMessageMicroOrderData : NSObject

@property (nonatomic, strong) NSNumber * Sex;
@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;

@property SocketMessageMicroOrderDataUser * User;

- (NSIndexPath*)indexPath;

@end


@interface SocketMessageMicroOrder : SocketMessageNotification

@property (nonatomic, strong) SocketMessageMicroOrderData * MicroOrderData;

@end

@interface SocketMessageMicroCancelOrder : SocketMessageNotification

@property (nonatomic, strong) NSNumber * Id;
@property (nonatomic, strong) NSNumber * State;

@end

NS_ASSUME_NONNULL_END
