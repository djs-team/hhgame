//
//  SocketMessageUserSitdown.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageUserSitdown.h"

@implementation SocketMessageUserSitdownUser

- (NSString*)displayId {
    NSString * display = self.PrettyId;
    if (!display) {
        display = self.UserId;
    }
    return display;
}

@end


@implementation SocketMessageUserSitdownMicroInfo

- (NSIndexPath*)indexPath {
    return [NSIndexPath indexPathForRow:self.Number.integerValue inSection:self.Type.integerValue];
}

@end


@implementation SocketMessageUserSitdownUserGuard

@end


@implementation SocketMessageUserSitdown

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"UserGuardList" : SocketMessageUserSitdownUserGuard.class
             };
}

@end

@implementation SocketMessageUserSitdownArgs

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"Args" : SocketMessageUserSitdown.class
             };
}

@end

