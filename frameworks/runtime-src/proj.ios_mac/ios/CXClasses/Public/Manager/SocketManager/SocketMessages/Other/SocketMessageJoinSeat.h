//
//  SocketMessageJoinSeat.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageJoinSeat : SocketMessageRequest

@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;
@property (nonatomic, strong) NSNumber * TargetUserId;
@property BOOL Force; // 是否是跳过

@end

NS_ASSUME_NONNULL_END
