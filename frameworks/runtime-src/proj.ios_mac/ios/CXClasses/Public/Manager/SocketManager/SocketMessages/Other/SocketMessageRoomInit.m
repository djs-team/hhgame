//
//  SocketMessageRoomInit.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageRoomInit.h"

@implementation SocketMessageRoomInitMicroInfoUser

@end


@implementation SocketMessageRoomInitMicroInfo

@end


@implementation SocketMessageRoomInitMicroOrder

@end


@implementation SocketMessageRoomInitRank

@end


@implementation SocketMessageRoomInitData

- (NSString *)RoomId {
    return self.Id.stringValue;
}

@end


@implementation SocketMessageRoomInit

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"MicroInfos" : [SocketMessageRoomInitMicroInfo class],
             @"ManMicroOrders" : [SocketMessageRoomInitMicroOrder class],
             @"WomenMicroOrders" : [SocketMessageRoomInitMicroOrder class]
//             @"Ranks" : [NSString class]
             };
}

@end
