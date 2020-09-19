//
//  SocketMessageJoinRoom.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageJoinRoom.h"


@implementation SocketMessageJoinRoomResponse

@end


@implementation SocketMessageJoinRoom

@dynamic response;

- (Class)responseClass {
    return SocketMessageJoinRoomResponse.class;
}

SocketMessageInitMethod(SocketMessageIDJoinRoom)

@end
