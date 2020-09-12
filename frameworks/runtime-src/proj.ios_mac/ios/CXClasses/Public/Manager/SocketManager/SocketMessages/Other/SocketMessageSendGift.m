//
//  SocketMessageSendGift.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageSendGift.h"

@implementation SocketMessageSendGift

@dynamic response;

- (Class)responseClass {
    return SocketMessageGroupGiftResponse.class;
}

SocketMessageInitMethod(SocketMessageIDSendGift)

@end
