//
//  SocketMessageSeatHeartValueUpdate.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageSeatHeartValueUpdate.h"

@implementation SocketMessageSeatHeartValueUpdate

- (NSIndexPath*)indexPath {
    return [NSIndexPath indexPathForRow:_Number.integerValue inSection:_Level.integerValue];
}

@end

@implementation SocketMessageSeatHeartValueUpdateArgs

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"Args" : SocketMessageSeatHeartValueUpdate.class
             };
}


@end
