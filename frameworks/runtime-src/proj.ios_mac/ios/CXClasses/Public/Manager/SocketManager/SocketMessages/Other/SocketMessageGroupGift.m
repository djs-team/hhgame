//
//  SocketMessageGroupGift.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageGroupGift.h"


@implementation SocketMessageGroupGiftResponse

@end


@implementation SocketMessageGroupGiftSeat

@end


@implementation SocketMessageGroupGift

@dynamic response;

- (Class)responseClass {
    return SocketMessageGroupGiftResponse.class;
}

SocketMessageInitMethod(SocketMessageIDGroupGift)

@end
