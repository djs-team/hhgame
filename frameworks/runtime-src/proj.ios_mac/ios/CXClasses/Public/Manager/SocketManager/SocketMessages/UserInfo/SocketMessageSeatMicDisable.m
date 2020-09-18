//
//  SocketMessageSeatMicDisable.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/23.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageSeatMicDisable.h"

@implementation SocketMessageSeatMicDisable

SocketMessageInitMethod(SocketMessageIDSeatMicDisable)

- (NSIndexPath*)indexPath {
    return [NSIndexPath indexPathForRow:_Number.integerValue inSection:_Level.integerValue];
}

@end

@implementation SocketMessageSeatMicDisableArgs
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"Args" : SocketMessageSeatMicDisableArgs.class
             };
}
@end
