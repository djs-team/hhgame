//
//  SocketMessageSeatUnlocked.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageSeatUnlocked : SocketMessageNotification

@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;

- (NSIndexPath*)indexPath;

@end

NS_ASSUME_NONNULL_END
