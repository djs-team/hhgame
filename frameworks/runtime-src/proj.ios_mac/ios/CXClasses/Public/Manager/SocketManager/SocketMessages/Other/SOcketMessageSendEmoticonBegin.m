//
//  SOcketMessageSendEmoticonBegin.m
//  hairBall
//
//  Created by 肖迎军 on 2019/8/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SOcketMessageSendEmoticonBegin.h"

@implementation SOcketMessageSendEmoticonBegin

SocketMessageInitMethod(SOcketMessageIDSendEmoticonBegin)

- (NSIndexPath*)index {
    return [NSIndexPath indexPathForRow:_MicroNumber.integerValue inSection:_MicroLevel.integerValue];
}

@end
