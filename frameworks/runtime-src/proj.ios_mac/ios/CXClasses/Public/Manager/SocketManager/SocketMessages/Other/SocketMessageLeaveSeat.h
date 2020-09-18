//
//  SocketMessageLeaveSeat.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageLeaveSeat : SocketMessageRequest

@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;    //自己下麦传100，其他参数表示把别人抱下麦

@end

NS_ASSUME_NONNULL_END
