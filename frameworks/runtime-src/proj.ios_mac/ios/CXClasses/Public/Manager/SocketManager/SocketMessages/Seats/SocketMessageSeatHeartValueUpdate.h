//
//  SocketMessageSeatHeartValueUpdate.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageSeatHeartValueUpdate : SocketMessageNotification

@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;
@property (nonatomic, strong) NSNumber * HeartValue;
@property (nonatomic, strong) NSNumber * Rose; // 麦位玫瑰数

@property (nonatomic, strong) NSArray <NSString *> * RoseRanks; // 麦位玫瑰数

- (NSIndexPath*)indexPath;

@end

@interface SocketMessageSeatHeartValueUpdateArgs : SocketMessageNotification
@property (nonatomic, strong) NSArray <SocketMessageSeatHeartValueUpdate *>*Args;
@end

NS_ASSUME_NONNULL_END
