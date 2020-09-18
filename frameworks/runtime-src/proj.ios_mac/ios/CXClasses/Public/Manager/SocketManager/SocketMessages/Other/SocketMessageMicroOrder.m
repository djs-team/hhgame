//
//  SocketMessageMicroOrder.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageMicroOrder.h"

@implementation SocketMessageMicroOrderDataUser

@end

@implementation SocketMessageMicroOrder

@end

@implementation SocketMessageMicroCancelOrder

@end

@implementation SocketMessageMicroOrderData

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"User" : SocketMessageMicroOrderDataUser.class
             };
}

-(NSIndexPath*)indexPath{
    return [NSIndexPath indexPathForRow:_Number.integerValue inSection:_Level.integerValue];
}

@end
