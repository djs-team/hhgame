//
//  SOcketMessageSendEmoticonResult.m
//  hairBall
//
//  Created by 肖迎军 on 2019/8/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SOcketMessageSendEmoticonResult.h"

@implementation SOcketMessageSendEmoticonResult

SocketMessageInitMethod(SOcketMessageIDSendEmoticonResult)

- (NSIndexPath*)index {
    return [NSIndexPath indexPathForRow:_MicroNumber.integerValue inSection:_MicroLevel.integerValue];
}

@end
