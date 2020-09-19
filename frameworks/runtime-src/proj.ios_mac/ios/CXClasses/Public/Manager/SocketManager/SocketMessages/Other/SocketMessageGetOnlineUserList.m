//
//  SocketMessageGetOnlineUserList.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageGetOnlineUserList.h"


@implementation SocketMessageGetOnlineUserListResponseOnlineUserUser

@end


@implementation SocketMessageGetOnlineUserListResponseOnlineUser

@end


@implementation SocketMessageGetOnlineUserListResponse

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"OnlineUsers" : [SocketMessageGetOnlineUserListResponseOnlineUser class]
             };
}

@end


@implementation SocketMessageGetOnlineUserList

@dynamic response;

- (Class)responseClass {
    return SocketMessageGetOnlineUserListResponse.class;
}

SocketMessageInitMethod(SocketMessageIDGetOnlineUserList)

@end
