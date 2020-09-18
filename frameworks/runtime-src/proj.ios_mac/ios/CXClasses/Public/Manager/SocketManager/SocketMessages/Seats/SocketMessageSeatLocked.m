//
//  SocketMessageSeatLocked.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageSeatLocked.h"

@implementation SocketMessageSeatLocked

- (NSIndexPath*)indexPath {
    return [NSIndexPath indexPathForRow:_Number.integerValue inSection:_Level.integerValue];
}

@end

@implementation SocketMessageSeatLockedArgs

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"Args" : SocketMessageSeatLocked.class
             };
}


@end
