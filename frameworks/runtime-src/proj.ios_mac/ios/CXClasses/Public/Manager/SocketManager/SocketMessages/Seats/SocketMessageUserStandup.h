//
//  SocketMessageUserStandup.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageUserStandup : SocketMessageNotification

@property (nonatomic, strong) NSNumber * IsKick;
@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;

- (NSIndexPath*)indexPath;

@end

@interface SocketMessageUserStandupArgs : SocketMessageNotification

@property (nonatomic, strong) NSArray <SocketMessageUserStandup *>*Args;

@end

NS_ASSUME_NONNULL_END
