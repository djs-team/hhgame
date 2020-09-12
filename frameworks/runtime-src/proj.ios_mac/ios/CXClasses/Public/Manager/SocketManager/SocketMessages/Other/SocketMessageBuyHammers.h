//
//  SocketMessageBuyHammers.h
//  hairBall
//
//  Created by shiwei on 2019/7/30.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN


@interface SocketMessageBuyHammersResponse : SocketMessageResponse

@property (nonatomic, copy) NSString *Harmmers; // 锤子当前数量
@property (nonatomic, strong) NSNumber * Beans; // M豆当前数量

@end


@interface SocketMessageBuyHammers : SocketMessageRequest

@property (nonatomic, strong) SocketMessageBuyHammersResponse * response;

@property (nonatomic, strong) NSNumber * BuyMode; // 0为红玫瑰购买，1为蓝玫瑰购买

@property (nonatomic, strong) NSNumber * Count; // 购买数量，最大1000

@end

NS_ASSUME_NONNULL_END
