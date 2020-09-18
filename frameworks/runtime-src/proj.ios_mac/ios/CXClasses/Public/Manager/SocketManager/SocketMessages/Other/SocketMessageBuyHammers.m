//
//  SocketMessageBuyHammers.m
//  hairBall
//
//  Created by shiwei on 2019/7/30.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageBuyHammers.h"


@implementation SocketMessageBuyHammersResponse

@end


@implementation SocketMessageBuyHammers

@dynamic response;

- (Class)responseClass {
    return SocketMessageBuyHammersResponse.class;
}

SocketMessageInitMethod(SocketMessageIDBuyHammers)

@end
